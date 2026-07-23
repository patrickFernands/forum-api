package com.example.forum.forum_api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.UserRegisterDTO;
import com.example.forum.forum_api.dtos.UserRegisterResponseDTO;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserResource {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserRegisterResponseDTO> register(@RequestBody UserRegisterDTO user) {

		String encryptedPassword = user.password();

		User newUser = new User(user.name(), user.email(), encryptedPassword, user.role());

		User savedUser = userService.register(newUser);

		UserRegisterResponseDTO response = new UserRegisterResponseDTO(savedUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/*
	 * PUT /users/me/name ➔ Edita o próprio nome (changeName).
	 * PUT /users/me/email ➔Edita o próprio e-mail (changeEmail). PUT
	 * /users/me/password ➔ Edita a própria senha (changePassword).
	 * DELETE /users/me ➔ Deleta a própria conta(deleteAccount).
	 * PUT /users/{id}/ban ➔ Bane um usuário (Admin - banAccount).
	 * PUT /users/{id}/unban ➔ Desbane um usuário (Admin - unbanAccount).
	 */

}
