package com.example.forum.forum_api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.forum.forum_api.dtos.EditCommentDTO;
import com.example.forum.forum_api.dtos.IdResponseDTO;
import com.example.forum.forum_api.dtos.NewCommentDTO;
import com.example.forum.forum_api.dtos.VoteDTO;
import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.entities.Vote;
import com.example.forum.forum_api.services.CommentService;
import com.example.forum.forum_api.services.CommentVoteService;

@RestController
@RequestMapping("/comments")
public class CommentResource {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentVoteService commentVoteService;

	@PostMapping()
	public ResponseEntity<IdResponseDTO> addComment(@RequestBody NewCommentDTO entity,
			@AuthenticationPrincipal User user) {

		Comment comment = commentService.addComment(user.getId(), entity.text(), entity.postId());

		IdResponseDTO savedComment = new IdResponseDTO(comment.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	}

	@PostMapping("/{id}/replies")
	public ResponseEntity<IdResponseDTO> addReply(@PathVariable Long id, @RequestBody NewCommentDTO entity,
			@AuthenticationPrincipal User user) {

		Comment comment = commentService.addReply(id, user.getId(), entity.text(), entity.postId());

		IdResponseDTO savedComment = new IdResponseDTO(comment.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> editComment(@PathVariable Long id, @RequestBody EditCommentDTO entity,
			@AuthenticationPrincipal User user) {

		commentService.editComment(user.getId(), id, entity.text());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id, @AuthenticationPrincipal User user) {

		commentService.deleteComment(user.getId(), id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/vote")
	public ResponseEntity<IdResponseDTO> addVote(@PathVariable Long id, @RequestBody VoteDTO entity,
			@AuthenticationPrincipal User user) {

		Vote vote = commentVoteService.vote(id, user.getId(), entity.wantsUpvote());

		IdResponseDTO savedVote = new IdResponseDTO(vote.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedVote);

	}
}
