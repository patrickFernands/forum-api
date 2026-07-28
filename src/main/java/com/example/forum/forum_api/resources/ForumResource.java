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

import com.example.forum.forum_api.dtos.ChangeContentDTO;
import com.example.forum.forum_api.dtos.ChangeNameDTO;
import com.example.forum.forum_api.dtos.ForumCreationDTO;
import com.example.forum.forum_api.dtos.ForumCreationResponseDTO;
import com.example.forum.forum_api.dtos.IdResponseDTO;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.services.ForumService;

@RestController
@RequestMapping("/forums")
public class ForumResource {

	@Autowired
	private ForumService forumService;

	@PostMapping()
	public ResponseEntity<ForumCreationResponseDTO> createForum(@RequestBody ForumCreationDTO entity,
			@RequestHeader("User-Id") Long userId) {

		Forum savedForum = forumService.createForum(entity.name(), userId, entity.description());

		ForumCreationResponseDTO response = new ForumCreationResponseDTO(savedForum.getName(),
				savedForum.getCreator().getName());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}/name")
	public ResponseEntity<Void> editName(@PathVariable Long id, @RequestBody ChangeNameDTO entity,
			@RequestHeader("User-Id") Long userId) {

		forumService.editName(userId, id, entity.name());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/description")
	public ResponseEntity<Void> editDescription(@PathVariable Long id, @RequestBody ChangeContentDTO entity,
			@RequestHeader("User-Id") Long userId) {

		forumService.editDescription(userId, id, entity.content());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteForum(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		forumService.deleteForum(userId, id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/admins/{adminId}")
	public ResponseEntity<IdResponseDTO> addAdmin(@PathVariable Long id, @PathVariable Long adminId,
			@RequestHeader("User-Id") Long userId) {

		forumService.addAdmin(userId, adminId, id);

		IdResponseDTO response = new IdResponseDTO(adminId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{id}/admins/{adminId}")
	public ResponseEntity<Void> removeAdmin(@PathVariable Long id, @PathVariable Long adminId,
			@RequestHeader("User-Id") Long userId) {

		forumService.removeAdmin(userId, adminId, id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/ban/{banId}")
	public ResponseEntity<IdResponseDTO> banUser(@PathVariable Long id, @PathVariable Long banId,
			@RequestHeader("User-Id") Long userId) {

		forumService.banUser(userId, banId, id);

		IdResponseDTO response = new IdResponseDTO(banId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{id}/unban/{banId}")
	public ResponseEntity<Void> unbanUser(@PathVariable Long id, @PathVariable Long banId,
			@RequestHeader("User-Id") Long userId) {

		forumService.unbanUser(userId, banId, id);

		return ResponseEntity.noContent().build();
	}

}