package com.example.forum.forum_api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.CommentVote;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.CommentRepository;
import com.example.forum.forum_api.repositories.CommentVoteRepository;
import com.example.forum.forum_api.repositories.UserRepository;

@Service
public class CommentVoteService {

	@Autowired
	private CommentVoteRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Transactional
	public CommentVote vote(Long commentId, Long userId, Boolean wantsUpvote) {

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new DomainException("Comment not found!"));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new DomainException("User not found!"));

		if (comment.getIsDeleted()) {
			throw new DomainException("You can't vote on deleted comments");
		}

		Optional<CommentVote> existingVote = repository.findByCommentAndVoter(comment, user);

		if (existingVote.isEmpty()) {
			CommentVote commentVote = new CommentVote(user, wantsUpvote, comment);
			repository.save(commentVote);
			return commentVote;
		}

		CommentVote vote = existingVote.get();
		boolean currentVote = vote.getIsUpvote();

		if (currentVote && wantsUpvote || !currentVote && !wantsUpvote) {

			repository.delete(vote);
			return null;

		}

		else if (currentVote && !wantsUpvote) {

			vote.downvote();

		}

		else {

			vote.upvote();

		}

		repository.save(vote);
		return vote;

	}

}
