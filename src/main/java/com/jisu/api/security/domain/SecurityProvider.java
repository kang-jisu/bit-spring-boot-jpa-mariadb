package com.jisu.api.security.domain;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/*
토큰을 제공하는 매니저
 */

@Log
@Component
@RequiredArgsConstructor
public class SecurityProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    @Value("${security.jwt.token.security-key}")
    private String secretKey;
    @Value("${security.jwt.token.expiration-length}")
    private long validityInMs ;

    /*
    일단그냥 추가함
    // TODO log 삭제해야함
    key, claim값 좀 보면서 확인하면 좋음 개발단계에서는
     */
    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        log.info("secretKEy" + secretKey);
    }

    public String createToken(String username, List<Role> roles){
        log.info("createToken 들어옴------------------");
        Claims claims = Jwts.claims().setSubject(username);
        log.info("--------------------claims" + claims);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .filter(Objects::nonNull).collect(Collectors.toList()));

        log.info("claim--------" + claims);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails.getAuthorities(), "", userDetails.getAuthorities());
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) throws Exception{
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (JwtException | IllegalArgumentException e){
            throw new Exception();
        }
    }

    /*
    추가
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }

}
