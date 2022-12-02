package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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
    public String saveForm(){
        return "memberPages/memberSave";
    }
    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return"memberPages/memberLogin";
    }
    @GetMapping("/member/login")
    public String loginForm(){
        return "memberPages/memberLogin";
    }
    @PostMapping("/member/login")
    public String login(@ModelAttribute MemberDTO memberDTO,HttpSession session,Model model){
        MemberDTO loginResult = memberService.login(memberDTO);
        if(loginResult !=null){
            session.setAttribute("loginEmail",memberDTO.getMemberEmail());
            model.addAttribute("modelEmail",memberDTO.getMemberEmail());
            return "memberPages/memberMain";
        }else{
            return "memberPages/memberLogin";
        }
    }
    @GetMapping("/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }
    @GetMapping("/member/")
    public String findAll(Model model){
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("members",memberDTOList);
        return "memberPages/memberList";
    }
    @GetMapping("/member/{id}")
    public String findById(@PathVariable Long id, Model model){
        System.out.println("id = " + id);
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member",memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/member/update")
    public String updateForm(Model model,HttpSession session){
        String loginEmail = (String) session.getAttribute("loginEmail");
        MemberDTO memberDTO = memberService.findByMemberEmail(loginEmail);
        model.addAttribute("member",memberDTO);
        return "memberPages/memberUpdate";
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return"memberPages/memberMain";
    }

    @GetMapping("/member/delete/{id}")
    public String delete(@PathVariable Long id){
        memberService.delete(id);
        return "redirect:/member/";
    }

    @PostMapping("/member/dup-check")
    public @ResponseBody String emailDuplicateCheck(@RequestParam("inputEmail") String memberEmail){
        System.out.println("memberEmail = " + memberEmail);
        String inputEmailResult = memberService.emailDuplicateCheck(memberEmail);
        return inputEmailResult;
    }

    
}
