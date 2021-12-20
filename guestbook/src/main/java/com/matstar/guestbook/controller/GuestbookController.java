package com.matstar.guestbook.controller;

import com.matstar.guestbook.dto.GuestbookDTO;
import com.matstar.guestbook.dto.PageRequestDTO;
import com.matstar.guestbook.entity.Guestbook;
import com.matstar.guestbook.service.GuestbookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 자동 주입을 위한 Annotation
public class GuestbookController {

    //반드시 final로 선언
    private final GuestbookService service;

    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";
    }

    //게시판 리스트 출력
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("Controller : list.........." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }


    //등록처리
    @GetMapping("/register")
    public void register() {

        log.info("register get........");
    }


    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){

        log.info("register post and dto"+dto);

        //새로 추가된 엔티티 번호
        Long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg",gno);

        return "redirect:/guestbook/list";
    }

    //조회
    @GetMapping("/read")
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        log.info("Controller read gno : " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto",dto);
    }
}
