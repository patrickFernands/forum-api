package com.example.forum.forum_api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.PostVote;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.repositories.PostVoteRepository;

@Service
public class PostVoteService {

	@Autowired
	private PostVoteRepository repository;

	@Transactional
	public PostVote vote(Post post, User user, Boolean wantsUpvote) {

		Optional<PostVote> existingVote = repository.findByPostAndVoter(post, user);

		if (existingVote.isEmpty()) {

			PostVote postVote = new PostVote(user, wantsUpvote, post);
			repository.save(postVote);
			return postVote;
		}

		PostVote vote = existingVote.get();
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
