package ch.teko.ft.task_management_backend.service.admin;

import java.util.List;

import ch.teko.ft.task_management_backend.constant.TaskStatus;
import ch.teko.ft.task_management_backend.dto.CommentDTO;
import ch.teko.ft.task_management_backend.dto.TaskDTO;
import ch.teko.ft.task_management_backend.dto.UserDto;

public interface AdminService {
  List<UserDto> getUsers();

  TaskDTO createTask(TaskDTO taskDTO);

  List<TaskDTO> getAllTasks();

  void deleteTask(Long id);

  TaskDTO getTaskById(Long id);

  TaskDTO updateTask(Long id, TaskDTO taskDTO);

  List<TaskDTO> searchTaskByTitle(String title);

  CommentDTO createComment(Long taskId, String content);

  List<CommentDTO> getCommentsByTaskId(Long taskId);
}
