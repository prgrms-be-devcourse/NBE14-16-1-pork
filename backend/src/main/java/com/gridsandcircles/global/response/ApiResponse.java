package com.gridsandcircles.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 모든 API 응답에 사용하는 공통 형식.
 *
 * <pre>
 * 성공: { "success": true,  "data": { ... } }
 * 실패: { "success": false, "error": { "code": "...", "message": "..." } }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(boolean success, T data, ErrorDetail error) {

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(true, data, null);
	}

	public static ApiResponse<Void> ok() {
		return new ApiResponse<>(true, null, null);
	}

	public static ApiResponse<Void> fail(String code, String message) {
		return new ApiResponse<>(false, null, new ErrorDetail(code, message));
	}

	public record ErrorDetail(String code, String message) {
	}
}
