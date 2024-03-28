package run.freshr.common.advice;

import static run.freshr.common.utils.RestUtilAware.error;
import static run.freshr.common.utils.RestUtilAware.getExceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLSyntaxErrorException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import run.freshr.common.exceptions.DuplicateException;
import run.freshr.common.exceptions.ParameterException;
import run.freshr.common.exceptions.UnAuthenticatedException;

/**
 * 전역 예외 처리 설정
 *
 * @author FreshR
 * @apiNote 전역 예외 처리 설정
 * @since 2024. 3. 28. 오후 2:21:33
 */
@RestControllerAdvice
public class AdviceController {

  //   ______  __    __       _______.___________.  ______   .___  ___.
  //  /      ||  |  |  |     /       |           | /  __  \  |   \/   |
  // |  ,----'|  |  |  |    |   (----`---|  |----`|  |  |  | |  \  /  |
  // |  |     |  |  |  |     \   \       |  |     |  |  |  | |  |\/|  |
  // |  `----.|  `--'  | .----)   |      |  |     |  `--'  | |  |  |  |
  //  \______| \______/  |_______/       |__|      \______/  |__|  |__|

  @ExceptionHandler(value = {ParameterException.class})
  protected ResponseEntity<?> parameterException(ParameterException e) {
    return error(getExceptions().getParameter(), e.getMessage());
  }

  @ExceptionHandler(value = {DuplicateException.class})
  protected ResponseEntity<?> duplicateException(DuplicateException e) {
    return error(getExceptions().getDuplicate(), e.getMessage());
  }

  @ExceptionHandler(value = {UnAuthenticatedException.class})
  protected ResponseEntity<?> unAuthenticatedException(UnAuthenticatedException e) {
    return error(getExceptions().getUnAuthenticated(), e.getMessage());
  }

  //  _______   _______  _______    ___      __    __   __      .___________.
  // |       \ |   ____||   ____|  /   \    |  |  |  | |  |     |           |
  // |  .--.  ||  |__   |  |__    /  ^  \   |  |  |  | |  |     `---|  |----`
  // |  |  |  ||   __|  |   __|  /  /_\  \  |  |  |  | |  |         |  |
  // |  '--'  ||  |____ |  |    /  _____  \ |  `--'  | |  `----.    |  |
  // |_______/ |_______||__|   /__/     \__\ \______/  |_______|    |__|

  @ExceptionHandler(value = {NullPointerException.class})
  protected ResponseEntity<?> nullPointerException(NullPointerException e) {
    return error(getExceptions().getNullPointer(), e.getMessage());
  }

  @ExceptionHandler(value = {EntityNotFoundException.class})
  protected ResponseEntity<?> entityNotFoundException(EntityNotFoundException e) {
    return error(getExceptions().getEntityNotFound(), e.getMessage());
  }

  @ExceptionHandler(value = {IOException.class})
  protected ResponseEntity<?> ioException(IOException e) {
    return error(getExceptions().getIo(), e.getMessage());
  }

  @ExceptionHandler(value = {FileSizeLimitExceededException.class})
  protected ResponseEntity<?> fileSizeLimitExceededException(
      FileSizeLimitExceededException e) {
    return error(getExceptions().getFileSizeLimitExceeded(), e.getMessage());
  }

  @ExceptionHandler(value = {JsonProcessingException.class})
  protected ResponseEntity<?> jsonProcessingException(JsonProcessingException e) {
    return error(getExceptions().getJsonProcessing(), e.getMessage());
  }

  @ExceptionHandler(value = {SQLSyntaxErrorException.class})
  protected ResponseEntity<?> sqlSyntaxErrorException(SQLSyntaxErrorException e) {
    return error(getExceptions().getSqlSyntaxError(), e.getMessage());
  }

  @ExceptionHandler(value = {InvalidDataAccessResourceUsageException.class})
  protected ResponseEntity<?> invalidDataAccessResourceUsageException(
      InvalidDataAccessResourceUsageException e) {
    if (e.getCause() instanceof SQLGrammarException) {
      if (e.getCause().getCause() instanceof SQLSyntaxErrorException) {
        return error(getExceptions().getSqlGrammar(), e.getCause().getCause().getMessage());
      } else {
        return error(getExceptions().getSqlSyntaxError(), e.getCause().getMessage());
      }
    }

    return error(getExceptions().getInvalidDataAccessResourceUsage(), e.getMessage());
  }

  @ExceptionHandler(value = {DataIntegrityViolationException.class})
  protected ResponseEntity<?> dataIntegrityViolationException(
      DataIntegrityViolationException e) {
    if (e.getCause() instanceof ConstraintViolationException) {
      if (e.getCause().getCause() instanceof BatchUpdateException) {
        return error(getExceptions().getBatchUpdate(), e.getCause().getCause().getMessage());
      } else {
        return error(getExceptions().getConstraintViolation(), e.getCause().getMessage());
      }
    } else if (e.getCause() instanceof DataException) {
      if (e.getCause().getCause() instanceof BatchUpdateException) {
        return error(getExceptions().getBatchUpdate(), e.getCause().getCause().getMessage());
      } else {
        return error(getExceptions().getData(), e.getCause().getMessage());
      }
    }

    return error(getExceptions().getDataIntegrityViolation(), e.getMessage());
  }

  @ExceptionHandler(value = {BatchUpdateException.class})
  protected ResponseEntity<?> batchUpdateException(BatchUpdateException e) {
    return error(getExceptions().getBatchUpdate(), e.getMessage());
  }

  @ExceptionHandler(value = {SQLGrammarException.class})
  protected ResponseEntity<?> sqlGrammarException(SQLGrammarException e) {
    return error(getExceptions().getSqlGrammar(), e.getMessage());
  }

  @ExceptionHandler(value = {ConstraintViolationException.class})
  protected ResponseEntity<?> constraintViolationException(ConstraintViolationException e) {
    return error(getExceptions().getConstraintViolation(), e.getMessage());
  }

  @ExceptionHandler(value = {DataException.class})
  protected ResponseEntity<?> dataException(DataException e) {
    return error(getExceptions().getData(), e.getMessage());
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  protected ResponseEntity<?> accessDeniedException(AccessDeniedException e) {
    return error(getExceptions().getAccessDenied(), e.getMessage());
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  protected ResponseEntity<?> illegalStateException(IllegalStateException e) {
    return error(getExceptions().getIllegalState(), e.getMessage());
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  protected ResponseEntity<?> illegalArgumentException(IllegalArgumentException e) {
    return error(getExceptions().getIllegalArgument(), e.getMessage());
  }

  @ExceptionHandler(value = {ExpiredJwtException.class})
  protected ResponseEntity<?> expiredJwtException(ExpiredJwtException e) {
    return error(getExceptions().getExpiredJwt(), e.getMessage());
  }

}
