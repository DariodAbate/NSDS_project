#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <mpi.h>
#include <math.h>

/*
 * Group number: 17
 *
 * Group members
 *  - Dario d'Abate
 *  - Lorenzo Corrado
 *  - Filippo Ranieri Pantaleone
 */

const float min = 0;
const float max = 1000;
const float len = max - min;
const int num_ants = 8 * 1000 * 1000;
const int num_food_sources = 10;
const int num_iterations = 5000;


float random_position() {
    return (float) rand() / (float)(RAND_MAX/(max-min)) + min;
}

/*
 * Process 0 invokes this function to initialize food sources.
 */
void init_food_sources(float* food_sources) {
    for (int i=0; i<num_food_sources; i++) {
        food_sources[i] = random_position();
    }
}

/*
 * Process 0 invokes this function to initialize the position of ants.
 */
void init_ants(float* ants) {
    for (int i=0; i<num_ants; i++) {
        ants[i] = random_position();
    }
}

float compute_local_sum(float* local_ants, int num_ants_proc){
    float local_sum = 0;
    for (int i = 0; i < num_ants_proc; i++){
        local_sum += local_ants[i];
    }
    return local_sum;
}

float compute_center(int rank, float* local_ants, int num_ants_procs) {
    float local_sum = compute_local_sum(local_ants, num_ants_procs);
    float global_sum;

    MPI_Reduce(
        &local_sum,//array of element that process wants ro reduce
        &global_sum,//reduced data that will be contained into root
        1, //num of element for each process to reduce; num of reduced elements in root
        MPI_FLOAT,
        MPI_SUM,//reduce operator
        0,
        MPI_COMM_WORLD);
    
    float center;
    //broadcast center
    if(rank == 0){
        center = global_sum/num_ants;
    }
    
    MPI_Bcast(
        &center,
        1,
        MPI_FLOAT,
        0,//root
        MPI_COMM_WORLD);

    return center;

}





int main() {
    MPI_Init(NULL, NULL);

    int rank;
    int num_procs;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &num_procs);

    srand(rank);

    int num_ants_procs = num_ants/num_procs;

    // Allocate space in each process for food sources and ants
    float* food_sources = malloc(sizeof(float) * num_food_sources);
    float* ants = malloc(sizeof(float) * num_ants);
    float* local_ants = malloc(sizeof(float) * num_ants_procs);

    // Process 0 initializes food sources and ants
    if(rank == 0){
        init_food_sources(food_sources);
        init_ants(ants);
    }

    // Process 0 distributed food sources and ants
    // Broadcast food sources
    // Scatter the ants between processes
    MPI_Bcast(
        food_sources,
        num_food_sources,
        MPI_FLOAT,
        0,//root
        MPI_COMM_WORLD);

    MPI_Scatter(
        ants,//data to be sent by root
        num_ants_procs,//num of element to scatter
        MPI_FLOAT,
        local_ants,//scattered data for each process
        num_ants_procs,//num of sc. element for each p
        MPI_FLOAT,
        0,//root
        MPI_COMM_WORLD);


    // Iterative simulation
    float center = 0; 
    center = compute_center(rank, local_ants, num_ants_procs);

    for (int iter=0; iter<num_iterations; iter++) {
        
        //update position of each ant
        float nearest_food_source = 10000;//init at very high value
        float f1;
        float f2;
        for (int i = 0; i < num_ants_procs; i++){
            for (int j = 0; j < num_food_sources; j++){
                float antFood_vector = food_sources[j] - local_ants[i];
                if(fabsf(antFood_vector) < fabsf(nearest_food_source)){
                    nearest_food_source = antFood_vector;
                }
            }
            f1 = 0.01 * nearest_food_source;
            float center_vector = center - local_ants[i] ;
            f2 = 0.012 * center_vector;
            local_ants[i] += f1 + f2; 
            nearest_food_source = 10000;
        }

        //update center
        center = compute_center(rank, local_ants, num_ants_procs);

        if (rank == 0) {
            printf("Iteration: %d - Average position: %f\n", iter, center);
        }
    }

    // Free memory
    free(food_sources); 
    free(ants);
    free(local_ants); 


    MPI_Finalize();
    return 0;
}
