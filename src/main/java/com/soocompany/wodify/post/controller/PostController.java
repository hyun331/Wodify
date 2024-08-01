package com.soocompany.wodify.post.controller;
import com.soocompany.wodify.common.exception.CommonResDto;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    // 좋아요 (Like) : 게시글의 좋아요는 Redis 를 통해 집계한다.
    // 글, 이미지, 영상을 올릴 수 있어야 한다. => 구현해야함
    @PostMapping("/create")
    public ResponseEntity<?> postCreate(@RequestParam String email, @ModelAttribute PostSaveReqDto dto) {
        Post post = postService.postCreate(email, dto);
        HttpStatus code = HttpStatus.CREATED;
        String msg = "글이 등록되었습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, post.getId());
        return new ResponseEntity<>(commonResDto, code);
    }

    // 키워드 검색이 가능하다. => 구현해야함.
    // 모든 유저가 조회할 수 있다.
    @GetMapping("/list")
    public ResponseEntity<?> postListPage(Pageable pageable) {
        Page<PostListResDto> posts = postService.postListPage(pageable);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 목록 조회에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, posts);
        return new ResponseEntity<>(commonResDto, code);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> postDetail(@RequestParam Long id) {
        PostDetResDto postDetResDto = postService.postDetail(id);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 상세 조회에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    //작성자만 본인의 글을 삭제할 수 있어야 한다.
    @PatchMapping("/delete")
    public ResponseEntity<?> postDelete(@RequestParam Long id, @RequestParam String email) {
        postService.postDelete(id, email);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    //작성자만 본인의 글을 수정할 수 있어야 하며, 수정 시 수정 표시와 시간이 나타난다.
    @PatchMapping("/update")
    public ResponseEntity<?> postUpdate(@RequestParam Long id, @RequestParam String email
            , @RequestBody PostUpdateReqDto postUpdateReqDto) {
        PostDetResDto postDetResDto = postService.postUpdate(id, email, postUpdateReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 수정에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    //댓글 (Comment) : 모든 유저가 댓글을 달 수 있다.
    @PostMapping("/comment/create")
    public ResponseEntity<?> commentCreate(@RequestParam Long postId
            , @RequestParam String email, @RequestBody CommentSaveReqDto commentSaveReqDto) {
        Post post = postService.commentCreate(postId, email, commentSaveReqDto);
        HttpStatus code = HttpStatus.CREATED;
        String msg = "댓글 생성에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, post.getId());
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/comment/delete")
    public ResponseEntity<?> commentDelete(@RequestParam Long id, @RequestParam String email) {
        postService.commentDelete(id, email);
        HttpStatus code = HttpStatus.OK;
        String msg = "댓글 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/comment/update")
    public ResponseEntity<?> commentUpdate(@RequestParam Long id, @RequestParam String email
            , @RequestBody CommentUpdateReqDto commentUpdateReqDto) {
        PostDetResDto postDetResDto = postService.commentUpdate(id, email, commentUpdateReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "댓글 수정에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }
}