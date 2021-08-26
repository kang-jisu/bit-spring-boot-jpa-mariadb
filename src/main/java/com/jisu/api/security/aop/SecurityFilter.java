package com.jisu.api.security.aop;

import com.jisu.api.security.domain.SecurityProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
doFilterInternal메서드를 통해
- 실제 필터링 로직을 수행한다.
- jwt 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장하는 역할을 수행할것이다.
TODO
 */
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final SecurityProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
