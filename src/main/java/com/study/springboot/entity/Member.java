package com.study.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String user_id;
    private String user_pw;
    private String user_name;
    private String user_role;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joindate;

    @Builder

    public Member(Long id, String user_id, String user_pw, String user_name, String user_role, LocalDate joindate) {
        this.Id = id;
        this.user_id = user_id;
        this.user_pw = user_pw;
        this.user_name = user_name;
        this.user_role = user_role;
        this.joindate = joindate;
    }

}
