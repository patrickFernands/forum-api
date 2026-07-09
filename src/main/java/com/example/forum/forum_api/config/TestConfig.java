package com.example.forum.forum_api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
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

		User user1 = new User("jose@gmail.com", "joao", "1234", Roles.USER);
		User user2 = new User("juca@gmail.com", "juca", "1234", Roles.USER);

		Forum forum1 = new Forum("cars", user1);
		Forum forum2 = new Forum("trucks", user1);

		userRepository.saveAll(Arrays.asList(user1, user2));

		forumRepository.saveAll(Arrays.asList(forum1, forum2));

		Post post1 = new Post(user1, forum1, "i like cars", "cars are cool");

		postRepository.saveAll(Arrays.asList(post1));

		forum1.addPost(post1);
		forumRepository.saveAll(Arrays.asList(forum1));

	}

}
