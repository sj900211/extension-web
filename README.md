# Extension > Web
> 공통 설정 및 기능 정의
> - ## [AdviceController](./src/main/java/run/freshr/common/advice/AdviceController.java)
>> 전역 예외 처리 설정
> - ## [CustomConfigurationAware](./src/main/java/run/freshr/common/configurations/CustomConfigurationAware.java)
>> 공통 설정 공통 상속 클래스
> - ## [URIConfigurationAware](./src/main/java/run/freshr/common/configurations/URIConfigurationAware.java)
>> URI 정보 공통 상속 클래스  
>> 서비스에서 사용하는 URI 를 `public static final` 로 정의  
>> URI 를 클래스로 따로 관리하는 이유는 하드코딩으로 작성해서 오타 등의 오류를 최소화하고  
>> 관리 포인트를 한 곳으로 집중하기 위해서 따로 클래스에서 관리하도록 설계
> - ## [RestUtilAware](./src/main/java/run/freshr/common/utils/RestUtilAware.java)
>> 자주 사용하는 기능을 정의한 추상 클래스