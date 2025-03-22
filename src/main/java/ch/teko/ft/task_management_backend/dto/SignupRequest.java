package ch.teko.ft.task_management_backend.dto;

import lombok.Data;

@Data
public class SignupRequest {

  private String name;

  private String email;

  private String password;
}
