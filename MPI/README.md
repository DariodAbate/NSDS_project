# Evaluation Lab - MPI Ant Colony Simulation

## Project Overview

This project implements a simple simulator that models the behavior of a colony of ants. The simulation takes place in a one-dimensional space, where ants move towards food sources and are also influenced by the position of their colony's center. The goal of the simulation is to observe the evolution of the colony's center over several iterations.

## Problem Description

### Simulation Space

- The ants move in a one-dimensional space, denoted as $x$, where $x ∈ [0, 1000]$.
- In this space, there are 10 sources of food, and their positions are stored in an array named `food_sources`.

### Colony Center

- The center of the colony is defined as the average position of all ants within the colony.

### Simulation Parameters

- The simulation evolves over a number of discrete rounds, defined by the parameter `num_iterations`.
- Before the first iteration, process $P_0$ computes the initial positions of the ants and the food sources. These positions are then distributed to all other processes.

### Ant Movement

During each iteration, every ant moves based on two forces, $F_1$ and $F_2$:

1. **Force $F_1$**:
   - This force attracts the ant towards the nearest source of food.
   - Given the distance between the ant and the nearest food source $d_1$, we compute $$F_1 = 0.01 * d_1$$

2. **Force $F_2$**:
   - This force attracts the ant towards the center of the colony.
   - Given the distance between the ant and the center of the colony $d_2$, we compute $$F_2 = 0.012 * d_2$$
     

3. **Ant Position Update**:
   - At the end of each iteration, we compute $$\text{New Position} = \text{Old Position} + F_1 + F_2$$
     

