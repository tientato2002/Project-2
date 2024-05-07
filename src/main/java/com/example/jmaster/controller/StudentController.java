package com.example.jmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.ResponseDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.dto.StudentDTO;
import com.example.jmaster.service.StudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/student")
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	
	//gia su khong upload file
	@PostMapping("/")
	public ResponseDTO<Void> newStudent(
			@RequestBody @Valid StudentDTO studentDTO){
		
		studentService.create(studentDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create succesful")
				.build();
	}
	
	@GetMapping("/")
	public ResponseDTO<StudentDTO> get(@RequestParam("id") int id){
		
		return ResponseDTO.<StudentDTO>builder()
				.status(200)
				.data(studentService.getById(id))
				.msg("ok!")
				.build();
	}
	

	@DeleteMapping("/") // ?id=1
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		studentService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("delete successful").build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<StudentDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {

		PageDTO<List<StudentDTO>> pageUser = studentService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<StudentDTO>>>builder().status(200).data(pageUser).build();
	}
	

	@PutMapping("/")
	public ResponseDTO<StudentDTO> edit(@RequestBody @Valid StudentDTO studentDTO) 
	throws Exception{
		studentService.update(studentDTO);
		return ResponseDTO.<StudentDTO>
		builder().status(200)
		.msg("edit ok")
		.data(studentDTO).build();
	}


	@GetMapping("/list")
	public ResponseDTO<List<StudentDTO>> list() {
		List<StudentDTO> studentDTOs = studentService.getAll();
		return ResponseDTO.<List<StudentDTO>>builder()
				.status(200)
				.data(studentDTOs)
				.msg("ok").build();
	}


}
