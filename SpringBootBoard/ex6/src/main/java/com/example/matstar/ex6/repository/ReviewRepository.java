package com.example.matstar.ex6.repository;

import com.example.matstar.ex6.entity.Member;
import com.example.matstar.ex6.entity.Movie;
import com.example.matstar.ex6.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

//엔티티의 특정한 속성값을 같이 로딩하도록 표시하는 어노테이션
    @EntityGraph(attributePaths = {"member"},type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);


    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);

}
