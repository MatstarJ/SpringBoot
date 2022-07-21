package com.matstar.board.Controller;

import com.matstar.board.dto.ReplyDTO;
import com.matstar.board.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping(value="/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno) {

        log.info("ReplyController /board/bno 확인 : " + bno);

        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO) {
        log.info("댓글 등록 메서드 replyDTO 파라미터 확인 : " + replyDTO);

        Long rno = replyService.register(replyDTO);

        return new ResponseEntity<>(rno,HttpStatus.OK);
    }

    @DeleteMapping("/{rno}")
    public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {

        log.info("댓글 삭제 메서드 rno : " + rno);

        replyService.remove(rno);

        return new ResponseEntity<>("success",HttpStatus.OK);
    }

    @PutMapping("/{rno}")
    public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {

        log.info("댓글 수정처리 확인 : " +replyDTO);

        replyService.modify(replyDTO);

        return new ResponseEntity<>("success",HttpStatus.OK);

    }

}
