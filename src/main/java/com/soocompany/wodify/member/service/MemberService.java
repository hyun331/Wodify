package com.soocompany.wodify.member.service;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.box.repository.BoxRepository;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.dto.MemberDetResDto;
import com.soocompany.wodify.member.dto.MemberListResDto;
import com.soocompany.wodify.member.dto.MemberSaveReqDto;
import com.soocompany.wodify.member.dto.MemberUpdateDto;
import com.soocompany.wodify.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BoxRepository boxRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, BoxRepository boxRepository){
        this.memberRepository = memberRepository;
        this.boxRepository = boxRepository;
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
            log.error("memberRegister() : 이미 존재하는 email 입니다. 회원가입 불가");
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
        Page<Member> members = memberRepository.findAllByDelYn(pageable, "N");
        Page<MemberListResDto> memberListResDtos = members.map(a->a.listFromEntity());
        return memberListResDtos;

    }

    public MemberDetResDto memberDetail(Long id) {
        Member member = memberRepository.findByIdAndDelYn(id, "N").orElseThrow(()->new EntityNotFoundException("member is not found"));
        return member.detFromEntity();
    }


    public MemberDetResDto memberUpdate(Long id, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member is not found"));
        member.memberInfoUpdate(memberUpdateDto);
        return member.detFromEntity();
    }

    public void memberDelete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("member is not found"));
        member.updateDelYn();
    }


    //코치의 박스 가입/변경 메서드
    //id = member id(코치의 id)
    public MemberDetResDto coachBoxUpdate(Long id, String boxCode) {
        //유효한 박스 코드인지 확인하기
        Box box = boxRepository.findByCodeAndDelYn(boxCode, "N").orElseThrow(()->new IllegalArgumentException("memberBoxUpdate() : 박스코드가 유효하지 않습니다."));
        Member member = memberRepository.findById(id).orElseThrow(()->new EntityNotFoundException("memberBoxUpdate() : id에 맞는 member가 없습니다."));

        //이 코치가 대표면 박스 가입/변경 불가
        if(member.getBox()!=null && member.equals(member.getBox().getMember())) {
            throw new IllegalArgumentException("coachBoxUpdate() : 박스의 대표는 다른 박스로 변경 불가합니다.");
        }

        //box 변경
        member.memberBoxUpdate(box);
        return member.detFromEntity();

    }


}