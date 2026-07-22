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
		User user1 = userService.register(new User("jose@gmail.com", "Joao da Silva", "1234", Roles.USER));
		User user2 = userService.register(new User("juca@gmail.com", "Juca Bala", "1234", Roles.USER));
		User admin = userService.register(new User("admin@reddit.com", "Admin Master", "admin123", Roles.ADMIN));
		User troll = userService.register(new User("troll@gmail.com", "Hater", "1234", Roles.USER));
		User userToDelete = userService.register(new User("temp@gmail.com", "Temp User", "1234", Roles.USER));

		userService.findAll();

		// Apenas o ID de quem está alterando o próprio perfil
		userService.changeName(user1.getId(), "Joao Silva");
		userService.changeEmail(user2.getId(), "jucabala_novo@gmail.com");
		userService.changePassword(user2.getId(), "novaSenha123");

		// Passando ID do Admin logado e o ID do alvo
		userService.banAccount(admin.getId(), troll.getId());
		userService.unbanAccount(admin.getId(), troll.getId());

		// Apenas o ID de quem está deletando a própria conta
		userService.deleteAccount(userToDelete.getId());

		// ==========================================
		// 2. Teste ForumService
		// ==========================================
		Forum forum1 = forumService.createForum(new Forum("cars", user1, "All about cars"));
		Forum forum2 = forumService.createForum(new Forum("trucks", user1, "Big trucks community"));
		Forum techForum = forumService.createForum(new Forum("programming", admin, "Software Engineering discussions"));

		// Substituindo entidade User pelo ID do usuário logado
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
		Post post1 = postService.addPost(new Post(user1, forum1, "I like cars", "Cars are really cool."));
		Post post2 = postService.addPost(new Post(admin, techForum, "Java 21 Released!", "Let's discuss."));
		Post postTroll = postService.addPost(new Post(troll, techForum, "Java is terrible", "Worst language."));

		// Substituindo entidade User pelo ID do usuário logado
		postService.editTitle(user1.getId(), post1.getId(), "I love JDM cars");
		postService.editContent(user1.getId(), post1.getId(), "Content updated with details about Honda and Toyota.");

		// APROVANDO OS POSTS PARA PERMITIR COMENTÁRIOS E VOTOS (com fix em memória)
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
		Comment c1 = commentService.addComment(new Comment(user2, "I agree, cars are amazing!", post1));
		Comment c2 = commentService.addComment(new Comment(user1, "Java 21 Virtual Threads are game changers!", post2));

		Comment reply = commentService.addReply(c2.getId(),
				new Comment(user2, "Totally! Thread performance is awesome now.", post2));

		// Substituindo entidade User pelo ID do usuário logado
		commentService.editComment(user2.getId(), c1.getId(), "I totally agree! Japanese cars are super reliable.");
		commentService.deleteComment(user2.getId(), reply.getId());

		// ==========================================
		// 5. Teste PostVoteService
		// ==========================================
		postVoteService.vote(post1.getId(), user2.getId(), true);
		postVoteService.vote(post1.getId(), user2.getId(), false);
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