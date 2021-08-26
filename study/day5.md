## CORS

Cross origin resoucres sharing (교차 출처 리소스 공유 )정책

다른 origin의 리소스를 요청할 때의 정책이다.

요청을 보낼 때 header의 origin 이라는 필드에 출처를 담아 보낸다.

### Simple request 방식

- GET, HEAD, POST method 한정
- POST 방식일 경우 Content-type이
    - application/x-www-form-urlencoded
    - multipart/form-data
    - text/plain
- custome header 전송 불가

일단 보내고 브라우저가 서버가 보내준 Access-Control-Allow-Origin값을 보고 판단하는 방식이다.

<br/>

서버에 1번 요청하고 서버도 1번 응답한다.

<br/>

### prefilght request 방식

Simple Request조건에 해당하지 않으면 다음 방식으로 요청한다.

<br/>

예비요청으로 Option 메소드를 먼저 보내 허용하는 origin과 method를 받아내고 본요청을 서버에 보낸다.

<br/>

Access-Control-Request-[Method|Header] 를 포함한 요청과 Access-Control-Allow-[Method|Header]를 포함한 응답이 온다.

<br/>

### Request with Credential

요청에 쿠키를 포함할 때 Credential을 설정한다.

Credential요청을 보낼땐 서버는 `Access-Control-Allo-Credentials:true` 이어야하고, `Access-Control-Allow-Origin`이 `*` 이면 안된다.

<br/>

### Access-Control-Expose-Headers

기본적으로 브라우저에 노출되는 HTTP Response Header는

- Cache-Control
- Content-Language
- Content-type
- Expires
- Last-Modified
- Pragma

인데, 브라우저 측에서 접근할 수 있는 헤더를 추가할 수 있다. 커스텀 헤더도 포함할 수 있다.

### <br/>

> Access-Control-Allow-Origin: origin | * 
>
> Access-Control-Allow-Methods : method | *
>
> [Access-Control-Max-Age (en-US)](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Access-Control-Max-Age)는 다른 preflight request를 보내지 않고, preflight request에 대한 응답을 캐시할 수 있는 시간(초)
>
> Access-Control-Expose-Headers : 브라우저가 접근할 수 있는 헤더를 서버의 화이트리스트에 추가
>
> Access-Control-Allow-Credentials : credentials 플래그가 true일 때 요청에 대한 응답을 표시할 수 있는지.



> 출처
>
> - github.io evan-moon [CORS는 왜 이렇게 우리를 힘들게 하는걸까?](https://evan-moon.github.io/2020/05/21/about-cors/)
>
> - github.io homoefficio [CrossOriginResourceSharing-CORS](https://homoefficio.github.io/2015/07/21/Cross-Origin-Resource-Sharing/)
>
>



---

## CorsConfig

컨트롤러에 `@CrossOrigin`으로 설정할 수 있는데, 이 방식과 전역적으로 config파일을 생성하는것 까지 알아보겠다.

<br/>

@CrossOrigin에서 지정할 수 있는 값은이고 아무 값도 지정하지않으면 모든 도메인, 모든 요청방식에 대해 허용한다는 의미이다.

- origins
- value = origins과 같은의미
- allowedHeaders
- exposeHeaders
- methods
- allowCredentials
- maxAge

## Swagger

---

swagger란 간단한 설정으로 프로젝트에서 지정한 URL들을 HTMl화면으로 확인할 수 있게 해주는 프로젝트이다. Api spec에 대한 문서를 자동화해준다 .

<br/>

일단 build.gradle에 의존성을 추가한다.  버전을 명시해주어야 오류가 나지 않는다.

```java
    implementation group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.9.2'
    implementation group: 'io.springfox', name: 'springfox-swagger2', version: '2.9.2'
```

<br/>

SwaggerConfig.java를 생성하여 Bean을 등록한다.

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any()) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
                .paths(PathSelectors.ant("/api/**")) // 그중 /api/** 인 URL들만 필터링
                .build();
    }
}
```

- Docket : Swagger의 문서화된 객체

<br/>

### Controller

```java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@DeleteMapping("/{id}")
@ApiOperation(value="delete", notes="delete")
@ApiResponses(value = {@ApiResponse(code = 400, message = "Something went wrong"),
                       @ApiResponse(code = 403, message = "Access Denied"),
                       @ApiResponse(code = 422, message = "Username is alredy in use")})
public void deleteById(@PathVariable Long id) {
  itemService.deleteById(id);
}
```

- Controller에 `@Api` 를 추가해 Swagger의 리소스임을 명시한다.

- @ApiOperation: API에 대한 설명, 이름 -> value는 지정하지 않으면 메소드 이름
- @ApiResponse : operation의 가능한 response를 명시
- @ApiParam: 파라미터 정보 명시



### useDefaultResponseMessage(false)

Response를 보면 Swagger에서는 200, 401, 403, 404 같은 기본상태코드에 대한 디폴트메세지를 제공해준다.
이 기본상태코드들을 제거하려면 Swagger JAVA 설정코드쪽에
.securitySchemes(Collections.singletonList(apiKey())) 이부분을
.securitySchemes(Collections.singletonList(apiKey())).useDefaultResponseMessages(false) 이렇게 고쳐주자.

https://kim-jong-hyun.tistory.com/49

