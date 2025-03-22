package ch.teko.ft.task_management_backend.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.teko.ft.task_management_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDetailsService userDetailsService() {
    return new UserDetailsService() {

      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        return userRepository.findFirstByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
      }
    };
  }
}
