package org.kkycp.server.repo.issue;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.SimpleExpression;

public enum TimeUnit {
    YEAR(DateExpression::year),
    MONTH(DateExpression::yearMonth),
    DAY(e -> e);

    private final GroupByHandler groupByHandler;

    TimeUnit(GroupByHandler groupByHandler) {
        this.groupByHandler = groupByHandler;
    }

    public Expression<?> groupBy(DateExpression<?> expression) {
        return groupByHandler.groupBy(expression);
    }

    @FunctionalInterface
    interface GroupByHandler {
        SimpleExpression<?> groupBy(DateExpression<?> expression);
    }
}
