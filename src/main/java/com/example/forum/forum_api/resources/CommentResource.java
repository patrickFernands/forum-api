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

import com.example.forum.forum_api.dtos.EditCommentDTO;
import com.example.forum.forum_api.dtos.NewCommentDTO;
import com.example.forum.forum_api.dtos.NewCommentResponseDTO;
import com.example.forum.forum_api.dtos.UserIdDTO;
import com.example.forum.forum_api.dtos.VoteDTO;
import com.example.forum.forum_api.dtos.VoteResponseDTO;
import com.example.forum.forum_api.entities.Comment;
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
	public ResponseEntity<NewCommentResponseDTO> addComment(@RequestBody NewCommentDTO entity) {

		Comment comment = commentService.addComment(entity.authorId(), entity.text(), entity.postId());

		NewCommentResponseDTO savedComment = new NewCommentResponseDTO(comment.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	}

	@PostMapping("/{id}/replies")
	public ResponseEntity<NewCommentResponseDTO> addReply(@PathVariable Long id, @RequestBody NewCommentDTO entity) {

		Comment comment = commentService.addReply(id, entity.authorId(), entity.text(), entity.postId());

		NewCommentResponseDTO savedComment = new NewCommentResponseDTO(comment.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> editComment(@PathVariable Long id, @RequestBody EditCommentDTO entity) {

		commentService.editComment(entity.userId(), id, entity.text());

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {

		commentService.deleteComment(userId, id);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/vote")
	public ResponseEntity<VoteResponseDTO> addVote(@PathVariable Long id, @RequestBody VoteDTO entity) {

		Vote vote = commentVoteService.vote(id, entity.voterId(), entity.wantsUpvote());

		VoteResponseDTO savedVote = new VoteResponseDTO(vote.getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(savedVote);

	}
}
