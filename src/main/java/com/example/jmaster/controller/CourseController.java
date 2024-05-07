package com.example.jmaster.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jmaster.dto.CourseDTO;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.ResponseDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.service.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	CourseService courseService;

	@GetMapping("/")
	public ResponseDTO<CourseDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<CourseDTO>builder()
				.status(200)
				.data(courseService.getById(id)).build();
	}

//	@PostMapping("/")
//	public ResponseDTO<Void> create(@ModelAttribute @Valid CourseDTO courseDTO) {
//		courseService.create(courseDTO);
//
//		return ResponseDTO.<Void>builder()
//				.status(200)
//				.msg("create succesful")
//				.build();
//	}
	// neu day du lieu len dang json, dung RequestBody
	// json khong upload duoc file
	@PostMapping("/")
	public ResponseDTO<Void> createNewJson(@RequestBody @Valid CourseDTO courseDTO) {
		courseService.create(courseDTO);

		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create succesful")
				.build();
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		courseService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("delete successful").build();
	}

	@PutMapping("/")
	public ResponseDTO<CourseDTO> edit(@RequestBody @Valid CourseDTO courseDTO) {
		courseService.update(courseDTO);
		return ResponseDTO.<CourseDTO>builder()
				.status(200)
				.msg("edit successful")
				.data(courseDTO).build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<CourseDTO>>> search(@RequestBody @Valid SearchDTO searchDTO) {
		PageDTO<List<CourseDTO>> pageDTO = courseService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<CourseDTO>>>builder()
				.status(200)
				.data(pageDTO).build();
	}

}
