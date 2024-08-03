package com.soocompany.wodify.post.controller;
import com.soocompany.wodify.common.dto.CommonResDto;
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

    @PostMapping("/create")
    public ResponseEntity<?> postCreate(@ModelAttribute PostSaveReqDto dto) {
        Post post = postService.postCreate(dto);
        HttpStatus code = HttpStatus.CREATED;
        String msg = "글이 등록되었습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, post.getId());
        return new ResponseEntity<>(commonResDto, code);
    }

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

    @PatchMapping("/delete")
    public ResponseEntity<?> postDelete(@RequestParam Long id) {
        postService.postDelete(id);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/image/delete")
    public ResponseEntity<?> imageDelete(@RequestParam Long id) {
        postService.imageDelete(id);
        HttpStatus code = HttpStatus.OK;
        String msg = "파일 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> postUpdate(@RequestParam Long id, @RequestBody PostUpdateReqDto postUpdateReqDto) {
        PostDetResDto postDetResDto = postService.postUpdate(id, postUpdateReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 수정에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PostMapping("/comment/create")
    public ResponseEntity<?> commentCreate(@RequestParam Long postId, @RequestBody CommentSaveReqDto commentSaveReqDto) {
        Post post = postService.commentCreate(postId, commentSaveReqDto);
        HttpStatus code = HttpStatus.CREATED;
        String msg = "댓글 생성에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, post.getId());
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/comment/delete")
    public ResponseEntity<?> commentDelete(@RequestParam Long id) {
        postService.commentDelete(id);
        HttpStatus code = HttpStatus.OK;
        String msg = "댓글 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/comment/update")
    public ResponseEntity<?> commentUpdate(@RequestParam Long id, @RequestBody CommentUpdateReqDto commentUpdateReqDto) {
        PostDetResDto postDetResDto = postService.commentUpdate(id, commentUpdateReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "댓글 수정에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/like")
    public ResponseEntity<?> postLike(@RequestBody PostLikeReqDto postLikeReqDto) {
        Long likeCount = postService.postLike(postLikeReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "좋아요 업데이트에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, likeCount);
        return new ResponseEntity<>(commonResDto, code);
    }
}