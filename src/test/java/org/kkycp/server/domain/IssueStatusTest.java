package org.kkycp.server.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueStatusTest {
    static Stream<Arguments> statusTransitions() {
        return Stream.of(
                Arguments.of(Issue.Status.NEW, Issue.Status.ASSIGNED),
                Arguments.of(Issue.Status.ASSIGNED, Issue.Status.FIXED),
                Arguments.of(Issue.Status.FIXED, Issue.Status.RESOLVED),
                Arguments.of(Issue.Status.RESOLVED, Issue.Status.CLOSED),
                Arguments.of(Issue.Status.RESOLVED, Issue.Status.REOPENED),
                Arguments.of(Issue.Status.REOPENED, Issue.Status.ASSIGNED)
        );
    }

    @ParameterizedTest
    @MethodSource("statusTransitions")
    void issueTransitionValid(Issue.Status currentStatus, Issue.Status desiredStatus) {
        assertThat(desiredStatus.canTransitionFrom(currentStatus)).isTrue();
    }

    static Stream<Arguments> invalidStatusTransitions() {
        return Stream.of(
                Arguments.of(Issue.Status.NEW, Issue.Status.FIXED),
                Arguments.of(Issue.Status.ASSIGNED, Issue.Status.CLOSED),
                Arguments.of(Issue.Status.FIXED, Issue.Status.NEW),
                Arguments.of(Issue.Status.REOPENED, Issue.Status.CLOSED),
                Arguments.of(Issue.Status.CLOSED, Issue.Status.REOPENED)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidStatusTransitions")
    void issueTransitionInvalid(Issue.Status currentStatus, Issue.Status desiredStatus) {
        assertThat(desiredStatus.canTransitionFrom(currentStatus)).isFalse();
    }
}
