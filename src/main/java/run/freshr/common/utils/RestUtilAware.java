package run.freshr.common.utils;

import static java.text.MessageFormat.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.types.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import run.freshr.common.data.ExceptionData;
import run.freshr.common.data.ExceptionsData;
import run.freshr.common.data.ResponseData;
import run.freshr.common.dto.response.IdResponse;

/**
 * 자주 사용하는 공통 기능을 정의
 *
 * @author FreshR
 * @apiNote 자주 사용하는 공통 기능을 정의
 * @since 2024. 3. 28. 오후 1:30:39
 */
@Component
public abstract class RestUtilAware {

  private static final ObjectMapper objectMapper;

  private static Environment environment; // Spring 서비스 환경 설정
  private static ExceptionsData exceptionsData; // 에러 설정

  /**
   * 기본 DATE 포맷 정의
   *
   * @apiNote 기본 DATE 포맷 정의
   * @since 2024. 3. 28. 오후 1:30:39
   */
  private static final String DATE_FORMAT = "yyyy-MM-dd";
  /**
   * 기본 DATETIME 포맷
   *
   * @apiNote 기본 DATETIME 포맷
   * @since 2024. 3. 28. 오후 1:30:39
   */
  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  // Object Mapper DATE, DATETIME 포맷 설정
  static {
    objectMapper = new ObjectMapper();

    JavaTimeModule javaTimeModule = new JavaTimeModule();

    javaTimeModule.addSerializer(LocalDate.class,
        new LocalDateSerializer(ofPattern(DATE_FORMAT)));
    javaTimeModule.addDeserializer(LocalDate.class,
        new LocalDateDeserializer(ofPattern(DATE_FORMAT)));
    javaTimeModule.addSerializer(LocalDateTime.class,
        new LocalDateTimeSerializer(ofPattern(DATE_TIME_FORMAT)));
    javaTimeModule.addDeserializer(LocalDateTime.class,
        new LocalDateTimeDeserializer(ofPattern(DATE_TIME_FORMAT)));

    objectMapper.registerModule(javaTimeModule);
  }

  @Autowired
  public RestUtilAware(Environment environment, ExceptionsData exceptionsData) {
    RestUtilAware.environment = environment;
    RestUtilAware.exceptionsData = exceptionsData;
  }

  /**
   * 성공 반환
   *
   * @return response entity
   * @apiNote 성공 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> ok() {
    return ok(ResponseData
        .builder()
        .message(exceptionsData.getSuccess().getMessage())
        .build());
  }

  /**
   * 성공 반환
   *
   * @param message message
   * @return response entity
   * @apiNote 성공 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> ok(final String message) {
    return ok(ResponseData
        .builder()
        .message(ofNullable(message).orElse(exceptionsData.getSuccess().getMessage()))
        .build());
  }

  /**
   * 성공 반환
   *
   * @param <T>  반환 데이터 유형
   * @param data 반환 데이터
   * @return response entity
   * @apiNote 성공 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static <T> ResponseEntity<?> ok(final T data) {
    return ok(ResponseData
        .builder()
        .message(exceptionsData.getSuccess().getMessage())
        .data(data)
        .build());
  }

  /**
   * 성공 반환
   *
   * @param <T>  반환 데이터 유형
   * @param list 반환 목록 데이터
   * @return response entity
   * @apiNote 성공 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static <T> ResponseEntity<?> ok(final List<T> list) {
    return ok(ResponseData
        .builder()
        .message(exceptionsData.getSuccess().getMessage())
        .list(list)
        .build());
  }

  /**
   * 성공 반환
   *
   * @param <T>  반환 데이터 유형
   * @param page 반환 페이지 데이터
   * @return response entity
   * @apiNote 성공 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static <T> ResponseEntity<?> ok(final Page<T> page) {
    return ok(ResponseData
        .builder()
        .message(exceptionsData.getSuccess().getMessage())
        .page(page)
        .build());
  }

  /**
   * 성공 반환
   *
   * @param body 반환 모델
   * @return response entity
   * @apiNote 성공 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> ok(final ResponseData body) {
    return ResponseEntity.ok().body(objectMapper.valueToTree(body));
  }

  /**
   * 에러 반환
   *
   * @param httpStatus HTTP Status 코드
   * @param message    메시지
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final HttpStatus httpStatus, final String message) {
    return error(
        httpStatus,
        null,
        null,
        ofNullable(message).orElse(exceptionsData.getError().getMessage())
    );
  }

  /**
   * 에러 반환
   *
   * @param message formatting 메시지
   * @param args    formatting 값 목록
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final String message, final Object[] args) {
    ExceptionData error = exceptionsData.getError();

    return error(
        error.getHttpStatus(),
        error.getHttpStatus().name(),
        error.getCode(),
        format(ofNullable(message).orElse(error.getMessage()), args)
    );
  }

  /**
   * 에러 반환
   *
   * @param exceptionData exception data
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final ExceptionData exceptionData) {
    return error(exceptionData, null, null, null);
  }

  /**
   * 에러 반환
   *
   * @param exceptionData exception data
   * @param message       메시지
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final ExceptionData exceptionData,
      final String message) {
    return error(exceptionData, message, null, null);
  }

  /**
   * 에러 반환
   *
   * @param exceptionData exception data
   * @param message       메시지
   * @param code          code
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 4. 2. 오후 2:50:05
   */
  public static ResponseEntity<?> error(final ExceptionData exceptionData,
      final String message, final String code) {
    return error(exceptionData, code, message, null);
  }

