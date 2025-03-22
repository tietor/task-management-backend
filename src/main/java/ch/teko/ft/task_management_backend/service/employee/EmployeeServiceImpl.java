package ch.teko.ft.task_management_backend.service.employee;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.teko.ft.task_management_backend.constant.TaskStatus;
import ch.teko.ft.task_management_backend.dto.CommentDTO;
import ch.teko.ft.task_management_backend.dto.TaskDTO;
import ch.teko.ft.task_management_backend.entity.Comment;
import ch.teko.ft.task_management_backend.entity.Task;
import ch.teko.ft.task_management_backend.entity.User;
import ch.teko.ft.task_management_backend.repository.CommentRepository;
import ch.teko.ft.task_management_backend.repository.TaskRepository;
import ch.teko.ft.task_management_backend.util.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final TaskRepository taskRepository;

  private final CommentRepository commentRepository;

  private final JwtUtil jwtUtil;

  @Override
  public List<TaskDTO> getTasksByUserId() {
    User user = jwtUtil.getLoggedInUser();
    if (user != null) {
      return taskRepository.findAllByUserId(user.getId())
          .stream()
          .sorted(Comparator.comparing(Task::getDueDate).reversed())
          .map(Task::getTaskDTO)
          .collect(Collectors.toList());
    }
    throw new EntityNotFoundException("User not found");
  }

  @Override
  public TaskDTO updateTask(Long id, String status) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    if (optionalTask.isPresent()) {
      Task existingTask = optionalTask.get();
      existingTask.setTaskStatus(mapStringToTaskStatus(status));
      return taskRepository.save(existingTask).getTaskDTO();
    }
    throw new EntityNotFoundException("Task not found");
  }

  @Override
  public CommentDTO createComment(Long taskId, String content) {
    Optional<Task> optionalTask = taskRepository.findById(taskId);
    User user = jwtUtil.getLoggedInUser();
    if (optionalTask.isPresent() && user != null) {
      Comment comment = new Comment();
      comment.setCreatedAt(new Date());
      comment.setContent(content);
      comment.setTask(optionalTask.get());
      comment.setUser(user);
      return commentRepository.save(comment).getCommentDTO();
    }
    throw new EntityNotFoundException("User or Task not found");
  }

  @Override
  public List<CommentDTO> getCommentsByTaskId(Long taskId) {
    return commentRepository.findAllByTaskId(taskId).stream().map(Comment::getCommentDTO).collect(Collectors.toList());
  }

  @Override
  public TaskDTO getTaskById(Long id) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    return optionalTask.map(Task::getTaskDTO).orElse(null);
  }

  private TaskStatus mapStringToTaskStatus(String status) {
    switch (status) {
      case "PENDING" -> {
        return TaskStatus.PENDING;
      }
      case "INPROGRESS" -> {
        return TaskStatus.INPROGRESS;
      }
      case "COMPLETED" -> {
        return TaskStatus.COMPLETED;
      }
      case "DEFFERED" -> {
        return TaskStatus.DEFERRED;
      }
      default -> {
        return TaskStatus.CANCELLED;
      }
    }
  }
}
