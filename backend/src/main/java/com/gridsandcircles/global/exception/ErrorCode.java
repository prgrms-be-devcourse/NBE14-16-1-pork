package com.gridsandcircles.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 팀 공통 응답 코드표의 에러 코드.
 * 코드 값은 {HTTP 상태 3자리}{일련번호 2자리} 형식이다. (예: 40401)
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// 400 잘못된 요청
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, 40001, "잘못된 요청입니다."),
	INVALID_EMAIL(HttpStatus.BAD_REQUEST, 40002, "유효하지 않은 이메일입니다."),
	INVALID_QUANTITY(HttpStatus.BAD_REQUEST, 40003, "주문 수량은 1 이상이어야 합니다."),
	INVALID_PRICE(HttpStatus.BAD_REQUEST, 40004, "상품 가격은 0보다 커야 합니다."),
	INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, 40005, "조회 기간이 올바르지 않습니다."),
	INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, 40006, "페이지 정보가 올바르지 않습니다."),
	INVALID_STATUS(HttpStatus.BAD_REQUEST, 40007, "유효하지 않은 상태 값입니다."),

	// 401 인증 오류
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 40101, "인증이 필요합니다."),

	// 403 권한 오류
	FORBIDDEN(HttpStatus.FORBIDDEN, 40301, "접근 권한이 없습니다."),

	// 404 리소스 조회 실패
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "상품을 찾을 수 없습니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, 40402, "주문을 찾을 수 없습니다."),
	SHIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 40403, "배송 정보를 찾을 수 없습니다."),

	// 409 상태 충돌
	PRODUCT_DELETED(HttpStatus.CONFLICT, 40901, "삭제된 상품입니다."),
	ORDER_ALREADY_CANCELED(HttpStatus.CONFLICT, 40902, "이미 취소된 주문입니다."),
	ORDER_CUTOFF_PASSED(HttpStatus.CONFLICT, 40903, "주문 마감 시간이 지났습니다."),
	INVALID_ORDER_STATUS(HttpStatus.CONFLICT, 40904, "현재 주문 상태에서는 처리할 수 없습니다."),

	// 500 서버 오류
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final int code;
	private final String message;

	/**
	 * 스프링 표준 예외의 HTTP 상태에 대응하는 공통 에러 코드를 찾는다.
	 * 코드표에 전용 코드가 없는 4xx(404, 405, 415 등)는 INVALID_REQUEST, 5xx는 INTERNAL_SERVER_ERROR를 사용한다.
	 * 404 전용 코드(상품·주문·배송)는 도메인 의미가 있으므로 여기서 사용하지 않는다.
	 */
	public static ErrorCode from(HttpStatusCode statusCode) {
		return switch (statusCode.value()) {
			case 401 -> UNAUTHORIZED;
			case 403 -> FORBIDDEN;
			default -> statusCode.is4xxClientError() ? INVALID_REQUEST : INTERNAL_SERVER_ERROR;
		};
	}
}
