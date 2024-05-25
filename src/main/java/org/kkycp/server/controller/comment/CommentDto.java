package org.kkycp.server.controller.comment;

import lombok.Data;

import java.time.LocalDate;

public class CommentDto {
    @Data
    public static class Request {
        private LocalDate createdDate;
        private String comment;
    }
}