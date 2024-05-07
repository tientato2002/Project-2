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

import com.example.jmaster.dto.DepartmentDTO;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.ResponseDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	DepartmentService departmentService;

	@GetMapping("/")
	//@ResponseStatus(code = HttpStatus.OK)
	public ResponseDTO<DepartmentDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<DepartmentDTO>builder()
				.status(200)
				.data(departmentService.getById(id)).build();
	}

	@PostMapping("/")
	public ResponseDTO<Void> create(@ModelAttribute @Valid DepartmentDTO departmentDTO) {
		departmentService.create(departmentDTO);

		return ResponseDTO.<Void>builder().status(200).msg("create succesful").build();
	}
	// neu day du lieu len dang json, dung RequestBody
	// json khong upload duoc file
	@PostMapping("/json")
	public ResponseDTO<Void> createNewJson(@RequestBody @Valid DepartmentDTO departmentDTO) {
		departmentService.create(departmentDTO);

		return ResponseDTO.<Void>builder().status(200).msg("create succesful").build();
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		departmentService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("delete successful").build();
	}

	@PutMapping("/")
	public ResponseDTO<DepartmentDTO> edit(@ModelAttribute @Valid DepartmentDTO departmentDTO) {
		departmentService.update(departmentDTO);
		return ResponseDTO.<DepartmentDTO>builder()
				.status(200)
				.msg("edit successful")
				.data(departmentDTO).build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<DepartmentDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {
		PageDTO<List<DepartmentDTO>> pageDTO = departmentService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<DepartmentDTO>>>builder()
				.status(200)
				.data(pageDTO).build();

	}

}
