package com.matstar.guestbook.dto;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO,EN> {

    //JPA를 이용하는 Repository에서는 페이지의 처리 결과를 Page<Entity>로 반환받는다.
    // 따라서 서비스 계층에서 이를 처리하기 위해서는 DTO객체로 변환해야 하기에 별도의 클래스를 만들어서 처리해야 한다.


    //DTO 리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    //목록 사이즈
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;

    //페이지 번호 목록
    private List<Integer> pageList;

    //생성자
    //function<EN,DTO> Entity 타입을 DTO 타입으로 변환
    // 클래스를 재사용하기 위해서 제네릭으로 만듬
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn) {

        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {

        this.page = pageable.getPageNumber() + 1;   //0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //temp and page
        int tempEnd = (int)(Math.ceil(page/10.0)*10) + 10;

        start = tempEnd - 9 ;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }


}
