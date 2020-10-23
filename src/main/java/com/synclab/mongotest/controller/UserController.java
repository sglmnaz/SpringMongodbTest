package com.synclab.mongotest.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.synclab.mongotest.entity.User;
import com.synclab.mongotest.repository.UserRepository;
import com.synclab.mongotest.utility.Constants;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping(value = "/post", consumes = "application/json")
	public ResponseEntity<String> postUser(@RequestBody User body) {
		Optional<User> userOptional = Optional.of(body);
		
		if(userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		else {
			User user = userOptional.get();
			
			if (user.getName()==null || user.getEmail()==null || user.getUsername()==null || user.getPassword() ==null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
				}
			if (user.getRoles().isEmpty())
				user.addRole(Constants.ROLE_USER);
			
			user.setCreationDate(new Date());
			user = userRepository.save(body);
			return ResponseEntity.ok(user.getName() + " was added to the database");
		}
	}
	
	@GetMapping(value = "/get/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") String id) {
		Optional<User> userOptional = userRepository.findById(id);
		if(userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		else {
			User user = userOptional.get();
			return ResponseEntity.ok(user);
		}
	}
	
	@GetMapping(value = "/get/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable(value = "username") String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if(userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		else {
			User user = userOptional.get();
			return ResponseEntity.ok(user);
		}
	}
	
	@GetMapping(value = "/get/all/{username}")
	public ResponseEntity<List<User>> getAll() {
		List<User> list = userRepository.findAll();
		if(list.isEmpty()) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		else
			return ResponseEntity.ok(list);
	}
}
