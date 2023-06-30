package com.button.demo1.service;

import com.button.demo1.entity.Student;
import com.button.demo1.mapper.StudentMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public void insert(Student student) {
        studentMapper.insert(student);
    }
}
