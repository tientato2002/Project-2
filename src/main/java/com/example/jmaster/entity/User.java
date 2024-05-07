package com.example.jmaster.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Table(name = "user") // map to table SQL
@Entity  
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private int age;
	private String name;
	//luu ten file path
	private String avatarURL;
	
	@ElementCollection
	@CollectionTable(name = "user_role",
		joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private List<String> roles;
	
	@Column(unique = true)
	private String username;
	private String password;
	
	@ManyToOne // bat buoc kieu du lieu Entity 
	private Department department;
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	private String email;
}
