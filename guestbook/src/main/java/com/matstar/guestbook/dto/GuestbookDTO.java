package com.matstar.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestbookDTO {

    //엔티티 클래스 Guestbook과 동일한 필드를 가지고 있고 lombok을 사용하여 getter/setter를 통해 자유롭게
    //값을 변경할 수 있게 구성한다.

    private Long gno;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime regDate, modDate;
}
