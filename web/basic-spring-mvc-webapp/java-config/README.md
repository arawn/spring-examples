Basic Spring @MVC Webapp with JavaConfig
========================================

JavaConfig와 Spring @MVC를 사용해 기본적인 웹 애플리케이션을 작성합니다.

예제 환경정보
-----------

* MAVEN : 3.0.3
* JDK : 1.7.0_21
* Servlet : 3.0.x
* JSP : 2.2.x
* Spring Framework 3.2.x

예제 실행방법
-----------

    > mvn jetty:run

브라우저를 열고 [http://localhost:8080/spring-examples](http://localhost:8080/spring-examples)에 접속하세요.

예제 개발정보
-----------

### Spring @MVC 설정

#### 웹 애플리케이션 환경변수 구성

* src/resources/META-INF/config.xml
* EnvironmentInitializer

Spring 3.1부터 지원하는 Environment Abstraction를 사용해서 환경변수를 구성한다.

#### JavaConfig로 Spring @MVC 구성

* WebConfig

#### 웹 애플리케이션 구성

* DispatcherServletInitializer
* web.xml

Servlet 3.0부터 지원하는 동적 서블릿 환경구성 기능을 사용해서 스프링의 DispatcherServlet을 등록하고,
동적처리가 되지 않는 오류 페이지 지정은 web.xml을 통해 설정한다.