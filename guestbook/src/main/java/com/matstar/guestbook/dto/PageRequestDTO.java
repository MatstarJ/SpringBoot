package com.matstar.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    //페이지에 관한 요청을 처리하는 DTO 클래스
    //화면에서 전달되는 목록 관련된 데이터를 처리하는 DTO -> 결과를 처리하는 PageResultDTO
    private int page;
    private int size;


    //검색처리를 위한 변수
    private String type;
    private String keyword;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    // 클래스를 생성한 목적은 JPA에서 사용하는 Pageable 타입의 객체를 생성하는 것이다.
    // 페이지의 시작번호가 0이라는 점을 감안하여 -1을 해준다.
    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page -1, size, sort);
    }
}
