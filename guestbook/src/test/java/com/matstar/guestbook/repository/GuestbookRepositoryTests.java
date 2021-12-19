package com.matstar.guestbook.repository;

import com.matstar.guestbook.entity.Guestbook;
import com.matstar.guestbook.entity.QGuestbook;
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
    public void insertDumies() {   // 300개의 더미 데이터를 넣는 작업을 수행

        IntStream.rangeClosed(1, 300).forEach(i -> {
            Guestbook guestbook = Guestbook.builder()
                    .title("Title....." + i)
                    .content("Content....." + i)
                    .writer("user.." + (i % 10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }
        @Test
        public void updateTest() {

            //300번의 글번호(gno)를 찾는다 (반환타입 Optional)
            Optional<Guestbook> result = guestbookRepository.findById(300L);

            if(result.isPresent()) {
                //Optional의 get()으로 Guestbook 엔티티 객체를 가져옴
                Guestbook guestbook = result.get();

                guestbook.changeTitle("Change Title...");
                guestbook.changeContent("Change Content..");

                guestbookRepository.save(guestbook);
            }
        }


        //단일 검색을 위한 querydsl 사용
        @Test
        public void testQuery1() {

        //페이지와 정렬 처리를 위한 객체 생성
            Pageable pageable = PageRequest.of(10,10, Sort.by("gno").descending());
        //Q도메인 클래스를 얻어온다 엔티티 객체에 선언된 필드를 변수로 사용할 수 있다.
            QGuestbook qGuestbook = QGuestbook.guestbook;    // 1

            String keyword = "1";

        //where문에 들어가는 조건들을 넣어주는 컨테이너
            BooleanBuilder builder = new BooleanBuilder(); //2

        //원하는 조건을 필드 값과 같이 결합해서 생성한다.
            BooleanExpression expression = qGuestbook.title.contains(keyword); //3

        //만들어진 조건은 and나 or같은 키워드와 결합시킨다.
            builder.and(expression); //4

        //findAll()을 사용하여 최종 처리한다.
            Page<Guestbook> result = guestbookRepository.findAll(builder, pageable); //5

            result.stream().forEach(guestbook -> {
                System.out.println(guestbook);
            });
        }



        @Test
        public void trestQuery2() {
            Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());

            QGuestbook qGuestbook = QGuestbook.guestbook;

            String keyword = "1";

            BooleanBuilder builder = new BooleanBuilder();

            BooleanExpression exTitle = qGuestbook.title.contains(keyword);
            BooleanExpression exContent = qGuestbook.content.contains(keyword);
            BooleanExpression exAll = exTitle.or(exContent);  // 1

            builder.and(exAll);  // 2
            builder.and(qGuestbook.gno.gt(0L)); // 3

            Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

            result.stream().forEach(guestbook -> {
                System.out.println(guestbook);
            });
        }










    }
