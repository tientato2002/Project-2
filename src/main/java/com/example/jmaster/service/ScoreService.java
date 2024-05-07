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

import com.example.jmaster.dto.AvgScoreByCourse;
import com.example.jmaster.dto.PageDTO;
import com.example.jmaster.dto.ScoreByStudent;
import com.example.jmaster.dto.ScoreDTO;
import com.example.jmaster.dto.SearchScoreDTO;
import com.example.jmaster.dto.StudentDTO;
import com.example.jmaster.entity.Course;
import com.example.jmaster.entity.Score;
import com.example.jmaster.entity.Student;
import com.example.jmaster.repository.CourseRepo;
import com.example.jmaster.repository.ScoreRepo;
import com.example.jmaster.repository.StudentRepo;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;

public interface ScoreService {
	
	void create(ScoreDTO scoreDTO);
	
	void update(ScoreDTO scoreDTO);
	
	void delete(int id);
	
	ScoreDTO getById(int id);
	
	PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchScoreDTO);
	
	
	//cac ham thong ke
	List<AvgScoreByCourse> avgScoreByCourse();
	
	List<ScoreByStudent> getScoreByStudentId(int id);
	
	PageDTO<List<ScoreDTO>> rankStudent(int courseId);
}

@Service
class ScoreServiceImpl implements ScoreService{

	@Autowired
	ScoreRepo scoreRepo;
	
	@Autowired
	StudentRepo studentRepo;
	
	@Autowired
	CourseRepo courseRepo;

	private ScoreDTO convertToDto(Score score) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(score, ScoreDTO.class);
	}
	
	
	@Override
	@Transactional
	public void create(ScoreDTO scoreDTO) {
		Score score = new ModelMapper().map(scoreDTO, Score.class);
		scoreRepo.save(score);
	}

	@Override
	@Transactional
	public void update(ScoreDTO scoreDTO) {
		Score score = scoreRepo.findById(scoreDTO.getId()).orElseThrow(NoResultException::new);
		if (score != null) {
			score.setScore(scoreDTO.getScore());
			
			Student student = studentRepo.findById(scoreDTO
					.getStudent().getUser().getId()).orElseThrow(NoResultException::new);
			score.setStudent(student);
			
			Course course = courseRepo.findById(scoreDTO
					.getCourse().getId()).orElseThrow(NoResultException::new);
			
			score.setStudent(student);
			score.setCourse(course);
			
			scoreRepo.save(score);
		}
	}

	@Override
	@Transactional
	public void delete(int id) {
		scoreRepo.deleteById(id);
		
	}

	@Override
	public ScoreDTO getById(int id) {
		Score score = 
				scoreRepo.findById(id)
				.orElseThrow(NoResultException::new);

		return convertToDto(score);
	}

	@Override
	public PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchScoreDTO) {
		Sort sortBy = Sort.by("id").ascending();

		if (StringUtils.hasText(searchScoreDTO.getSortedField())) {
			sortBy = Sort.by(searchScoreDTO.getSortedField()).ascending();
		}

		if (searchScoreDTO.getCurentPage() == null) {
			searchScoreDTO.setCurentPage(0);
		}
		if (searchScoreDTO.getSize() == null) {
			searchScoreDTO.setSize(5);
		}
		if (searchScoreDTO.getKeyword() == null) {
			searchScoreDTO.setKeyword("");
		}

		PageRequest pageRequest = PageRequest.of(searchScoreDTO.getCurentPage(), searchScoreDTO.getSize(), sortBy);
		Page<Score> page = scoreRepo.findAll(pageRequest);
		
		if(searchScoreDTO.getCourseId() != null) {
			page = scoreRepo.searchByCourse(searchScoreDTO.getCourseId(), pageRequest);
		}
		else if(searchScoreDTO.getStudentId() != null){
			page = scoreRepo.searchByStudent(searchScoreDTO.getStudentId(), pageRequest);
		}
		
		PageDTO<List<ScoreDTO>> pageDTO = new PageDTO<>();
		
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());
		
//		List<Course> departments = page.getContent();
		List<ScoreDTO> scoreDTOs 
			= page.get().map(u -> convertToDto(u))
			.collect(Collectors.toList());
		pageDTO.setData(scoreDTOs);
		
		return pageDTO;
	}

	@Override
	public List<AvgScoreByCourse> avgScoreByCourse() {
		return scoreRepo.avgScoreByCourse();
	}


	@Override
	public List<ScoreByStudent> getScoreByStudentId(int id) {
		return scoreRepo.getScoreByStudentId(id);
	}


	@Override
	public PageDTO<List<ScoreDTO>> rankStudent(int courseId) {
		Sort sortBy = Sort.by("score").ascending();
		PageRequest pageRequest = PageRequest.of(0, 5,sortBy);
		
		Page<Score> page = scoreRepo.rankStudent(courseId,pageRequest);
		
		PageDTO<List<ScoreDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());
		
		List<ScoreDTO> scoreDTOs 
		= page.get().map(u -> convertToDto(u))
		.collect(Collectors.toList());
		
		pageDTO.setData(scoreDTOs);
		
		return pageDTO;
	}
	
}
