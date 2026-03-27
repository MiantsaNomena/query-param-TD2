package com.example.student.controller;
import com.example.student.exception.BadRequestException;
import com.example.student.service.StudentService;
import com.example.student.validator.StudentValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.student.entity.Student;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StudentController {
    private final StudentValidator studentValidator = new StudentValidator();
    private final StudentService studentService = new StudentService();


    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(
            @RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Paramètre 'name' manquant");
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body("Welcome " + name);
    }


    @PostMapping("/students")
    public ResponseEntity<?> createStudents(
            @RequestBody List<Student> newStudents) {
        try {
            studentValidator.validateAll(newStudents);
            List<Student> allStudents = studentService.saveAll(newStudents);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(allStudents);
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }



    @GetMapping("/students")
    public ResponseEntity<?> getStudents(
            @RequestHeader(value = "Accept", required = false) String accept) {
        if (accept == null || accept.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Header 'Accept' manquant");
        }
        try {
            List<Student> allStudents = studentService.getAll();
            if (accept.equals("text/plain")) {
                String result = allStudents.stream()
                        .map(s -> s.getFirstName() + " " + s.getLastName())
                        .collect(Collectors.joining(", "));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type", "text/plain")
                        .body(result);
            } else if (accept.equals("application/json")) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body(allStudents);
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .body("Format non supporté");
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }
}