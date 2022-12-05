package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Member;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "memberPages/memberLogin";
    }

    @GetMapping("/member/login")
    public String loginForm(@RequestParam(value = "redirectURL",defaultValue = "/member/main")String redirectURL, Model model) {
        model.addAttribute("redirectURL",redirectURL);
        return "memberPages/memberLogin";
    }

    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session,
                            @RequestParam(value = "redirectURL",defaultValue = "/member/main")String redirectURL) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
            //인터셉터에서 걸려서 로그인한 사용자가 직전에 요청한 페이지로 보내주기 위해 redirect:/직전요청주소
            //인터셉터 걸리지 않고 로그인을 하는 사용자는 defaultValue에 의해 main으로 이동
            return "redirect:" +redirectURL;
//            return "memberPages/memberMain";
        } else {
            return "memberPages/memberLogin";
        }
    }
    @GetMapping("/member/main")
    public String mainPage(){
        return "memberPages/memberMain";
    }

    @GetMapping("/member/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    @GetMapping("/member/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("members", memberDTOList);
        return "memberPages/memberList";
    }

    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model) {
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/member/update")
    public String updateForm(Model model, HttpSession session) {
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberUpdate";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "memberPages/memberMain";
    }

    @GetMapping("/member/delete/{id}")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/member/";
    }

    @PostMapping("/member/dup-check")
//    public @ResponseBody String emailDuplicateCheck(@RequestParam("inputEmail") String memberEmail){
    public ResponseEntity emailDuplicateCheck(@RequestParam("inputEmail") String memberEmail) {
        //body뿐만 아니라 headers 정보도 같이 보내줌
        System.out.println("memberEmail = " + memberEmail);
        String inputEmailResult = memberService.emailDuplicateCheck(memberEmail);
//        return inputEmailResult;
        if (inputEmailResult != null) {
            return new ResponseEntity<>("사용할 수 있는 이메일입니다.", HttpStatus.OK);
            //HttpStatus: 오류코드, 에러 핸들링 방식, OK: 200
        } else {
            return new ResponseEntity<>("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT);
            //409오류
        }
    }

    @GetMapping("/member/ajax/{id}")
//    public @ResponseBody MemberDTO list_ajax(@PathVariable Long id){
      public ResponseEntity findByIdAxios(@PathVariable Long id){
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
        if(memberDTO !=null){
            return new ResponseEntity<>(memberDTO,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
/*
REST API
get: /member/{id}, 조회
post: /member/{id}, 저장
delete: /member/{id}, 삭제
put: /member/{id}, 수정
요청 메시지를 전달하는 목적
 */
    @DeleteMapping("/member/{id}")
    public ResponseEntity deleteByAxios(@PathVariable Long id){
        System.out.println("id = " + id);
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/member/{id}")
    public ResponseEntity updateByAxios(@PathVariable Long id, @RequestBody MemberDTO memberDTO){
        System.out.println("id = " + id + ", memberDTO = " + memberDTO);
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

