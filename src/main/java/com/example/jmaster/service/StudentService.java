package com.example.jmaster.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.dto.StudentDTO;
import com.example.jmaster.entity.Student;
import com.example.jmaster.entity.User;
import com.example.jmaster.repository.StudentRepo;
import com.example.jmaster.repository.UserRepo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

public interface StudentService {
	void create(StudentDTO studentDTO);
	
	void update(StudentDTO studentDTO);
	
	void delete(int id);
	
	StudentDTO getById(int id);
	
	PageDTO<List<StudentDTO>> search(SearchDTO searchDTO);
	
	List<StudentDTO> getAll();
}


@Service
class StudentServiceImpl implements StudentService{


	@Autowired
	StudentRepo studentRepo;
	
	@Autowired
	UserRepo userRepo;
	
	@Override
	@Transactional
	public void create(StudentDTO studentDTO) {
//		User user = new ModelMapper().map(
//				studentDTO.getUser(), User.class);
//		userRepo.save(user);
		
		//dung casecade
//		Student student = new Student();
//		student.setUser(user);
//		student.setStudentCode(studentDTO.getStudentCode());
		Student student = new ModelMapper().map(studentDTO, Student.class);
		studentRepo.save(student);
	}

	@Override
	@Transactional
	public void update(StudentDTO studentDTO) {
		
		Student student = studentRepo.findById(studentDTO.getUser().getId()).orElseThrow(NoResultException::new);

		student.setStudentCode(studentDTO.getStudentCode());
		student.setUser(new ModelMapper().map(studentDTO.getUser(), User.class));
		
		studentRepo.save(student);
		
	}

	@Override
	@Transactional
	public void delete(int id) {
		studentRepo.findById(id).orElseThrow(NoResultException::new);
		studentRepo.deleteById(id);
	}

	@Override
	public StudentDTO getById(int id) {
		Student student = 
				studentRepo.findById(id)
				.orElseThrow(NoResultException::new);
		
		return convertToDto(student);
	}
	
	private StudentDTO convertToDto(Student student) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(student, StudentDTO.class);
	}
	
	
	
	@Override
	public PageDTO<List<StudentDTO>> search(SearchDTO searchDTO) {
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
		Page<Student> page = studentRepo.searchByName("%"+searchDTO.getKeyword()+"%", pageRequest);
		
		PageDTO<List<StudentDTO>> pageDTO = new PageDTO<>();
		
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());
		
		//List<Department> departments = page.getContent();
		List<StudentDTO> studentDTOs 
			= page.get().map(u -> convertToDto(u))
			.collect(Collectors.toList());
		
		pageDTO.setData(studentDTOs);
		
		return pageDTO;

	}
	
	@Override
	public List<StudentDTO> getAll() {
		List<Student> students = studentRepo.findAll();
		
		return students.stream().map(u -> convertToDto(u))
				.collect(Collectors.toList());
	}
}