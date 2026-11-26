# ☕ Grids & Circles - Backend

로컬 카페 **Grids & Circles**의 온라인 커피 원두 주문 및 관리 서비스입니다.

## 📁 프로젝트 구조

```
.
├── backend/    # Spring Boot API 서버
└── frontend/   # 프런트엔드 (예정)
```

## 🛠 기술 스택 (Backend)

| 구분 | 기술 |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.2.0 |
| ORM | Spring Data JPA |
| Database | H2 |
| Build | Gradle (Groovy DSL) |
| IDE | IntelliJ IDEA |
| Convention | [Naver Java Coding Convention](https://naver.github.io/hackday-conventions-java/) |

## 🚀 실행 방법

`backend` 폴더에서 실행합니다.

```bash
./gradlew bootRun
```

- API 서버: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:file:./data/gridsandcircles`
  - User: `sa` / Password: (비워둠)

## 📦 백엔드 패키지 구조

도메인별로 패키지를 나눕니다.

```
com.gridsandcircles
├── global/                  # 공통 모듈
│   ├── config/              # 설정 (JPA Auditing 등)
│   ├── entity/              # BaseTimeEntity
│   ├── exception/           # ErrorCode, BusinessException, GlobalExceptionHandler
│   └── response/            # ApiResponse
└── {domain}/                # 예: product, order
    ├── controller/
    ├── service/
    ├── repository/
    ├── entity/
    └── dto/
```

## 📏 코드 컨벤션

[네이버 Java 코딩 컨벤션](https://naver.github.io/hackday-conventions-java/)을 따릅니다.

- 들여쓰기는 **탭**, 한 줄은 최대 **120자**
- `./gradlew build` 실행 시 Checkstyle이 `src/main/java`를 함께 검사합니다 (`./gradlew checkstyleMain`으로 따로 실행 가능)
- 규칙 파일은 `rule-config/`에 있습니다 ([naver/hackday-conventions-java](https://github.com/naver/hackday-conventions-java))

### IntelliJ 설정

1. **포매터**: `Settings > Editor > Code Style > Java` → ⚙️ → `Import Scheme > IntelliJ IDEA code style XML` → `rule-config/naver-intellij-formatter.xml`
2. **EditorConfig**: `Settings > Editor > Code Style` → `Enable EditorConfig support` 체크
3. **Checkstyle (선택)**: `CheckStyle-IDEA` 플러그인 설치 → `rule-config/naver-checkstyle-rules.xml` 등록
   - `suppressionFile` 속성 값: `rule-config/naver-checkstyle-suppressions.xml`
4. **Lombok**: `Settings > Build > Compiler > Annotation Processors` → `Enable annotation processing` 체크

## 🌿 브랜치 전략: GitHub Flow

| 브랜치 | 용도 |
| --- | --- |
| `main` | 배포 브랜치 (직접 push 금지) |
| `feat/{파트}/{도메인}/{기능}` | 기능 개발. 예: `feat/fe/product/list` |
| `fix/{파트}/{도메인}/{내용}` | 버그 수정. 예: `fix/be/order/total-price` |

- 파트: `be`(백엔드), `fe`(프런트엔드)
- 작업 브랜치는 `main`에서 만들고, PR로 리뷰를 받은 뒤 **squash merge**로 `main`에 병합합니다.

## 📝 커밋 메시지 (Conventional Commits)

```
type: 제목 (50자 이내)

본문 (무엇을, 왜)

Refs: HUB-TASK-12
```

| type | 의미 |
| --- | --- |
| `feat` | 새 기능 |
| `fix` | 버그 수정 |
| `refactor` | 리팩토링 |
| `style` | 포맷, 세미콜론 등 |
| `docs` | 문서 |
| `test` | 테스트 |
| `chore` | 자잘한 변경 |
| `build` | 빌드 / 외부 종속성 수정 |

## 🔀 Pull Request

- 제목 형식: `[FEATURE] 로그인 기능 구현`
- 템플릿(`.github/PULL_REQUEST_TEMPLATE.md`)에 맞춰 작성하고, 작업한 내용에 맞는 라벨을 붙입니다.
- **squash merge**로 병합합니다.
