package com.example.forum.forum_api.entities;

import java.util.Objects;

public class User {

  private static Integer idCount = 1;

  private Integer id;
  private String name;
  private String password;
  private Boolean isAdmin;
  private Boolean isBanned;

  public User() {
  }

  public User(String name, String password) {
    id = idCount;
    idCount++;
    this.name = name;
    this.password = password;
    isAdmin = false;
    isBanned = false;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
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

  public Boolean getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public Boolean getIsBanned() {
    return isBanned;
  }

  public void setIsBanned(Boolean isBanned) {
    this.isBanned = isBanned;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User) obj;
    return Objects.equals(this.id, other.id);
  }

}
