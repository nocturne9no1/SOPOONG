# sopoong

## Project setup
```
yarn install
```

### Compiles and hot-reloads for development
```
yarn serve
```

### Compiles and minifies for production
```
yarn build
```

### Lints and fixes files
```
yarn lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).





## Won-hyeok Jung

### 구현중 / 구현했으나 수정할 부분

- `FindAccount.vue`
  - 이메일 검증을 `this.$store.dispatch("checkExistingEmail", email);` 통하여 실시간으로 존재하는 이메일인지 받아오는 방식을 구현할까 고려중.
    - 정보 보호에 좋지 않을거 같아 되도록이면 현재 이메일 양식만 테스트하는 방향으로 고려중.
  - 이메일 양식 완성시 버튼이 등장하는 방식 / 버튼을 비활성화 하는 방식
- `Fixed`로 navBar 구현 후, absolute는 navbar 밑으로도 들어갈수 있어서 화면이 안정적으로 구성되는데 비해 table은 화면이 안정적으로 구성되지 못하는 점.



### 고려해야할 것

- 유저별 주소 `동적 라우트 매칭`



### 필수 개발 요소

- 회원가입 페이지
  - 실시간 데이터 교환 방식으로 아이디, 닉네임, 영문주소 중복 확인



### 일별 진행상황

| 일자 | 내용                                                         |
| ---- | ------------------------------------------------------------ |
| 0802 | 로그인, 회원가입 페이지 개발 / 정규식 및 경고창 적용         |
| 0803 | 이메일 인증, ID&PW찾기 개발 / 중첩 라우트 적용               |
| 0804 | 일지 작성 모달 개발 / 모달 내 사진 업로드 개발 / 프로그레스 바 추가 |
| 0805 | 사진 api 테스트 및 기타 css 수정                             |
| 0806 |                                                              |





`사진 업로드 -> 사진별로 회색핀에 찍고 -> 회색핀마다 눌러서 일지를 쓸 수 있도록 한다. -> 개별 일지 작성(모달을 통해) 후 저장`



git checkout develop

git flow init

다 엔터하고,

git flow featrue start <원하는이름>



### 주의사항

- `yarn add vue-filepond@6` : 7버젼(최신)은 vue2 지원을 안함.