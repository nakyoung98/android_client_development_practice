# Activity Result?
```
Q. 타 액티비티에 의해 UI의 내용이 변경되었을 시 UI갱신이 되지 않는다. 이럴 땐 어떻게 해야할까?
```
1. onResume()에 ui갱신 코드 작성 : api에 의한 갱신일 경우 서버 부하
2. 변경되었을 시 UI변경이 필요한 Context에 알려줌 !!
