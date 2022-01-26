package com.matstar.ex2.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="tbl_board")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    // 엔티티 클래스로 데이터베이스의 테이블과 같은 구조로 작성한다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;

}
