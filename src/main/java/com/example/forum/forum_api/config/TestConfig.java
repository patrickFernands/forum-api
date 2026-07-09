package com.example.forum.forum_api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.forum.forum_api.entities.Comment;
import com.example.forum.forum_api.entities.CommentVote;
import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.Post;
import com.example.forum.forum_api.entities.PostVote;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.repositories.CommentRepository;
import com.example.forum.forum_api.repositories.CommentVoteRepository;
import com.example.forum.forum_api.repositories.ForumRepository;
import com.example.forum.forum_api.repositories.PostRepository;
import com.example.forum.forum_api.repositories.PostVoteRepository;
import com.example.forum.forum_api.repositories.UserRepository;

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

	// Substituímos o VoteRepository genérico pelos específicos
	@Autowired
	private PostVoteRepository postVoteRepository;

	@Autowired
	private CommentVoteRepository commentVoteRepository;

	@Override
	public void run(String... args) throws Exception {

		// 1. CRIANDO USUÁRIOS
		User user1 = new User("jose@gmail.com", "Joao da Silva", "1234", Roles.USER);
		User user2 = new User("juca@gmail.com", "Juca Bala", "1234", Roles.USER);
		User admin = new User("admin@reddit.com", "Admin Master", "admin123", Roles.ADMIN);
		User troll = new User("troll@gmail.com", "Hater", "1234", Roles.USER);

		userRepository.saveAll(Arrays.asList(user1, user2, admin, troll));

		// 2. CRIANDO FÓRUNS
		Forum forum1 = new Forum("cars", user1);
		Forum forum2 = new Forum("trucks", user1);
		Forum techForum = new Forum("programming", admin);

		forumRepository.saveAll(Arrays.asList(forum1, forum2, techForum));

		// 3. CRIANDO POSTS
		Post post1 = new Post(user1, forum1, "I like cars", "Cars are really cool, especially JDM ones.");
		Post post2 = new Post(user2, forum1, "What is the best car?", "I want to buy a new one, suggestions?");
		Post post3 = new Post(admin, techForum, "Java 21 Released!", "Let's discuss the new features.");
		Post postTroll = new Post(troll, techForum, "Java is terrible", "Worst language ever."); // Post para ser apagado

		postRepository.saveAll(Arrays.asList(post1, post2, post3, postTroll));

		// 4. CRIANDO COMENTÁRIOS E RESPOSTAS (ANINHAMENTO)
		Comment c1 = new Comment(user2, "I agree, cars are amazing!", post1);
		Comment c2 = new Comment(user1, "Toyota and Honda are the most reliable.", post2);
		Comment trollComment = new Comment(troll, "Nobody cares about your opinion.", post1); // Comentário para ser apagado

		// Salvamos os comentários raízes primeiro para gerar o ID
		commentRepository.saveAll(Arrays.asList(c1, c2, trollComment));

		// Criando uma resposta a um comentário existente
		Comment nestedComment = new Comment(user2, "True! I had a Civic and it never broke down.", post2);
		c2.addNestedComment(nestedComment); // Faz o vínculo bidirecional

		// Salvamos o comentário aninhado (e atualizamos o pai se necessário)
		commentRepository.saveAll(Arrays.asList(c2, nestedComment));

		// 5. REGISTRANDO VOTOS (Usando as novas classes filhas)
		PostVote pv1 = new PostVote(user2, true, post1); // user2 dá upvote no post1
		PostVote pv2 = new PostVote(user1, true, post3); // user1 dá upvote na notícia do admin
		PostVote pv3 = new PostVote(troll, false, post3); // troll dá downvote na notícia

		CommentVote cv1 = new CommentVote(user1, true, c1); // user1 dá upvote no comentário c1

		// Separamos o salvamento usando os repositórios corretos
		postVoteRepository.saveAll(Arrays.asList(pv1, pv2, pv3));
		commentVoteRepository.saveAll(Arrays.asList(cv1));

		// 6. APLICANDO REGRAS DE NEGÓCIO E ATUALIZANDO O BANCO

		// Apagando lógicamente o post do troll
		postTroll.setIsDeleted();
		postRepository.save(postTroll);

		// Apagando logicamente um comentário ofensivo
		trollComment.setIsDeleted();
		commentRepository.save(trollComment);

		// Banindo o troll do fórum de tecnologia
		techForum.banUser(troll);
		forumRepository.save(techForum);

	}

}
