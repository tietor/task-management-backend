package ch.teko.ft.task_management_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.teko.ft.task_management_backend.constant.UserRole;
import ch.teko.ft.task_management_backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findFirstByEmail(String username);

  Optional<User> findByUserRole(UserRole userRole);
}
