package com.example.member.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//implements: 인터페이스 구현하는 클래스 만들 때
public class LoginCheckInterceptor implements HandlerInterceptor {
    //로그인 여부 확인
   //로그인 하지 않은 상태라면 로그인페이지로 보내고
   //로그인을 수행하면 직전에 요청한 주소로 보내줌
   //로그인 상태라면 넘어감

   @Override
   //메서드 재정의: 타입, 메서드이름, 매개변수 변경 못함
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
                            //요청 객체                  //응답 객체
       String requestURL = request.getRequestURI();
       //request에서 제공하는 메서드, 요청한 주소 가져와 준다.
       System.out.println("requestURL = " + requestURL);
       HttpSession session = request.getSession();
       //request에서 session 가져옴
       if(session.getAttribute("loginEmail") == null){
           response.sendRedirect("/member/login?redirectURL="+requestURL);
            //로그인 주소로 보내면서 로그인 끝나면 다시 돌아갈 주소도 함께 보냄
           return false;
       }
       return true;
   }
}

