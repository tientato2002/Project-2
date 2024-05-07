package com.example.jmaster.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.jmaster.dto.DepartmentDTO;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.entity.Department;
import com.example.jmaster.repository.DepartmentRepo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepo departmentRepo;
	
	@Autowired
	CacheManager cacheManager;

	private DepartmentDTO convertToDto(Department department) {
		return new ModelMapper().map(department, DepartmentDTO.class);
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "department-search", allEntries = true)
	public void create(DepartmentDTO departmentDTO) {
		Department department = new ModelMapper().map(departmentDTO, Department.class);
		// save entity
		departmentRepo.save(department);
		
//		Cache cache = cacheManager.getCache("department-search");
//		cache.invalidate();//xoa het
		
	}

	@Override
	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "department-search", allEntries = true)
	},
			put = {
			@CachePut(cacheNames = "department", key = "#departmentDTO.id")
	})
//	@CacheEvict(cacheNames = "department", key = "#departmentDTO.id")
	public DepartmentDTO update(DepartmentDTO departmentDTO) {
		Department currentDepartment = departmentRepo.findById(departmentDTO.getId()).orElse(null);
		if (currentDepartment != null) {
			currentDepartment.setName(departmentDTO.getName());
			departmentRepo.save(currentDepartment);
		}
		return convertToDto(currentDepartment);
	}

	@Override
	@Transactional
//	@CacheEvict(cacheNames = "department",key = "#id")
	@Caching(evict = {
			@CacheEvict(cacheNames = "department", key = "#id"),
			@CacheEvict(cacheNames = "department-search", allEntries = true)
	})
	public void delete(int id) {
				departmentRepo.findById(id)
				.orElseThrow(NoResultException::new);
		departmentRepo.deleteById(id);
	}

	@Override
	@Cacheable(cacheNames = "department",key = "#id",
			unless = "#result == null")
	public DepartmentDTO getById(int id) {
		System.out.println("Chua co Cache");
		//Optional
		Department department = 
				departmentRepo.findById(id)
				.orElseThrow(NoResultException::new);

		return convertToDto(department);
	}

	@Override
	@Cacheable(cacheNames = "department-search")
	public PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO) {
		Sort sortBy = Sort.by("id").ascending();

		if (StringUtils.hasText(searchDTO.getSortedField())) {
			sortBy = Sort.by(searchDTO.getSortedField()).ascending();
		}

		if (searchDTO.getCurentPage() == null) {
			searchDTO.setCurentPage(0);
		}
		if (searchDTO.getSize() == null) {
			searchDTO.setSize(5);
		}
		if (searchDTO.getKeyword() == null) {
			searchDTO.setKeyword("");
		}

		PageRequest pageRequest = PageRequest.of(searchDTO.getCurentPage(), searchDTO.getSize(), sortBy);
		Page<Department> page = departmentRepo.searchName(searchDTO.getKeyword(), pageRequest);
		
		PageDTO<List<DepartmentDTO>> pageDTO = new PageDTO<>();
		
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());
		
		//List<Department> departments = page.getContent();
		List<DepartmentDTO> departmentDTOs 
			= page.get().map(u -> convertToDto(u))
			.collect(Collectors.toList());
		pageDTO.setData(departmentDTOs);
		
		return pageDTO;

	}

}
