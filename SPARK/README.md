# Evaluation lab - Apache Spark

## Overview

This project demonstrates the use of Apache Spark for both batch and streaming data processing. The project involves performing various queries on a set of datasets related to university courses, professors, videos, and video visualizations. The goal is to efficiently compute specific metrics and provide timely updates for streaming data.

## Project Structure

- **Q1**: Compute the total number of hours of lectures for each professor.
- **Q2**: For each course, compute the total number of visualizations of videos for that course, updated every minute and refreshed every 10 seconds.
- **Q3**: For each video, compute the total number of visualizations relative to the number of students enrolled in the course using that video.

## Datasets

The project uses four input datasets:

1. **profs**
   - **Type**: Static, CSV file
   - **Fields**: `prof_name`, `course_name`
   
2. **courses**
   - **Type**: Static, CSV file
   - **Fields**: `course_name`, `course_hours`, `course_students`

3. **videos**
   - **Type**: Static, CSV file
   - **Fields**: `video_id`, `video_duration`, `course_name`

4. **visualizations**
   - **Type**: Dynamic, stream
   - **Fields**: `timestamp`, `value`
   - **Description**: Each entry with a value `v` indicates that someone watched a video with `video_id` equal to `v`.

## Query Descriptions

### Q1: Total Lecture Hours per Professor

This query computes the total number of lecture hours for each professor by joining the `profs` and `courses` datasets. The resulting output shows the total lecture hours associated with each professor based on the courses they teach.

### Q2: Visualizations per Course

This query computes the total number of visualizations for each course's videos over a rolling window of one minute, updating every 10 seconds. It leverages the `videos` dataset to associate visualizations with courses and streams this information to provide near real-time updates.

### Q3: Visualizations Relative to Course Enrollment

This query calculates the total number of visualizations for each video relative to the number of students enrolled in the course that uses that video. The query joins the `videos` dataset with the streaming `visualizations` dataset and normalizes the count of visualizations by the number of students in the corresponding course.

