> **이 문서는 우리 팀이 Git 브랜치를 어떻게 사용하고 관리할 것인지 정의한 가이드입니다. 모든 팀원은 아래 규칙을 따라 브랜치를 생성하고 병합하여 안정적인 협업 환경을 유지해주세요.**

## 🔁 브랜치 구조 요약

| 브랜치명          | 용도         | 설명                                               |
| ------------- | ---------- | ------------------------------------------------ |
| `main`        | 배포 브랜치     | 항상 배포 가능한 최신 안정 버전. 코드 리뷰 및 CI 통과된 기능만 머지 가능     |
| `develop`     | 통합 테스트 브랜치 | 개발 완료된 기능을 통합하고 최종 테스트하는 브랜치                     |
| `feature/기능명` | 기능 개발 브랜치  | 새로운 기능 개발용 브랜치 (예: `feature/login-api`)              |
| `bug/버그명`     | 버그 수정 브랜치  | 발견된 오류 수정용 브랜치 (예: `bug/login-error`)            |
| `style/변경내용`  | 코드 스타일 수정  | 컨벤션 위반 또는 코드 정리 브랜치 (예: `style/indentation-fix`) |

<br>

## 📚 브랜치 사용 규칙

### ☑️ 브랜치 이름
- 브랜치 이름은 **영문 소문자와 하이픈(-)** 으로 구분하여 작성합니다.

  - ✅ 예시: `feature/login-api`, `bug/image-upload-fix`, `style/rename-method`
  - ❌ 피하기: `feature/Login`, `bugfix/login`, `my-branch`

### ☑️ 메인 브랜치 (`main`)

- 배포 가능한 **가장 안정적인 버전** 유지
- 반드시 코드 리뷰 및 테스트를 통과한 브랜치만 머지
- 배포 후 **태그(tag)** 추가 권장 (예: `v1.0.0`)

### ☑️ 개발 브랜치 (`develop`)

- 모든 기능(feature), 버그 수정(bug), 스타일 수정(style)은 **develop 브랜치에서 파생**하여 작업
- PR은 develop 브랜치 기준으로 생성
- CI 또는 자체 테스트 완료 후 `main`으로 병합

### ☑️ 기능 브랜치 (`feature/*`)

- 사용 예: `feature/signup`, `feature/comment-api`
- 새 기능은 반드시 `develop` 브랜치에서 파생
- 구현 완료 후 PR 생성 대상은 `develop` 브랜치

### ☑️ 버그 브랜치 (`bug/*`)

- 사용 예: `bug/image-upload-error`, `bug/null-pointer-profile`
- 기존 기능에서 오류가 발생한 경우 `develop`에서 브랜치 생성
- 단순한 수정이더라도 **반드시 별도 브랜치에서 수정 후 PR 요청**

### ☑️ 스타일 브랜치 (`style/*`)
- 사용 예: `style/remove-console-logs`, `style/fix-naming`
- 코드 포맷, 변수명, 들여쓰기 등 컨벤션 관련 수정 시 사용
- 기능과 무관한 코드 스타일 변경은 반드시 분리하여 PR

<br>

## 🔁 브랜치 병합(Merge) 규칙

- `feature/*`, `bug/*`, `style/*` → `develop`**으로 머지**
- `develop` → CI 또는 테스트 완료 후 → `main`**으로 머지**

모든 병합은 **PR(Pull Request)** 를 통해 진행되며, 최소 1인의 승인 후 머지합니다.

<br>

## 🧹 브랜치 삭제 규칙

- 브랜치는 `main`**브랜치에 머지된 후** 삭제합니다.
- GitHub PR 병합 시 `"Delete branch after merge"` 옵션 사용 권장
- 병합되지 않은 브랜치는 **삭제 금지**

<br>

## ✅ 추가 권장 사항

- 커밋 메시지는 [커밋 컨벤션](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%93%91-Commit-Convention)에 따라 작성해주세요.

- PR 생성 시에는 [PR 템플릿](https://github.com/Developer-Groo/Mobble-Server/blob/main/.github/pull_request_template.md)을 사용해 변경 사항을 명확하게 정리합니다.

<br>
