### Spring Boot Security Jwt

  - Jwt 인증기반 REST API 서버를 Spring boot, security를 사용해 구현한다.
  - oauth2 인증 체계는 사용하지 않으므로, spring-security-oauth2는 사용하지 않는다.
  - 커스텀 [Jwt 인증 필터 (JwtAuthenticationTokenFilter)](src/main/java/com/github/iyboklee/security/JwtAuthenticationTokenFilter.java)와 [커스텀 인증 Provider (ApiUserAuthenticationProvider)](/src/main/java/com/github/iyboklee/security/ApiUserAuthenticationProvider.java)를 사용한다.
  - 인증 경로는 /api/auth 이며, 인증 확인 경로는 /api/user/me 이다.
  - 인증 경로를 제외한 모든 API 경로는 Request Header에 반드시 아래와 같은 정보를 포함해야 한다.
    - Header name: Authorization
    - Heaver value: Bearer {JWT token}