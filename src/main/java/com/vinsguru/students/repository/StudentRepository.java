package com.vinsguru.students.repository;

import com.vinsguru.students.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
