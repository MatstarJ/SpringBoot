package com.matstar.guestbook.controller;

import com.matstar.guestbook.dto.GuestbookDTO;
import com.matstar.guestbook.dto.PageRequestDTO;
import com.matstar.guestbook.service.GuestbookService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@AllArgsConstructor
public class GuestbookController {

    private final GuestbookService service;  // final로 선언


    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";
    }


    @GetMapping({"/list"})
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("Controller -> List,  pageRequestDTO 출력 -> : "+pageRequestDTO);

        model.addAttribute("result",service.getList(pageRequestDTO));
    }


    @GetMapping("/register")
    public void register() {
        log.info("등록호출(GET)");

    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {

        log.info("등록호출(POST)");

        //새로 추가된 엔티티 번호
        Long gno = service.register(dto);

        redirectAttributes.addFlashAttribute("msg",gno);

        return "redirect:/guestbook/list";
    }

    /*@GetMapping("/read")*/
    @GetMapping({"/read","/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        log.info("Controller -> read or modify");
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto",dto);
    }


    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes) {

        log.info("Controller -> remove(POST)");

        service.remove(gno);

        redirectAttributes.addFlashAttribute("msg",gno);

        return "redirect:/guestbook/list";
    }

    @PostMapping("/modify")
    public String modify(GuestbookDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes) {

        log.info("Controller -> modify(POST)");
        log.info("dto : " + dto);


        service.modify(dto);

        redirectAttributes.addFlashAttribute("page", requestDTO.getPage());
        redirectAttributes.addFlashAttribute("gno", dto.getGno());
        redirectAttributes.addFlashAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addFlashAttribute("type",requestDTO.getType());

        return "redirect:/guestbook/read";
    }
}
