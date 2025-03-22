package ch.teko.ft.task_management_backend.controller.employee;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.teko.ft.task_management_backend.dto.CommentDTO;
import ch.teko.ft.task_management_backend.dto.TaskDTO;
import ch.teko.ft.task_management_backend.service.employee.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping("/tasks")
  public ResponseEntity<List<TaskDTO>> getTasksByUserId() {
    return ResponseEntity.ok(employeeService.getTasksByUserId());
  }

  @GetMapping("/task/{id}")
  public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
    return ResponseEntity.ok(employeeService.getTaskById(id));
  }

  @GetMapping("/comments/{taskId}")
  public ResponseEntity<List<CommentDTO>> getCommentByTaskId(@PathVariable Long taskId) {
    return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
  }

  @PostMapping("/task/comment/{taskId}")
  public ResponseEntity<CommentDTO> createComment(@PathVariable Long taskId, @RequestParam String content) {
    CommentDTO createdCommentDTO = employeeService.createComment(taskId, content);
    if (createdCommentDTO == null) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDTO);
  }

  @GetMapping("/task/{id}/{status}")
  public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @PathVariable String status) {
    TaskDTO updatedTaskDTO = employeeService.updateTask(id, status);
    if (updatedTaskDTO == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    return ResponseEntity.ok(updatedTaskDTO);
  }
}
