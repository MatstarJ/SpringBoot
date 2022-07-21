package com.matstar.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
// 화면에서 전달되는 page와 size 라는 파라미터를 수집하는 역할
//PageRequestDTO의 목적은 JPA쪽에서 사용하는 Pageable 타입의 객체를 생성하는 것
// JPA에서 사용하는 Pageable 객체를 생성하는 역할
public class PageRequestDTO {

    private int page;
    private int size;

    //검색 조건 추가
    private String type;
    private String keyword;

    public PageRequestDTO() {
        this.page = 1;
        this.size =10;
    }

    // 페이지 번호가 0부터 시작하기 때문에 1페이지의 경우 0이 될 수 있도록 page-1을 한다.
    public Pageable getPageable(Sort sort) {
        System.out.println("PageRequestDTO 실제 페이지 넘버 확인 : " + (page-1));
        return PageRequest.of(page-1,size,sort);
    }
}
