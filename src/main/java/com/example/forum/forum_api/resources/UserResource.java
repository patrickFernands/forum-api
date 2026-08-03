package com.example.forum.forum_api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.ChangeEmailDTO;
import com.example.forum.forum_api.dtos.ChangeNameDTO;
import com.example.forum.forum_api.dtos.ChangePasswordDTO;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.services.UserService;

@RestController
@RequestMapping("/users")
public class UserResource {

	@Autowired
	private UserService userService;

	@PutMapping("/{id}/name")
	public ResponseEntity<Void> changeName(@PathVariable Long id, @AuthenticationPrincipal User user,
			@RequestBody ChangeNameDTO entity) {

		if (!id.equals(user.getId())) {
			throw new DomainException("You can only edit your own account");
		}

		userService.changeName(user.getId(), entity.name());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/email")
	public ResponseEntity<Void> changeEmail(@PathVariable Long id, @AuthenticationPrincipal User user,
			@RequestBody ChangeEmailDTO entity) {

		if (!id.equals(user.getId())) {
			throw new DomainException("You can only edit your own account");
		}

		userService.changeEmail(id, entity.email());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<Void> changePassword(@PathVariable Long id, @AuthenticationPrincipal User user,
			@RequestBody ChangePasswordDTO entity) {

		if (!id.equals(user.getId())) {
			throw new DomainException("You can only edit your own account");
		}

		userService.changePassword(id, entity.password());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<Void> deleteAccount(@PathVariable Long id, @AuthenticationPrincipal User user) {

		if (!id.equals(user.getId())) {
			throw new DomainException("You can only delete your own account");
		}

		userService.deleteAccount(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/ban")
	public ResponseEntity<Void> ban(@PathVariable Long id, @AuthenticationPrincipal User user) {

		userService.banAccount(user.getId(), id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/unban")
	public ResponseEntity<Void> unban(@PathVariable Long id, @AuthenticationPrincipal User user) {

		userService.unbanAccount(user.getId(), id);
		return ResponseEntity.noContent().build();
	}

}
