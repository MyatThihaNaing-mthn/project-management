package com.th.pm.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskRequest {
   private String title;
   private String content;
   private LocalDate deadline;
}
