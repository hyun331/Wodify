package com.soocompany.wodify.member.repository;

import com.soocompany.wodify.box.domain.Box;
import com.soocompany.wodify.member.domain.Member;
import com.soocompany.wodify.member.domain.Role;
import com.soocompany.wodify.member.dto.MemberManagementListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndDelYn(String email, String delYn);

    Page<Member> findAll(Pageable pageable);

    Page<Member> findAllByDelYn(Pageable pageable, String delYn);

    Optional<Member> findByIdAndDelYn(Long id, String delYn);


    Page<Member> findAllByBoxAndRoleAndDelYn(Pageable pageable, Box box, Role role, String delYn);
    List<Member> findByBoxAndRoleAndDelYn(Box box, Role role, String delYn);

    Optional<Member> findByEmailAndBoxAndDelYn(String email, Box box, String delYn);

    List<Member> findByBoxAndDelYn(Box box, String n);



    //회원 리스트 출력..
    @Query("SELECT new com.soocompany.wodify.member.dto.MemberManagementListDto(" +
            "m.id, m.name, m.email, m.address, m.phone, " +
            "CASE WHEN h.isOnHold = true THEN '정지' ELSE '등록' END, " +
            "ri.registrationDate, ri.endDate) " +
            "FROM Member m " +
            "LEFT JOIN FETCH RegistrationInfo ri ON m.id = ri.member.id AND ri.id = " +
            "(SELECT MAX(ri2.id) FROM RegistrationInfo ri2 WHERE ri2.member.id = m.id) " +
            "LEFT JOIN FETCH HoldingInfo h ON m.id = h.member.id AND h.id = " +
            "(SELECT MAX(h2.id) FROM HoldingInfo h2 WHERE h2.member.id = m.id) " +
            "WHERE m.box.id = :boxId AND m.role = :role")
    Page<MemberManagementListDto> findMemberManagementListByBox(@Param("boxId") Long boxId, @Param("role") Role role, Pageable pageable);


    @Query("SELECT new com.soocompany.wodify.member.dto.MemberManagementListDto(" +
            "m.id, m.name, m.email, m.address, m.phone, " +
            "CASE WHEN h.isOnHold = true THEN '정지' ELSE '등록' END, " +
            "ri.registrationDate, ri.endDate) " +
            "FROM Member m " +
            "LEFT JOIN FETCH RegistrationInfo ri ON m.id = ri.member.id AND ri.id = " +
            "(SELECT MAX(ri2.id) FROM RegistrationInfo ri2 WHERE ri2.member.id = m.id) " +
            "LEFT JOIN FETCH HoldingInfo h ON m.id = h.member.id AND h.id = " +
            "(SELECT MAX(h2.id) FROM HoldingInfo h2 WHERE h2.member.id = m.id) " +
            "WHERE m.box.id = :boxId AND m.role = :role AND m.name LIKE %:searchName%")
    Page<MemberManagementListDto> findMemberManagementListByBoxAndName(@Param("boxId") Long boxId, @Param("role") Role role, @Param("searchName") String searchName, Pageable pageable);


}
