package com.example.jmaster.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;



//oneToOne mac dinh fetch = Eager
//oneToMany mac dinh la Lazy
//manyToOne mac dinh la Eager

// lazy khi select ko join bang

@Entity
@Data
public class Student {
	@Id
	private int userId; //user_id
	
	@OneToOne(cascade = CascadeType.ALL, 
				fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	@MapsId //copy id cua user set cho id cua student
	private User user; //user_id
	
	private String studentCode;
	
	@OneToMany(mappedBy = "student")
	private List<Score> scores;
}
