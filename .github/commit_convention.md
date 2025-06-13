> **이 문서는 팀 프로젝트에서 일관된 커밋 메시지 작성을 위해 정의된 규칙입니다. 모든 팀원은 아래 규칙을 따라 커밋 메시지를 작성해주세요.**

## 📌 Prefix 목록 및 의미

| Type | Description |
|------|-------------|
| feat | 새로운 기능 추가 |
| fix | 버그 수정 |
| docs | 문서 변경 (README 등 문서 파일 수정) |
| style | 코드 스타일 변경 (세미콜론, 들여쓰기, 공백 등) |
| refactor | 코드 리팩터링 (기능 변화 없음, 변수명 정리 등) |
| test | 테스트 코드 추가 및 수정 |
| chore | 빌드 설정, 패키지 매니저 설정, 기타 중요하지 않은 작업 |

<br>

## 📌 커밋 메시지 작성 규칙

- 커밋 메시지는 영문 소문자로 시작
- 형식은 `type: message` 사용
- 하나의 커밋에는 하나의 목적만 작성
- 메시지는 명확하게 무엇을 했는지 설명

<br>

## 📌 Optional
- 하위 커밋 설명이 필요한 경우, 본문(body)을 줄바꿈하여 추가 설명 작성
~~~ text
feat: support social login

Added Kakao and Google login support.
Refactored auth controller to handle provider-based login.
~~~

<br>
