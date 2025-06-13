> **이 문서는 일관된 코드 스타일을 유지하기 위해 작성되었습니다. 모든 팀원은 아래 규칙을 따라 개발해주세요.**

## ✅ 생성자

### `@RequiredArgsConstructor` 사용
- `final` 필드나 `@NonNull` 필드만을 포함하는 생성자는 Lombok의 `@RequiredArgsConstructor`로 자동 생성합니다.

~~~java
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

}
~~~

<br>

### Entity 기본 생성자
- JPA Entity는 **기본 생성자**가 필수입니다.
- 외부 생성을 막기 위해 `@NoArgsConstructor(access = AccessLevel.PROTECTED)` 사용.

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
```

<br>

## ✅ 정적 팩토리 메서드 사용

### Custom 생성자는 `toEntity()` 네이밍 사용
- `private` 생성자 + `toEntity()` 정적 메서드로 객체 생성
- **생성자 직접 호출 금지**

```java
@Entity
public class Post {

    private final String title;
    private final String content;

    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post toEntity(String title, String content) {
        return new Post(title, content);
    }
}
```

<br>

## ✅ DTO 설계

### DTO는 `record` 사용
- DTO는 `record`를 기본 구조로 사용하며, 생성은 `toDto()`로 정의

```java
public record PostDto(String title, String content) {
    public static PostDto toDto(Post post) {
        return new PostDto(post.getTitle(), post.getContent());
    }
}
```

<br>

## ✅ 컨트롤러 응답 방식

### `ResponseEntity.status()` 사용
- `ResponseEntity.ok()` 대신 **명시적으로 status(HttpStatus.XXX)**를 사용
- `.body()`는 줄바꿈하여 가독성 확보

```java
@GetMapping("/posts")
public ResponseEntity<List<PostDto>> getPosts() {
    List<PostDto> posts = postService.findAll();

    return ResponseEntity.status(HttpStatus.OK)
            .body(posts);
}
```

<br>

## ✅ 트랜잭션 처리

### 클래스 단위 기본은 `@Transactional(readOnly = true)`
- 읽기 전용이 기본이며, 쓰기 작업이 필요한 경우 메서드 단위로 `@Transactional` 지정

```java
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void createPost(PostDto dto) {
        postRepository.save(Post.toEntity(dto.title(), dto.content()));
    }
}
```

<br>

## ✅ 파라미터 줄바꿈 스타일

### 메서드 또는 생성자 파라미터 줄바꿈 기준
- 메서드 또는 생성자 정의 시, **파라미터가 4개 이상**일 경우 줄바꿈  
- 그 외는 한 줄로 작성

```java
// 4줄 이상일 경우
public Post toEntity(
        String title,
        String content,
        String category,
        String writer
) {
    return new Post(title, content, category, writer);
}

// 3줄 이하
public PostDto toDto(String title, String content, String writer) {
    return new PostDto(title, content, writer);
}
```

<br>

## ✅ return 키워드 위치

### return 키워드는 한 줄 띄우고 작성

```java
public List<PostDto> getAllPosts() {
    List<Post> posts = postRepository.findAll();

    return posts.stream()
            .map(PostDto::toDto)
            .toList();
}
```

<br>
