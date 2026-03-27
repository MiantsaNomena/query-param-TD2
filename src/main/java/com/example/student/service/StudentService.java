package com.example.student.service;
import com.example.student.entity.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private List<Student> studentsInMemory = new ArrayList<>();

    public List<Student> saveAll(List<Student> newStudents) {
        studentsInMemory.addAll(newStudents);
        return studentsInMemory;
    }

    public List<Student> getAll() {
        return studentsInMemory;
    }
}
