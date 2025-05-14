package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.expert.domain.common.entity.Timestamped;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "log")
public class Log extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // 예: "매니저 등록 요청"
    private String detail; // 요청 내용, 에러 메시지 등

    public Log(String action, String detail) {
        this.action = action;
        this.detail = detail;
    }

}