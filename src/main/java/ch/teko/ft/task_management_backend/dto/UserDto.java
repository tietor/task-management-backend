package ch.teko.ft.task_management_backend.dto;

import ch.teko.ft.task_management_backend.constant.UserRole;

import lombok.Data;

@Data
public class UserDto {
  private Long id;

  private String name;

  private String email;

  private String password;

  private UserRole userRole;
}
