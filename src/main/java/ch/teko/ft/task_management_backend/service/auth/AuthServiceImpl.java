package ch.teko.ft.task_management_backend.service.auth;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ch.teko.ft.task_management_backend.constant.UserRole;
import ch.teko.ft.task_management_backend.dto.SignupRequest;
import ch.teko.ft.task_management_backend.dto.UserDto;
import ch.teko.ft.task_management_backend.entity.User;
import ch.teko.ft.task_management_backend.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;


  @PostConstruct
  public void createAdminAccount() {
    Optional<User> optionalUser = userRepository.findByUserRole(UserRole.ADMIN);
    if (optionalUser.isEmpty()) {
      User user = new User();
      user.setEmail("admin@test.com");
      user.setName("admin");
      user.setPassword(new BCryptPasswordEncoder().encode("admin"));
      user.setUserRole(UserRole.ADMIN);
      userRepository.save(user);
      System.out.println("Admin account created successfully!");
    } else {
      System.out.println("Admin account already exists");
    }
  }

  @Override
  public UserDto signupUser(SignupRequest signupRequest) {
    User user = new User();
    user.setEmail(signupRequest.getEmail());
    user.setName(signupRequest.getName());
    user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
    user.setUserRole(UserRole.EMPLOYEE);
    User createdUser= userRepository.save(user);
    return createdUser.getUserDto();
  }

  @Override
  public boolean hasUserWithEmail(String email) {
    return userRepository.findFirstByEmail(email).isPresent();
  }
}
