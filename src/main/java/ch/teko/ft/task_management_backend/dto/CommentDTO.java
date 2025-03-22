package ch.teko.ft.task_management_backend.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CommentDTO {

  private Long id;

  private String content;

  private Date createdAt;

  private Long taskId;

  private Long userId;

  private String postedBy;


}
