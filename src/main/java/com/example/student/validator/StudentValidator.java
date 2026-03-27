package com.example.student.validator;
import com.example.student.entity.Student;
import com.example.student.exception.BadRequestException;
import java.util.List;

public class StudentValidator {

    public void validate(Student student) {
        if (student.getReference() == null || student.getReference().isBlank()) {
            throw new BadRequestException("NewStudent.reference cannot be null");
        }
        if (student.getFirstName() == null || student.getFirstName().isBlank()) {
            throw new BadRequestException("NewStudent.firstName cannot be null");
        }
        if (student.getLastName() == null || student.getLastName().isBlank()) {
            throw new BadRequestException("NewStudent.lastName cannot be null");
        }
    }

    public void validateAll(List<Student> students) {
        for (Student student : students) {
            validate(student);
        }
    }
}