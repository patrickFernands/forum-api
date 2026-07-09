package com.example.forum.forum_api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.CommentVote;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.repositories.CommentVoteRepository;

@Service
public class CommentVoteService {

	@Autowired
	private CommentVoteRepository repository;

	@Transactional
	public CommentVote vote(Comment comment, User user, Boolean wantsUpvote) {

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
