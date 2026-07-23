package com.example.forum.forum_api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.ChangeContentDTO;
import com.example.forum.forum_api.dtos.ChangeTitleDTO;
import com.example.forum.forum_api.dtos.IdResponseDTO;
import com.example.forum.forum_api.dtos.NewPostDTO;
import com.example.forum.forum_api.dtos.VoteDTO;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.Vote;
import com.example.forum.forum_api.services.PostService;
import com.example.forum.forum_api.services.PostVoteService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/posts")
public class PostResource {

	@Autowired
	private PostService postService;

	@Autowired
	private PostVoteService postVoteService;

	@PostMapping()
	public ResponseEntity<IdResponseDTO> addPost(@RequestBody NewPostDTO entity, @RequestHeader("User-Id") Long userId) {

		Post newPost = postService.addPost(userId, entity.forumId(), entity.title(), entity.content());

		IdResponseDTO response = new IdResponseDTO(newPost.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}/title")
	public ResponseEntity<Void> editTitle(@PathVariable Long id, @RequestBody ChangeTitleDTO entity,
			@RequestHeader("User-Id") Long userId) {

		postService.editTitle(userId, id, entity.title());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/content")
	public ResponseEntity<Void> editContent(@PathVariable Long id, @RequestBody ChangeContentDTO entity,
			@RequestHeader("User-Id") Long userId) {

		postService.editContent(userId, id, entity.content());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		postService.deletePost(userId, id);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<Void> approvePost(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		postService.approvePost(userId, id);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/lock")
	public ResponseEntity<Void> lockPost(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		postService.lockPost(userId, id);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/unlock")
	public ResponseEntity<Void> unlockPost(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		postService.unlockPost(userId, id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/vote")
	public ResponseEntity<IdResponseDTO> addVote(@PathVariable Long id, @RequestBody VoteDTO entity,
			@RequestHeader("User-Id") Long userId) {

		Vote vote = postVoteService.vote(id, userId, entity.wantsUpvote());

		IdResponseDTO savedVote = new IdResponseDTO(vote.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedVote);

	}

}
