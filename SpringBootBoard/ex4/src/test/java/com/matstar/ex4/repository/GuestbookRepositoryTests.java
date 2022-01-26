package com.matstar.ex4.repository;


import com.matstar.ex4.entity.Guestbook;
import com.matstar.ex4.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1,300).forEach( i->{
            Guestbook guestbook = Guestbook.builder()
                    .title("Title...."+i)
                    .content("Content..."+i)
                    .writer("user"+(i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }


    @Test
    public void updateTest() {

        Optional<Guestbook> result = guestbookRepository.findById(300L);

        if(result.isPresent()) {

            Guestbook guestbook = result.get();

            guestbook.changeContent("Change content...");
            guestbook.changeTitle("Change Title...");
            guestbookRepository.save(guestbook);
        }
    }


    //querydsl을 활용한 단일 검색
    @Test
    public void testQuery1() {

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        // Q도메인 클래스를 얻는다.
        QGuestbook qGuestbook = QGuestbook.guestbook;

        // 검색 키워드
        String keyword = "1";

        //BooleanBuilder는 where 문에 들어가는 조건 같은 개념이다.
        //BooleanBuilder에 들어가는 값은 com.querydsl.core.types.Predicate 타입을 사용한다.
        BooleanBuilder builder = new BooleanBuilder();

        // 원하는 조건은 BooleanExpression으로 만든다.
        BooleanExpression expression =  qGuestbook.title.contains(keyword);

        //만들어진 조건은 where문에 and나 or 같은 키워드를 사용한다.
        builder.and(expression);

        //BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용할 수 있다.
        Page<Guestbook> result = guestbookRepository.findAll(builder,pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }


    //다중 항목 검색
    @Test
    public void testQuery2() {

        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword ="1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exTitle = qGuestbook.title.contains(keyword);

        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        BooleanExpression exAll = exTitle.or(exContent);

        builder.and(exAll);

        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder,pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });

    }






}
