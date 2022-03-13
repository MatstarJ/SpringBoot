package com.matstar.ex4.service;


import com.matstar.ex4.dto.GuestbookDTO;
import com.matstar.ex4.dto.PageRequestDTO;
import com.matstar.ex4.dto.PageResultDTO;
import com.matstar.ex4.entity.Guestbook;

public interface GuestbookService {

    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO,Guestbook> getList(PageRequestDTO requestDTO);

    GuestbookDTO read(Long gno);

    void remove(Long gno);

    void modify(GuestbookDTO dto);

    // dto 객체를 생성
    default Guestbook dtoToEntity(GuestbookDTO dto) {

        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getContent())
                .build();
        return entity;
    }

    //엔티티 객체를 dto로 변환
    default GuestbookDTO entityToDto(Guestbook entity) {

        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }

}
