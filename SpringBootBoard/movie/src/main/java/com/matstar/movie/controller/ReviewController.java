package com.matstar.movie.controller;

import com.matstar.movie.dto.ReviewDTO;
import com.matstar.movie.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{mno}/all")
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno) {

        log.info("ReviewController.getList : " + mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);

    }


    @PostMapping("/{mno}")
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO movieReviewDTO){

        log.info("ReviewController.addReview reviewDTO : " + movieReviewDTO);

        Long reviewnum = reviewService.register(movieReviewDTO);

        return new ResponseEntity<>(reviewnum,HttpStatus.OK);

    }

    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum, @RequestBody ReviewDTO movieReviewDTO) {

        log.info("ReviewController.modifyReview, movieReviewDTO : " + movieReviewDTO );
        log.info("ReviewController.modifyReview, reviewnum, reviewnum : "  +reviewnum );

        reviewService.modify(movieReviewDTO);

        return new ResponseEntity<>(reviewnum,HttpStatus.OK);

    }

    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable Long reviewnum) {

        log.info("Review.controller.removeReviw, reviewnum : "+reviewnum);

        reviewService.remove(reviewnum);
        return new ResponseEntity<>(reviewnum,HttpStatus.OK);
    }


}
