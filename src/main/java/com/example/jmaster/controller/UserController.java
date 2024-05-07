package com.example.jmaster.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.ResponseDTO;
import com.example.jmaster.dto.SearchDTO;
import com.example.jmaster.dto.UserDTO;
import com.example.jmaster.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired // DI : map tu container vao bien userService
	UserService userService;

	@PostMapping("/")
	public ResponseDTO<Void> newUser(
			@ModelAttribute @Valid UserDTO userDTO)
	throws IllegalStateException, IOException{

		if (!userDTO.getFile().isEmpty()) {
			// ten file upload
			String filename = userDTO.getFile().getOriginalFilename();
			// luu lai file vao o cung may chu
			File saveFile = new File("D:/" + filename);
			userDTO.getFile().transferTo(saveFile);
			// lay ten file luu xuong database
			userDTO.setAvatarURL(filename);
		}
		userService.create(userDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create succesful")
				.build();
	}
	
	@GetMapping("/download") // ?filename=abc.jpg
	public void download(@RequestParam("filename") String filename, HttpServletResponse resp) throws IOException {
		File file = new File("D:/" + filename);
		Files.copy(file.toPath(), resp.getOutputStream());
	}

	@DeleteMapping("/") // ?id=1
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		userService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("delete successful").build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<UserDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {

		PageDTO<List<UserDTO>> pageUser = userService.searchName(searchDTO);

		return ResponseDTO.<PageDTO<List<UserDTO>>>
			builder().status(200)
			.msg("search ok")
			.data(pageUser).build();
	}
	

	@PutMapping("/")
	public ResponseDTO<UserDTO> edit(@ModelAttribute @Valid UserDTO userDTO) 
	throws Exception{
		
		if (!userDTO.getFile().isEmpty()) {
			// ten file upload
			String filename = userDTO.getFile().getOriginalFilename();
			// luu lai file vao o cung may chu
			File saveFile = new File("D:/" + filename);
			userDTO.getFile().transferTo(saveFile);
			// lay ten file luu xuong database
			userDTO.setAvatarURL(filename);
		}
		
		userService.update(userDTO);
		return ResponseDTO.<UserDTO>
		builder().status(200)
		.msg("edit ok")
		.data(userDTO).build();
	}


	@GetMapping("/list")
	public ResponseDTO<List<UserDTO>> list() {
		List<UserDTO> usersDtos = userService.getAll();
		return ResponseDTO.<List<UserDTO>>builder()
				.status(200)
				.data(usersDtos)
				.msg("ok").build();
	}

}
