package com.example.forum.forum_api.exceptions;

public class DomainException extends RuntimeException {

  public static final long serialVersionUID = 1l;

  public DomainException(String msg) {
    super(msg);
  }

}
