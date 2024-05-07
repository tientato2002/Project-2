package com.example.jmaster.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.jmaster.entity.User;




public interface UserRepo extends JpaRepository<User, Integer>{

	//findBy / deleteBy : mac dinh tu dender query (so sanh bang)
	// tim theo userName
	// select user where username = ? (tu gender query)
	User findByUsername(String username);
	
	// where name = :s (tu gender query)
	Page<User> findByName(String name, Pageable pageable);
	
	
	@Query("SELECT u FROM User u WHERE u.name LIKE %:x% ") //jdql not sql
	Page<User> searchByName(@Param("x") String s,Pageable pageable);
	
	//
	
	@Query("SELECT u FROM User u WHERE u.name LIKE :x OR u.username LIKE :x") //jdql not sql
	List<User> searchByNameAndUsername(@Param("x") String s);
	
	
	@Modifying // cac ham them/sua/xoa them modifying
	@Query("DELETE FROM User u WHERE u.username = :x")
	int deleteUser(@Param("x") String username);
	
	//tu gender lenh xoa
	void deleteByUsername(String username);
	
	
	@Query("SELECT u FROM User u WHERE "
			+ "MONTH(u.birthdate) = :month AND DAY(u.birthdate) = :date") //jdql not sql
	List<User> searchByBirthday(@Param("date") int date,@Param("month") int month);
	
}
