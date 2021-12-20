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
    //Page<Entity> 타입을 이용해서 생성
    //function<EN,DTO> -> Entity 타입을 DTO 타입으로 변환
    // 클래스를 재사용하기 위해서 제네릭으로 만듬
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn) {

        //Page 정보가 담긴 result 객체를 stream으로 만들고
        // map(fn)을 이용해서 스트림 내 요소들을 하나씩 특정 값으로 변환해 준다.
       //  Guestbook를 stream()을 통해 한줄로 늘어놓고 map(fn)을 통해서
        //Guestbook entity를 map(Function<EN,DTO>)을 통해 entityToDTO(GuestbookDTO로 변환)를 하게됩니다.
        //그리고나서 Collectors.toList()를 통해 다시 List<String> 형식으로 return하게됩니다.
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }


    private void makePageList(Pageable pageable) {

        //0부터 시작하므로 1을 추가
        this.page = pageable.getPageNumber() + 1;

        this.size = pageable.getPageSize();

        //끝 번호 계산 (정확한 값이 아니기에 tempEnd 변수를 만들어 사용
        //페이지 번호가 10개씩 보인다고 가정
        // 1~10페이지의 경우 끝 번호 10 , 11~20페이지의 경우 끝 번호가 20
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        //시작 번호 계산
        start = tempEnd - 9 ;

        // 이전 페이지, 시작 번호가 1보다 크면 참
        prev = start > 1;

        //실제 끝 페이지 계산

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }


}
