package org.kkycp.server.controller.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

public class CommentDto {
    @Data
    public static class Request {
        private String comment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        private String commenter;
        private String comment;
        private LocalDate created_date;
    }
}
