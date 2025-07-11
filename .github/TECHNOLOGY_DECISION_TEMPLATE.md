<!--기술적 의사결정의 핵심 주제를 간결하게 작성-->
작성일: YYYY-MM-DD     
"로컬 개발 환경에서의 DB 선택: SQLite vs MySQL"

<br>

## 🧩 배경
<!--이 의사결정을 하게 된 배경 또는 문제 상황을 설명-->
- 어떤 문제가 발생했는가? or 왜 지금 이 결정을 내려야 하는가? or 어떤 맥락에서 이 결정이 필요한가?

<br>

## 🎯 목표
<!--이 의사결정을 통해 이루고자 하는 목표 또는 해결하고자 하는 문제-->
- 성능 개선
- 개발 편의성 향상
- 비용 절감 등

<br>

## ⚖️ 고려한 대안
<!--검토했던 기술적 대안들을 정리-->
각 항목에는 간단한 설명과 장단점을 포함

### 1. 대안 A (예: SQLite)
- ✅ 장점:
  - 경량, 설정이 쉬움
  - 빠른 시작 가능
- ❌ 단점:
  - 동시성 문제
  - 실제 운영 환경과 괴리

### 2. 대안 B (예: MySQL)
- ✅ 장점:
  - 운영 환경과 동일
  - 성능, 확장성 우수
- ❌ 단점:
  - 설정 및 실행 환경 구성 복잡

<br>

## 🧠 최종 결정
<!--선택한 대안과 이유를 명확히 기재-->

**선택된 대안:** `MySQL`

**선정 이유:**  
- 운영 환경과 일치시켜 테스트의 신뢰성 확보  
- 장기적으로 테스트와 운영의 불일치로 인한 문제를 예방

<br>

## 🧪 참고자료 / 실험 결과 (Optional)
<!--실험 결과, 벤치마크, 문서 링크 등 참고한 근거가 있다면 작성-->
- [링크1](https://example.com)
- 내부 테스트 결과 요약

**Optional 섹션은 불필요하다면 작성하지 않아도 됩니다.**
