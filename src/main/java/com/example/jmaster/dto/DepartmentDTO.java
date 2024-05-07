package com.example.jmaster.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DepartmentDTO {
	private int  id;
	
	@NotBlank
	private String name;
	
	private Date createdAt;
	private Date updatedAt;
}
