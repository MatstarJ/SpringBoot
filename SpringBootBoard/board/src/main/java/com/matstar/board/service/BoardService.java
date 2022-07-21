package com.matstar.board.service;

import com.matstar.board.dto.BoardDTO;
import com.matstar.board.dto.PageRequestDTO;
import com.matstar.board.dto.PageResultDTO;
import com.matstar.board.entity.Board;
import com.matstar.board.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto);

    //JPQL의 결과로 나오는 Opject[]를 DTO로 변환하는 기능
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); //목록처리

    //게시물 조회
    BoardDTO get(Long bno);

    //게시물 삭제 기능
    void removeWithReplies(Long bno);

    //게시물 수정
    void modify(BoardDTO boardDTO);

    default Board dtoToEntity(BoardDTO dto) {
        Member member = Member
                .builder()
                .email(dto.getWriterEmail())
                .build();

        Board board = Board
                .builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();

        return board;
    }

    //BoardService에 추가하는 entityToDTO()
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getTitle())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) // Long으로 나오므로 int로 처리하도록
                .build();
        return boardDTO;
    }


}
