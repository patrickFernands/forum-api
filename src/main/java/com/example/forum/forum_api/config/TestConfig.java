package com.example.forum.forum_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.enums.PostStatus;
import com.example.forum.forum_api.services.CommentService;
import com.example.forum.forum_api.services.CommentVoteService;
import com.example.forum.forum_api.services.ForumService;
import com.example.forum.forum_api.services.PostService;
import com.example.forum.forum_api.services.PostVoteService;
import com.example.forum.forum_api.services.UserService;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private ForumService forumService;

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private PostVoteService postVoteService;

	@Autowired
	private CommentVoteService commentVoteService;

	@Override
	public void run(String... args) throws Exception {

		// ==========================================
		// 1. Teste UserService
		// ==========================================
		User user1 = userService.register(new User("Joao da Silva", "jose@gmail.com", "1234", Roles.USER));
		User user2 = userService.register(new User("Juca Bala", "juca@gmail.com", "1234", Roles.USER));
		User admin = userService.register(new User("Admin Master", "admin@reddit.com", "admin123", Roles.ADMIN));
		User troll = userService.register(new User("Hater", "troll@gmail.com", "1234", Roles.USER));
		User userToDelete = userService.register(new User("Temp User", "temp@gmail.com", "1234", Roles.USER));

		userService.findAll();

		userService.changeName(user1.getId(), "Joao Silva");
		userService.changeEmail(user2.getId(), "jucabala_novo@gmail.com");
		userService.changePassword(user2.getId(), "novaSenha123");

		userService.banAccount(admin.getId(), troll.getId());
		userService.unbanAccount(admin.getId(), troll.getId());

		userService.deleteAccount(userToDelete.getId());

		// ==========================================
		// 2. Teste ForumService
		// ==========================================
		Forum forum1 = forumService.createForum("cars", user1.getId(), "All about cars");
		Forum forum2 = forumService.createForum("trucks", user1.getId(), "Big trucks community");
		Forum techForum = forumService.createForum("programming", admin.getId(), "Software Engineering discussions");

		forumService.editName(user1.getId(), forum1.getId(), "automobiles");
		forumService.editDescription(user1.getId(), forum1.getId(), "Updated description: All about cars and vehicles");

		forumService.addAdmin(admin.getId(), user2.getId(), techForum.getId());
		forumService.removeAdmin(admin.getId(), user2.getId(), techForum.getId());

		forumService.banUser(admin.getId(), troll.getId(), techForum.getId());
		forumService.unbanUser(admin.getId(), troll.getId(), techForum.getId());

		forumService.deleteForum(user1.getId(), forum2.getId());

		// ==========================================
		// 3. Teste PostService
		// ==========================================
		Post post1 = postService.addPost(user1.getId(), forum1.getId(), "I like cars", "Cars are really cool.");
		Post post2 = postService.addPost(admin.getId(), techForum.getId(), "Java 21 Released!", "Let's discuss.");
		Post postTroll = postService.addPost(troll.getId(), techForum.getId(), "Java is terrible", "Worst language.");

		postService.editTitle(user1.getId(), post1.getId(), "I love JDM cars");
		postService.editContent(user1.getId(), post1.getId(), "Content updated with details about Honda and Toyota.");

		postService.approvePost(admin.getId(), post1.getId());
		post1.setStatus(PostStatus.APPROVED);

		postService.approvePost(admin.getId(), post2.getId());
		post2.setStatus(PostStatus.APPROVED);

		postService.lockPost(admin.getId(), post2.getId());
		postService.unlockPost(admin.getId(), post2.getId());

		postService.deletePost(admin.getId(), postTroll.getId());

		// ==========================================
		// 4. Teste CommentService
		// ==========================================
		Comment c1 = commentService.addComment(user2.getId(), "I agree, cars are amazing!", post1.getId());
		Comment c2 = commentService.addComment(user1.getId(), "Java 21 Virtual Threads are game changers!", post2.getId());

		Comment reply = commentService.addReply(c2.getId(), user2.getId(), "Totally! Thread performance is awesome now.",
				post2.getId());

		commentService.editComment(user2.getId(), c1.getId(), "I totally agree! Japanese cars are super reliable.");
		commentService.deleteComment(user2.getId(), reply.getId());

		// ==========================================
		// 5. Teste PostVoteService
		// ==========================================
		postVoteService.vote(post1.getId(), user1.getId(), true);
		postVoteService.vote(post1.getId(), user2.getId(), false);

		// ==========================================
		// 6. Teste CommentVoteService
		// ==========================================
		commentVoteService.vote(c1.getId(), user1.getId(), true);
		commentVoteService.vote(c2.getId(), user2.getId(), true);

		commentVoteService.vote(c1.getId(), user1.getId(), false);
		commentVoteService.vote(c1.getId(), user1.getId(), false);
	}
}