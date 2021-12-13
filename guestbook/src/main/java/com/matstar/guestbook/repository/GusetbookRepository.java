package com.matstar.guestbook.repository;


import com.matstar.guestbook.entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GusetbookRepository extends JpaRepository<Guestbook,Long> {

}
