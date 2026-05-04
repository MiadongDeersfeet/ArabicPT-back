# ArabicPT Backend

아라빅 학습 서비스 **ArabicPT**의 REST API 서버입니다.  
폴더·문장 세트·문장·오디오 등 학습 콘텐츠를 다루며, 프론트엔드(`front`)와 동일 도메인 정책으로 통신합니다.

---

## 기술 스택

| 구분 | 내용 |
|------|------|
| Runtime | Java **21** |
| Framework | Spring Boot **3.3** |
| Persistence | **MyBatis 3** · Oracle JDBC |
| Build | **Gradle** (Kotlin DSL 아님, Groovy `build.gradle`) |
| Validation | `spring-boot-starter-validation` |

---

## 사전 요구 사항

- **JDK 21** (Gradle toolchain이 21을 기대합니다.)
- **Oracle Database** 접속 정보 (로컬 또는 원격)
- (선택) Postman 등으로 API를 호출할 클라이언트

---

## 빠른 시작

### 1. 저장소 클론 후 백엔드 디렉터리로 이동

```bash
cd back
```

### 2. 환경 변수 설정

`application.yml`은 아래 값을 **환경 변수**에서 읽습니다. 로컬에서는 OS 환경 변수 또는 IDE Run Configuration에 설정하면 됩니다.

| 변수 | 설명 |
|------|------|
| `DB_URL` | JDBC URL (Oracle) |
| `DB_USERNAME` | DB 사용자 |
| `DB_PASSWORD` | DB 비밀번호 |
| `PORT` | 서버 포트 (생략 시 **8080**) |

민감 정보가 들어간 **`application-private.yml`** 을 `src/main/resources/` 에 두는 방식도 지원합니다  
(`spring.config.import: optional:classpath:application-private.yml`).  
해당 파일은 **`.gitignore`에 포함되어 커밋되지 않습니다.**

### 3. 실행

Windows:

```bat
gradlew.bat bootRun
```

macOS / Linux:

```bash
./gradlew bootRun
```

기동 후 기본 주소: **`http://localhost:8080`** (또는 설정한 `PORT`).

### 4. 빌드만 할 때

```bash
./gradlew clean build
```

> 이 프로젝트는 OneDrive 등에서 `build` 폴더 잠금 이슈를 피하려고 출력 디렉터리를 **`.gradle-build`** 로 두는 설정이 있습니다. clone 직후라도 동일하게 동작합니다.

---

## API 개요

REST 규칙에 맞춘 JSON API이며, 성공 시 공통 래퍼 `ApiResponseDTO` 형태로 내려갑니다.

대표 엔드포인트 예:

- `GET/POST/PATCH/DELETE` **`/api/folders`** — 폴더
- `GET/POST/PATCH/DELETE` **`/api/sets`** — 문장 세트
- **`/api/sets/{setId}/sentences`** — 세트 내 문장
- **`/api/sentences/{sentenceId}`** — 문장 단건

상세 요청/응답 예시는 저장소 내 문서를 참고하세요.

- [`docs/POSTMAN_LIBRARY_REFACTOR.md`](docs/POSTMAN_LIBRARY_REFACTOR.md) — 문장 세트·폴더 중심 호출 예시

---

## 패키지 구조 (요약)

```
src/main/java/com/arabicpt/
├── MainApplication.java
├── common/          # 공통 응답·예외
├── folder/          # 폴더 도메인
├── sentenceset/     # 문장 세트
├── sentence/        # 문장
├── audio/           # 오디오(구조)
├── member/          # 회원
├── auth/            # 인증
└── ...
src/main/resources/mapper/   # MyBatis XML 매퍼
```

---

## 보안·운영 참고

- DB 비밀번호, OAuth 클라이언트 시크릿 등은 **환경 변수 또는 `application-private.yml`** 으로만 관리하고, 저장소에는 올리지 마세요.
- `.gitignore`에 `.env`, `application-*.yml`(로컬/비공개 패턴), 키 스토어 등이 포함되어 있습니다.

---

## 라이선스 · 문의

프로젝트 정책에 맞게 상단에 LICENSE 또는 팀 내 규칙을 두면 됩니다.

문제가 있으면 이슈로 남겨 주세요. (그리고 커피 한 잔 값은 감사합니다 ☕)
# ArabicPT-back
