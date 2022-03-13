package com.matstar.ex5.service;

import com.matstar.ex5.dto.BoardDTO;
import com.matstar.ex5.dto.PageRequestDTO;
import com.matstar.ex5.dto.PageResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        BoardDTO dto = BoardDTO
                .builder()
                .title("Test..")
                .content("Test..")
                .writerEmail("user35@aaa.com") // DB에 존재하는 회원
                .build();
        Long bno = boardService.register(dto);
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for(BoardDTO boarDto : result.getDtoList()) {
            System.out.println(boarDto);
        }
    }

    @Test
    public void testGet() {
        Long bno = 100L;

        BoardDTO boardDTO = boardService.get(bno);

        System.out.println(boardDTO);
    }

    @Test
    public void testRemove() {
        Long bno = 20L;
        boardService.removeWithReplies(bno);
    }


    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO
                .builder()
                .bno(2L)
                .title("제목수정테스트")
                .content("내용수정테스트")
                .build();

        boardService.modify(boardDTO);
    }


}
