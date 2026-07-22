package com.example.forum.forum_api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.PostVote;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.PostStatus;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.PostRepository;
import com.example.forum.forum_api.repositories.PostVoteRepository;
import com.example.forum.forum_api.repositories.UserRepository;

@Service
public class PostVoteService {

	@Autowired
	private PostVoteRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Transactional
	public PostVote vote(Long postId, Long userId, Boolean wantsUpvote) {

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new DomainException("Post not found!"));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new DomainException("User not found!"));

		if (post.getIsLocked() || post.getIsDeleted()) {
			throw new DomainException("You can't vote on locked or deleted posts");
		}

		if (post.getStatus().equals(PostStatus.PENDING)) {
			throw new DomainException("Pending posts can't receive votes");
		}

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
