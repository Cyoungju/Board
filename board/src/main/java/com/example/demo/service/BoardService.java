package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.dto.FileDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.File;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;


@Transactional(readOnly = true) //서비스에는 readOnly 들어가야함
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository BoardRepository;
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
        Page<Board> boards = BoardRepository.findAll(
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
        Optional<Board> boardOptional = BoardRepository.findById(id);

        if (boardOptional.isPresent()) {
            Board board = BoardRepository.findById(id).get();
            return BoardDTO.toboardDTO(board);
        }else {
            return null;
        }
    }


    //변경사항이 발생
    @Transactional
    public void save(BoardDTO boardDTO, MultipartFile[] files) throws IOException{
        BoardRepository.save(boardDTO.toEntity());

        // ** 파일 정보 저장

        for(MultipartFile file : files){
            if(file != null && file.isEmpty()){
                createFilePath(file);
                FileDTO fileDTO = new FileDTO();
                fileDTO.setFileName(file.getOriginalFilename());

                fileRepository.save(fileDTO.toEntity());
            }
        }

    }

    private String createFilePath(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(filePath); // 기본경로

        // ** 만약 경로가 없다면, 경로 생성
        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);


        // ** 파일명 추출
        String originalFileName = file.getOriginalFilename();
        System.out.println(originalFileName); //실행해서 확장자 붙어잇는지 아닌지 여부확인

        if (originalFileName != null) {
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

            // ** 저장
            Files.copy(
                    file.getInputStream(), // 업로드된 파일의 입력 스트림
                    path, // 저장할 경로
                    StandardCopyOption.REPLACE_EXISTING // 이미 파일이 존재하면 덮어쓰기
            );
            return "";
        }
    }


    @Transactional
    public void delete(Long id) {
        BoardRepository.deleteById(id);
    }


    @Transactional
    public void update(BoardDTO boardDTO) {
        Optional<Board> boardOptional = BoardRepository.findById(boardDTO.getId());

        //if (BoardRepository.findById(id).isPresent())...  예외처리 생략

        Board board = boardOptional.get();
        board.updateFromDTO(boardDTO);

        BoardRepository.save(board);
    }
}

























