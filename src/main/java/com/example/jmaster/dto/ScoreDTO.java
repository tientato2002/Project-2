package com.example.jmaster.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ScoreDTO {
	private int id;
	private double score;
	
	private CourseDTO course;
	private StudentDTO student;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date updatedAt;
}
