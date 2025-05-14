package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(todo)
                        .leftJoin(todo.user, user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    @Override
    public Page<TodoSearchResponse> searchTodos(TodoSearchCondition cond, Pageable pageable) {
        List<TodoSearchResponse> content = queryFactory
                .select(Projections.constructor(
                        TodoSearchResponse.class,
                        todo.title,
                        JPAExpressions.select(comment.count())
                                .from(comment)
                                .where(comment.todo.eq(todo)),
                        JPAExpressions.select(manager.count())
                                .from(manager)
                                .where(manager.todo.eq(todo))
                ))
                .from(todo)
                .where(
                        titleContains(cond.getTitle()),
                        nicknameContains(cond.getNickname()),
                        createdAtBetween(cond.getStart(), cond.getEnd())
                )
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // total count 쿼리
        Long total = Optional.ofNullable(
                queryFactory.
                        select(todo.count())
                        .from(todo)
                        .where(
                                titleContains(cond.getTitle()),
                                nicknameContains(cond.getNickname()),
                                createdAtBetween(cond.getStart(), cond.getEnd())
                        )
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    private Predicate titleContains(String title) {
        return title != null ? todo.title.containsIgnoreCase(title) : null;
    }

    private Predicate nicknameContains(String nickname) {
        return nickname != null ? todo.managers.any().user.nickname.containsIgnoreCase(nickname) : null;
    }

    private Predicate createdAtBetween(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return todo.createdAt.between(start, end);
        } else if (start != null) {
            return todo.createdAt.goe(start);
        } else if (end != null) {
            return todo.createdAt.loe(end);
        } else {
            return null;
        }
    }

}