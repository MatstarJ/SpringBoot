package com.matstar.ex5.repository;

import com.matstar.ex5.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {
}
