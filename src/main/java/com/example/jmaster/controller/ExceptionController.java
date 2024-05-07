package com.example.jmaster.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.jmaster.dto.ResponseDTO;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

	@ExceptionHandler({ NoResultException.class })
	public ResponseDTO<String> notFound(NoResultException e) {
		log.info("INFO", e);
		// cach1:
//		return ResponseDTO.<String>builder()
//				.status(404).msg("No Data").build();
		// cach2:
//		ResponseDTO<String> responseDTO = new ResponseDTO<>();
//		responseDTO.setStatus(404);
//		responseDTO.setMsg("No Data");
		// cach3:
		ResponseDTO<String> responseDTO = new ResponseDTO<>(400, "No Data");
		return responseDTO;
	}

	@ExceptionHandler({ BindException.class })
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) // HTTP Status code(ko co mac dinh la 200)
	public ResponseDTO<String> badRequest(BindException e) {
		log.info("bad request");

		List<ObjectError> errors = e.getBindingResult().getAllErrors();
		String msg = "";
		for (ObjectError err : errors) {
			FieldError fieldError = (FieldError) err;

			msg += fieldError.getField() + ":" + err.getDefaultMessage() + ";";
		}
		return ResponseDTO.<String>builder().status(400).msg(msg).build();
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public ResponseDTO<String> duplicate(Exception e) {
		log.info("INFO", e);

		return ResponseDTO.<String>builder().status(409).msg("Duplicate Data").build();
	}
}
