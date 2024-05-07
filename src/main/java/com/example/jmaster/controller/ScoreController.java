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

import com.example.jmaster.dto.AvgScoreByCourse;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.ResponseDTO;
import com.example.jmaster.dto.ScoreByStudent;
import com.example.jmaster.dto.ScoreDTO;
import com.example.jmaster.dto.SearchScoreDTO;
import com.example.jmaster.service.ScoreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/score")
public class ScoreController {

	@Autowired
	ScoreService scoreService;

	@GetMapping("/")
	public ResponseDTO<ScoreDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<ScoreDTO>builder()
				.status(200)
				.data(scoreService.getById(id)).build();
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
	public ResponseDTO<Void> createNewJson(@RequestBody @Valid ScoreDTO scoreDTO) {
		scoreService.create(scoreDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("create succesful")
				.build();
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		scoreService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("delete successful").build();
	}

	@PutMapping("/")
	public ResponseDTO<ScoreDTO> edit(@RequestBody @Valid ScoreDTO scoreDTO) {
		scoreService.update(scoreDTO);
		return ResponseDTO.<ScoreDTO>builder()
				.status(200)
				.msg("edit successful")
				.data(scoreDTO).build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<ScoreDTO>>> search(@RequestBody @Valid SearchScoreDTO searchScoreDTO) {
		PageDTO<List<ScoreDTO>> pageDTO = scoreService.search(searchScoreDTO);
		return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder()
				.status(200)
				.data(pageDTO).build();
	}

	
	@GetMapping("/avg-score-by-course")
	public ResponseDTO<List<AvgScoreByCourse>> avgScoreByCourse(){
		return ResponseDTO.<List<AvgScoreByCourse>>builder()
				.status(200)
				.data(scoreService.avgScoreByCourse())
				.build();
	}
	@GetMapping("/score-by-student")
	public ResponseDTO<List<ScoreByStudent>> getScoreByStudent(@RequestParam("id") int id){
		return ResponseDTO.<List<ScoreByStudent>>builder()
				.status(200)
				.data(scoreService.getScoreByStudentId(id))
				.build();
	}
	
	@GetMapping("/rank-student-by-coure")
	public ResponseDTO<PageDTO<List<ScoreDTO>>> rankStudent(@RequestParam("id") int id){
		
		return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder()
				.status(200)
				.data(scoreService.rankStudent(id))
				.build();
	}
	
}
