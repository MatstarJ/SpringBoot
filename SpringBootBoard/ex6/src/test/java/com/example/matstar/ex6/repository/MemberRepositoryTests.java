package com.example.matstar.ex6.repository;

import com.example.matstar.ex6.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder().email("r"+i+"@user.com").pw("1111").nickname("review"+i).build();

            memberRepository.save(member);
        });
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteMember() {
        Long mid = 1L; //Member의 mid

        Member member = Member.builder().mid(mid).build();

        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(mid);


    }

}
