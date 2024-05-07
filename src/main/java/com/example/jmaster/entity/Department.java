package com.example.jmaster.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Department {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@CreatedDate //auto gender new date
	@Column(updatable = false)
	private Date createdAt; //java.util
	
	@LastModifiedDate
	private Date updatedAt;
	
	//khong bat buoc / can lay department+danh sach user cua no
	//one department to many users
	//mapby la ten thuoc tinh manytoone ben entity user
	
	@OneToMany(mappedBy = "department",
			fetch = FetchType.LAZY
	//		,cascade = CascadeType.ALL
			) 
	private List<User> users;
}
