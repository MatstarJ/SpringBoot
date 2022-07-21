package com.matstar.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    //Board Entity
    private Long bno;

    private String title;

    private String content;


    //Member Entity

    //작성자 이름
    private String writerName;

    //작성자 이메일 (id)
    private String writerEmail;

    private LocalDateTime regDate;

    private LocalDateTime modDate;


    //Reply Entity

    //댓글의 수
    private int replyCount;
}
