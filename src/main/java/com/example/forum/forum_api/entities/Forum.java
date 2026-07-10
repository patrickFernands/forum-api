package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.forum.forum_api.enums.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_forum")
public class Forum {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne
  @JoinColumn(name = "creator_id", nullable = false)
  private User creator;

  @ManyToMany
  @JoinTable(name = "forum_admins", joinColumns = @JoinColumn(name = "forum_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> forumAdmins = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "banned_users", joinColumns = @JoinColumn(name = "forum_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> bannedUsers = new ArrayList<>();

  @OneToMany(mappedBy = "forum")
  private List<Post> posts = new ArrayList<>();

  @Column(nullable = false)
  private Boolean isDeleted;

  @Column(nullable = false)
  private String description;

  public Forum() {

  }

  public Forum(String name, User creator, String description) {
    this.name = name;
    this.creator = creator;
    this.description = description;
    forumAdmins.add(creator);
    isDeleted = false;
  }

  public User getCreator() {
    return creator;
  }

  public void addPost(Post post) {
    posts.add(post);
  }

  public void removePost(Post post) {
    post.setStatus(PostStatus.REMOVED);
  }

  public void approvePost(Post post) {
    post.setStatus(PostStatus.APPROVED);
  }

  public void banUser(User userToBan) {
    bannedUsers.add(userToBan);
  }

  public void unbanUser(User userToUnban) {
    bannedUsers.remove(userToUnban);
  }

  public List<Post> getPosts() {
    return Collections.unmodifiableList(posts);
  }

  public void addAdmin(User user) {
    forumAdmins.add(user);
  }

  public void removeAdmin(User user) {
    forumAdmins.remove(user);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
    isDeleted = true;
  }

}
