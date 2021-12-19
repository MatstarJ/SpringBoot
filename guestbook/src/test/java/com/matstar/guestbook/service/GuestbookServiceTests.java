package com.matstar.guestbook.service;


import com.matstar.guestbook.dto.GuestbookDTO;
import com.matstar.guestbook.dto.PageRequestDTO;
import com.matstar.guestbook.dto.PageResultDTO;
import com.matstar.guestbook.entity.Guestbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;


    //DTO -> Entity
    @Test
    public void testRegister() {

        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title..")
                .content("sample Content..")
                .writer("user0")
                .build();
        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList() {

        //PageRequestDTO 객체 생성
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();

        //pageRequestDTO를 PageResultDTO로 변환
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV" + resultDTO.isPrev());
        System.out.println("NEXT" + resultDTO.isNext());
        System.out.println("TOTAL" + resultDTO.getTotalPage());

        System.out.println("--------------------------------------");

        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
            System.out.println(guestbookDTO);
        }

        System.out.println("---------------------------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

}
