package com.jisu.api.security.config;

import com.jisu.api.security.domain.SecurityProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SecurityProvider provider; // json web token값 제공

    //database에 비밀번호를 저장해버리면 관리자가 볼 수 있으니, 비밀번호를 인코딩
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    // 웹에서 return하는 값을 modelMapper로 감쌀 것
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //메모리에 로그인했던 사람의 데이터 저장하지 않는 상태
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/users/signin").permitAll() // 토큰 없어도 접속가능
                .antMatchers("/users/signup").permitAll()
                .antMatchers("/h2-console/**/**").permitAll() // 개발 편의상 허용할 수 있는 옵션들
                .antMatchers("/admin/access").permitAll()  // 어드민 개발 확인용
                .anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/login");
        http.apply(new SecurityConfig(provider));

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "*/**")
                .antMatchers("/","/h2-console/**");
    }
}
