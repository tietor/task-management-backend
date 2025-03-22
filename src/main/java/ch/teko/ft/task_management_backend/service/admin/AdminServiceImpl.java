package ch.teko.ft.task_management_backend.service.admin;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.teko.ft.task_management_backend.constant.TaskStatus;
import ch.teko.ft.task_management_backend.constant.UserRole;
import ch.teko.ft.task_management_backend.dto.CommentDTO;
import ch.teko.ft.task_management_backend.dto.TaskDTO;
import ch.teko.ft.task_management_backend.dto.UserDto;
import ch.teko.ft.task_management_backend.entity.Comment;
import ch.teko.ft.task_management_backend.entity.Task;
import ch.teko.ft.task_management_backend.entity.User;
import ch.teko.ft.task_management_backend.repository.CommentRepository;
import ch.teko.ft.task_management_backend.repository.TaskRepository;
import ch.teko.ft.task_management_backend.repository.UserRepository;
import ch.teko.ft.task_management_backend.util.JwtUtil;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;

  private final JwtUtil jwtUtil;
  private final CommentRepository commentRepository;

  @Override
  public List<UserDto> getUsers() {
    return userRepository.findAll()
        .stream()
        .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
        .map(User::getUserDto)
        .collect(Collectors.toList());
  }

  @Override
  public TaskDTO createTask(TaskDTO taskDTO) {
    Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
    if (optionalUser.isPresent()) {
      Task task = new Task();
      task.setTitle(taskDTO.getTitle());
      task.setDescription(taskDTO.getDescription());
      task.setPriority(taskDTO.getPriority());
      task.setDueDate(taskDTO.getDueDate());
      task.setTaskStatus(TaskStatus.INPROGRESS);
      task.setUser(optionalUser.get());
      return taskRepository.save(task).getTaskDTO();
    }
    return null;
  }

  @Override
  public List<TaskDTO> getAllTasks() {
    return taskRepository.findAll()
        .stream()
        .sorted(Comparator.comparing(Task::getDueDate).reversed())
        .map(Task::getTaskDTO)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }

  @Override
  public TaskDTO getTaskById(Long id) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    return optionalTask.map(Task::getTaskDTO).orElse(null);
  }

  @Override
  public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
    Optional<Task> optionalTask = taskRepository.findById(id);
    Optional<User> optionalUser = userRepository.findById(taskDTO.getEmployeeId());
    if (optionalTask.isPresent() && optionalUser.isPresent()) {
      Task existingTask = optionalTask.get();
      existingTask.setTitle(taskDTO.getTitle());
      existingTask.setDescription(taskDTO.getDescription());
      existingTask.setDueDate(taskDTO.getDueDate());
      existingTask.setPriority(taskDTO.getPriority());
      existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDTO.getTaskStatus())));
      existingTask.setUser(optionalUser.get());
      return taskRepository.save(existingTask).getTaskDTO();
    }
    return null;
  }

  @Override
  public List<TaskDTO> searchTaskByTitle(String title) {
    return taskRepository.findAllByTitleContaining(title)
        .stream()
        .sorted(Comparator.comparing(Task::getDueDate).reversed())
        .map(Task::getTaskDTO)
        .collect(Collectors.toList());
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
