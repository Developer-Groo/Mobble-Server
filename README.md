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
|**우현**|**태준**|**호진**|
|-------|-------|-------|
|<img width="110" height="110" src="https://github.com/user-attachments/assets/c1c5bccc-9245-403c-b422-e4c1b0ecff92"/>|--|<img width="110" height="110" src="https://github.com/user-attachments/assets/ac3a7f06-8c81-4f58-a1b1-aee9d5014d0a" />|
|[GitHub](https://github.com/Developer-Groo)|[GitHub](https://github.com/taejunUM)|[GitHub](https://github.com/Hojin02)|


## 📑 Wiki

### 👉 [Code Convention](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%93%91-Code-Convention)
### 👉 [Commit Convention](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%93%91-Commit-Convention)
### 👉 [Git Workflow Guid](https://github.com/Developer-Groo/Mobble-Server/wiki/%F0%9F%9A%80-Git-Workflow-Guide)
### 🚨 [Trouble Shooting](https://github.com/Developer-Groo/Mobble-Server/wiki)

## 🛠 Technology


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

## 🧬 Service Architecture


## 🚨 Trouble Shooting


## 🍰 Performance Comparison

