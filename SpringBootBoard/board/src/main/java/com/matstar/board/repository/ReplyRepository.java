package com.matstar.board.repository;

import com.matstar.board.entity.Board;
import com.matstar.board.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno")
    void deleteByBno(Long bno);

    //게시글로 댓글 목록 가져오기
    List<Reply> getReplyByBoardOrderByRno(Board board);

}
