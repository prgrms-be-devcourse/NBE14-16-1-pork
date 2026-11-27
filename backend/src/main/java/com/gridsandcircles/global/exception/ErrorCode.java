package com.gridsandcircles.global.exception;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 서비스 전반에서 사용하는 에러 코드.
 * 도메인별 에러는 아래에 구역을 나눠 추가한다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력입니다."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "요청한 리소스를 찾을 수 없습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C003", "지원하지 않는 HTTP 메서드입니다."),
	NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "C004", "요청한 응답 형식을 지원하지 않습니다."),
	CONTENT_TOO_LARGE(HttpStatus.CONTENT_TOO_LARGE, "C005", "요청 크기가 허용 범위를 초과했습니다."),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C006", "지원하지 않는 Content-Type입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C999", "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	/**
	 * HTTP 상태 코드에 맞는 공통 에러 코드를 찾는다.
	 * 정확히 일치하는 코드가 없으면 4xx는 INVALID_INPUT, 그 외는 INTERNAL_SERVER_ERROR를 사용한다.
	 */
	public static ErrorCode from(HttpStatusCode statusCode) {
		return Arrays.stream(values())
			.filter(errorCode -> errorCode.status.value() == statusCode.value())
			.findFirst()
			.orElse(statusCode.is4xxClientError() ? INVALID_INPUT : INTERNAL_SERVER_ERROR);
	}
}
