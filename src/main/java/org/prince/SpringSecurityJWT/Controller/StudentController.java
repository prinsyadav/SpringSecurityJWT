package org.prince.SpringSecurityJWT.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.prince.SpringSecurityJWT.model.Student;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> students = new ArrayList<>() {{
        add(new Student(1, "John Doe", 90));
        add(new Student(2, "Jane Doe", 80));
        add(new Student(3, "Alice", 70));
        add(new Student(4, "Bob", 60));
    }};

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
}
