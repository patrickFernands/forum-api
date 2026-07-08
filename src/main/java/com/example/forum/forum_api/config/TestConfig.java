package com.example.forum.forum_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.forum.forum_api.repositories.CommentRepository;
import com.example.forum.forum_api.repositories.ForumRepository;
import com.example.forum.forum_api.repositories.PostRepository;
import com.example.forum.forum_api.repositories.UserRepository;
import com.example.forum.forum_api.repositories.VoteRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private ForumRepository forumRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Override
	public void run(String... args) throws Exception {

	}

}
