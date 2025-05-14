package org.example.expert.domain.todo.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoSearchCondition {

    private String title;            // 일정 제목 (부분 일치)
    private String nickname;         // 담당자 닉네임 (부분 일치)
    private LocalDateTime start; // 생성일 시작
    private LocalDateTime end;   // 생성일 끝
    // 정렬은 생성일 최신순으로

}