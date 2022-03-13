package com.matstar.ex5.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Long bno;

    private String title;

    private String content;

    //작성자 이름
    private String writerName;

    //작성자 이메일 (id)
    private String writerEmail;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    //댓글의 수
    private int replyCount;
}
