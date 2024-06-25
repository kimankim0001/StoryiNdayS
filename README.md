# 📰프로젝트 : Story iN dayS
<p align="center"><img src ="https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fb5d1d565-c92e-4130-bf54-ede45aafddc6%2FUntitled.png?table=block&id=a0ca1a5b-893f-4e96-93db-7106c1d4c738&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=f5443b43-0da2-49a8-8654-631e651aad1c&cache=v2" width=400></p>

이 프로젝트는 Spring Framework 기반의 프로젝트로 회원가입, 로그인, 게시물, 댓글, 팔로우, 좋아요 등의 SNS 기능을 구현한 프로젝트입니다.

### 프로젝트 상세설명
Story iN dayS 는 유저들이 자신의 하루에 대한 게시글을 올려서 서로 공유하고 댓글을 달면서 소통할 수 있는 SNS 사이트입니다. <br/>
로그인 기능이 있어 유저별로 개인 식별이 가능하고 각자의 프로필을 설정할 수 있습니다.  <br/>
유저마다 자신의 고유한 페이지가 있으며 각 유저의 페이지에서 특정 유저의 게시글 조회가 가능하며 전체 페이지에서 모든 게시글을 확인하는 것도 가능합니다. <br/>
로그인을 한 유저라면 댓글을 달아 서로 소통할 수 있으며 자신의 댓글을 삭제하거나 수정할 수 있습니다. <br/>
로그인을 하지 않아도 사이트 이용이 가능하지만, 사이트에 있는 내용만 조회만 할 수 있습니다. <br/>
카카오 로그인을 지원합니다.

### 프로젝트 팀원
<p><img src = "https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F9f81f4e9-2343-4ae1-b662-47f367c70a0e%2FUntitled.png?table=block&id=723c7ba1-b9b3-45da-9d98-308a9e3b7b4b&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1060&userId=f5443b43-0da2-49a8-8654-631e651aad1c&cache=v2"></p>

<details>
<summary><big>권수연</big></summary>
<div markdown="1">

- Security Filter
    - 인증/인가
- User
    - 회원가입
    - 로그인
    - 액세스 토큰 재발급
    - 로그아웃
    - 소셜 로그인 API
- 백오피스 (User)
    - 관리자 회원 전체조회
</div>
</details>

<details>
<summary><big>김기남</big></summary>
<div markdown="1">

- PostLike
    - 게시글 좋아요 / 게시글 좋아요 취소
- CommentLike
    - 댓글 좋아요 등록 / 댓글 좋아요 취소
- Comment
    - 댓글 작성
    - 댓글 조회
    - 댓글 수정
    - 댓글 삭제
 - 백오피스 Comment
     - 특정 댓글 수정 / 특정 댓글 삭제
</div>
</details>

<details>
<summary><big>이인호</big></summary>
<div markdown="1">

- Post
    - 게시글 작성
    - 게시글 조회
    - 게시글 수정
    - 게시글 삭제
- 백오피스 Post
    - 공지 등록
    - 관리자 게시글 수정
    - 관리자 게시글 삭제
    - 게시글 상단 고정
- 팔로우 Post
    - 팔로우 추가
    - 팔로우 취소
    - 팔로우 게시글 조회
</div>
</details>

<details>
<summary><big>한승훈</big></summary>
<div markdown="1">

- 프로필
    - 프로필 조회 / 프로필 수정
- 백오피스 User
    - 특정 회원 권한 변경
    - 특정 회원 상태 변경
    - 특정 회원 정보 수정
</div>
</details>

### 개발환경
Language - Java 17

Spring Boot - 3.3.0 version

IDE - IntelliJ

DataBase - MySQL

협업툴 - Github, Notion, Slack, draw.io

### 기능구현사항
- 필수 구현 기능
  - 사용자 인증 기능 : 회원가입 기능 / 로그인 기능 / 로그아웃 기능
  - 프로필 관리 : 프로필 수정 기능
  - 댓글 CRUD 기능
  - 게시글 CRUD 기능
- 추가 구현 기능
  - 백오피스 : 회원 관리 / 게시글 관리 / 댓글 관리
  - 팔로우 기능
  - 좋아요 기능
  - 소셜 로그인 기능

### 프로젝트 진행 일정
Day0 06.18.화요일	코드컨벤션 / 깃헙룰 <br/>
Day1 06.19.수요일	프로젝트명 / 프로젝트내용결정 / API명세서 작성 / ERD 설계 / 클래스 구조 설계 / 역할분배 / 깃 테스트 <br/>
Day2 06.20.목요일	필수구현 코딩 / application.yml 파일 사용 / 포스트맨 팀 워크스페이스 사용 / 튜터님 중간피드백 / 피드백 반영 리팩토링 <br/>
Day3 06.21.금요일	필수구현 완료 / 추가구현 코딩 / 매니저님 중간점검 / 튜터님 중간피드백 / 피드백 반영 리팩토링 <br/>
Day4 06.22.토요일	추가구현 코딩 <br/>
Day5 06.23.일요일	추가구현 코딩 <br/>
Day6 06.24.월요일	추가구현 완료 / 발표 및 시연영상, Read Me 작성 역할분배 및 준비/ 최종 테스트 / 코드 리뷰 <br/>
Day7 06.25.화요일	프로젝트 제출 / 발표 / KPT 회고 <br/>

### API 명세서
https://www.notion.so/teamsparta/41a9055499354e6b8d5befbc6e7a809a?v=a9228db1ac62435aaf46ed00b5cf4a48&pvs=4

### ERD
<p><img src = "https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F113f4ada-4f7b-4a3c-a0b5-1747414e3752%2FUntitled.png?table=block&id=3345930c-0b72-4c59-989b-bce2467be62d&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=f5443b43-0da2-49a8-8654-631e651aad1c&cache=v2"></p>

### 코드 컨벤션
https://www.notion.so/teamsparta/Code-Convention-78154a39fefd40e39c99f2f7a22b898e

### 깃헙 룰
https://www.notion.so/teamsparta/Github-Rules-7c920f21d612400e9d0bced04058bffe

### API docs
https://documenter.getpostman.com/view/34881408/2sA3XY7JAd
