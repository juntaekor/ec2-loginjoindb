package com.study.springboot.Dto;

import com.study.springboot.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

//Anotation    제약조건 : https://pig-programming.tistory.com/47
//@NotNull    Null 불가
//@Null    Null만 입력 가능
//@NotEmpty    Null, 빈 문자열 불가
//@NotBlank    Null, 빈 문자열, 스페이스만 있는 문자열 불가
//@Size(min=,max=)    문자열, 배열등의 크기가 만족하는가?
//@Pattern(regex=)    정규식을 만족하는가?
//@Max(숫자)    지정 값 이하인가?
//@Min(숫자)    지정 값 이상인가
//@Future    현재 보다 미래인가?
//@Past    현재 보다 과거인가?
//@Positive    양수만 가능
//@PositiveOrZero    양수와 0만 가능
//@Negative    음수만 가능
//@NegativeOrZero    음수와 0만 가능
//@Email    이메일 형식만 가능
//@Digits(integer=, fraction = )    대상 수가 지정된 정수와 소수 자리 수 보다 작은가?
//@DecimalMax(value=)     지정된 값(실수) 이하인가?
//@DecimalMin(value=)    지정된 값(실수) 이상인가?
//@AssertFalse    false 인가?
//@AssertTrue    true 인가?

@Getter
@Setter
@Builder

public class MemberJoinDto {
    @Nullable
    private Long id;

    @Size(min=1,max=10,message = "user_id가 10자를 넘을 수 없습니다.")

    @NotBlank(message = "user_id에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    private String user_id;

    @NotBlank(message = "user_pw에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    private String user_pw;

    private String user_name;

    @NotBlank(message = "user_role에 null, 빈문자열, 스페이스문자열만을 넣을 수 없습니다.")
    private String user_role;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joindate;

    public Member toSaveEntity() {
        return Member.builder()
                .user_id(user_id)
                .user_pw(user_pw)
                .user_name(user_name)
                .user_role(user_role)
                .joindate(joindate)
                .build();
    }

    public Member toUpdateEntity() {
        return Member.builder()

                .user_id(user_id)
                .user_pw(user_pw)
                .user_name(user_name)
                .user_role(user_role)
                .joindate(joindate)
                .build();
    }

}
