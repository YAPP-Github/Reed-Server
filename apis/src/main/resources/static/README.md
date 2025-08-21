# Kakao 로그인 테스트 가이드

이 문서는 Kakao 소셜 로그인 기능을 테스트하기 위한 가이드입니다.

## 파일 설명

- `kakao-login.html`: Kakao 로그인을 시작하는 메인 페이지
- `kakao-callback.html`: Kakao 인증 후 리다이렉트되는 콜백 페이지

## 설정 방법

1. [카카오 개발자 사이트](https://developers.kakao.com/)에 로그인합니다.
2. 애플리케이션을 생성하고 REST API 키와 JavaScript 키를 확인합니다.
3. 애플리케이션 설정 > 플랫폼 > Web에서 사이트 도메인을 등록합니다. (예: `http://localhost:8080`)
4. 애플리케이션 설정 > 카카오 로그인 > 활성화 설정을 ON으로 변경합니다.
5. 애플리케이션 설정 > 카카오 로그인 > Redirect URI에 `http://localhost:8080/kakao-callback.html`을 등록합니다.
6. `kakao-login.html` 파일에서 `KAKAO_JS_KEY` 값을 실제 JavaScript 키로 변경합니다.
7. `kakao-callback.html` 파일에서 `KAKAO_REST_API_KEY` 값을 실제 REST API 키로 변경합니다.

## 테스트 방법

1. 애플리케이션을 실행합니다.
2. 브라우저에서 `http://localhost:8080/kakao-login.html`에 접속합니다.
3. "카카오 계정으로 로그인" 버튼을 클릭합니다.
4. Kakao 로그인 페이지에서 로그인합니다.
5. 로그인 성공 시, 다음과 같은 정보가 표시됩니다:
   - JWT 토큰
   - 사용자 정보

## 동작 원리

1. 사용자가 "카카오 계정으로 로그인" 버튼을 클릭하면 Kakao 인증 페이지로 리다이렉트됩니다.
2. 사용자가 Kakao에 로그인하면 `kakao-callback.html`로 리다이렉트되고 인증 코드가 URL에 포함됩니다.
3. `kakao-callback.html`은 인증 코드를 Kakao 액세스 토큰으로 교환합니다.
4. Kakao 액세스 토큰을 가지고 `kakao-login.html`로 리다이렉트됩니다.
5. `kakao-login.html`은 Kakao 액세스 토큰을 백엔드 API(`/v1/auth/kakao/login`)에 전송합니다.
6. 백엔드는 Kakao 액세스 토큰을 검증하고, 사용자가 존재하면 로그인하고, 존재하지 않으면 자동으로 회원가입 후 로그인합니다.
7. 백엔드는 JWT 토큰을 반환하고, 이 토큰은 인증이 필요한 API 호출에 사용됩니다.

## 문제 해결

- **CORS 오류**: 백엔드 API에 요청할 때 CORS 오류가 발생하면, 백엔드의 CORS 설정을 확인하세요.
- **인증 코드 오류**: Redirect URI가 정확히 등록되어 있는지 확인하세요.
- **액세스 토큰 교환 실패**: REST API 키가 올바른지 확인하세요.
- **백엔드 API 오류**: 백엔드 로그를 확인하여 오류 원인을 파악하세요.