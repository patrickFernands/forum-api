package com.example.forum.forum_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.PostVote;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.entities.Vote;

public interface PostVoteRepository extends JpaRepository<PostVote, Long> {
	Optional<PostVote> findByPostAndVoter(Post post, User voter);
}
