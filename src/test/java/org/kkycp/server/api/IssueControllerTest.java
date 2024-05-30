package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kkycp.server.controller.issue.*;
import org.kkycp.server.domain.*;
import org.kkycp.server.repo.issue.IssueSearchCondition;
import org.kkycp.server.repo.issue.IssueStatistics;
import org.kkycp.server.repo.issue.TimeUnit;
import org.kkycp.server.services.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.kkycp.server.testutil.AuthenticationTestUtil.insertTestUser;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = IssueController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    @Test
    public void testCreateIssue() throws Exception {
        insertTestUser();

        IssueDto.Request issueDto = IssueDto.Request.builder()
                .title("Test Issue")
                .description("Test Description")
                .priority(Issue.Priority.MAJOR)
                .type("Bug")
                .build();

        doNothing().when(issueService)
                .createIssue(anyLong(), anyString(), any(Report.class), any(LocalDate.class));

        // Perform the request and document the response
        // You would need to provide a valid request body
        this.mockMvc.perform(post("/project/{projectId}/issues", 1L)
                        .with(user("test user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueDto)))
                .andExpect(status().isCreated())
                .andDo(document("create-issue",
                        requestFields(
                                fieldWithPath("title").description("이슈 타이틀"),
                                fieldWithPath("description").description("이슈 내용"),
                                fieldWithPath("priority").description("우선순위"),
                                fieldWithPath("type").description("이슈 타입")
                        )));
    }

    @Test
    public void testGetSimplifiedIssues() throws Exception {
        SimpleIssueDto issueDto1 = SimpleIssueDto.builder()
                .id(1L)
                .title("Test Issue 1")
                .reporter("test reporter")
                .reportedDate(LocalDate.of(2024, 6, 18))
                .priority(Issue.Priority.MAJOR)
                .status(Issue.Status.FIXED)
                .type("Bug")
                .build();
        SimpleIssueDto issueDto2 = SimpleIssueDto.builder()
                .id(2L)
                .title("Test Issue 2")
                .reporter("test reporter")
                .reportedDate(LocalDate.of(2024, 6, 18))
                .priority(Issue.Priority.TRIVIAL)
                .status(Issue.Status.NEW)
                .type("Feature")
                .build();

        // Set up the mock service
        when(issueService.getSimplifiedIssues(anyLong(), any(IssueSearchCondition.class), anyInt(), anyInt())).thenReturn(
                List.of(issueDto1, issueDto2));

        // Perform the request and document the response
        this.mockMvc.perform(get("/project/{projectId}/issues", 1L)
                        .queryParam("offset", "0")
                        .queryParam("limit", "50"))
                .andExpect(status().isOk())
                .andDo(document("get-simplified-issues",
                        queryParameters(
                                parameterWithName("offset").description("시작 인덱스"),
                                parameterWithName("limit").description("가져올 개수")
                        ),
                        pathParameters(parameterWithName("projectId").description("프로젝트 id")),
                        responseFields(
                                fieldWithPath("[].id").description("이슈의 id"),
                                fieldWithPath("[].title").description("이슈의 제목"),
                                fieldWithPath("[].reporter").description("이슈 리포터"),
                                fieldWithPath("[].reported_date").description("이슈가 보고된 날짜"),
                                fieldWithPath("[].priority").description("이슈의 우선순위"),
                                fieldWithPath("[].status").description("이슈의 상태"),
                                fieldWithPath("[].type").description("이슈의 타입")
                        )));
    }

    @Test
    public void testSearchSimplifiedIssues() throws Exception {
        SimpleIssueDto issueDto2 = SimpleIssueDto.builder()
                .id(2L)
                .title("Test Issue 2")
                .reporter("test reporter")
                .reportedDate(LocalDate.of(2024, 6, 18))
                .priority(Issue.Priority.TRIVIAL)
                .status(Issue.Status.NEW)
                .type("Feature")
                .build();

        // Set up the mock service
        when(issueService.getSimplifiedIssues(anyLong(), any(IssueSearchCondition.class), anyInt(), anyInt())).thenReturn(
                List.of(issueDto2));

        SearchConditionDto cond = new SearchConditionDto(null, "test reporter", Issue.Priority.TRIVIAL, Issue.Status.NEW, "ss", "Feature");

        // Perform the request and document the response
        this.mockMvc.perform(get("/project/{projectId}/issues", 1L)
                        .queryParam("offset", "0")
                        .queryParam("limit", "50")
                        .queryParam("assignee", cond.getAssigneeName())
                        .queryParam("reporter", cond.getReporterName())
                        .queryParam("priority", cond.getPriority().toString())
                        .queryParam("status", cond.getStatus().toString())
                        .queryParam("title", cond.getTitle())
                        .queryParam("type", cond.getType()))
                .andExpect(status().isOk())
                .andDo(document("search-simplified-issues",
                        pathParameters(parameterWithName("projectId").description("프로젝트 id")),
                        queryParameters(
                                parameterWithName("offset").description("시작 인덱스"),
                                parameterWithName("limit").description("가져올 개수"),
                                parameterWithName("assignee").description("필터링 할 담당자 이름"),
                                parameterWithName("reporter").description("필터링 할 리포터 이름"),
                                parameterWithName("priority").description("필터링 할 우선순위"),
                                parameterWithName("status").description("필터링 할 상태"),
                                parameterWithName("title").description("검색할 제목"),
                                parameterWithName("type").description("필터링 할 타입")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("이슈의 id"),
                                fieldWithPath("[].title").description("이슈의 제목"),
                                fieldWithPath("[].reporter").description("이슈 리포터"),
                                fieldWithPath("[].reported_date").description("이슈가 보고된 날짜"),
                                fieldWithPath("[].priority").description("이슈의 우선순위"),
                                fieldWithPath("[].status").description("이슈의 상태"),
                                fieldWithPath("[].type").description("이슈의 타입")
                        )));
    }

    @Test
    public void testGetIssue() throws Exception {
        Issue issue = Issue.builder()
                .title("Test Issue")
                .project(new Project("test project"))
                .description("Test Description")
                .reporter(new User("test user 3"))
                .reportedDate(LocalDate.of(2024, 6, 18))
                .priority(Issue.Priority.MAJOR)
                .type("Bug")
                .build();

        User commenter1 = new User("commenter1");
        User commenter2 = new User("commenter2");
        List<Comment> comments = List.of(
                new Comment(commenter1, "comment content 1", LocalDate.of(2024, 6, 18)),
                new Comment(commenter1, "comment content 2", LocalDate.of(2024, 6, 19)),
                new Comment(commenter2, "comment content 3", LocalDate.of(2024, 6, 21))
        );

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            ReflectionTestUtils.setField(comment, "id", (long) i+1);
            issue.addComment(comment);
        }

        // Set up the issue object as needed
        when(issueService.getIssue(anyLong())).thenReturn(issue);

        this.mockMvc.perform(get("/project/{projectId}/issues/{issueId}", 1L, 12L))
                .andExpect(status().isOk())
                .andDo(document("get-issue",
                        responseFields(
                                // Document the fields in the response
                                fieldWithPath("id").description("이슈의 id"),
                                fieldWithPath("title").description("이슈의 제목"),
                                fieldWithPath("description").description("이슈의 내용"),
                                fieldWithPath("reporter").description("이슈 리포터"),
                                fieldWithPath("fixer").description("이슈 수정자"),
                                fieldWithPath("assignee").description("이슈 담당자"),
                                fieldWithPath("reported_date").description("이슈 보고 날짜"),
                                fieldWithPath("priority").description("이슈 우선순위"),
                                fieldWithPath("status").description("이슈 상태"),
                                fieldWithPath("type").description("이슈 타입"),
                                fieldWithPath("comments").description("이슈의 댓글 목록"),
                                fieldWithPath("comments.[].id").description("댓글 id"),
                                fieldWithPath("comments.[].commenter").description("댓글 작성자"),
                                fieldWithPath("comments.[].comment").description("댓글 내용"),
                                fieldWithPath("comments.[].created_date").description("댓글 작성 날짜")
                        )));
    }

    @Test
    public void testAssignIssue() throws Exception {
        // Set up the mock service
        // No return value for this service method
        IssueUpdateDto.Request updateDto = IssueUpdateDto.Request.builder()
                .title("New Title")
                .description("New Description")
                .assignee("test user 2")
                .status(Issue.Status.ASSIGNED)
                .priority(Issue.Priority.MAJOR)
                .type("Bug")
                .build();

        // Perform the request and document the response
        // You would need to provide a valid request body
        this.mockMvc.perform(patch("/project/{projectId}/issues/{issueId}", 1L, 12L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isOk())
                .andDo(document("assign-issue",
                        requestFields(
                                fieldWithPath("title").description("이슈 타이틀"),
                                fieldWithPath("description").description("이슈 내용"),
                                fieldWithPath("assignee").description("이슈 담당자"),
                                fieldWithPath("status").description("이슈 상태"),
                                fieldWithPath("priority").description("우선순위"),
                                fieldWithPath("type").description("이슈 타입")
                        )));
    }

    @Test
    public void testFixIssue() throws Exception {
        // Set up the mock service
        // No return value for this service method
        IssueUpdateDto.Request updateDto = IssueUpdateDto.Request.builder()
                .title("New Title")
                .description("New Description")
                .fixer("test user 2")
                .status(Issue.Status.FIXED)
                .priority(Issue.Priority.MAJOR)
                .type("Bug")
                .build();

        // Perform the request and document the response
        // You would need to provide a valid request body
        this.mockMvc.perform(patch("/project/{projectId}/issues/{issueId}", 1L, 12L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                )
                .andExpect(status().isOk())
                .andDo(document("fix-issue",
                        requestFields(
                                fieldWithPath("title").description("이슈 타이틀"),
                                fieldWithPath("description").description("이슈 내용"),
                                fieldWithPath("fixer").description("이슈 수정자"),
                                fieldWithPath("status").description("이슈 상태"),
                                fieldWithPath("priority").description("우선순위"),
                                fieldWithPath("type").description("이슈 타입")
                        )));
    }

    @Test
    public void testGetStatisticsByTime() throws Exception {
        List<IssueStatistics.Time> timeStatistics = List.of(
                new IssueStatistics.Time(LocalDate.of(2024, 6, 18).toString(), 10L),
                new IssueStatistics.Time(LocalDate.of(2024, 6, 19).toString(), 5L)
        );

        // Set up the mock service
        when(issueService.getStatisticsByTime(TimeUnit.DAY)).thenReturn(timeStatistics);

        // Perform the request and document the response
        this.mockMvc.perform(get("/project/{projectId}/statistics/time", 1L)
                        .queryParam("time_unit", "DAY"))
                .andExpect(status().isOk())
                .andDo(document("get-statistics-by-time",
                        queryParameters(
                                parameterWithName("time_unit").description("시간 단위. DAY, MONTH, YEAR 중 1")
                        ),
                        responseFields(
                                fieldWithPath("[].time").description("시간. DAY, MONTH, YEAR 마다 포맷이 다르니, 파싱해서 사용"),
                                fieldWithPath("[].count").description("이슈 개수")
                        )));
    }
}