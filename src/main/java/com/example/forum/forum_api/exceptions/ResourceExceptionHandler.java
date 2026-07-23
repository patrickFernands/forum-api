package com.example.forum.forum_api.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<StandardError> entityNotFound(DomainException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;

		StandardError err = new StandardError(
				Instant.now(),
				status.value(),
				"Domain exception violated",
				e.getMessage(),
				request.getRequestURI());

		return ResponseEntity.status(status).body(err);
	}
}