package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.forum.forum_api.exceptions.DomainException;

public class Forum {

  private String name;
  private User creator;
  private List<User> forumAdmins;
  private List<User> bannedUsers;
  private Map<Integer, Post> approvedPosts;
  private Map<Integer, Post> postsWaitingList;
  private Boolean isDeleted;

  public Forum() {

  }

  public Forum(String name, User creator) throws DomainException {
    this.name = name;
    if (this.name == null || this.name.equals("")) {
      throw new DomainException("A forum must have a name!");
    }
    this.creator = creator;
    if (this.creator == null || this.creator.getIsBanned()) {
      throw new DomainException("A forum must have a valid creator!");
    }
    forumAdmins = new ArrayList<>();
    forumAdmins.add(creator);
    bannedUsers = new ArrayList<>();
    approvedPosts = new HashMap<>();
    postsWaitingList = new HashMap<>();
    isDeleted = false;
  }

  public User getCreator() {
    return creator;
  }

  public void addPost(User user, Post post) throws DomainException {
    if (bannedUsers.contains(user) || user.getIsBanned()) {
      throw new DomainException("Banned users can't post");
    } else {
      postsWaitingList.put(post.getId(), post);
    }
  }

  public void removePost(User user, Integer postId) throws DomainException {

    Post post = approvedPosts.get(postId);

    if (post == null) {
      throw new DomainException("Post not found!");
    }
    if (!post.getAuthor().equals(user) && !user.getIsAdmin() && !forumAdmins.contains(user)) {
      throw new DomainException("You aren't allowed to delete this post!");
    }
    post.setIsDeleted();
  }

  public void approvePost(User user, Integer postId, Boolean isApproved) throws DomainException {
    if (isApproved == null) {
      throw new DomainException("Approval status cannot be null!");
    }
    if (!forumAdmins.contains(user)) {
      throw new DomainException("User isn't forum admin");
    }
    if (postsWaitingList.isEmpty()) {
      throw new DomainException("No posts to approve!");
    }

    Post post = postsWaitingList.get(postId);

    if (post == null) {
      throw new DomainException("Post not found!");
    }
    if (isApproved) {
      approvedPosts.put(postId, postsWaitingList.get(postId));
    }
    postsWaitingList.remove(postId);
  }

  public void banUser(User forumAdmin, User userToBan) throws DomainException {
    if (!forumAdmins.contains(forumAdmin)) {
      throw new DomainException("You aren't allowed to ban users!");
    }
    if (forumAdmins.contains(userToBan)) {
      throw new DomainException("Forum creator and admins can't be banned!");
    }
    bannedUsers.add(userToBan);
  }

  public void unbanUser(User forumAdmin, User userToUnban) throws DomainException {
    if (!forumAdmins.contains(forumAdmin)) {
      throw new DomainException("User isn't forum admin");
    }
    bannedUsers.remove(userToUnban);
  }

  public Map<Integer, Post> getApprovedPosts() throws DomainException {
    if (approvedPosts.isEmpty()) {
      throw new DomainException("Forum is empty!");
    }
    return Collections.unmodifiableMap(approvedPosts);
  }

  public Map<Integer, Post> getPostsWaitingList() {
    return Collections.unmodifiableMap(postsWaitingList);
  }

  public void addAdmin(User forumCreator, User user) throws DomainException {
    if (!forumCreator.equals(creator)) {
      throw new DomainException("No one other than forum creator can give admin permissions");
    }
    forumAdmins.add(user);
  }

  public void removeAdmin(User forumCreator, User user) throws DomainException {
    if (!forumCreator.equals(creator)) {
      throw new DomainException("No one other than forum creator can remove admin permissions");
    }
    if (user.equals(creator)) {
      throw new DomainException("Creator can't be removed");
    }
    if (!forumAdmins.contains(user)) {
      throw new DomainException("User isn't admin");
    }
    forumAdmins.remove(user);
  }

  public void setName(User user, String name) throws DomainException {
    if (name == null || name.equals("")) {
      throw new DomainException("Invalid name!");
    }
    if (!forumAdmins.contains(user)) {
      throw new DomainException("Only admins can change the forum title");
    }
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<User> getForumAdmins() {
    return Collections.unmodifiableList(forumAdmins);
  }

  public List<User> getBannedUsers() {
    return Collections.unmodifiableList(bannedUsers);
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted() {
    name = "Deleted Forum";
    creator = null;
    forumAdmins.clear();
    bannedUsers.clear();
    approvedPosts.clear();
    postsWaitingList.clear();
    isDeleted = true;
  }

}
