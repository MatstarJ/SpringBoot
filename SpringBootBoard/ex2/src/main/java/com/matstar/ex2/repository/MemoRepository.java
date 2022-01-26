package com.matstar.ex2.repository;

import com.matstar.ex2.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//jparepository를 상속할 때는 엔티티의 타입정보(엔티티 클래스)와 @Id의 타입을 지정해야 한다.
public interface MemoRepository extends JpaRepository<Memo,Long> {

    /*쿼리메서드. 주로 findBy나 getBy로 시작하고 이후에 필요한 필드 조건이나 And, Or같은 키워드를 이용해 메서드의
     이름 자체로 질의 조건을 만든다.

    아래의 경우 mno를 기준으로해서 between 구문을 사용하고 orderby를 적용한 형태이다.
    (파라미터는 between에 사용되는 파라미터임)
     */
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);


    //쿼리 메서드는 Pageable 파라미터를 같이 결합해서 사용할 수 있다.
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);


    //쿼리메서드를 이용한 삭제
    void deleteMemoByMnoLessThan(Long num);
}
