package com.soocompany.wodify.member.service;

import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.dto.MemberDetResDto;
import com.soocompany.wodify.member.dto.MemberListResDto;
import com.soocompany.wodify.member.dto.MemberSaveReqDto;
import com.soocompany.wodify.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }


    // email, delYn을 통해 member 구하기
    public MemberDetResDto memberFindByEmailAndDelYn(String email, String yn){
        Member member = memberRepository.findByEmailAndDelYn(email, yn).orElseThrow(()->new EntityNotFoundException(email+" : member is not found."));
        return member.detFromEntity();

    }

    //회원가입 멤버 체크용
    public MemberDetResDto isMemberExist(String email, String yn){
        Optional<Member> member = memberRepository.findByEmailAndDelYn(email, yn);
        return member.map(Member::detFromEntity).orElse(null);

    }

    public MemberDetResDto memberRegister(MemberSaveReqDto memberSaveReqDto){
        if(memberRepository.findByEmailAndDelYn(memberSaveReqDto.getEmail(), "N").isPresent()){
            throw  new IllegalArgumentException("이미 존재하는 email 입니다. 회원가입 불가");
        }
        if(memberSaveReqDto.getName()==null)
            throw new IllegalArgumentException("이름 미입력");
        if(memberSaveReqDto.getPhone()==null)
            throw new IllegalArgumentException("전화번호 미입력");
        if(memberSaveReqDto.getRole()==null)
            throw new IllegalArgumentException("역할 미입력");


        Member newMember = memberRepository.save(memberSaveReqDto.toEntity());
        return newMember.detFromEntity();


    }


    public Page<MemberListResDto> memberList(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        Page<MemberListResDto> memberListResDtos = members.map(a->a.listFromEntity());
        return memberListResDtos;

    }

    public MemberDetResDto memberDetail(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member is not found"));
        return member.detFromEntity();
    }
}
