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
    public ResponseEntity<?> addStudents(
            @RequestBody List<Student> newStudents) {

        try {
            students.addAll(newStudents);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json")
                    .body(students);

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
            if (accept.equals("text/plain")) {

                String result = students.stream()
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
                        .body(students);

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