package com.example.forum.forum_api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.forum_api.entities.User;
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
	public void changeName(User loggedUser, Long id, String name) {

		if (name == null || name.isBlank()) {
			throw new DomainException("Title can't be empty");
		}

		User user = repository.findById(id).orElseThrow(() -> new DomainException("User not found!"));

		if (!loggedUser.getId().equals(id)) {
			throw new DomainException("You can only change your name");
		}

		if (repository.findByName(name).isPresent()) {
			throw new DomainException("This name is already in use");
		}

		user.setName(name);

		repository.save(user);
	}

	@Transactional
	public void changeEmail(User loggedUser, Long id, String email) {

		User user = repository.findById(id).orElseThrow(() -> new DomainException("User not found!"));

		if (!loggedUser.getId().equals(id)) {
			throw new DomainException("You can only change your email");
		}

		if (repository.findByEmail(email).isPresent()) {
			throw new DomainException("This email is already in use");
		}

		user.setEmail(email);
		repository.save(user);
	}

	@Transactional
	public void changePassword(User loggedUser, Long id, String password) {

		User user = repository.findById(id).orElseThrow(() -> new DomainException("User not found!"));

		if (!loggedUser.getId().equals(id)) {
			throw new DomainException("You can only change your password");
		}

		user.setPassword(password);
		repository.save(user);
	}

	@Transactional
	public void deleteAccount(User loggedUser, Long id) {

		User user = repository.findById(id)
				.orElseThrow(() -> new DomainException("User not found!"));

		if (!loggedUser.getId().equals(id)) {
			throw new DomainException("You can't delete someone else account");
		}

		user.setName("[deleted_" + user.getId() + "]");
		user.setEmail("deleted_" + user.getId() + "@deleted.com");
		user.setPassword("DELETED");

		user.setIsBanned(true);

		repository.save(user);
	}

	@Transactional
	public void banAccount(Long userId) {

		User user = repository.findById(userId)
				.orElseThrow(() -> new DomainException("User not found!"));

		user.setIsBanned(true);
		repository.save(user);
	}

	@Transactional
	public void unbanAccount(Long userId) {

		User user = repository.findById(userId)
				.orElseThrow(() -> new DomainException("User not found!"));

		user.setIsBanned(false);
		repository.save(user);
	}

}
