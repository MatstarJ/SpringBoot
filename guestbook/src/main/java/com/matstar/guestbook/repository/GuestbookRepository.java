package com.matstar.guestbook.repository;


import com.matstar.guestbook.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

//QuerydslPredicateExecutor<>   동적 쿼리 사용을 위한 querydsl을 사용하기 위해 해당 인터페이스를 상속해야 한다.
public interface GuestbookRepository extends JpaRepository<Guestbook,Long>, QuerydslPredicateExecutor<Guestbook> {

}
