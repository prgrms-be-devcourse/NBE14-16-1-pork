package com.gridsandcircles.global.response;

import com.gridsandcircles.global.exception.ErrorCode;

/**
 * 모든 API 응답에 사용하는 공통 형식.
 *
 * <pre>
 * 성공: { "code": 200,   "message": null,              "content": { ... } }
 * 실패: { "code": 40401, "message": "상품을 찾을 수 없습니다.", "content": null }
 * </pre>
 *
 * <p>message와 content는 값이 없어도 응답에서 빠지지 않고 null로 내려간다.</p>
 */
public record ApiResponse<T>(int code, String message, T content) {

	public static final int SUCCESS_CODE = 200;

	public static <T> ApiResponse<T> ok(T content) {
		return new ApiResponse<>(SUCCESS_CODE, null, content);
	}

	public static ApiResponse<Void> ok() {
		return new ApiResponse<>(SUCCESS_CODE, null, null);
	}

	public static ApiResponse<Void> fail(ErrorCode errorCode) {
		return new ApiResponse<>(errorCode.getCode(), errorCode.getMessage(), null);
	}

	public static ApiResponse<Void> fail(ErrorCode errorCode, String message) {
		return new ApiResponse<>(errorCode.getCode(), message, null);
	}
}
