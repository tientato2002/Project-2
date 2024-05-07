package com.example.jmaster.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreByStudent {
	private int studentId;
	private String studentName;
	private String courseName;
	private double score;
}
