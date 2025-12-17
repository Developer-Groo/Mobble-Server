# Mobble

**모블(mobble)은 누구나 쉽게 모임을 개설하고 참여하여, 관심사를 나누고 관계를 확장할 수 있는 소모임 플랫폼입니다.**

## ☑️ Index
- [🏁 Team](#-Team)
- [📑 Wiki](#-Wiki)
- [🛠 Technology](#-Technology)
- [🎯 Features](#-Features)
- [🔗 ERD](#-ERD)
- [🧬 Service Architecture](#-Service-Architecture)
- [🚨 Trouble Shooting](#-Trouble-Shooting)
- [🍰 Performance Comparison](#-Performance-Comparison)

## 🏁 Team
|**우현**|**태준**|
|-------|-------|
|<img width="110" height="110" src="https://github.com/user-attachments/assets/c1c5bccc-9245-403c-b422-e4c1b0ecff92"/>|<img width="110" height="110" src="https://github.com/user-attachments/assets/c10e91bf-cccd-4fd3-a8c3-9e2985cd63bc"/>|
|[GitHub](https://github.com/Developer-Groo)|[GitHub](https://github.com/taejunUM)|

## 📑 Wiki

### 👉 [Code Convention](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%93%91-Code-Convention)
### 👉 [Commit Convention](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%93%91-Commit-Convention)
### 👉 [Git Workflow Guid](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%9A%80-Git-Workflow-Guide)
### 🚨 [Trouble Shooting](https://github.com/Developer-Groo/Mobble-Server/wiki)

## 🔗 ERD

### **‼️ Liam ERD 로 변경 예정**
~~~ mermaid
erDiagram

  MEMBER {
    bigint member_id PK
    varchar name
    int age
    tinyint gender
    varchar email
    varchar phone
    varchar ground
    tinyint terms_agreed
    tinyint privacy_agreed
    varchar profile_image
    varchar social_provider
    varchar social_id
    tinyint is_deleted
    timestamp updated_at
    timestamp created_at
  }

  CLUB_MEMBER {
    bigint club_member_id PK
    bigint member_id FK
    bigint club_id FK
    enum role
    tinyint join_status
    timestamp created_at
  }

  CLUB {
    bigint club_id PK
    bigint club_category_id FK
    varchar name
    varchar ground
    int headcount
    tinyint join_type
    timestamp updated_at
    timestamp created_at
  }

  CLUB_IMAGE {
    bigint club_image_id PK
    bigint club_id FK
    varchar url
    int sort_order
    timestamp created_at
  }

  CLUB_AGE_GROUP {
    bigint club_age_group_id PK
    bigint club_id FK
    enum age_group_type
  }

  CLUB_CATEGORY {
    bigint club_category_id PK
    varchar name
  }

  ARTICLE {
    bigint article_id PK
    bigint club_id FK
    bigint member_id FK
    enum article_type
    varchar title
    text content
    timestamp updated_at
    timestamp created_at
  }

  ARTICLE_IMAGE {
    bigint article_image_id PK
    bigint article_id FK
    varchar url
    int sort_order
    timestamp created_at
  }

  COMMENT {
    bigint comment_id PK
    bigint article_id FK
    bigint member_id FK
    bigint parent_id
    varchar content
    timestamp created_at
    timestamp updated_at
  }

  ARTICLE_LIKE {
    bigint article_like_id PK
    bigint article_id FK
    bigint member_id FK
  }

  COMMENT_LIKE {
    bigint comment_like_id PK
    bigint comment_id FK
    bigint member_id FK
  }

  CLUB_LIKE {
    bigint club_like_id PK
    bigint club_id FK
    bigint member_id FK
  }

  MEETING {
    bigint meeting_id PK
    bigint club_member_id FK
    varchar title
    timestamp datetime
    varchar location
    varchar cost
    int member_limit
    enum type
    timestamp updated_at
    timestamp created_at
  }

  MEETING_MEMBER {
    bigint meeting_member_id PK
    bigint meeting_id FK
    bigint member_id FK
    timestamp created_at
  }

  CHAT_ROOM {
    bigint chat_room_id PK
    enum type
    timestamp created_at
  }

  CLUB_CHAT_ROOM {
    bigint club_id PK
    bigint chat_room_id FK
  }

  DIRECT_CHAT_ROOM {
    bigint direct_chat_room_id PK
    bigint chat_room_id FK
    bigint member1_id FK
    bigint member2_id FK
  }

  CHAT_ROOM_PARTICIPANT {
    bigint chat_room_member_id PK
    bigint chat_room_id FK
    bigint member_id FK
    timestamp joined_at
    bigint last_read_message_id
    tinyint notified
  }

  CHAT_MESSAGE {
    bigint chat_message_id PK
    bigint chat_room_id FK
    bigint sender_id FK
    text content
    enum type
    timestamp created_at
  }

  CHAT_MESSAGE_MENTION {
    bigint chat_message_id FK
    bigint mentioned_member_id FK
  }

  NOTIFICATION {
    bigint notification_id PK
    bigint receiver_id FK
    enum type
    varchar content
    tinyint is_read
    bigint related_id
    timestamp created_at
  }

  %% -------- Relationships --------

  CLUB_MEMBER }o--|| MEMBER : member_id
  COMMENT }o--|| MEMBER : member_id
  ARTICLE_LIKE }o--|| MEMBER : member_id
  COMMENT_LIKE }o--|| MEMBER : member_id
  CLUB_LIKE }o--|| MEMBER : member_id
  MEETING_MEMBER }o--|| MEMBER : member_id
  CHAT_ROOM_PARTICIPANT }o--|| MEMBER : member_id
  CHAT_MESSAGE }o--|| MEMBER : sender_id
  CHAT_MESSAGE_MENTION }o--|| MEMBER : mentioned_member_id
  NOTIFICATION }o--|| MEMBER : receiver_id

  CLUB_MEMBER }o--|| CLUB : club_id
  CLUB_IMAGE }o--|| CLUB : club_id
  CLUB }o--|| CLUB_CATEGORY : club_category_id
  CLUB_AGE_GROUP }o--|| CLUB : club_id
  CLUB_LIKE }o--|| CLUB : club_id
  MEETING }o--|| CLUB_MEMBER : club_member_id

  ARTICLE_IMAGE }o--|| ARTICLE : article_id
  COMMENT }o--|| ARTICLE : article_id
  ARTICLE_LIKE }o--|| ARTICLE : article_id

  COMMENT }o--|| COMMENT : parent_id
  COMMENT_LIKE }o--|| COMMENT : comment_id

  MEETING_MEMBER }o--|| MEETING : meeting_id

  CHAT_ROOM_PARTICIPANT }o--|| CHAT_ROOM : chat_room_id
  CHAT_MESSAGE }o--|| CHAT_ROOM : chat_room_id
  CHAT_MESSAGE_MENTION }o--|| CHAT_MESSAGE : chat_message_id
  CHAT_ROOM_PARTICIPANT }o--|| CHAT_MESSAGE : last_read_message_id
  CLUB_CHAT_ROOM }o--|| CHAT_ROOM : chat_room_id
  CLUB_CHAT_ROOM }o--|| CLUB : club_id
  DIRECT_CHAT_ROOM }o--|| CHAT_ROOM : chat_room_id
  DIRECT_CHAT_ROOM }o--|| MEMBER : member1_id
  DIRECT_CHAT_ROOM }o--|| MEMBER : member2_id
