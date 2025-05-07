package com.houseoflyrics.backend.service;

import com.houseoflyrics.backend.entity.Course;
import com.houseoflyrics.backend.entity.Lesson;
import com.houseoflyrics.backend.repository.CourseRepository;
import com.houseoflyrics.backend.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public List<Lesson> findAllByCourseId(Long courseId) {
        return lessonRepository.findAllByCourseId(courseId);
    }

    public Optional<Lesson> findById(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson saveLesson(Lesson lesson) {
        Optional<Course> courseOpt = courseRepository.findById(lesson.getCourse().getId());
        courseOpt.ifPresent(lesson::setCourse);

        return lessonRepository.save(lesson);
    }


    public boolean deleteLesson(Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
