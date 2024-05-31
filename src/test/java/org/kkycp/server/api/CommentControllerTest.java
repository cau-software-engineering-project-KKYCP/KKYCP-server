package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.comment.CommentController;
import org.kkycp.server.controller.comment.CommentDto;
import org.kkycp.server.services.CommentService;
import org.kkycp.server.testutil.AuthenticationTestUtil;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    public void testPostComment() throws Exception {
        CommentDto.Request request = new CommentDto.Request();
        request.setComment("Test comment");

        AuthenticationTestUtil.insertTestUser();
        Mockito.doNothing().when(commentService).commentIssue(anyLong(), anyLong(), anyString(), anyString(), any(LocalDate.class));

        mockMvc.perform(post("/project/{projectId}/issues/{issueId}/comments", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(document("post-comment",
                        requestFields(
                                fieldWithPath("comment").description("댓글 내용")
                        )));
    }

    @Test
    public void testPutComment() throws Exception {
        CommentDto.Request request = new CommentDto.Request();
        request.setComment("Updated comment");

        AuthenticationTestUtil.insertTestUser();
        Mockito.doNothing().when(commentService).updateComment(anyLong(), anyString(), any(LocalDate.class));

        mockMvc.perform(put("/project/{projectId}/issues/{issueId}/comments/{commentId}", 1L, 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("put-comment",
                        requestFields(
                                fieldWithPath("comment").description("댓글 내용")
                        )));
    }

    @Test
    public void testDeleteComment() throws Exception {
        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong());

        mockMvc.perform(delete("/project/{projectId}/issues/{issueId}/comments/{commentId}", 1L, 1L, 1L))
                .andExpect(status().isOk())
                .andDo(document("delete-comment"));
    }
}