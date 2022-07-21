package com.matstar.movie.service;

import com.matstar.movie.dto.MovieDTO;
import com.matstar.movie.dto.MovieImageDTO;
import com.matstar.movie.dto.PageRequestDTO;
import com.matstar.movie.dto.PageResultDTO;
import com.matstar.movie.entity.Movie;
import com.matstar.movie.entity.MovieImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MovieService {

    Long register(MovieDTO movieDTO);

    PageResultDTO<MovieDTO,Object[]> getList (PageRequestDTO requestDTO);

    MovieDTO getMovie(Long mno);


    default Map<String,Object> dtoToEntity(MovieDTO movieDTO) {

        Map<String,Object> entityMap = new HashMap<String,Object>();

        Movie movie = Movie.builder()
                .mno(movieDTO.getMno())
                .title(movieDTO.getTitle())
                .build();

        System.out.println("(service.dtoToEntity) Movie.mno 확인"+ movieDTO.getMno());

        entityMap.put("movie",movie);

        List<MovieImageDTO> imageDTOList = movieDTO.getImageDTOList();

        //MovieImageDTO 처리
        if(imageDTOList != null && imageDTOList.size() > 0 ) {

           System.out.println("(service.dtoToEntity) imageDTOList 확인 : " + imageDTOList);

            List<MovieImage> movieImageList = imageDTOList.stream().map(movieImageDTO -> {

                System.out.println("(service.dtoToEntity)  movieImageDTO 확인 : " + movieImageDTO);

                MovieImage movieImage = MovieImage
                        .builder()
                        .path(movieImageDTO.getPath())
                        .imgName(movieImageDTO.getImgName())
                        .uuid(movieImageDTO.getUuid())
                        .movie(movie)
                        .build();

                return movieImage;
            }).collect(Collectors.toList());
            entityMap.put("imgList",movieImageList);
        }
        return entityMap;
    }

    default MovieDTO entitiesToDTO(Movie movie, List<MovieImage> movieImages,Double avg, Long reviewCnt) {

        MovieDTO movieDTO = MovieDTO
                .builder()
                .mno(movie.getMno())
                .title(movie.getTitle())
                .regDate(movie.getRegDate())
                .modDate(movie.getModDate())
                .build();


        List<MovieImageDTO> movieImageDTOList = movieImages
                .stream().map(movieImage -> {
                    return MovieImageDTO
                            .builder()
                            .imgName(movieImage.getImgName())
                            .path(movieImage.getPath())
                            .uuid(movieImage.getUuid())
                            .build();
                }).collect(Collectors.toList());

        movieDTO.setImageDTOList(movieImageDTOList);
        movieDTO.setAvg(avg);
        movieDTO.setReviewCnt(reviewCnt.intValue());

        return movieDTO;
    }



}
