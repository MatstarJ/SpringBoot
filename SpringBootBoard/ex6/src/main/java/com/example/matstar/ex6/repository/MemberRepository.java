package com.example.matstar.ex6.repository;

import com.example.matstar.ex6.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {


}
