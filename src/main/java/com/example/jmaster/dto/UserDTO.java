package com.example.jmaster.dto;


import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class UserDTO {
	
	private int id;
	@Min(value = 0)
	private int age;
	@NotBlank(message = "{not.blank}")
	private String name;
	private String avatarURL;//luu ten file path
	
	private String username;
	private String password;
	//many to one
	private DepartmentDTO department;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date birthdate;
	
	
	@JsonIgnore 
	private MultipartFile file;
	
	private List<String> roles;
}



