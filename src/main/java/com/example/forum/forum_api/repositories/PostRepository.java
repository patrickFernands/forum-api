package com.example.forum.forum_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
