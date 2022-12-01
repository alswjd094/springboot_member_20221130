package com.example.member;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.lang.reflect.Member;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    //서비스 메서드 호출
    @Autowired
    private MemberService memberService;
    //회원가입 테스트
    //신규회원 데이터 생성(DTO)
    //회원가입 기능 실행
    //가입 성공 후 가져온 id 값으로 DB를 조회하고
    //가입시 사용한 email과 DB에서 조회한 email이 일치하는지를 판단

    @Test
    @Transactional
    @Rollback(value = true)
    public void memberSaveTest(){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("testEmail5");
        memberDTO.setMemberPassword("testPassword");
        memberDTO.setMemberName("testName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");

        //회원가입 기능 실행
        //서비스의 save 메소드 호출하고 리턴 받아오기
        Long savedId = memberService.save(memberDTO);

        //db에서 조회한 email
        MemberDTO savedMember = memberService.findById(savedId);

       //assertThat: static있어서 클래스 안붙여도 됨
        assertThat(memberDTO.getMemberEmail()).isEqualTo(savedMember.getMemberEmail());
    }

    //로그인 테스트
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인 테스트")
    public void loginTest(){
       String loginEmail = "loginEmail";
       String loginPassword = "loginPassword";

        //1. 회원가입기능 수행
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail(loginEmail);
        memberDTO.setMemberPassword(loginPassword);
        memberDTO.setMemberName("loginName");
        memberDTO.setMemberAge(22);
        memberDTO.setMemberPhone("010-1111-1111");
        memberService.save(memberDTO);

        //2. 로그인 수행
        MemberDTO loginDTO = new MemberDTO();
        loginDTO.setMemberEmail(loginEmail);
        loginDTO.setMemberPassword(loginPassword);
        //dto에 있는 로그인 정보
        MemberDTO loginResult = memberService.login(loginDTO);
        //3. 로그인 결과가 null이 아니면 테스트 통과
        assertThat(loginResult).isNotNull();
    }
    //수정테스트
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("내정보수정 테스트")
    public void updateTest(){
        //회원가입기능 수행
        MemberDTO memberDTO = newMemberDTO();
        Long savedId = memberService.save(memberDTO);
        //수정한 이름
        memberDTO.setId(savedId);
        memberDTO.setMemberName("수정 이름");
        //수정처리
        memberService.update(memberDTO);

        //db에서 조회한 이름
        MemberDTO memberDB = memberService.findById(savedId);

        //수정할때 사용한 이름, db 조회 이름 일치
        assertThat(memberDB.getMemberName()).isEqualTo(memberDTO.getMemberName());
    }

public MemberDTO newMemberDTO(){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setMemberEmail("testEmail22");
    memberDTO.setMemberPassword("testPassword22");
    memberDTO.setMemberName("loginName");
    memberDTO.setMemberAge(22);
    memberDTO.setMemberPhone("010-1111-1111");
    return memberDTO;
}
//삭제 테스트
    @Test
    @Transactional
    @Rollback(value = true)
    public void deleteTest(){
        //회원가입기능 수행
        MemberDTO memberDTO = newMemberDTO();
        Long savedId = memberService.save(memberDTO);

        //삭제처리
        memberService.delete(savedId);

        //로그인 결과가 null이면 테스트 통과
        assertThat(memberService.findById(savedId)).isNull();
    }

}
