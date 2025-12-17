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

    IMAGE {
        BIGINT image_id PK
        VARCHAR url
        VARCHAR original_name
        BIGINT size
        VARCHAR content_type
        VARCHAR type
        BOOLEAN is_default
    }

    MEMBER {
        BIGINT member_id PK
        VARCHAR name
        INT age
        VARCHAR gender
        VARCHAR email
        VARCHAR phone
        VARCHAR address1
        VARCHAR address2
        VARCHAR city
        VARCHAR district
        DOUBLE latitude
        DOUBLE longitude
        BIGINT profile_image_id FK
        BOOLEAN terms_agreed
        BOOLEAN privacy_agreed
        VARCHAR social_provider
        VARCHAR social_id
        BOOLEAN is_deleted
        DATETIME deleted_at
        DATETIME created_at
        DATETIME updated_at
    }

    CATEGORY {
        BIGINT category_id PK
        VARCHAR code
        VARCHAR target_type
    }

    CLUB {
        BIGINT club_id PK
        VARCHAR name
        BIGINT leader_id FK
        BIGINT main_image_id FK
        BIGINT category_id FK
        VARCHAR age_group
        VARCHAR description
        BOOLEAN is_auto_join
        INT member_count
        VARCHAR address1
        VARCHAR address2
        VARCHAR city
        VARCHAR district
        DOUBLE latitude
        DOUBLE longitude
        DATETIME created_at
        DATETIME updated_at
    }

    CLUB_MEMBER {
        BIGINT club_member_id PK
        BIGINT member_id FK
        BIGINT club_id FK
        VARCHAR club_member_role
        VARCHAR join_status
        DATETIME status_updated_at
        DATETIME created_at
        DATETIME updated_at
    }

    ARTICLE {
        BIGINT article_id PK
        BIGINT club_id FK
        BIGINT member_id FK
        VARCHAR article_type
        BIGINT image_id FK
        VARCHAR title
        VARCHAR body
        DATETIME created_at
        DATETIME updated_at
    }

    COMMENT {
        BIGINT comment_id PK
        BIGINT member_id FK
        BIGINT article_id FK
        BIGINT parent_id FK
        VARCHAR body
        DATETIME created_at
        DATETIME updated_at
    }

    ARTICLE_LIKE {
        BIGINT id PK
        BIGINT member_id FK
        BIGINT article_id FK
    }

    CLUB_LIKE {
        BIGINT id PK
        BIGINT member_id FK
        BIGINT club_id FK
    }

    COMMENT_LIKE {
        BIGINT id PK
        BIGINT member_id FK
        BIGINT comment_id FK
    }

    LIKE_COUNTER {
        BIGINT like_counter_id PK
        VARCHAR like_type
        BIGINT target_id
        INT cnt
    }

    MEETING {
        BIGINT meeting_id PK
        BIGINT club_id FK
        BIGINT owner_id FK
        BIGINT main_image_id FK
        VARCHAR title
        DATETIME datetime
        VARCHAR location
        VARCHAR cost
        INT member_limit
        VARCHAR type
    }

    MEETING_MEMBER {
        BIGINT meeting_member_id PK
        BIGINT meeting_id FK
        BIGINT member_id FK
    }

    CHAT_ROOM {
        BIGINT chat_room_id PK
        VARCHAR type
    }

    ROOM_INFO {
        BIGINT room_info_id PK
        BIGINT chat_room_id FK
        VARCHAR room_kind
    }

    DIRECT_ROOM_INFO {
        BIGINT room_info_id PK
        BIGINT member_a_id FK
        BIGINT member_b_id FK
    }

    CHAT_ROOM_PARTICIPANT {
        BIGINT chat_room_participant_id PK
        BIGINT chat_room_id FK
        BIGINT member_id FK
        BIGINT last_read_message_id
        BOOLEAN notified
        DATETIME created_at
    }

    CHAT_MESSAGE {
        BIGINT chat_message_id PK
        BIGINT chat_room_id FK
        BIGINT sender_id FK
        VARCHAR content
        VARCHAR type
        DATETIME created_at
    }

    CHAT_MESSAGE_MENTION {
        BIGINT chat_message_mention_id PK
        BIGINT chat_message_id FK
        BIGINT mentioned_member_id FK
        DATETIME created_at
    }

    NOTIFICATION {
        BIGINT notification_id PK
        BIGINT receiver_id FK
        VARCHAR type
        VARCHAR title
        VARCHAR content
        BOOLEAN is_read
        VARCHAR target_type
        BIGINT target_id
        DATETIME created_at
    }

    DEVICE_TOKEN {
        BIGINT device_token_id PK
        BIGINT member_id FK
        VARCHAR token
        VARCHAR platform
        BOOLEAN enabled
        DATETIME created_at
        DATETIME updated_at
    }

    NOTIFICATION_SETTING {
        BIGINT notification_setting_id PK
        BIGINT member_id FK
        BOOLEAN push_global
        DATETIME created_at
        DATETIME updated_at
    }

    NOTIFICATION_SETTING_ITEM {
        BIGINT notification_setting_item_id PK
        BIGINT setting_id FK
        VARCHAR type
        BOOLEAN enabled
        DATETIME created_at
        DATETIME updated_at
    }

    PUSH_OUTBOX {
        BIGINT push_outbox_id PK
        VARCHAR token
        VARCHAR title
        VARCHAR body
        VARCHAR data_json
        VARCHAR status
        INT attempts
        DATETIME next_attempt_at
        VARCHAR last_error
        DATETIME created_at
        DATETIME updated_at
    }

    IMAGE o|--o{ MEMBER : profile_image
    IMAGE o|--o{ ARTICLE : used_by
    IMAGE o|--o{ MEETING : main_image
    IMAGE o|--o| CLUB : main_image

    CATEGORY ||--o{ CLUB : categorizes
    MEMBER   ||--o{ CLUB : leads
    CLUB     ||--o{ CLUB_MEMBER : has_members
    MEMBER   ||--o{ CLUB_MEMBER : joins

    CLUB    ||--o{ ARTICLE : has
    MEMBER  ||--o{ ARTICLE : writes
    ARTICLE ||--o{ COMMENT : has
    MEMBER  ||--o{ COMMENT : writes
    COMMENT o|--o{ COMMENT : replies_to

    MEMBER  ||--o{ ARTICLE_LIKE : likes
    ARTICLE ||--o{ ARTICLE_LIKE : liked_by
    MEMBER  ||--o{ CLUB_LIKE : likes
    CLUB    ||--o{ CLUB_LIKE : liked_by
    MEMBER  ||--o{ COMMENT_LIKE : likes
    COMMENT ||--o{ COMMENT_LIKE : liked_by

    CLUB    ||--o{ MEETING : hosts
    MEMBER  ||--o{ MEETING : owns
    MEETING ||--o{ MEETING_MEMBER : attendees
    MEMBER  ||--o{ MEETING_MEMBER : attends

    CHAT_ROOM ||--|| ROOM_INFO : has
    ROOM_INFO ||--|| DIRECT_ROOM_INFO : subtype
    CHAT_ROOM ||--o{ CHAT_ROOM_PARTICIPANT : participants
    MEMBER   ||--o{ CHAT_ROOM_PARTICIPANT : joins

    CHAT_ROOM ||--o{ CHAT_MESSAGE : messages
    MEMBER   ||--o{ CHAT_MESSAGE : sends
    CHAT_MESSAGE ||--o{ CHAT_MESSAGE_MENTION : mentions
    MEMBER ||--o{ CHAT_MESSAGE_MENTION : mentioned

    MEMBER ||--o{ DIRECT_ROOM_INFO : memberA
    MEMBER ||--o{ DIRECT_ROOM_INFO : memberB

    MEMBER ||--o{ NOTIFICATION : receives
    MEMBER ||--o{ DEVICE_TOKEN : owns

    MEMBER ||--|| NOTIFICATION_SETTING : has
    NOTIFICATION_SETTING ||--o{ NOTIFICATION_SETTING_ITEM : items
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
    

