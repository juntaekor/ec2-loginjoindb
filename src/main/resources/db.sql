USE mydb;

DESCRIBE member;

SELECT * FROM member;

-- 회원가입 DB 만들기
-- 1.테이블 삭제
drop table member;

-- 2.테이블 생성
create table member(
    id          BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id     VARCHAR(255) NOT NULL,
    user_pw     VARCHAR(255) NOT NULL,
    user_name   VARCHAR(255),
    user_role   VARCHAR(255) DEFAULT 'ROLE_USER',
    joindate    DATE DEFAULT NOW()
);

-- 3. ROW 추가
insert into member( id, user_id, user_pw, user_name, user_role, joindate )
      values ( 0, 'hong', '1234', '홍길동', 'ROLE_USER', '2022-09-09');
insert into member( id, user_id, user_pw, user_name, user_role, joindate )
      values ( 0, 'tom', '1234', '톰아저씨', 'ROLE_USER', '2022-09-09' );
insert into member( id, user_id, user_pw, user_name, user_role, joindate )
      values ( 0, 'admin', '1234', '관리자', 'ROLE_ADMIN', '2022-09-09' );

-- 4. 검색하기
select * from member;

-- 7. 커밋하기 : 오라클은 오토커밋이 아님. INSERT, UPDATE, DELETE 후에 반드시
--               commit 해야 됨.
--               MySQL은 오토커밍 이다. INSERT, UPDATE, DELETE 후에 바로 적용됨.
--               물리적 파일에 자동 저장됨.
-- COMMIT;