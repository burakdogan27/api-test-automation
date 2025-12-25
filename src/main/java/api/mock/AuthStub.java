package api.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import api.models.request.auth.LoginRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class AuthStub {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";

	private static final String APPLICATION_JSON_UTF8 = "application/json; charset=UTF-8";

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void stubLoginSuccess(LoginRequest loginRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(loginRequest)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"token\": \"mock_jwt_token\" }")));
	}

	public void stubLoginFailure(LoginRequest loginRequest) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/auth/login"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(loginRequest)))
			.willReturn(aResponse().withStatus(401)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Authentication failed\" }")));
	}

}
