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
@RequiredArgsConstructor //의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    //서비스 계층에서는 파라미터를 DTO로 받기 때문에 이를 JPA로 처리하기 위해서는 엔티티 타입의 객체로 변환해야 한다.


    private final GuestbookRepository repository;  //반드시 final로 선언

    // 등록처리 메서드
    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO--------------------------------------------");
        log.info(dto);

        // DTO -> Entity
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        //PageRequestDTO를 받아서
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        //검색 조건 처리
        BooleanBuilder booleanBuilder = getSearch(requestDTO);


        //Page<> 객체를 생성
        //Page<Guestbook> result = repository.findAll(pageable);

        //Querydsl사용
        Page<Guestbook> result = repository.findAll(booleanBuilder,pageable);


        //Service 인터페이스에 정의된 default 메소드 entityToDto를 이용해서
        // Function을 호출하고,
        Function<Guestbook,GuestbookDTO> fn = (entity -> entityToDto(entity));
        //이것을 PageResultDTO로 변환시킨다. (생성자를 리턴하도록 처리)
        return new PageResultDTO<>(result,fn);
    }


    //조회처리
    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    //삭제처리
    @Override
    public void remove(Long gno) {

        repository.deleteById(gno);

    }
    //수정
    @Override
    public void modify(GuestbookDTO dto) {


        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()) {

            //select된 entity를 가져옴
            Guestbook entity = result.get();

            //dto에 있는 걸 entity로 넣어서 수정
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }



    //검색조건 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {

        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestbook qGuestbook = QGuestbook.guestbook;

        //gno가 0>0 인 조건만 생성한다.
        BooleanExpression expression = qGuestbook.gno.gt(0L);

        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0 ) {

            return booleanBuilder;
        }

        //검색 조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }

        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return  booleanBuilder;

    }

}
