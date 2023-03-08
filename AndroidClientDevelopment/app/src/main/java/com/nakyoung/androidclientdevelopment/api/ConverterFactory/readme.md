Retrofit의 컨버터는 매개변수를 HTTP요청 본문으로 반환하거나 HTTP응답을 메서드의 반환타입으로 변환하는 역할을 함

- GsonConverterFactory : JSON 응답으로 받은 문자열을 메서드의 반환 타입으로 변환
- simplexml: xml을 변환
- protobuf: protocol buffer의 바이너리를 반환
- scalars: 기본 자료형 반환(string, int 등)

Retrofit에 준비된 컨버터로 부족할 시 직접 컨버터를 만들어 사용할 수 있음
- 참고) LocalDateConverterFactory: Converter.Factory()

Converter.Factory의 구현 메소드
1. requestBodyConverter(): 특정 타입을 요청 본문으로 만들기 위한 컨버터 반환
2. responseBodyConverter(): 응답 본문을 특정 타입으로 변환하는 컨버터를 반환
3. stringConverter(): 특정 타입을 문자열로 변환하는 컨버터를 반환

=> 이러한 컨버터팩토리들은 Builder()에 addConverterFactory(팩토리객체)하면 추가된다