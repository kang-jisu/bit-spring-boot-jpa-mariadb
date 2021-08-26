웹사이트에서 아이디와 패스워드를 통해 로그인하는 과정 : 인증(Authentication)

특정 기준을 충족해야 웹사이트의 서비스를 이용하도록 하는것 : 인가(Authorization)

보호 대상에 접근하는 유저 : 접근 주체(Principal)

인증된 주체가 어플리케이션의 동작을 수행할 수 있도록 허락되었는지를 결정할 때 사용 : 권한

# 기본 개념

## 1. Spring Security

스프링 어플리케이션에서 인증과 권한을 포함하는 보안을 담당하는 스프링의 하위 프레임워크이다.

인증과 인가를 위한 세션관리를 자체적으로 구현하지 않아도, 스프링 시큐리티를 적용하면 해결할 수 있다.

스프링 시큐리티는 본래 **세션 기반 인증**을 사용하기 때문에 JWT와 분리해서 잘 이해해야한다.

<br/>

### Spring Security Filter

스프링 시큐리티는 필터(Filter)를 기반으로 동작하기 때문에, 스프링 MVC와는 분리되어 관리되고 동작한다.

Spring Security 3.2 부터는 자바 config 클래스로 간단하게 설정할 수 있다.

스프링은 클라이언트로 부터 요청이 들어오면 DispatcherServlet에서 1차적으로 요청을 처리한다. **필터는 요청이 DispatcherServlet으로 오기 전 과정**에 존재하며, 클라이언트와 Servlet 자원 사이에서 여러 사전 처리를 수행한다.

<br/>

Spring Security에서 제공하는 필터는 10개 이상으로, 이를 묶어 Security Filter Chain이라고 한다.

