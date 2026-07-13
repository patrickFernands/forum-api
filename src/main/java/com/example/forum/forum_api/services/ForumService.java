package com.example.forum.forum_api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.ForumRepository;

@Service
public class ForumService {

	@Autowired
	private ForumRepository repository;

	@Transactional
	public Forum createForum(Forum forum) {

		User creator = forum.getCreator();

		if (creator.getIsBanned()) {
			throw new DomainException("Banned users can't create forums");
		}

		if (forum.getName() == null || forum.getName().isBlank()) {
			throw new DomainException("Your forum must have a name");
		}

		if (forum.getDescription() == null || forum.getDescription().isBlank()) {
			throw new DomainException("Your forum must have a description");
		}

		if (repository.findByName(forum.getName()).isPresent()) {
			throw new DomainException("This name is already in use");
		}

		Forum savedForum = repository.save(forum);
		return savedForum;
	}


	@Transactional
	public void editName(User user, Long id, String name){

		Forum forum = repository.findById(id).orElseThrow(() -> new DomainException("Forum not found!"));

		if(!forum.getCreator().equals(user)){
			throw new DomainException("You aren't allowed to edit the name.");
		}

		if (name == null || name.isBlank()) {
			throw new DomainException("Your forum must have a name");
		}

		if (repository.findByName(name).isPresent()) {
			throw new DomainException("This name is already in use");
		}

		forum.setName(name);
		repository.save(forum);
	}


	@Transactional
	public void editDescription(User user, Long id, String content){

		Forum forum = repository.findById(id).orElseThrow(() -> new DomainException("Forum not found!"));

		if(!forum.getCreator().equals(user)){
			throw new DomainException("You aren't allowed to edit the description.");
		}

		if (content == null || content.isBlank()) {
			throw new DomainException("Your forum must have a description");
		}

		forum.setDescription(content);
		repository.save(forum);
	}



	//remover/adicionar admins, excluir forum.

}
