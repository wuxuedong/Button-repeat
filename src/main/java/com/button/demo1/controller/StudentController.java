package com.button.demo1.controller;

import com.button.demo1.annotation.PreventRepeatSubmit;
import com.button.demo1.entity.Student;
import com.button.demo1.service.StudentService;
import com.button.demo1.utils.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/student")
@RestController()
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreventRepeatSubmit(interval=30)
    @RequestMapping("/insert")
    public ResponseResult insertStudent(@RequestBody Student student) {
        studentService.insert(student);
        return ResponseResult.okResult();
    }
}
