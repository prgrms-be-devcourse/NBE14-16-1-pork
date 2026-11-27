package com.gridsandcircles.global.exception;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gridsandcircles.global.response.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.TestController.class)
@Import(GlobalExceptionHandlerTest.TestController.class)
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("성공 응답은 code 200, message null, content에 데이터")
	void successResponse() throws Exception {
		mockMvc.perform(get("/api/test/ok"))
			.andExpect(status().isOk())
			.andExpect(content().json("{\"code\": 200, \"message\": null, \"content\": \"coffee\"}",
				JsonCompareMode.STRICT));
	}

	@Test
	@DisplayName("필수 쿼리 파라미터가 없으면 400")
	void missingRequestParameter() throws Exception {
		mockMvc.perform(get("/api/test/param"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("필수 헤더가 없으면 400")
	void missingRequestHeader() throws Exception {
		mockMvc.perform(get("/api/test/header"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("파라미터 타입이 맞지 않으면 400")
	void typeMismatch() throws Exception {
		mockMvc.perform(get("/api/test/param").param("page", "abc"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("메서드 파라미터 검증(@Min)에 실패하면 400")
	void handlerMethodValidation() throws Exception {
		mockMvc.perform(get("/api/test/param").param("page", "-1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("요청 본문 검증(@Valid)에 실패하면 400과 필드 메시지")
	void methodArgumentNotValid() throws Exception {
		mockMvc.perform(post("/api/test/body")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\": \"\"}"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(40001))
			.andExpect(jsonPath("$.message").value(startsWith("name: ")));
	}

	@Test
	@DisplayName("JSON 형식이 깨져 있으면 400")
	void messageNotReadable() throws Exception {
		mockMvc.perform(post("/api/test/body")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{broken"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("지원하지 않는 Content-Type이면 415")
	void unsupportedMediaType() throws Exception {
		mockMvc.perform(post("/api/test/body")
				.contentType(MediaType.TEXT_PLAIN)
				.content("name=coffee"))
			.andExpect(status().isUnsupportedMediaType())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("지원하지 않는 HTTP 메서드면 405와 Allow 헤더")
	void methodNotAllowed() throws Exception {
		mockMvc.perform(post("/api/test/param"))
			.andExpect(status().isMethodNotAllowed())
			.andExpect(header().exists("Allow"))
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("없는 경로면 404")
	void noResourceFound() throws Exception {
		mockMvc.perform(get("/api/test/none"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(40001));
	}

	@Test
	@DisplayName("BusinessException은 지정한 에러 코드로 응답")
	void businessException() throws Exception {
		mockMvc.perform(get("/api/test/business"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value(40401))
			.andExpect(jsonPath("$.message").value("원두를 찾을 수 없습니다."))
			.andExpect(jsonPath("$.content").value(nullValue()));
	}

	@Test
	@DisplayName("예상하지 못한 예외는 500과 고정 메시지")
	void unexpectedException() throws Exception {
		mockMvc.perform(get("/api/test/error"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value(50001))
			.andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다."));
	}

	@Validated
	@RestController
	@RequestMapping("/api/test")
	static class TestController {

		@GetMapping("/ok")
		ApiResponse<String> ok() {
			return ApiResponse.ok("coffee");
		}

		@GetMapping("/param")
		String param(@RequestParam @Min(0) int page) {
			return "ok";
		}

		@GetMapping("/header")
		String header(@RequestHeader("X-Request-Id") String requestId) {
			return "ok";
		}

		@PostMapping("/body")
		String body(@Valid @RequestBody TestRequest request) {
			return "ok";
		}

		@GetMapping("/business")
		String business() {
			throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND, "원두를 찾을 수 없습니다.");
		}

		@GetMapping("/error")
		String error() {
			throw new IllegalStateException("unexpected");
		}
	}

	record TestRequest(@NotBlank String name) {
	}
}
