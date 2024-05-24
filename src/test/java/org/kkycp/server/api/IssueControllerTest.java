package org.kkycp.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kkycp.server.controller.issue.IssueController;
import org.kkycp.server.controller.issue.SimpleIssueDto;
import org.kkycp.server.domain.Issue;
import org.kkycp.server.repo.issue.IssueSearchCondition;
import org.kkycp.server.services.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = IssueController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@AutoConfigureRestDocs
public class IssueControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    IssueService issueService;

    @Test
    void getSimpleIssuePage() throws Exception {
        List<SimpleIssueDto> response = generateFakeResponse();

        when(issueService.getSimplifiedIssues(anyInt(), any(IssueSearchCondition.class),  anyInt(),  anyInt()))
                .thenReturn(response);

        mockMvc.perform(get("/project/{projectId}/issues", 13L)
                        .queryParam("offset", "0")
                        .queryParam("limit", "20"))
                .andExpect(status().isOk())
                .andDo(document("issue/get-simple-issue-page",
                                pathParameters(
                                        parameterWithName("projectId").description(
                                                "프로젝트 id. 만약 해당 id의 프로젝트가 없으면 404 Not found로 응답.")
                                ),
                                queryParameters(
                                        parameterWithName("offset").description(
                                                "Optional. 페이지 시작 offset. 기본값은 0."),
                                        parameterWithName("limit").description(
                                                "Optional. 한 페이지에 보여줄 이슈 개수. 기본값은 50.")
                                )
                        )

                );
    }

    private static List<SimpleIssueDto> generateFakeResponse() {
        return List.of(
                SimpleIssueDto.builder()
                        .id(1L)
                        .title("title")
                        .reportedDate(LocalDate.now())
                        .priority(Issue.Priority.MAJOR)
                        .status(Issue.Status.NEW)
                        .build(),
                SimpleIssueDto.builder()
                        .id(2L)
                        .title("title2")
                        .reportedDate(LocalDate.now())
                        .priority(Issue.Priority.CRITICAL)
                        .status(Issue.Status.ASSIGNED)
                        .build(),
                SimpleIssueDto.builder()
                        .id(3L)
                        .title("title3")
                        .reportedDate(LocalDate.now())
                        .priority(Issue.Priority.TRIVIAL)
                        .status(Issue.Status.RESOLVED)
                        .build()
        );
    }

    //TODO: get simple issues using search conditions
}
