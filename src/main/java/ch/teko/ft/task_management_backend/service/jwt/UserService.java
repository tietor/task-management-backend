package ch.teko.ft.task_management_backend.service.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService {

  UserDetailsService userDetailsService();

}
