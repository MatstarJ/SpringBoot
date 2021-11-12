package com.matstar.ex02.repository;

import com.matstar.ex02.entity.Memo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemoRepository extends JpaRepository<Memo,Long> {

    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

//    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);



}
