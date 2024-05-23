package org.kkycp.server.controller.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDto {
    public static class Request {
        private LocalDate createdDate;
        private String comment;
    }
}
