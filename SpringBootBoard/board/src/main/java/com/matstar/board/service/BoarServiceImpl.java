package com.matstar.board.service;


import com.matstar.board.dto.BoardDTO;
import com.matstar.board.dto.PageRequestDTO;
import com.matstar.board.dto.PageResultDTO;
import com.matstar.board.entity.Board;
import com.matstar.board.entity.Member;
import com.matstar.board.repository.BoardRepository;
import com.matstar.board.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoarServiceImpl implements BoardService {

    private final BoardRepository repository; //자동주입 final

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {
        log.info("register 메서드 :" + dto);

        Board board = dtoToEntity(dto);

        repository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        log.info("getList 메서드 파마미터 확인 : " + pageRequestDTO);

        Function<Object[],BoardDTO> fn = (en -> entityToDTO((Board) en[0],(Member)en[1],(Long)en[2]));
        //Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));

        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),pageRequestDTO.getKeyword(),pageRequestDTO.getPageable(Sort.by("bno").descending()));

        return new PageResultDTO<>(result,fn);

    }


    @Override
    public BoardDTO get(Long bno) {

        Object result = repository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board)arr[0],(Member)arr[1],(Long)arr[2]);
    }


    @Transactional
    @Override
    public void removeWithReplies(Long bno) {
        //댓글 부터 삭제
        replyRepository.deleteByBno(bno);

        repository.deleteById(bno);
    }


    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> modi = repository.findById(boardDTO.getBno());
        if(modi.isPresent()) {
            Board board = modi.get();
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());
            repository.save(board);
        }
    }
}
