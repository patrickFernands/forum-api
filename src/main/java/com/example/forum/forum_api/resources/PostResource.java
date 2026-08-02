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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.ChangeContentDTO;
import com.example.forum.forum_api.dtos.ChangeTitleDTO;
import com.example.forum.forum_api.dtos.IdResponseDTO;
import com.example.forum.forum_api.dtos.NewPostDTO;
import com.example.forum.forum_api.dtos.PostDetailDTO;
import com.example.forum.forum_api.dtos.PostSummaryDTO;
import com.example.forum.forum_api.dtos.VoteDTO;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.entities.Vote;
import com.example.forum.forum_api.services.PostService;
import com.example.forum.forum_api.services.PostVoteService;

@RestController
@RequestMapping("/posts")
public class PostResource {

	@Autowired
	private PostService postService;

	@Autowired
	private PostVoteService postVoteService;

	@PostMapping()
	public ResponseEntity<IdResponseDTO> addPost(@RequestBody NewPostDTO entity, @AuthenticationPrincipal User user) {

		Post newPost = postService.addPost(user.getId(), entity.forumId(), entity.title(), entity.content());

		IdResponseDTO response = new IdResponseDTO(newPost.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}/title")
	public ResponseEntity<Void> editTitle(@PathVariable Long id, @RequestBody ChangeTitleDTO entity,
			@AuthenticationPrincipal User user) {

		postService.editTitle(user.getId(), id, entity.title());

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/content")
	public ResponseEntity<Void> editContent(@PathVariable Long id, @RequestBody ChangeContentDTO entity,
			@AuthenticationPrincipal User user) {

		postService.editContent(user.getId(), id, entity.content());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user) {

		postService.deletePost(user.getId(), id);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/approve")
	public ResponseEntity<Void> approvePost(@PathVariable Long id, @AuthenticationPrincipal User user) {

		postService.approvePost(user.getId(), id);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/lock")
	public ResponseEntity<Void> lockPost(@PathVariable Long id, @AuthenticationPrincipal User user) {

		postService.lockPost(user.getId(), id);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/unlock")
	public ResponseEntity<Void> unlockPost(@PathVariable Long id, @AuthenticationPrincipal User user) {

		postService.unlockPost(user.getId(), id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/vote")
	public ResponseEntity<IdResponseDTO> addVote(@PathVariable Long id, @RequestBody VoteDTO entity,
			@AuthenticationPrincipal User user) {

		Vote vote = postVoteService.vote(id, user.getId(), entity.wantsUpvote());

		IdResponseDTO savedVote = new IdResponseDTO(vote.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedVote);

	}

	@GetMapping()
	public ResponseEntity<List<PostSummaryDTO>> listPosts(@RequestParam Long forumId) {
		return ResponseEntity.ok(postService.getPostsByForum(forumId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDetailDTO> getPost(@PathVariable Long id) {
		return ResponseEntity.ok(postService.getPostById(id));
	}

	@GetMapping("/search")
	public ResponseEntity<List<PostSummaryDTO>> search(@RequestParam String q) {
		return ResponseEntity.ok(postService.searchPosts(q));
	}

}
