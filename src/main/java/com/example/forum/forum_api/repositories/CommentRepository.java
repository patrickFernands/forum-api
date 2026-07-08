package com.example.forum.forum_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
