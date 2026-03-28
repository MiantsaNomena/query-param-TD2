package com.example.student.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.student.entity.Student;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StudentController {
    private List<Student> students = new ArrayList<>();

    @GetMapping("/welcome")
    public String welcome(@RequestParam String name) {
        return "Welcome " + name;
    }

    @PostMapping("/students")
    public List<String> addStudents(@RequestBody List<Student> newStudents) {
        students.addAll(newStudents);
        return students.stream()
                .map(s -> s.getFirstName() + " " + s.getLastName())
                .collect(Collectors.toList());
    }

    @GetMapping("/students")
    public ResponseEntity<String> getStudents(
            @RequestHeader(value = "Accept", defaultValue = "text/plain") String accept) {

        if (accept.equals("text/plain")) {
            String result = students.stream()
                    .map(s -> s.getFirstName() + " " + s.getLastName())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.ok()
                    .header("Content-Type", "text/plain")
                    .body(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                    .body("Format non supporté");
        }
    }
}