package com.matstar.board.Controller;

import com.matstar.board.dto.BoardDTO;
import com.matstar.board.dto.PageRequestDTO;
import com.matstar.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("list페이지 로딩 pageRequest 파라미터 확인 : " + pageRequestDTO);

        model.addAttribute("result",boardService.getList(pageRequestDTO));

    }


    @GetMapping("/register")
    public void register() {
        log.info("register get 작동..");
    }

    @PostMapping(name="/register")
    public String registerPost(BoardDTO dto, RedirectAttributes redirectAttributes) {
        log.info("register post 작동.. BoardDTO 파라미터 확인 : " + dto);

        //새로 추가되는 엔티티의 번호
        Long bno = boardService.register(dto);

        redirectAttributes.addFlashAttribute("msg",bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read","/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {
        log.info("read or modify .. 작동 글번호 확인 : " + bno);

        BoardDTO boardDTO = boardService.get(bno);

        log.info("read or modify ..boardDTO 확인 : " + boardDTO);

        model.addAttribute("dto",boardDTO);
    }

    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes redirectAttributes) {
        log.info("remove 실행, 글 번호 확인 : " + bno);

        boardService.removeWithReplies(bno);
        redirectAttributes.addFlashAttribute("msg",bno);

        return "redirect:/board/list";
    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes redirectAttributes) {

        log.info("post modify 실행 BoardDTO 확인: " + dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("bno",dto.getBno());

        return "redirect:/board/read";
    }




}
