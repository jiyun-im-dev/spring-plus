package org.example.expert.domain.todo.dto.response;

import lombok.RequiredArgsConstructor;

// Projections에 사용될 응답 DTO
@RequiredArgsConstructor
public class TodoSearchResponse {

    private String title;        // 일정 제목
    private String userCount;    // 담당자 수
    private String commentCount; // 댓글 수

}