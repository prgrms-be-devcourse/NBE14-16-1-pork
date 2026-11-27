package com.gridsandcircles.global.exception;

import java.util.stream.Collectors;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gridsandcircles.global.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외 처리기.
 *
 * <p>스프링 MVC 표준 예외(파라미터 누락, Content-Type 오류 등)는 {@link ResponseEntityExceptionHandler}가
 * 알맞은 HTTP 상태 코드로 처리하고, 응답 본문만 {@link ApiResponse} 형식으로 바꿔서 내보낸다.
 * 처리되지 않은 예외만 500으로 응답한다.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
		log.warn("BusinessException: {}", exception.getMessage());
		ErrorCode errorCode = exception.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(ApiResponse.fail(errorCode.getCode(), exception.getMessage()));
	}

	/**
	 * 클래스에 @Validated를 붙인 빈의 메서드 파라미터 검증 실패.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException exception) {
		String message = exception.getConstraintViolations().stream()
			.map(ConstraintViolation::getMessage)
			.collect(Collectors.joining(", "));
		return ResponseEntity.badRequest()
			.body(ApiResponse.fail(ErrorCode.INVALID_INPUT.getCode(), message));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
		log.error("Unhandled exception", exception);
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(errorCode.getStatus())
			.body(ApiResponse.fail(errorCode.getCode(), errorCode.getMessage()));
	}

	/**
	 * ResponseEntityExceptionHandler가 처리한 모든 표준 예외의 응답 본문을 ApiResponse 형식으로 바꾼다.
	 * 상태 코드와 헤더(예: 405의 Allow)는 스프링이 정한 값을 그대로 사용한다.
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
		HttpStatusCode statusCode, WebRequest request) {
		ErrorCode errorCode = ErrorCode.from(statusCode);

		if (statusCode.is5xxServerError()) {
			log.error("Server error while handling request", exception);
		} else {
			log.warn("Client error ({}): {}", statusCode.value(), exception.getMessage());
		}

		String message = resolveMessage(exception, statusCode, errorCode);
		ApiResponse<Void> response = ApiResponse.fail(errorCode.getCode(), message);
		return ResponseEntity.status(statusCode).headers(headers).body(response);
	}

	private String resolveMessage(Exception exception, HttpStatusCode statusCode, ErrorCode errorCode) {
		// 서버 오류는 내부 정보가 노출되지 않도록 고정 메시지를 사용한다.
		if (statusCode.is5xxServerError()) {
			return errorCode.getMessage();
		}

		return switch (exception) {
			case MethodArgumentNotValidException e -> e.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));
			case HandlerMethodValidationException e -> e.getAllErrors().stream()
				.map(MessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(", "));
			case ErrorResponse e when e.getBody().getDetail() != null
				&& statusCode.value() != HttpStatus.NOT_FOUND.value() -> e.getBody().getDetail();
			default -> errorCode.getMessage();
		};
	}
}
