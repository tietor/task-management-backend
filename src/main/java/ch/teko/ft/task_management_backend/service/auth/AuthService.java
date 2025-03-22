package ch.teko.ft.task_management_backend.service.auth;

import ch.teko.ft.task_management_backend.dto.SignupRequest;
import ch.teko.ft.task_management_backend.dto.UserDto;

public interface AuthService {
  UserDto signupUser(SignupRequest signupRequest);

  boolean hasUserWithEmail(String email);
}
