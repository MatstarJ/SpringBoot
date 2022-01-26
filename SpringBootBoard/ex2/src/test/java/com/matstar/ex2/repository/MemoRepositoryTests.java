package com.matstar.ex2.repository;

import com.matstar.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {


    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }


    // 삽입 작업 테스트
    @Test
    public void testInsertDummies() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..." + i).build();
            memoRepository.save(memo);

        });
    }


    // 조회 작업 테스트
    @Test
    public void testSelect() {

        //데이터 베이스에 존재하는 mno
        Long mno = 100L;

        // findById의 리턴 타입이 Optional 타입이다.
        Optional<Memo> result = memoRepository.findById(mno);

        System.out.println("======================================================");

        if(result.isPresent()){
            Memo memo = result.get();

            System.out.println(memo);
        }
    }

    //수정작업 테스트
    @Test
    public void testUpdate() {

        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        System.out.println(memoRepository.save(memo));
    }


    //삭제작업 테스트
    @Test
    public void testDelete() {

        Long mno = 100L;

        //해당 데이터가 존재하지 않으면 EmptyResultDataAccessException 에러가 발생한다.
        memoRepository.deleteById(mno);
    }



    //페이징 테스트
    @Test
    public void testPageDefault() {

        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);
    }

    @Test
    public void testPageDefault2() {

        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);

        System.out.println("-----------------------------------------------------------------");

        //총 페이지
        System.out.println("Total Pages : " + result.getTotalPages());

        //전체 개수
        System.out.println("Total Count : " + result.getTotalElements());

        //현재 페이지 번호
        System.out.println("Page Number : " + result.getNumber());

        // 페이지당 데이터 개수
        System.out.println("Page Size : " + result.getSize());

        //다음 페이지 존재 여부
        System.out.println("has next page : " + result.hasNext());

        // 시작 페이지(0) 여부
        System.out.println( "first page : " + result.isFirst());


        System.out.println("----------------------------------------------------------------");
        //레코드 출력
        for(Memo memo : result.getContent()) {
            System.out.println(memo);
        }
    }


    //정렬 조건 추가하기
    @Test
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0,10,sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {System.out.println(memo);
        });
    }


    // Sort 객체의 and()를 이용해서 정렬 조건을 각각 다르게 줄 수 있다.
    @Test
    public void testSort2() {

        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);

        Pageable pageable = PageRequest.of(0,10,sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }


    //쿼리 메서드 테스트
    @Test
    public void testQueryMethods() {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo: list) {
            System.out.println(memo);
        }
    }


    //쿼리 메서드는 Pageable 파라미터를 같이 결합해서 사용할 수 있다.
    @Test
    public void testQueryMethodWithPageable() {

        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);

        result.get().forEach(memo -> {System.out.println(memo);

        });
    }


    //쿼리 메서드 삭제
    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {

        memoRepository.deleteMemoByMnoLessThan(10L);
    }




}
