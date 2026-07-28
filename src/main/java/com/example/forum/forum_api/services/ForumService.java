package com.example.forum.forum_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forum.forum_api.entities.Forum;
import com.example.forum.forum_api.entities.User;
import com.example.forum.forum_api.enums.Roles;
import com.example.forum.forum_api.exceptions.DomainException;
import com.example.forum.forum_api.repositories.ForumRepository;
import com.example.forum.forum_api.repositories.UserRepository;

@Service
public class ForumService {

	@Autowired
	private ForumRepository repository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public Forum createForum(String name, Long userId, String description) {

		User creator = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (creator.getIsBanned()) {
			throw new DomainException("Banned users can't create forums");
		}

		if (name == null || name.isBlank()) {
			throw new DomainException("Your forum must have a name");
		}

		if (description == null || description.isBlank()) {
			throw new DomainException("Your forum must have a description");
		}

		if (repository.findByName(name).isPresent()) {
			throw new DomainException("This name is already in use");
		}

		Forum forum = new Forum(name, creator, description);
		Forum savedForum = repository.save(forum);
		return savedForum;
	}

	@Transactional
	public void editName(Long userId, Long forumId, String name) {

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!forum.getCreator().equals(user)) {
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
	public void editDescription(Long userId, Long forumId, String content) {

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!forum.getCreator().equals(user)) {
			throw new DomainException("You aren't allowed to edit the description.");
		}

		if (content == null || content.isBlank()) {
			throw new DomainException("Your forum must have a description");
		}

		forum.setDescription(content);
		repository.save(forum);
	}

	@Transactional
	public void removeAdmin(Long userId, Long adminId, Long forumId) {

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		User admin = userRepository.findById(adminId).orElseThrow(() -> new DomainException("User not found!"));

		if (!user.equals(forum.getCreator())) {
			throw new DomainException("You aren't allowed to removed admins");
		}

		if (!forum.getForumAdmins().contains(admin)) {
			throw new DomainException("Admin not found!");
		}

		forum.removeAdmin(admin);
		repository.save(forum);
	}

	@Transactional
	public void addAdmin(Long userId, Long adminId, Long forumId) {

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		User admin = userRepository.findById(adminId).orElseThrow(() -> new DomainException("User not found!"));

		if (!user.equals(forum.getCreator())) {
			throw new DomainException("You aren't allowed to add admins");
		}

		if (forum.getForumAdmins().contains(admin)) {
			throw new DomainException("User is already an admin!");
		}

		if (forum.getBannedUsers().contains(admin) || admin.getIsBanned()) {
			throw new DomainException("Banned users can't become admins");
		}

		forum.addAdmin(admin);
		repository.save(forum);
	}

	@Transactional
	public void deleteForum(Long userId, Long forumId) {

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		User user = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		if (!user.equals(forum.getCreator()) && !user.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to delete forums!");
		}

		forum.setIsDeleted();
		repository.save(forum);
	}

	@Transactional
	public void banUser(Long adminId, Long userId, Long forumId) {

		User userToBan = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		User admin = userRepository.findById(adminId).orElseThrow(() -> new DomainException("User not found!"));

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		if (!forum.getForumAdmins().contains(admin) && !admin.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to ban users!");
		}

		forum.banUser(userToBan);
		repository.save(forum);
	}

	@Transactional
	public void unbanUser(Long adminId, Long userId, Long forumId) {

		User userToBan = userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found!"));

		User admin = userRepository.findById(adminId).orElseThrow(() -> new DomainException("User not found!"));

		Forum forum = repository.findById(forumId).orElseThrow(() -> new DomainException("Forum not found!"));

		if (!forum.getForumAdmins().contains(admin) && !admin.getRole().equals(Roles.ADMIN)) {
			throw new DomainException("You aren't allowed to unban users!");
		}

		forum.unbanUser(userToBan);
		repository.save(forum);
	}

}
