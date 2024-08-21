package com.soocompany.wodify.post.controller;
import com.soocompany.wodify.common.dto.CommonResDto;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.service.CommentService;
import com.soocompany.wodify.post.service.ImageService;
import com.soocompany.wodify.post.service.LikeService;
import com.soocompany.wodify.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<?> postCreate(@ModelAttribute PostSaveReqDto dto) {
        Post post = postService.postCreate(dto);
        HttpStatus code = HttpStatus.CREATED;
        String msg = "글이 등록되었습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, post.getId());
        return new ResponseEntity<>(commonResDto, code);
    }
//
//    @PostMapping("/upload-media")
//    public ResponseEntity<?> uploadMedia(@ModelAttribute ImageSaveReqDto dto) throws IOException {
//        String url = imageService.uploadMedia(dto.getFile());
//        HttpStatus code = HttpStatus.OK;
//        String msg = "파일이 s3에 업로드 되었습니다.";
//        CommonResDto commonResDto = new CommonResDto(code, msg, url);
//        return new ResponseEntity<>(commonResDto, code);
//    }
//
//    @DeleteMapping("/delete-media")
//    public ResponseEntity<?> deleteMedia(@RequestParam String imageUrl) {
//        imageService.deleteMedia(imageUrl);
//        HttpStatus code = HttpStatus.OK;
//        String msg = "파일이 s3에서 삭제 되었습니다.";
//        CommonResDto commonResDto = new CommonResDto(code, msg, imageUrl);
//        return new ResponseEntity<>(commonResDto, code);
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<?> postList() {
//        List<PostListResDto> posts = postService.postList();
//        HttpStatus code = HttpStatus.OK;
//        String msg = "전체 게시글 목록 조회에 성공하였습니다.";
//        CommonResDto commonResDto = new CommonResDto(code, msg, posts);
//        return new ResponseEntity<>(commonResDto, code);
//    }
//
    @GetMapping("/list/notice")
    public ResponseEntity<?> postListNotice() {
        List<PostListResDto> posts = postService.postListNotice();
        HttpStatus code = HttpStatus.OK;
        String msg = "박스 공지사항 조회에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, posts);
        return new ResponseEntity<>(commonResDto, code);
    }

    @GetMapping("/list/page")
    public ResponseEntity<?> postListPage(PostSearchDto postSearchDto, Pageable pageable) {
        System.out.println("postSearchDto = " + postSearchDto);
        Page<PostListResDto> posts = postService.postListPage(postSearchDto, pageable);
        HttpStatus code = HttpStatus.OK;
        String msg = "박스 게시글 목록 조회에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, posts);
        return new ResponseEntity<>(commonResDto, code);
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<?> postDetail(@PathVariable Long postId) {
        PostDetResDto postDetResDto = postService.postDetail(postId);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 상세 조회에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/delete/{postId}")
    public ResponseEntity<?> postDelete(@PathVariable Long postId) {
        postService.postDelete(postId);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/update/{postId}")
    public ResponseEntity<?> postUpdate(@PathVariable Long postId, @ModelAttribute PostUpdateReqDto postUpdateReqDto) {
        PostDetResDto postDetResDto = postService.postUpdate(postId, postUpdateReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "게시글 수정에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PostMapping("/comment/create/{postId}")
    public ResponseEntity<?> commentCreate(@PathVariable Long postId, @RequestBody CommentSaveReqDto commentSaveReqDto) {
        CommentResDto commentResDto = commentService.commentCreate(postId, commentSaveReqDto);
        HttpStatus code = HttpStatus.CREATED;
        String msg = "댓글 생성에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, commentResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/comment/delete/{commentId}")
    public ResponseEntity<?> commentDelete(@PathVariable Long commentId) {
        commentService.commentDelete(commentId);
        HttpStatus code = HttpStatus.OK;
        String msg = "댓글 삭제에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, null);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PatchMapping("/comment/update/{commentId}")
    public ResponseEntity<?> commentUpdate(@PathVariable Long commentId, @RequestBody CommentUpdateReqDto commentUpdateReqDto) {
        PostListResDto postDetResDto = commentService.commentUpdate(commentId, commentUpdateReqDto);
        HttpStatus code = HttpStatus.OK;
        String msg = "댓글 수정에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postDetResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> postLike(@PathVariable Long postId) {
        Long likeCount = likeService.postLike(postId);
        HttpStatus code = HttpStatus.OK;
        String msg = "좋아요 업데이트에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, likeCount);
        return new ResponseEntity<>(commonResDto, code);
    }

    @GetMapping("/record/{date}")
    public ResponseEntity<?> getRecord(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        PostRecordResDto postRecordResDto= postService.postRecord(date);
        HttpStatus code = HttpStatus.OK;
        String msg = "기록 조회에 성공하였습니다.";
        CommonResDto commonResDto = new CommonResDto(code, msg, postRecordResDto);
        return new ResponseEntity<>(commonResDto, code);
    }

}