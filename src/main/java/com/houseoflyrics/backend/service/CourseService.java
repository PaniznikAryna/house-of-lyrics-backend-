package com.houseoflyrics.backend.service;


import com.houseoflyrics.backend.entity.Course;
import com.houseoflyrics.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findByTitle(String title){
        return courseRepository.findByTitle(title);
    }

    public Optional<Course> findById(Long id){
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course){
        return courseRepository.save(course);
    }

    public boolean deleteCourse(Long id) {
        if (courseRepository.existsById(id)){
            courseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
