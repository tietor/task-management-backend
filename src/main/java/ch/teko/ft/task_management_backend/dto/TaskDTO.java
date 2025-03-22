package ch.teko.ft.task_management_backend.dto;

import java.util.Date;

import ch.teko.ft.task_management_backend.constant.TaskStatus;

import lombok.Data;

@Data
public class TaskDTO {

  private Long id;

  private String title;
  private String description;
  private Date dueDate;
  private String priority;
  private TaskStatus taskStatus;

  private Long employeeId;
  private String employeeName;
}
