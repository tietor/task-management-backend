package ch.teko.ft.task_management_backend.entity;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.teko.ft.task_management_backend.constant.TaskStatus;
import ch.teko.ft.task_management_backend.dto.TaskDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private Date dueDate;
  private String priority;
  private TaskStatus taskStatus;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private User user;

  public TaskDTO getTaskDTO() {
    TaskDTO taskDTO = new TaskDTO();
    taskDTO.setId(id);
    taskDTO.setTitle(title);
    taskDTO.setDescription(description);
    taskDTO.setEmployeeName(user.getName());
    taskDTO.setEmployeeId(user.getId());
    taskDTO.setTaskStatus(taskStatus);
    taskDTO.setDueDate(dueDate);
    taskDTO.setPriority(priority);
    return taskDTO;
  }
}
