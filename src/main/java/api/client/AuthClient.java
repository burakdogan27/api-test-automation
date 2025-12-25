package api.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import api.models.request.auth.LoginRequest;

import static io.restassured.RestAssured.given;

public class AuthClient {

	public Response login(LoginRequest loginRequest) {
		return given().contentType(ContentType.JSON).body(loginRequest).when().post("/auth/login");
	}

}