  /**
   * 에러 반환
   *
   * @param exceptionData exception data
   * @param code          code
   * @param message       formatting 메시지
   * @param args          formatting 값 목록
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final ExceptionData exceptionData,
      final String code, final String message, final Object[] args) {
    return error(
        exceptionData.getHttpStatus(),
        exceptionData.getHttpStatus().getReasonPhrase(),
        ofNullable(code).orElse(exceptionData.getCode()),
        format(ofNullable(message).orElse(exceptionData.getMessage()), args)
    );
  }

  /**
   * 에러 반환
   *
   * @param httpStatus HTTP Status 코드
   * @param name       에러 이름
   * @param code       에러 코드
   * @param message    메시지
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final HttpStatus httpStatus, final String name,
      final String code, final String message) {
    return ResponseEntity
        .status(httpStatus)
        .body(objectMapper.valueToTree(
            ResponseData
                .builder()
                .name(name)
                .code(code)
                .message(message)
                .build()
        ));
  }

  /**
   * 에러 반환
   *
   * @param bindingResult RequestBody Valid 에러 객체
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final BindingResult bindingResult) {
    return ResponseEntity
        .badRequest()
        .body(bindingResult);
  }

  /**
   * 에러 반환
   *
   * @param errors ModelAttribute Valid 에러 객체
   * @return response entity
   * @apiNote 에러 반환
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ResponseEntity<?> error(final Errors errors) {
    return ResponseEntity
        .badRequest()
        .body(errors);
  }

  /**
   * valid 에러 항목 추가
   *
   * @param name          에러 필드 이름
   * @param bindingResult RequestBody Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 잘못된 값
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectWrong(final String name, BindingResult bindingResult) {
    bindingResult.rejectValue(name, "wrong value");
  }

  /**
   * valid 에러 항목 추가
   *
   * @param name          name
   * @param description   description
   * @param bindingResult RequestBody Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 잘못된 값
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectWrong(final String name, final String description,
      BindingResult bindingResult) {
    bindingResult.rejectValue(name, "wrong value", description);
  }

  /**
   * valid 에러 항목 추가
   *
   * @param name   name
   * @param errors ModelAttribute Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 잘못된 값
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectWrong(final String name, Errors errors) {
    errors.rejectValue(name, "wrong value");
  }

  /**
   * valid 에러 항목 추가
   *
   * @param name        name
   * @param description description
   * @param errors      errors
   * @apiNote valid 에러 항목 추가 - 잘못된 값
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectWrong(final String name, final String description, Errors errors) {
    errors.rejectValue(name, "wrong value", description);
  }

  /**
   * valid 에러 항목 추가
   *
   * @param bindingResult RequestBody Valid 에러 객체
   * @param names         names
   * @apiNote valid 에러 항목 추가 - 필수 항목
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectRequired(BindingResult bindingResult, final String... names) {
    stream(names).forEach(name -> rejectRequired(name, bindingResult));
  }

  /**
   * valid 에러 항목 추가
   *
   * @param name          name
   * @param bindingResult RequestBody Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 필수 항목
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectRequired(final String name, BindingResult bindingResult) {
    bindingResult.rejectValue(name, "required value");
  }

  /**
   * valid 에러 항목 추가
   *
   * @param errors ModelAttribute Valid 에러 객체
   * @param names  names
   * @apiNote valid 에러 항목 추가 - 필수 항목
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectRequired(Errors errors, final String... names) {
    stream(names).forEach(name -> rejectRequired(name, errors));
  }

  /**
   * valid 에러 항목 추가
   *
   * @param name   name
   * @param errors ModelAttribute Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 필수 항목
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectRequired(final String name, Errors errors) {
    errors.rejectValue(name, "required value");
  }

  /**
   * valid 에러 항목 추가
   *
   * @param bindingResult RequestBody Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 권한
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectAuth(BindingResult bindingResult) {
    bindingResult.rejectValue("UnAuthenticated", "permission denied");
  }

  /**
   * valid 에러 항목 추가
   *
   * @param errors ModelAttribute Valid 에러 객체
   * @apiNote valid 에러 항목 추가 - 권한
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static void rejectAuth(Errors errors) {
    errors.rejectValue("UnAuthenticated", "permission denied");
  }

  /**
   * 설정한 예외 항목 조회
   *
   * @return exceptions
   * @apiNote 설정한 예외 항목 조회
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static ExceptionsData getExceptions() {
    return RestUtilAware.exceptionsData;
  }

  /**
   * 실행중인 서비스의 profile 체크
   *
   * @param profile profile 이름
   * @return boolean
   * @apiNote 실행중인 서비스의 profile 체크
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static boolean checkProfile(final String profile) {
    return stream(environment.getActiveProfiles())
        .anyMatch(active -> active.equalsIgnoreCase(profile));
  }

  /**
   * 실행중인 서비스의 profile 체크
   *
   * @param profiles profile 이름 목록
   * @return boolean
   * @apiNote 실행중인 서비스의 profile 체크
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static boolean checkProfile(final String... profiles) {
    return stream(environment.getActiveProfiles())
        .anyMatch(active -> stream(profiles).anyMatch(active::equalsIgnoreCase));
  }

  /**
   * ID 반환.
   *
   * @param <ID> ID 데이터 유형
   * @param id   일련 번호
   * @return id response
   * @apiNote {@link IdResponse} 를 설정
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static <ID> IdResponse<ID> buildId(ID id) {
    return IdResponse.<ID>builder().id(id).build();
  }

  /**
   * 필드 이름 조회
   *
   * @param path path
   * @return string
   * @apiNote QueryDsl Path 클래스를 활용하여 field 이름을 조회
   *          하드코딩을 줄이기 위한 방책
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static String field(Path<?> path) {
    String qPath = path.toString();
    int qDotPoint = qPath.indexOf(".") + 1;
    String target = qPath.substring(0, qDotPoint);

    return qPath.replace(target, "").replace(")", "");
  }

  /**
   * 필드 이름 조회
   *
   * @param map map
   * @return string
   * @apiNote 필드 이름 조회
   * @author FreshR
   * @since 2024. 3. 28. 오후 1:30:39
   */
  public static String field(HashMap<?, ?> map) {
    return Optional.of(map.get("name").toString()).orElse("");
  }

}
