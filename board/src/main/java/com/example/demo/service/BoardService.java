package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.FileDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardFile;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Transactional(readOnly = true) //서비스에는 readOnly 들어가야함
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    // 절대경로
    // youngju 사용하는 pc이름으로 바꿔줘야함


    private final String filePath = "C:/Users/youngju/Desktop/developerPortfolio/boardList/board/Board Files/";


    /*
    파일 업로드 작업
    1. 있는지 없는지 확인해서 불러온다 -> board 에서 확인
    2. 보드 저장소 → 게시물확인 → 파일 있음 → 파일 저장소 → 파일
    3. 보드서비스 에서 파일 레파지토리 관리 할수 있어야함
    (파일 유무 확인하고 파일 가져오는 걸 권장) -> 파일 컨트롤러, 파일 서비스 작성할 필요 없음
     */


    // ** 페이징을 위한 함수
    // Page<BoardDTO> 페이징을 표현할 리스트
    // pageable 수량 정보 작성
    public Page<BoardDTO> paging(Pageable pageable){
        int page = pageable.getPageNumber() - 1; // 페이지가 1부터 시작하기 (페이지 시작 번호 셋팅)
        int size = 5; // 몇개의 게시물을 넣을것인가 (페이지에 포함될 개수)

        // ** 전체 게시물을 불러온다
        Page<Board> boards = boardRepository.findAll(
                // 정렬시켜서 사가져옴
                PageRequest.of(page, size)
        );

        return boards.map(board -> new BoardDTO( //람다식
                board.getId(),
                board.getUserName(),
                board.getTitle(),
                board.getContents(),
                board.getCreateTime(),
                board.getUpdateTime()
                // for문 이라고 생각하면됨
                // 람다식 공부하기!
        ));
    }


    public BoardDTO findById(Long id) {
        //Optional존재유무 확인
        Optional<Board> boardOptional = boardRepository.findById(id);

        if (boardOptional.isPresent()) {
            Board board = boardRepository.findById(id).get();
            return BoardDTO.toboardDTO(board);
        }else {
            return null;
        }
    }


    //변경사항이 발생
    @Transactional
    public void save(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        //게시물 현재시간 저장
        boardDTO.setCreateTime(LocalDateTime.now());

        // ** 게시글 DB에 저장 후 pk을 받아옴.
        Long id = boardRepository.save(boardDTO.toEntity()).getId();
        Board board = boardRepository.findById(id).get();


        filesSave(board, files);
    }

    private void filesSave(Board board, MultipartFile[] files) throws IOException {
        if (files != null && files.length > 0) {
            // ** 파일 정보 저장.
            for(MultipartFile file : files) {

                Path uploadPath = Paths.get(filePath);

                // ** 만약 경로가 없다면... 경로 생성.
                if (!Files.exists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                    } catch (IOException e) {
                        // 예외 처리 추가 (예: 로깅)
                        e.printStackTrace();
                    }
                }

                // ** 파일명 추출
                String originalFileName = file.getOriginalFilename();
                if (originalFileName != null && !originalFileName.isEmpty()) {
                    // ** 확장자 추출
                    String formatType = originalFileName.substring(
                            originalFileName.lastIndexOf("."));

                    // ** UUID 생성
                    String uuid = UUID.randomUUID().toString();

                    // ** 경로 지정
                    // ** C:/Users/G/Desktop/green/Board Files/{uuid + originalFileName}
                    String path = filePath + uuid + originalFileName;

                    // ** 경로에 파일을 저장.  DB 아님
                    file.transferTo( new File(path) );

                    BoardFile boardFile = BoardFile.builder()
                            .filePath(filePath)
                            .fileName(originalFileName)
                            .uuid(uuid)
                            .fileType(formatType)
                            .fileSize(file.getSize())
                            .board(board)
                            .build();

                    fileRepository.save(boardFile);
                }
            }

        }
    }

/*
    private String createFilePath(MultipartFile file) throws IOException {

        // 업로드 경로
        Path uploadPath = Paths.get(filePath);

        // 만약 경로가 없다면 -> 생성
        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 업로드 된 파일명 추출
        String originalFileName = file.getOriginalFilename();

        // UUID
        String uuid = UUID.randomUUID().toString();

        // 저장할 파일의 경로 설정
        Path path = uploadPath.resolve(
                uuid + originalFileName
        );

        Files.copy(
                file.getInputStream(),
                path,
                StandardCopyOption.REPLACE_EXISTING
        );

        return !originalFileName.isEmpty() ? path.toString() : null;
    }


    private String createFileName(MultipartFile file) {

        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && !originalFileName.isEmpty() && originalFileName.contains(".")) {

            originalFileName = originalFileName.substring(
                    0, originalFileName.lastIndexOf(".")
            );
        }
        return originalFileName;
    }


    private String createFileType(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        if (originalFileName != null && !originalFileName.isEmpty() && originalFileName.contains(".")) {

            String formatType = originalFileName.substring(
                    originalFileName.lastIndexOf("."));

            return formatType;

        }else {
            return null;
        }
    }


    private Long createFileSize(MultipartFile file) {
        Long fileSize = file.getSize();

        return fileSize;
    }
*/
    /*
    private String createFilePath(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(filePath); // 기본경로

        // ** 만약 경로가 없다면, 경로 생성
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        // ** 파일명 추출
        String originalFileName = file.getOriginalFilename();
        System.out.println(originalFileName); //실행해서 확장자 붙어잇는지 아닌지 여부확인

        if (originalFileName != null && !originalFileName.isEmpty() && originalFileName.contains(".")) {

            // ** 확장자 추출
            String formatType = originalFileName.substring(
                    originalFileName.lastIndexOf("."));
            System.out.println(formatType);


            // ** 파일 이름만 남김
            originalFileName = originalFileName.substring(
                    0, originalFileName.lastIndexOf(".")
            );

            System.out.println(originalFileName);

            // ** UUID생성
            String uuid = UUID.randomUUID().toString();


            // ** 저장할 파일의 경로 설정
            Path path = uploadPath.resolve(
                    uuid + originalFileName + formatType
            );

            //두 경로를 결합하여 새로운 경로를 생성
            //uploadPath + (uuid + originalFileName + formatType)

            // ** 저장
            Files.copy(
                    file.getInputStream(), // 업로드된 파일의 입력 스트림
                    path, // 저장할 경로
                    StandardCopyOption.REPLACE_EXISTING // 이미 파일이 존재하면 덮어쓰기
            );

        }
        return "";
    }
    */

    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


    @Transactional
    public void update(BoardDTO boardDTO, MultipartFile[] files) throws IOException {
        Optional<Board> boardOptional = boardRepository.findById(boardDTO.getId());

        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            board.updateFromDTO(boardDTO);

            //게시글에 당하는  파일 찾기
            List<BoardFile> boardFiles = fileRepository.findByBoardId(board.getId());

            // 기존에 연결된 파일들 삭제

            // 기존에 연결된 파일들 삭제
            if (boardFiles != null && !boardFiles.isEmpty()) {
                for (BoardFile file : boardFiles) {
                    fileRepository.delete(file);
                }
            }
            // 파일 저장
            if (files != null && files.length > 0) {
                filesSave(board, files);
            }


            // 게시물 업데이트
            boardRepository.save(board);
        }
    }

}

























