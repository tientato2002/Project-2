package com.example.jmaster.service;

import java.util.List;

import com.example.jmaster.dto.DepartmentDTO;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.SearchDTO;

public interface DepartmentService {
	void create(DepartmentDTO departmentDTO);
	
	DepartmentDTO update(DepartmentDTO departmentDTO);
	
	void delete(int id);
	
	DepartmentDTO getById(int id);
	
	PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO);
}
