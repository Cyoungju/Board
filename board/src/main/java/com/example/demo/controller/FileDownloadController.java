package com.example.demo.controller;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileDownloadController {

    // 엔드 포인트로 GET요청
    @GetMapping("/download/{uuid}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid,
                                                 @PathVariable String fileName){
        // UUID와 파일이름을 사용하여 파일 경로 구성
        Path filePath = Paths.get("C:/Users/youngju/Desktop/developerPortfolio/boardList/board/Board Files/" + uuid + fileName);

        try {
            // 지정된 경로의 파일을 나타내는 Resource 객체 생성
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하고 읽을 수 있는지 확인합니다.
            if (resource.exists() || resource.isReadable()){
                // 파일에 접근 가능한 경우, 파일을 응답 본문으로 하는 ResponseEntity를 반환
                return ResponseEntity.ok()
                        .header("Content-Disposition",
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}
