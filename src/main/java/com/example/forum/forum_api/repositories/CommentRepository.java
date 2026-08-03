package com.example.forum.forum_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.CommentVote;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
