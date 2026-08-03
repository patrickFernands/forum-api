package com.example.forum.forum_api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.ChangeContentDTO;
import com.example.forum.forum_api.dtos.ChangeNameDTO;
import com.example.forum.forum_api.dtos.ForumCreationDTO;
import com.example.forum.forum_api.dtos.ForumCreationResponseDTO;
import com.example.forum.forum_api.dtos.ForumSummaryDTO;
import com.example.forum.forum_api.dtos.IdResponseDTO;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.services.ForumService;

@RestController
@RequestMapping("/forums")
public class ForumResource {

	@Autowired
	private ForumService forumService;

	@PostMapping()
	public ResponseEntity<ForumCreationResponseDTO> createForum(@RequestBody ForumCreationDTO entity,
			@AuthenticationPrincipal User user) {

		Forum savedForum = forumService.createForum(entity.name(), user.getId(), entity.description());

		ForumCreationResponseDTO response = new ForumCreationResponseDTO(savedForum.getName(),
				savedForum.getCreator().getName());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}/name")
	public ResponseEntity<Void> editName(@PathVariable Long id, @RequestBody ChangeNameDTO entity,
			@AuthenticationPrincipal User user) {

		forumService.editName(user.getId(), id, entity.name());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/description")
	public ResponseEntity<Void> editDescription(@PathVariable Long id, @RequestBody ChangeContentDTO entity,
			@AuthenticationPrincipal User user) {

		forumService.editDescription(user.getId(), id, entity.content());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteForum(@PathVariable Long id, @AuthenticationPrincipal User user) {

		forumService.deleteForum(user.getId(), id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/admins/{adminId}")
	public ResponseEntity<IdResponseDTO> addAdmin(@PathVariable Long id, @PathVariable Long adminId,
			@AuthenticationPrincipal User user) {

		forumService.addAdmin(user.getId(), adminId, id);

		IdResponseDTO response = new IdResponseDTO(adminId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{id}/admins/{adminId}")
	public ResponseEntity<Void> removeAdmin(@PathVariable Long id, @PathVariable Long adminId,
			@AuthenticationPrincipal User user) {

		forumService.removeAdmin(user.getId(), adminId, id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/ban/{banId}")
	public ResponseEntity<IdResponseDTO> banUser(@PathVariable Long id, @PathVariable Long banId,
			@AuthenticationPrincipal User user) {

		forumService.banUser(user.getId(), banId, id);

		IdResponseDTO response = new IdResponseDTO(banId);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/{id}/unban/{banId}")
	public ResponseEntity<Void> unbanUser(@PathVariable Long id, @PathVariable Long banId,
			@AuthenticationPrincipal User user) {

		forumService.unbanUser(user.getId(), banId, id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping()
	public ResponseEntity<List<ForumSummaryDTO>> listForums() {
		return ResponseEntity.ok(forumService.getAllForums());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ForumSummaryDTO> getForum(@PathVariable Long id) {
		return ResponseEntity.ok(forumService.getForumById(id));
	}

}