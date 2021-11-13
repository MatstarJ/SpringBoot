package com.matstar.ex02.repository;

import com.matstar.ex02.entity.Memo;
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

        System.out.println("클래스 네임 확인 : " + memoRepository.getClass().getName());
    }

    // Insert
    @Test
    public void testInsertDummies() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("sample... :"+ i).build();
            memoRepository.save(memo);
        });
    }

    // select one
    @Test
    public void testSelect() {

        //데이터 베이스에 존재하는 mno
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("============================================");

        if(result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }
    }

    //select one2
    @Transactional
    @Test
    public void testSelect2() {

        Long mno = 100L;

        Memo memo = memoRepository
                .getOne(mno);

        System.out.println("========================");

        System.out.println(memo);
    }


    @Test
    public void testUpdate() {
        Memo memo = Memo.builder()
                .mno(100L)
                .memoText("Update Text")
                .build();

        System.out.println(memoRepository.save(memo));
    }

    @Test
    public void testDelete() {

        Long mno = 100L;

        memoRepository.deleteById(mno);
    }


    //페이징 처리
    @Test
    public void testPageDefault() {

        //1페이지 10개
        Pageable pageable = PageRequest.of(0,10);

        Page<Memo> result = memoRepository.findAll(pageable);

        System.out.println(result);


        System.out.println("==========================");

        System.out.println("Total Pages : " + result.getTotalPages());  // 총 페이지 수

        System.out.println("Total Count : " + result.getTotalElements()); // 전체 개수

        System.out.println("Page Number : " + result.getNumber());  // 현재 페이지 번호 (0부터 시작)

        System.out.println("Page Size : " + result.getSize());   // 페이지당 데이터 갯수

        System.out.println("has next Page? : " + result.hasNext()); // 다음 페이지 존재 여부

        System.out.println("first page ? : " + result.isFirst());  // 시작 페이지(0) 여부

        System.out.println("===============================");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
        }

    }

    @Test
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0,10,sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo -> {

            System.out.println(memo);

        });
    }

   @Test
    public void testQueryMethods() {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);

        for(Memo memo : list) {
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable() {

        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L,pageable);

        result.get().forEach(memo -> System.out.println(memo));
    }


    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods() {

        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}