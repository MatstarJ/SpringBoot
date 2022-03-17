package com.example.matstar.ex6.controller;

import com.example.matstar.ex6.dto.UploadResultDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    @Value("${com.matstar.upload.path}") //application.properties의 변수
    private String uploadPath;

    private String makeFolder(){

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/",File.separator);

        //make folder
        File uploadPathFolder = new File(uploadPath,folderPath);

        if(uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }


    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles) {

            log.info("(controller.uploadAjax)파일의 컨텐츠 타입 확인 : "+ uploadFile.getContentType());

            if(uploadFile.getContentType().startsWith("image") == false) {
                log.warn("this file is not image type");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            //실제 파일 이름, IE나 Edge는 전체 경로가 들어감
            String originalName = uploadFile.getOriginalFilename();

            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("(controller.uploadAjax) 원본 파일 이름 확인 : " + originalName);
            log.info("(controller.uploadAjax) 업로드 파일 이름 화인 + " + fileName);

            //날짜 폴더 생성
            String folderPath = makeFolder();

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분
            String saveName = uploadPath + File.separator + folderPath
                    + File.separator + uuid + "_" + fileName;

            log.info("(controller.uploadAjax)저장되는 파일 이름과 경로 확인 : " + saveName);


            Path savePath = Paths.get(saveName);
            try {
                //원본 파일 저장
                uploadFile.transferTo(savePath);

                //섬네일을 위한 파일 이름
                String thumbnailSaveName = uploadPath + File.separator +
                        folderPath + File.separator + "s_"+uuid+"_"+fileName;


                File thumbnailFile = new File(thumbnailSaveName);

                //섬네일 생성
                // 원본 파일 객체, 섬네일로 만들 파일 객체,너비,높이
                Thumbnailator.createThumbnail(savePath.toFile(),thumbnailFile,100,100);


                resultDTOList.add(new UploadResultDTO(fileName,uuid,folderPath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }


    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size) {

        ResponseEntity<byte[]> result = null;

        try {
            String srcFileName = URLDecoder.decode(fileName,"UTF-8");
            log.info("(controller.display) 디코딩된 업로드된 파일 이름 확인 ; " + srcFileName);

            File file = new File(uploadPath+File.separator+srcFileName);

            if(size != null && size.equals("1")){
                file = new File(file.getParent(),file.getName().substring(2));
            }
            log.info("file :  " + file);

            log.info("(controller.display) 경로를 포함한 전체 파일 이름 확인 : " + file);

            HttpHeaders header = new HttpHeaders();

            //MIME 타입 처리
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            log.info("(controller.display) Contents Type 확인 : " + Files.probeContentType(file.toPath()));


            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
        } catch(Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;

    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName) {

        String srcFileName = null;

        try{
            srcFileName = URLDecoder.decode(fileName,"UTF-8");
            File file = new File(uploadPath+File.separator+srcFileName);

            boolean result = file.delete();

            File thumbnail = new File (file.getParent(),"s_"+file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result,HttpStatus.OK);

        }catch(UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
