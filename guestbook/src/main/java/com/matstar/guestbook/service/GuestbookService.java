package com.matstar.guestbook.service;

import com.matstar.guestbook.dto.GuestbookDTO;
import com.matstar.guestbook.dto.PageRequestDTO;
import com.matstar.guestbook.dto.PageResultDTO;
import com.matstar.guestbook.entity.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    //PageRequestDTO를 파라미터로, PageResultDTO를 리턴 타입으로 사용
    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);



    //조회처리
    GuestbookDTO read(Long gno);



    // DTO(GuestbookDTO)를 Entity 객체(Guestbook)로 변경한다.
    // default 메서드 사용하여 구현 클래스에서 사용하도록 처리한다.
    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }


    //엔티티 객체를 DTO 객체로 변환한다.
    default GuestbookDTO entityToDto(Guestbook entity) {

        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getRegDate())
                .build();

        return dto;
    }

}
