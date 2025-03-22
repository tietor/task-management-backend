package ch.teko.ft.task_management_backend.service.employee;

import java.util.List;

import ch.teko.ft.task_management_backend.dto.CommentDTO;
import ch.teko.ft.task_management_backend.dto.TaskDTO;

public interface EmployeeService {
  List<TaskDTO> getTasksByUserId();

  TaskDTO updateTask(Long id, String status);

  CommentDTO createComment(Long taskId, String content);

  List<CommentDTO> getCommentsByTaskId(Long taskId);

  TaskDTO getTaskById(Long id);
}
