package com.example.forum.forum_api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.ChangeEmailDTO;
import com.example.forum.forum_api.dtos.ChangeNameDTO;
import com.example.forum.forum_api.dtos.ChangePasswordDTO;
import com.example.forum.forum_api.dtos.UserRegisterDTO;
import com.example.forum.forum_api.dtos.UserRegisterResponseDTO;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.services.UserService;

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

	@PutMapping("/{id}/name")
	public ResponseEntity<Void> changeName(@PathVariable Long id, @RequestBody ChangeNameDTO entity) {

		userService.changeName(id, entity.name());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/email")
	public ResponseEntity<Void> changeEmail(@PathVariable Long id, @RequestBody ChangeEmailDTO entity) {

		userService.changeEmail(id, entity.email());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO entity) {

		userService.changePassword(id, entity.password());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {

		userService.deleteAccount(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/ban")
	public ResponseEntity<Void> ban(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		userService.banAccount(userId, id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/unban")
	public ResponseEntity<Void> unban(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		userService.unbanAccount(userId, id);
		return ResponseEntity.noContent().build();
	}

}