![img](https://blog.kakaocdn.net/dn/bJ51wr/btqYE69wD6W/zKZPrVK8BFaeISABGEYiD1/img.png)출처 : https://atin.tistory.com/590

<br/>

- **SecurityContextPersistenceFilter** : SecurityContextRepository에서 SecurityContext를 가져오거나 저장

- **LogoutFilter** : 설정된 로그아웃 URL로 오는 요청을 처리하며, 유저를 로그아웃 처리한다.

- (UsernamePassword)**AuthenticationFilter** :

  form 태그 기반의 인증을 담당하며, 설정된 로그인 URL로 오는 요청을 처리하며 유저를 인증처리한다.

    - AuthenticationManager를 통해 인증 실행
    - 인증 성공시, 획득한 Authentication 객체를 SecurityContext에 저장 후, AuthenticationSuccessHandler를 실행
    - 인증 실패시, AuthenticationFailureHandler을 실행

### Spring Security 인증 관련 아키텍쳐

아이디와 비밀번호를 통해서 로그인 관련 인증을 처리하는 필터는 AutenticationFilter이다.

![img](https://blog.kakaocdn.net/dn/ceanmM/btqYIkl9GF0/3AKGUzcpXgrHg1hOFOvNz0/img.png)

- 클라이언트가 로그인을 위한 요청을 보낸다.
- AuthenticationFilter는 이 요청을 가로채서, 가로챈 정보를 통해 UsernamePasswordAuthenticationToken이라는 인증용 객체를 생성한다.
- AutenticationManager의 구현체인 ProvideManager에게 UPAToken객체를 전달한다.
- 그다음 uthenticationProvider(s), UserDetailService를 통해 사용자 정보를 읽어온다.
    - UserDeatilService는 인터페이스이며, 이를 구현한 Bean을 생성하면 스프링 시큐리티가 해당 Bean을 사용한다. 사용자 정보를 읽어오기 위해 개발자가 어떤 DB를 사용할지 정할 수 있다.
    - UserDetailService가 로그인한 ID에 해당하는 정보를 DB에서 읽어들여 UserDetails를 구현한 객체를 반환한다. 반환된 UserDetails 객체에 저장된 사용자 정보는 세션에 저장된다.
- 스프링 시큐리티는 **세션 저장소인 SecurityContextHolder**에서 UserDetails정보를 저장하며, 이후 Session Id와 함께 응답을 보낸다.
- 이후 요청이 들어올 경우 쿠키에 포함된 Session ID 정보를 통해 로그인 정보가 저장되어 있는지 서버에서 확인하며, 정보가 저장되어 있고 유효하다고 판단되면 인증처리를 해주게 된다. **-> 스프링 시큐리티는 세션-쿠키 기반의 인증 방식을 사용함을 의미한다.**

<br/>

## 2. JWT (Json Web Token)

JSON 객체를 통해 안전하게 정보를 전송할 수 있는 웹 표준이다.

'.'를 구분자로 세 부분으로 구분되어있는 문자열로 이루어져있다.

![Encoded JWT](https://cdn.auth0.com/content/jwt/encoded-jwt3.png) \[출처 jwt.io]

```
헤더(header).(내용)payload.(서명)signature
```

JSON 객체를 암호화하여 만든 문자열 값으로 위, 변조가 어려운 정보이며 다른 토큰과 달리 토큰 자체에 데이터를 가지고 있다는 특징이 있다. 이 특징으로 사용자의 인증 요청시 필요한 정보를 전달할 수 있다.

<br/>

API 서버는 JWT의 payload 값을 확인해 권한이 있는 사용자인지 확인하고 리소스를 제공하게 된다. 이부분에 담기는 정보의 한 조각을 `Claim`이라고 하며 Claim은 name/value 쌍으로 이루어져있다.

<br/>

세션-쿠키 기반의 로그인이 아닌 JWT 같은 토큰 기반의 로그인을 하게 되면 세션이 유지되지 않는 다중 서버 환경에서도 로그인을 유지할 수 있게 된다는 장점이 있다.

<br/>

**단점**으로는

- 토큰 자체에 정보를 담기 때문에 토큰의 길이가 길어져 부하가 생길 수 있고, payload를 디코딩하여 데이터를 볼 수 있다는 점이다. 데이터에 중요한 내용을 넣지 않거나 한번 더 암호화 해야한다.
- 또한 한번 만들고 나면 서버에서 제어가 불가능하므로(임의 삭제 불가능) 만료시간을 지정해주어야 한다.
- 그리고 토큰을 클라이언트 단에서 저장해주어야 한다.

# 코드 이해

### JWT와 Security 설정 및 현재 작성한 파일,디렉토리

```bash
 ├── security
    │   │               │   ├── aop
    │   │               │   │   ├── SecurityAccessDeniedHandler.java
    │   │               │   │   ├── SecurityAuthenticationEntryPoint.java
    │   │               │   │   ├── SecurityExceptionHandler.java
    │   │               │   │   └── SecurityFilter.java
    │   │               │   ├── config
    │   │               │   │   ├── SecurityConfig.java
    │   │               │   │   └── WebSecurityConfig.java
    │   │               │   ├── domain
    │   │               │   │   ├── Role.java
    │   │               │   │   ├── SecurityProvider.java
    │   │               │   │   ├── SecurityToken.java
    │   │               │   │   └── UserDetailsServiceImpl.java
    │   │               │   ├── exception
    │   │               │   │   ├── ErrorCode.java
    │   │               │   │   ├── LoginRunnerException.java
    │   │               │   │   └── SecurityRuntimeException.java

```

- JWT관련
    - `SecurityProvider` : 유저 정보로 JWT 토큰을 만들거나 토큰을 바탕으로 유저정보를 가져옴
    - `SecurityFilter` : Spring Request 앞단에 붙일 Custom Filter
- Spring Security 관련
    - `SecurityConfig` : JWT Filter를 추가
    - `aop/xxhandler` : 에러 처리
    - `WebSecurityConfig` : 스프링 시큐리티에 필요한 설정

<br/>

### Spring Security관련

#### SecurityWebApplicationInitializer

는 AbstractSecurityWebApplicationInitializer을 상속받은 클래스로, Spring Security 사용을 위한 프록시 필터 관련 설정을 담당한다.

```java
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
 
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
 
}
```

<br/>

#### WebSecurityConfig extends WebSecurityConfigurerAdapter

구체적인 로그인/로그아웃 등 보안 관련 설정을 어떻게 할 지를 결정한다.

사용자 인증에 대한 정보를 WebSecurityConfigurerAdapter의 configure(HttpSecurity http)안에 있는 설정에서 제공하기 때문이다.

```java
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

```

##### configure(WebSecurity web)

- 인증이나 인가가 필요없는 페이지에 대한 설정을 담당
- antMatchers로 설정한 해당 부분에 대해서는 인증이나 인가를 거치지 않게다는 의미이다.

##### configure(AuthenticationManagerBuilder auth) or AuthenticationManager

- 좀 더 알아보기

##### configure(HttpSecurity http)

- 인증이나 인가가 필요한 페이지에 대한 설정
- 리소스 접근 권한 설정, 로그인 로그아웃 인증 후 페이지 이동, 인증 실패시 이동과 같은 설정
- 커스텀 필터 설정
- csrf, https 등 거의 모든 시큐리티의 설정을 담당

> - http.csrf().disable();
    >   - crsf는 보안 설정 중 post방식으로 값을 전송할 때 token을 사용해야하는 보안 설정인데, 개발단계에서는 disable 해둔다.
>
> - http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    >   - 스프링 시큐리티의 세션 관리 설정
          >     - ALWAYS : 항상 세션 생성
>     - IF_REQUIRED(default) : 필요시 생성
>     - NEVER : 생성하지 않지만, 기존에 존재하면 사용
>     - STATELESS : 생성하지도 않고 기존것을 사용하지도 않음
>   - jwt토큰과 같은 인증방식을 사용할 경우 세션을 사용하지 않는 Stateless 설정을 해준다. 메모리에 로그인했던 사람의 데이터 저장하지 않는 상태
> - http.authorizeRequests()
    >   -  HttpServletRequest 요청 URL에 따라 접근권한을 설정할 것이다.
> - .antMatchers("주소").permitAll()
    >   - // 토큰 없어도 접속가능
> - .anyRequest().authenticated();
    >   - 명시되지 않은 나머지에 대해서는 로그인 과정을 거쳐야함
> - http.exceptionHandling().accessDeniedPage("/login");
    >   - 권한이 없는 사용자가 접근했을 경우 이동할 경로
> - http.apply(new SecurityConfig(provider));
    >   - custom confg(security config)를 적용한다.

#### SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>

JwtFilter를 Security로직에 적용하는 역할을 한다.

```java
@RequiredArgsConstructor
public class SecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final SecurityProvider provider;

    // provider를 주입받아 jwt filter를 통해 security 로직에 필터를 등록한다.
    @Override
    public void configure(HttpSecurity builder) throws Exception {
        SecurityFilter filter = new SecurityFilter(provider);
        builder.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
```

#### SecurityFilter ( JwtFilter)

```java
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final SecurityProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
```

- `OncePerRequestFilter` 인터페이스를 구현했기 때문에 요청받을 때 단 한번만 실행된다.
- `doFilterInternal`
    - 실제 필터링 로직을 수행한다.
    - jwt 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장하는 역할을 수행할것이다.

<br/>

### HandlerExceptionResolver를 이용한 인증 실패시 핸들링

#### AuthenticationEntryPoint

```java
@RequiredArgsConstructor
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        handlerExceptionResolver.resolveException(request, response, null, authException);
    }
}
```

유저 정보 없이 접근하면 (authException)401 응답을 내려준다

#### AccessDeniedHandler

```java
@RequiredArgsConstructor
public class SecurityAccessDeniedHandler implements AccessDeniedHandler {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
    }
}

```

유저 정보는 있으나 자원에 접근할 권한이 없을 경우 (accessDeniedException)403응답을 내려준다.

<br/>

### JWT 토큰 생성 관련

```java
@Log
public class SecurityToken {
    private final String token; // 내부에서 랜덤값으로 만드는 토큰이라 requiredArgconstructor로 주입받으면 안됨
    private final String key;

    public SecurityToken(String key){
        this.token = createToken();
        this.key = key;
    }

    private String createToken(){
        try {
            Map<String, Object> headers = new HashMap<>();
            headers.put("typ", "JWT");
            headers.put("alg", "HS256"); // HMAX ,SHA 256 or RSA

            Map<String, Object> payload = new HashMap<>();
            payload.put("data", "dummy");

            long expirationTime = 1000 * 6L * 2L;

            Date ext = new Date();
            ext.setTime(ext.getTime() + expirationTime);

            return Jwts.builder()
                    .setHeader(headers)
                    .setClaims(payload)
                    .setSubject("user")
                    .setExpiration(ext)
                    .signWith(SignatureAlgorithm.HS256, key.getBytes())
                    .compact();
        }catch (SecurityException e){
            log.info("SecurityException JWT");
        }catch (MalformedJwtException e){
            log.info("MalformedJwtException JWT");
        }catch (ExpiredJwtException e){
            log.info("ExpiredJwtException JWT");
        }catch (UnsupportedJwtException e){
            log.info("UnsupportedJwtException JWT");
        }catch (IllegalAccessError e){
            log.info("IllegalAccessError JWT");
        }return null;
    }
}

```

##### header

- headers.put("alg", "HS256");
- headers.put("typ", "JWT");
    - 토큰의 타입(JWT만 존재)과 해싱 알고리즘을 지정해준다. 알고리즘은 토큰 검증시에 사용한다.

##### payload

claims를 넣어준다.

다음은 권장되는 Reserved Claims이다.

- iss (String) : 토큰 발행자
- exp (Number) : expiration time 만료일
- sub (String) : 제목
- iat (String) : 생성 시간

##### Signature

header와 payload의 데이터 무결성과 변조 방지를 위한 서명

header와 payload를 합친 후 secret key와 함께 해싱알고리즘으로 인코딩한다.

### Securityprovider implements AuthenticationProvider

Spring Security 에서 인증 절차를 정의할 때, ProviderManager가 가진 목록에 존재하는 AuthenticationProvider가 UserDetailsService를 이용해 특정 유형의 (id/pw기반) 인증을 수행한다.

```java
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
    추가 - user 구현 후 
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
```

#### @PostConstruct

의존성 주입이 이루어진 후 초기화를 수행하는 메서드 . Service를 수행하기 전에 발생한다.



출처

- 인증과인가 | tistory junu0516 [SpringSecurity사용하기](https://junu0516.tistory.com/100)
- 스프링 시큐리티 세션 정책| tistory  [https://fenderist.tistory.com/342](https://fenderist.tistory.com/342)
- tistory webfirewood [Spring Security+ jwt회원가입,로그인기능 구현](https://webfirewood.tistory.com/115)
- tistory bcp0109 [Spring Security +jwt](https://bcp0109.tistory.com/301) **
- https://catsbi.oopy.io/c0a4f395-24b2-44e5-8eeb-275d19e2a536 ** security 그림이랑 엄청 잘 정리되어있다. 참고 !!
- tistory zorba91 [@PostConstruct](https://zorba91.tistory.com/223)

----

