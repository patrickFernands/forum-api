package com.example.forum.forum_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.CommentVote;
import com.example.forum.forum_api.entities.User;

public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
	Optional<CommentVote> findByCommentAndVoter(Comment comment, User voter);
}
