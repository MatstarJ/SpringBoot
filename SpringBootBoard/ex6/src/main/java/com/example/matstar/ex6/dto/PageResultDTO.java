package com.example.matstar.ex6.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO,EN> {

    //DTO 리스트
    private List<DTO> dtoList;

    // 전체 데이터 개수
    private int totalPage;

    //현재 페이지 번호
    private int page;

    //목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;

    //페이지 번호 목록
    private List<Integer> pageList;


    //생성자 엔티티객체를 DTO 객체로 변환
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn) {

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        // 전체 데이터 개수
        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }


    private void makePageList(Pageable pageable) {

        System.out.println("pageable 파라미터 확인"+pageable);

        this.page = pageable.getPageNumber()+1 ;  // 0부터 시작하므로 1을 추가

        System.out.println("현재 페이지 번호"+this.page);
        this.size = pageable.getPageSize();


        //temp end page  10, 20, 30, 으로 끝나게..
        // 1페이지의 경우 -> Math.ceil(0.1) * 10 = 10
        // 10페이지의 경우 -> Math.ceil(1) * 10 = 10
        // 11페이지의 경우 -> Math.ceil(1.1) * 10 = 20
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10 ;

        // 1, 11, 21 ...
        start = tempEnd -9;

        // 시작페이지가 1보다 크면 이전 페이지가 존재함
        prev = start >1;

        // 토탈 페이지가 더 크면..  tempend로..
        end = totalPage > tempEnd ? tempEnd : totalPage;

        // 토탈 페이지가 더 크면 다음 페이지는 존재함..
        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

    }
}
