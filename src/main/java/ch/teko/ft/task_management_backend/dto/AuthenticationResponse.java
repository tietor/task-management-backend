package ch.teko.ft.task_management_backend.dto;

import ch.teko.ft.task_management_backend.constant.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {

  private String jwt;
  private Long userId;
  private UserRole userRole;

}
