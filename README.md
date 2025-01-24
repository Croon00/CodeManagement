# CodeManagement

## 코드 관리 시스템

### 백엔드

- **Spring Boot**: 애플리케이션 개발 프레임워크
- **Spring Security**: 인증 및 권한 관리
  - SecurityConfig를 통한 각 API 엔드포인트에 대한 Filter 구현
  - JwtTokenAuthenticationFilter를 통한 각 요청에 대한 인가 구현
  - CustomerAuthenticationFilter를 통한 로그인에 대한 인증 구현
  - AccessToken은 로컬스토리지에 저장 한 후 각 요청 헤더에 담아 인가, RefreshToken은 쿠키에 담게 설정
- **Spring Data JPA**: 데이터베이스 ORM
  - Entity끼리의 단방향 매핑 및 양방향 매핑 구현
  - JPQL을 이용한 DB 접근 (추후 QueryDsl도 사용 및 적용 가능)
  - FetchType.LAZY에서 Code에서 ParentCode 조회하는 경우 FETCH JOIN을 통해서 성능 향상 및 N + 1 방지
- **JWT (JSON Web Token)**: 인증 토큰 관리
- **MySQL**: 관계형 데이터베이스
- **SpringDoc OpenAPI**: API 문서 자동 생성

### 프론트엔드

- **React**: 사용자 인터페이스 개발 라이브러리
- **MUI (Material-UI)**: React UI 컴포넌트 라이브러리
- **Axios**: HTTP 요청 처리
- **Recoil**: 상태 관리 라이브러리

### ERD 설계

![Image](https://github.com/user-attachments/assets/d191ff0e-c9ab-4afd-aee5-3db68c6e7241)

### 디렉토리 구조

![Image](https://github.com/user-attachments/assets/2d9ec789-7218-4d66-aef5-ba006553d00a)

- MVC 패턴으로 구현
- Controller에서 받는 파라미터 값과 반환 파라미터를 위해 각각 RequestDto와 ResponseDto 작성 (순환참조 방지)
- Service단에서 Transactional을 각 메서드에 이용하여 트랜잭션 제어

![Image](https://github.com/user-attachments/assets/1f05ea65-ca16-4e60-a4be-689fe3151745)

- User 컴포넌트 및 Code 컴포넌트 작성
- api 보내는 과정은 따로 패키지 작성 후 각 컴포넌트에서 import 해서 사용
- state 패키지에 각 기본적인 상태 구현 및 컴포넌트에서 가져다 사용

### API 구조

![Image](https://github.com/user-attachments/assets/23f1ae9a-d219-47fe-8ae8-8d7b6aedde13)