~~~

## ✅ 작업 목록
## 😃 우현
    ✅ 댓글 도메인 기능 개발
    ✅ 클럽 도메인 기능 개발
    ✅ 이미지 도메인 기능 개발
    ✅ 채팅 도메인 기능 개발 
    ✅ 알림 도메인 기능 개발 
    ☑️ 테스트 코드 작성
    ☑️ 채팅 도메인 멘션 기능 개발 및 테스트 코드 작성
    ☑️ 알림 도메인 아키텍처 적용 및 테스트 코드 작성
    ☑️ API 문서화 디테일 작업
    ☑️ Liam ERD 로 시각화 변경 작업
    ☑️ 각 도메인 성능, 부하 테스트 및 최적화
    ☑️ CI/CD 파이프 라인 구축 및 인프라 설계, 배포
    ☑️ DDL 제약조건 명명 규칙 정리
    
#### 📌 제약조건 명명 규칙 정의
    • MySQL 기본 제약조건 이름이 난해하게 생성되는 문제를 개선하여 식별 가능한 규칙 기반 네이밍으로 변경
    
#### ☑️ 채팅 도메인 부하 테스트 및 최적화: 채팅의 경우 대량 트래픽 발생이 예상 됨, Kafka 적용 예정/MySQL -> MongoDB 변경 구상

#### 📌 채팅 도메인 부하 테스트 및 최적화
    •	채팅 서비스는 실시간성이 핵심, 동시 접속자 수 증가에 따라 초당 수천~수만 건의 메시지 발생 예상
    •	단일 DB 기반 처리 시 쓰기 작업에 트래픽이 몰려 병목 현상 발생 가능성 높음
    •	따라서 부하 테스트를 통해 검증할 예정
    •	TPS: 초당 메시지 전송/수신 처리량
    •	Latency: 메시지 송수신 지연 시간

#### 📌 Kafka 적용 근거
    •	고가용성 & 확장성: 파티션 기반 수평 확장을 통해 초당 수십만 건 이상 메시지 처리 가능
    •	내결함성: 브로커 클러스터와 Replication으로 단일 장애점 제거
    •	비동기 처리 모델: Producer와 Consumer 간 decoupling으로 DB/애플리케이션 부하 완화
    •	내장 메시지 보존: 메시지를 일정 기간 유지하여 장애 시 재처리 및 메시지 유실 방지
    
## 😃 태준
    ✅ 좋아요 도메인 기능 개발
    ✅ 미팅 도메인 기능 개발
    ✅ 멤버 도메인 기능 개발
    ✅ JWT 기반 로그인 및 시큐리티 기능 개발
    ☑️ 테스트 코드 작성
    ☑️ 좋아요 기능 동시성 테스트 및 최적화
    ☑️ Meeting 참석 기능 동시성 테스트 및 최적화
    ☑️ 각 도메인 성능, 부하 테스트 및 최적화
    

