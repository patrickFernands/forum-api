package com.example.forum.forum_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByForumAndIsDeletedFalse(Forum forum);

	List<Post> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title);
}
