package com.example.jmaster.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.jmaster.dto.CourseDTO;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.entity.Course;
import com.example.jmaster.repository.CourseRepo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

public interface CourseService {
	
	void create(CourseDTO courseDTO);
	
	void update(CourseDTO courseDTO);
	
	void delete(int id);
	
	CourseDTO getById(int id);
	
	PageDTO<List<CourseDTO>> search(SearchDTO searchDTO);
	
	List<CourseDTO> getAll();
}

@Service
class CourseServiceImpl implements CourseService{

	@Autowired
	private CourseRepo courseRepo;
	
	
	
	@Override
	public void create(CourseDTO courseDTO) {
		Course course = new ModelMapper().map(courseDTO, Course.class);
		courseRepo.save(course);
	}

	@Override
	public void update(CourseDTO courseDTO) {
		Course course = courseRepo.findById(courseDTO.getId()).orElse(null);
		if (course != null) {
			course.setName(courseDTO.getName());
			//
			courseRepo.save(course);
		}
		
	}

	@Override
	@Transactional
	public void delete(int id) {
		courseRepo.deleteById(id);
	}

	@Override
	public CourseDTO getById(int id) {
		Course course = 
				courseRepo.findById(id)
				.orElseThrow(NoResultException::new);

		return convertToDto(course);
	}

	@Override
	public List<CourseDTO> getAll() {
		List<Course> courses = courseRepo.findAll();
		
		return courses.stream().map(u -> convertToDto(u))
				.collect(Collectors.toList());
	}

	@Override
	public PageDTO<List<CourseDTO>> search(SearchDTO searchDTO) {
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
		Page<Course> page = courseRepo.searchName(searchDTO.getKeyword(), pageRequest);
		
		PageDTO<List<CourseDTO>> pageDTO = new PageDTO<>();
		
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());
		
//		List<Course> departments = page.getContent();
		List<CourseDTO> courseDTOs 
			= page.get().map(u -> convertToDto(u))
			.collect(Collectors.toList());
		pageDTO.setData(courseDTOs);
		
		return pageDTO;
	}
	
	private CourseDTO convertToDto(Course course) {
		return new ModelMapper().map(course, CourseDTO.class);
	}
}

