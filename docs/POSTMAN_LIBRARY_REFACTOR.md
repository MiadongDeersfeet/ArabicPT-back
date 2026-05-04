# 문장 세트 중심 API — Postman 예시

스키마는 저장소에 포함하지 않으며, 사용 중인 Oracle DDL(폴더·세트·문장·오디오, UK 제약 등)과 일치합니다.

Base URL: `http://localhost:8080`  
공통 헤더: `Content-Type: application/json`

## 1. 폴더 없이 문장 세트 생성

`POST /api/sets`

```json
{
  "setName": "요한복음 3장",
  "description": "요한복음 3장 암송 문장"
}
```

## 2. 전체 문장 세트 조회

`GET /api/sets`

## 3. 폴더 생성

`POST /api/folders`

```json
{
  "folderName": "성경 문장",
  "description": "성경 암송용 문장 모음"
}
```

## 4. 특정 세트를 폴더로 이동

`PATCH /api/sets/{setId}`

```json
{
  "folderId": 1
}
```

## 5. 특정 폴더의 세트만 조회

`GET /api/sets?folderId=1`

## 6. 세트에 문장 생성

`POST /api/sets/{setId}/sentences`

```json
{
  "frontLang": "ko",
  "frontText": "하나님이 세상을 이처럼 사랑하사",
  "backLang": "ar",
  "backText": "لأَنَّهُ هَكَذَا أَحَبَّ اللهُ الْعَالَمَ",
  "memo": "لأنّه: 왜냐하면 / هكذا: 이처럼"
}
```

## 7. 세트의 문장 목록

`GET /api/sets/{setId}/sentences`

## 8. 문장 상세

`GET /api/sentences/{sentenceId}`

## 9. 문장 수정

`PATCH /api/sentences/{sentenceId}`

```json
{
  "memo": "수정된 메모"
}
```

## 10. 문장 삭제

`DELETE /api/sentences/{sentenceId}`

## 세트를 폴더에서 제거

`PATCH /api/sets/{setId}`

```json
{
  "clearFolder": true
}
```

## 오디오 (구조만)

- `POST /api/sentences/{sentenceId}/audio`
- `GET /api/sentences/{sentenceId}/audio`

`SPEAKING_RATE`는 DB상 `VARCHAR2`(예: `NORMAL`). 생략 시 서버가 DB 기본값과 동일하게 채웁니다. `AUDIO_URL`은 컬럼이 NOT NULL이라, 요청에 없으면 임시값 `pending`이 들어갑니다(TTS 연동 후 실제 URL로 교체).
