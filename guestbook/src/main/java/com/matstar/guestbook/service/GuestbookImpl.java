package com.matstar.guestbook.service;

import com.matstar.guestbook.dto.GuestbookDTO;
import com.matstar.guestbook.dto.PageRequestDTO;
import com.matstar.guestbook.dto.PageResultDTO;
import com.matstar.guestbook.entity.Guestbook;
import com.matstar.guestbook.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동 주입
public class GuestbookImpl implements GuestbookService{

    private final GuestbookRepository repository;  //반드시 final로 선언

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO--------------------------------------------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {


        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        Page<Guestbook> result = repository.findAll(pageable);

        Function<Guestbook,GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result,fn);
    }

}
