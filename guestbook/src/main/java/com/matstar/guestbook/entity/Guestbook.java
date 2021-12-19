package com.matstar.guestbook.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@Builder // 빌더를 사용하기 위해서 아래 두 어노테이션을 사용해야 한다.
@AllArgsConstructor
@NoArgsConstructor
public class Guestbook extends BaseEntity { // 객체의 수정/변경시간을 처리하는 BaseEntity 추상 클래스를 상속

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;


    // 컬럼을 수정하기 위한 메서드
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
