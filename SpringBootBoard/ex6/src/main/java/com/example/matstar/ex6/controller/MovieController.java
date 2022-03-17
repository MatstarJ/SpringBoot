package com.example.matstar.ex6.controller;

import com.example.matstar.ex6.dto.MovieDTO;
import com.example.matstar.ex6.dto.PageRequestDTO;
import com.example.matstar.ex6.service.MovieService;
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
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/register")
    public void register() {

    }

    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes) {

        log.info("(controller.register) movieDTO 확인 : " + movieDTO);

        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg",mno);

        return "redirect:/movie/list";

    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        log.info("(controller.list) pageRequestDTO 확인 : "+pageRequestDTO);

        model.addAttribute("result",movieService.getList(pageRequestDTO));
    }

    @GetMapping({"/read","/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model) {

        log.info("controller.read or modify mno 확인 :" + mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto",movieDTO);
    }


}
