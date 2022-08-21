# Focus_Zandi_Server
UMC 2nd 실시간 학습 몰입(4) - Server 
## 🍃 Zandi Time - 실시간 학습 몰입 앱 서비스
###### #Spring #SpringBoot #UMC

### 👥 Co-Work

1. 서버와 프론트  
    - REST API를 이용하여 GET / POST 메소드를 이용한 서버에 연동(with Alamofire, URLSession, Postman)
2. Git으로 협업하기
    - GitHub Organization을 만들어 Branch, Fork, PR을 통해 Spring Project를 팀원과 공유
3. 소통하기
    - 전체 공지 : Kakao Talk
    - 문서 공유 : Notion
    - 온라인 미팅 : Zoom
   
---

### 📆 Plan
- 22.05.25 ~ 22.06.14 디자이너 모집기간 
- 22.06.15 ~ 22.06.22 디자이너 & 기획자 미팅 및 아이디어 Develop
- 22.06.23 ~ 22.06.28 팀원 매칭
- 22.07.01 ~ 22.07.02 해커톤
- 22.07.03 ~ 22.08.15 팀별 몰입 개발기간
- 22.08.25 ~ 22.08.26 UMC 2기 데모데이 (공덕역 프론트원)

---

### 🗣 MOM

<img width="1023" alt="스크린샷 2022-08-16 21 37 30" src="https://user-images.githubusercontent.com/74387813/184881299-13bb0c72-bb63-446f-9d33-eede1ce8c275.png">

---

### 📱 Final Result

https://www.youtube.com/watch?v=EAQBYl3l6Tc&t=8s

---

### 🤖 Prerequisite
![image](https://user-images.githubusercontent.com/101084034/185771405-9946f393-b974-41ac-ba94-07faa11217e9.png)
- JDK : 11
- Spring Boot : 2.6.9
- IDE : IntelliJ IDEA
- Server : AWS EC2
- DB : RDS Mysql


---
#### Structure
![image](https://user-images.githubusercontent.com/101084034/185771434-5414829a-e306-45e9-abec-c79535364c7b.png)

---

### API Sheet
![image](https://user-images.githubusercontent.com/55794835/185589055-af864d2c-93b7-44bb-b519-c65c1478c87b.png)

---

### Main Function 

#### Security
    1. 최초에는 폼 로그인 방식을 선택하려 했으나, 앱기반에서는 부적합하여 소셜로그인으로 변경. 
    물론 json을 받아서 폼으로 변환할 수 있는 Filter를 만들어서 사용하면 작동은 가능함.
    
    2. 두번째로는 WEB기반 OAUTH2를 사용하려고 하였으나, IOS 환경에서는 WEB으로 로그인하는게 불가능했고 결국 ClientCredential 방식으로 변경하기로 하였다.
    
    3. 최종적으로 프론트에서 OAuth2를 담당하고 백엔드는 JWT를 발급하고 이를 관리해주는 방식을 선택하였다. 
    

#### User Authentication 
    Login
        유저가 로그인한 상태를 식별 및 request한 유저를 파악하기 위한 방법으로 JWT와 Authentication을 이용
        프론트에서 OAuth2를 통해 로그인하고 거기서 얻은 정보들을 JSON에 담아서 보내면 이를 컨트롤러가 받아서 회원가입 혹은 로그인을 진행하고
        JWT를 만들어서 클라이언트로 값을 반환해준다. 이로써 토큰로그인 시스템을 구현했다.
        
    Authentication
        JWT를 헤더에 넣어가지고 API URL에 요청을 보내면, 시큐리티로 인해 JwtAuthorizationFilter를 무조건 거치게 되고, 이 Filter에는 JWT토큰을 검증하고, 이를 기반으로 
        SecurityContextHolder.getContext().setAuthentication(authentication)을 수행한다. 이를 통해서 Authentication안에 PrincipalDetails를 넣을 수 있고, 각 컨트롤러에서 
        이를 기반으로 유저를 식별한다. 

#### Controller 
    MemberController
        showMember : 프론트 요구에 맞춘 멤버 정보 반환 
        memberQuit : 유저 관련 모든 정보 삭제, FK로 연결되어 있는 테이블이 많아서 Follower, Records, Member, MemberDetails 순으로 정리한다. 
        editMember : 유저 상세 정보를 기입한다. 최초 가입시에는 null이나 Default로 생성된다. 
        addFriend/{email} : 이메일을 넣으면 친구추가를 해준다. 
        removeFriend/{email} : 이메일을 넣으면 친구삭제를 해준다.
        findFriend : 요청한 유저의 전체 친구의 username을 반환해준다. 
        
    RecordController
        saveRecords/today : 오늘자 공부기록을 저장한다. 중복저장을 막기 위해 오늘자의 레코드가 이미 있는지 한번 확인하고, 있다면 그 레코드를 불러와서 변경감지 형식으로 
        덮어씌워서 저장한다
        records?date=YYYY-MM-DD : 특정 날짜의 기록을 조회한다. RequestParam으로 날짜를 받아서 처리한다. 
        records/monthly?month=MM : 특정 날의 기록을 전부 조회해서 프론트 요구사항에 맞춘 객체에 담아서 MAP형태로 반환한다. 
        
---

### 기술스택         
#### 데이터베이스
    RDS mySql : 서비스의 DB이다. 
    MariaDB : 로컬에서 테스트할 떄 사용하였다. 
    JPA : Spring과 DB를 연결하고 엔티티를 구성할 때 사용하였다.

#### 로그인 관련
    Security : 시큐리티로 필터체인 구성 및 접근권한 제어 
    JWT : Json Web Token을 이용한 Token로그인 구현을 위해 사용

#### 컨틀롤러
    PathVariable, RequestParam
        
    
### 🧑‍💻 Ref. link

- Idea Info. https://cake-tarn-9a3.notion.site/d053e9512d7c415898fd41261b315f8f
- UMC https://makeus-challenge.notion.site/Univ-MakeUs-Challenge-2nd-Board-fba760aa7ed94edc93ebb42722307945
