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

import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.dto.UserDTO;
import com.example.jmaster.entity.Department;
import com.example.jmaster.entity.User;
import com.example.jmaster.repository.UserRepo;

import jakarta.transaction.Transactional;

@Service // tao bean: new Uservice, quan ly boi springcontain
public class UserService {

	@Autowired
	UserRepo userRepo;
	
	
	private UserDTO convert(User user) {
//		UserDTO userDTO = new UserDTO();
//		userDTO.setName(user.getName());
//		userDTO.setAge(user.getAge());
//		userDTO.setUsername(user.getUsername());
//		userDTO.setPassword(user.getPassword());
//		userDTO.setAvatarURL(user.getAvatarURL());
//		userDTO.setId(user.getId());
//
//		return userDTO;
		return new ModelMapper().map(user, UserDTO.class);
	}

	public List<UserDTO> getAll() {
		List<User> userList = userRepo.findAll();
		
		return userList.stream().map(u -> convert(u))
				.collect(Collectors.toList());
	}

	@Transactional
	public void create(UserDTO userDTO) {
		// convert user dto -> user
//		User user = new User();
//		user.setName(userDTO.getName());
//		user.setAge(userDTO.getAge());
//		user.setUsername(userDTO.getUsername());
//		user.setPassword(userDTO.getPassword());
//		user.setAvatarURL(userDTO.getAvatarURL());
		User user = new ModelMapper().map(userDTO, User.class);
		// save entity
		userRepo.save(user);
	}

	@Transactional
	public void delete(int id) {
		userRepo.deleteById(id);
	}

	public UserDTO getByID(int id) {
		// Optional
		User user = userRepo.findById(id).orElse(null);
		if (user != null) {
			return convert(user);
		}
		return null;
	}

	@Transactional
	public void update(UserDTO userDTO) {
		// check
		User currentUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currentUser != null) {
			currentUser.setName(userDTO.getName());
			currentUser.setAge(userDTO.getAge());
			
			Department department = new Department();
			department.setId(userDTO.getDepartment().getId());
			
			currentUser.setDepartment(department);
			
			userRepo.save(currentUser);
		}
	}

	@Transactional
	public void updatePassword(UserDTO userDTO) {
		// check
		User currentUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currentUser != null) {
			currentUser.setPassword(userDTO.getPassword());
			userRepo.save(currentUser);
		}
	}

	public PageDTO<List<UserDTO>> searchName(SearchDTO searchDTO) {
		Sort sortBy = Sort.by("id").ascending();

		if (StringUtils.hasText(searchDTO.getSortedField())) {
			sortBy = Sort.by(searchDTO.getSortedField()).ascending();
		}

		if (searchDTO.getCurentPage() == null) {
			searchDTO.setCurentPage(0);
		}
		if (searchDTO.getSize() == null) {
			searchDTO.setSize(2);
		}

		PageRequest pageRequest = PageRequest.of(searchDTO.getCurentPage(), searchDTO.getSize(), sortBy);
		Page<User> page = userRepo.searchByName(searchDTO.getKeyword(), pageRequest);
		
		PageDTO<List<UserDTO>> pageDTO = new PageDTO<>();
		
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());
		
		//List<User> users = page.getContent();
		List<UserDTO> userDTOs 
			= page.get().map(u -> convert(u))
			.collect(Collectors.toList());
		pageDTO.setData(userDTOs);
		
		return pageDTO;
	}

}
