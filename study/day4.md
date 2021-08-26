### maria db란

오픈소스 RDBMS 소프트웨어 ( 상용으로 사용 가능한 mysql )

mysql 를 fork 한 RDBMS이다. 추가로 perconaServer 엔진을 함께 사용

리눅스는 mysql대신에 mariaDB를 표준으로 채택하고있다.



### Service와 ServiceImpl 분리

역할인 Service와 실제 구현체인 Impl을 분리하여서 나중에 구현하는 것이 바뀌더라도 Impl만 바꾸면 되도록 만든 것이다.

1. 객체지향의 다형성과 연관이 있다. 기존 구현 객체와 비즈니스 로직이 다른 기능을 추가해야할 경우 다른 구현 객체를 만들어 사용하면 되어 유지보수 측면에서 좋다.
2. 스프링에서 AOP를 구현할 때 JDK의 기본 프록시를 사용하는데, 이 프록시가 인터페이스 기반으로 동작하기 때문에 Service를 인터페이스로 만들어 사용한다.

> 스프링에서 taransactional 같은 AOP는 dynamic proxy를 이용하여 구현되는데요
>
> dynamic proxy를 생성하는 방법으로는 JDK lib 하고 CGlib 두개를 스프링이 사용하는데 디폴트로
>
> JDK를 쓰는걸로 알고있습니다. JDK Dynamic Proxy는 interface가 있어야만 생성할 수 있기때문에
>
> interface를 만들어야하는거고요 proxy-class 옵션을주면 CGlib 를 쓸겁니다. CGLIB Proxy는 인터페이스
>
> 생성없이도 proxy를 생성할 수 있습니다. 클래스에 트랜잭션을 선언할때는 제한사항이 있는데 final 클래스같은상속이 안되는 클래스에는 적용이 안된다는 단점이 있습니다.
>
> 서비스에 인터페이스를 만드는것에 대해서는 앞으로 변경사항이 있다거나 추가가 되는 로직이 발생될 만한비즈니스 로직일 경우에만 쓰는게 맞다고 생각들고 단순 DAO 호출같은 1:1 맵핑이면 필요없다고 생각합니다. 개인적인 생각입니다.
>
> https://okky.kr/article/682158

> 출처
>
> https://velog.io/@aquarius1997/Service%EC%99%80-ServiceImpl



하지만 이런 관습적인 추상화로 1:1관계의 service, impl은 오히려 구조를 복잡하게 만들 수 있다. 당장 강의를 따라 치고 사용하는 것이 아니라,  왜 사용하는지, Impl이라는 네이밍이 사용에 좋을지, 지금 당장 1:1 관계더라도 앞으로 1:N으로 서비스의 구현체가 늘어날 수 있을지 모두 고려한 뒤 사용하도록 하자.



### spring-data-jpa vs spring-boot-starter-data-jpa

starter가 의존성과 설정을 자동화해준다. 