package com.matstar.guestbook.service;


import com.matstar.guestbook.dto.GuestbookDTO;
import com.matstar.guestbook.dto.PageRequestDTO;
import com.matstar.guestbook.dto.PageResultDTO;
import com.matstar.guestbook.entity.Guestbook;
import com.matstar.guestbook.entity.QGuestbook;
import com.matstar.guestbook.repository.GuestbookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("Service Register 메소드 실행");
        log.info(dto);

        //생성한 dto 객체를 entity 클래스에 주입
        Guestbook entity = dtoToEntity(dto);

        log.info("DTO to Entity 메소드 실행 : " + dto);

        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        //getPageable = Pagerequest.of(page-1, size, sort) 메서드 실행
        //Pageable 객체 생성
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        //검색조건 처리
        BooleanBuilder booleanBuilder=getSearch(requestDTO);

        //Page객체 생성
        Page<Guestbook> result = repository.findAll(booleanBuilder,pageable);

        //검색조건 처리 이전 코드
        //Page<Guestbook> result = repository.findAll(pageable);

        //엔티티 객체를 DTO 객체로 변환하는 Function 생성
        Function<Guestbook,GuestbookDTO> fn = (entity -> entityToDto(entity));

        //PageResultDTO 객체를 생성해서 DTO리스트 및 페이지 처리와 필요한 값들을 생성한다.
        return new PageResultDTO<>(result,fn);

    }

    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Guestbook> result = repository.findById(gno);

        return result.isPresent()? entityToDto(result.get()):null;
    }

    @Override
    public void remove(Long gno) {

        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {

        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()) {


            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    //QueryDsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){

        String type =requestDTO.getType();

        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        BooleanExpression expression = qGuestbook.gno.gt(0L);

        booleanBuilder.and(expression);

        //검색 조건이 없는 경우
        if(type == null || type.trim().length() ==0) {
            return booleanBuilder;
        }

        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }

        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }


        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;

    }


}
