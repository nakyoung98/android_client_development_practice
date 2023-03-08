# Response<T>
1. 메서드의 반환 값을 제네릭 클래스인 Response<T>로 처리하면 HTTP응답의 코드 및 헤더와 같은 정보를 활용할 수 있음
2. Response는 code() 메서드로 HTTP응답 코드를 가져올 수 있음
3. headers() 메서드로는 HTTP응답 헤더를 가져올 수 있음
4. body() 메서드는 타입<T> 매개변수로 선언한 응답의 본문을 가져올 수 있음

```
참고: ApiService.kt -> suspend fun getQuestion
```

# @GET
## 특징
HTTP는 GET을 사용하는 것을 알리고 API의 경로를 지정함
@POST, @PUT, @PATCH, @DELETE등도 똑같이 사용하면 됨

## 활용
1. param {qid} : 메서드를 호출할 때 채워질 위치 
2. @Path :해당 매개변수가 경로에서 사용됨을 의미 
3. @Header: 요청의 헤더에 값 추가 
4. @Query: 쿼리에 값 추가 
5. @Field: 필드로 추가 
6. @Part: 파트로 추가 
7. @Body: 요청 본문으로 사용

# @Post, @Put
## 특징
1. 본문을 가짐
## 활용
1. @FormUrlEncoded: 요청의 Content-Type을 application/x-www-form-urlencoded로 만듦
2. @Field: 매개변수에 해당 어노테이션이 붙을 경우 해당 매개변수를 본문으로 만든다는 의미
3. @Multipart: 파일을 보내거나 여러 타입의 데이터를 하나의 요청으로 보내기 위해 사용
4. @Part: MultipartBody.Part 타입의 매개변수에 사용하여 각 파트를 정의
5. @Body: JSON으로 요청을 보낼 때 주로 사용. 요청에 전달할 매개변수를 객체로 만들고 매서드의 매개변수로 전달함. 이 매개변수에 @Body 어노테이션을 붙이면 컨버터가 객체를 Serialize 하여 요청의 본문으로 사용함