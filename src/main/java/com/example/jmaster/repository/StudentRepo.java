package com.example.jmaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jmaster.entity.Student;

public interface StudentRepo extends JpaRepository<Student, Integer>{
	
	@Query("SELECT s FROM Student s WHERE s.user.name LIKE :x")
	Page<Student> searchByName (@Param("x") String s,Pageable pageable);
}
