package com.soocompany.wodify.post.service;
import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.repository.MemberRepository;
import com.soocompany.wodify.post.domain.Post;
import com.soocompany.wodify.post.domain.Type;
import com.soocompany.wodify.record.domain.Record;
import com.soocompany.wodify.post.dto.*;
import com.soocompany.wodify.post.repository.PostRepository;
import com.soocompany.wodify.reservation.service.ReservationService;
import com.soocompany.wodify.reservation_detail.service.ReservationDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.swing.text.html.Option;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final ImageService imageService;

    public Post postCreate(PostSaveReqDto dto) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postCreate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Box box = member.getBox();
        if (box == null) { throw new IllegalArgumentException("box 에 등록되지 않은 사용자입니다.");}
        return postRepository.save(dto.toEntity(member));
    }

    public List<PostListResDto> postList() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postList() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        List<Post> posts = postRepository.findAllByDelYnOrderByIdDesc("N");
        return posts.stream().map(PostListResDto::fromEntity).collect(Collectors.toList());
    }

    public List<PostListResDto> postListNotice() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postListNotice() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        List<Post> posts = postRepository.findAllByTypeAndBoxAndDelYnOrderByIdDesc(Type.NOTICE, member.getBox(), "N");
        return posts.stream().map(PostListResDto::fromEntity).collect(Collectors.toList());
    }

    public Page<PostListResDto> postListPage(PostSearchDto searchDto, Pageable pageable) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postListPage() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();

                if (searchDto.getSearchCategory() != null) {
                    if (searchDto.getSearchCategory().equals("title")) {
                        predicateList.add(criteriaBuilder.like(root.get("title"), "%" + searchDto.getSearchText() + "%"));
                    } else if (searchDto.getSearchCategory().equals("memberName")) {
                        predicateList.add(criteriaBuilder.like(root.get("memberName"), "%" + searchDto.getSearchText() + "%"));
                    }
                }
                // 추가 조건: type = "POST"
                predicateList.add(criteriaBuilder.equal(root.get("type"), Type.POST));
                // 추가 조건: BoxId = member.getBox().getId()
                predicateList.add(criteriaBuilder.equal(root.get("box").get("id"), member.getBox().getId()));
                // 추가 조건: delYn = "N"
                predicateList.add(criteriaBuilder.equal(root.get("delYn"), "N"));
                // 최종적으로 모든 조건을 AND로 묶기
                return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
            }
        };
        return postRepository.findAll(specification, pageable).map(PostListResDto::fromEntity);
    }

    public PostDetResDto postDetail(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            log.error("postDetail() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postDetail() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        return PostDetResDto.fromEntity(post);
    }

    public void postDelete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("postDelete() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postDelete() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postDelete() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!post.getMember().getId().equals(Long.parseLong(id))) {
            log.error("postDelete() : 본인의 게시글이 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        post.deletePost();
    }

    public PostDetResDto postUpdate(Long postId, PostUpdateReqDto postUpdateReqDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("postUpdate() : 해당 id의 게시글을 찾을 수 없습니다.");
            return new EntityNotFoundException("해당 id의 게시글을 찾을 수 없습니다.");
        });
        if (post.getDelYn().equals("Y")) {
            log.error("postUpdate() : 삭제된 게시글 입니다.");
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postUpdate() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        if (!post.getMember().getId().equals(member.getId())) {
            log.error("postUpdate() : 본인의 게시글이 아닙니다.");
            throw new IllegalArgumentException("본인의 게시글이 아닙니다.");
        }
        post.updatePost(postUpdateReqDto);
        Post savedPost = postRepository.save(post);
        return PostDetResDto.fromEntity(savedPost);
    }

    public PostRecordResDto postRecord(LocalDate date) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByIdAndDelYn(Long.parseLong(id), "N").orElseThrow(() -> {
            log.error("postRecord() : Email 에 해당하는 member 가 없습니다.");
            return new EntityNotFoundException("Email 에 해당하는 member 가 없습니다.");
        });
        Box box = member.getBox();
        if (box == null) { throw new IllegalArgumentException("box 에 등록되지 않은 사용자입니다.");}
        System.out.println("before findTopRecordByDateAndBoxIdAndMemberId");
        Optional<Tuple> tupleOptional = postRepository.findTopRecordByDateAndBoxIdAndMemberId(date, box.getId(), member.getId());
        if (tupleOptional.isEmpty()) {
            log.error("postRecord() : record가 없습니다.");
            throw new EntityNotFoundException("record가 없습니다.");
        }
        System.out.println("after findTopRecordByDateAndBoxIdAndMemberId");
//        List<PostRecordResDto> postRecordResDtoList = new ArrayList<>();
//        for (Record record : recordList) { postRecordResDtoList.add(PostRecordResDto.fromEntity(record)); }
        return convertToDto(tupleOptional.get());
    }

    public PostRecordResDto convertToDto(Tuple tuple) {
        return new PostRecordResDto(
                tuple.get("exerciseTime", Time.class),  // 쿼리에서 지정한 별칭
                tuple.get("snf", Character.class),           // 쿼리에서 지정한 별칭
                tuple.get("comments", String.class)       // 쿼리에서 지정한 별칭
        );
    }
}
