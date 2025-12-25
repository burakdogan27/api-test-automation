package api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public abstract class BaseTest {

	protected static WireMockServer wiremockServer;

	@BeforeAll
	public static void setupTestEnvironment() {
		RestAssured.filters(new AllureRestAssured());

		wiremockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
		wiremockServer.start();

		configureFor("localhost", wiremockServer.port());

		RestAssured.baseURI = "http://localhost";
		RestAssured.port = wiremockServer.port();
	}

	@AfterAll
	public static void tearDown() {
		if (wiremockServer != null) {
			wiremockServer.stop();
		}
	}

}
