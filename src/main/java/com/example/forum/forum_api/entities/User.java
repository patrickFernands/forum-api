package com.example.forum.forum_api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.example.forum.forum_api.enums.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Roles role;

  @Column(nullable = false)
  private Boolean isBanned = false;

  @JsonIgnore
  @OneToMany(mappedBy = "creator")
  private List<Forum> forumsMade = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "author")
  private List<Comment> commentsMade = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "poster")
  private List<Post> postsMade = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "voter")
  private List<PostVote> postVotesMade = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "voter")
  private List<CommentVote> commentVotesMade = new ArrayList<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "forumAdmins")
  private List<Forum> adminForums = new ArrayList<>();

  public User() {
  }

  public User(String email, String name, String password, Roles role) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.role = role;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Roles getRole() {
    return role;
  }

  public void setRole(Roles role) {
    this.role = role;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getIsBanned() {
    return isBanned;
  }

  public void setIsBanned(Boolean isBanned) {
    this.isBanned = isBanned;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public List<Comment> getCommentsMade() {
    return Collections.unmodifiableList(commentsMade);
  }

  public List<PostVote> getPostVotesMade() {
    return Collections.unmodifiableList(postVotesMade);
  }

  public List<CommentVote> getCommentVotesMade() {
    return Collections.unmodifiableList(commentVotesMade);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || !(obj instanceof User))
      return false;
    User other = (User) obj;
    return id != null && id.equals(other.getId());
  }

  public List<Forum> getForumsMade() {
    return Collections.unmodifiableList(forumsMade);
  }

  public List<Post> getPostsMade() {
    return Collections.unmodifiableList(postsMade);
  }

  public List<Forum> getAdminForums() {
    return Collections.unmodifiableList(adminForums);
  }

}
