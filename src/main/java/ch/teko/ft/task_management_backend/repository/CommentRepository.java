package ch.teko.ft.task_management_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.teko.ft.task_management_backend.dto.CommentDTO;
import ch.teko.ft.task_management_backend.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByTaskId(Long taskId);
}
