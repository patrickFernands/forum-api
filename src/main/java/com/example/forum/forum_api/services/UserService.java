package com.example.forum.forum_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.UserRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public List<User> findAll() {
		return repository.findAll();
	}

	@Transactional
	public User register(User user) {

		if (repository.findByEmail(user.getEmail()).isPresent()) {
			throw new DomainException("This email is already in use");
		}

		if (repository.findByName(user.getName()).isPresent()) {
			throw new DomainException("This name is already in use");
		}

		User savedUser = repository.save(user);
		return savedUser;
	}

	@Transactional
	public void changeName(Long id, String name) {

		if (name == null || name.isBlank()) {
			throw new DomainException("Name can't be empty");
		}

		User user = repository.findById(id).orElseThrow(() -> new DomainException("User not found!"));

		if (name.equals(user.getName())) {
			throw new DomainException("Enter a different name!");
		}

		if (repository.findByName(name).isPresent()) {
			throw new DomainException("This name is already in use");
		}

		user.setName(name);
		repository.save(user);
	}

	@Transactional
	public void changeEmail(Long id, String email) {

		User user = repository.findById(id).orElseThrow(() -> new DomainException("User not found!"));

		if (email == null || email.isBlank()) {
			throw new DomainException("Email can't be empty");
		}

		if (email.equals(user.getEmail())) {
			throw new DomainException("Enter a different email!");
		}

		if (repository.findByEmail(email).isPresent()) {
			throw new DomainException("This email is already in use");
		}

		user.setEmail(email);
		repository.save(user);
	}

	@Transactional
	public void changePassword(Long id, String password) {

		User user = repository.findById(id).orElseThrow(() -> new DomainException("User not found!"));

		user.setPassword(password);
		repository.save(user);
	}

	@Transactional
	public void deleteAccount(Long id) {

		User user = repository.findById(id)
				.orElseThrow(() -> new DomainException("User not found!"));

		user.setName("[deleted_" + user.getId() + "]");
		user.setEmail("deleted_" + user.getId() + "@deleted.com");
		user.setPassword("DELETED");

		user.setIsBanned(true);

		repository.save(user);
	}

	@Transactional
	public void banAccount(Long id, Long userId) {

		User admin = repository.findById(id)
				.orElseThrow(() -> new DomainException("User not found!"));

		if (!admin.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to ban users");
		}

		User user = repository.findById(userId)
				.orElseThrow(() -> new DomainException("User not found!"));

		user.setIsBanned(true);
		repository.save(user);
	}

	@Transactional
	public void unbanAccount(Long id, Long userId) {

		User admin = repository.findById(id)
				.orElseThrow(() -> new DomainException("User not found!"));

		if (!admin.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to unban users");
		}

		User user = repository.findById(userId)
				.orElseThrow(() -> new DomainException("User not found!"));

		user.setIsBanned(false);
		repository.save(user);
	}

}
