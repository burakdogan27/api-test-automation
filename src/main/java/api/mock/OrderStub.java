package api.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;

import api.models.request.order.CreateOrderRequest;
import api.models.request.order.PutOrderRequest;
import api.models.request.order.OrderStatusRequest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class OrderStub {

	private static final String CONTENT_TYPE_HEADER = "Content-Type";

	private static final String APPLICATION_JSON_UTF8 = "application/json; charset=UTF-8";

	private final ObjectMapper objectMapper = new ObjectMapper();

	// ---------- CREATE ORDER ----------

	public void stubCreateOrderSuccess(CreateOrderRequest request) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(request)))
			.willReturn(aResponse().withStatus(201)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"orderId\": \"MOCK-ORDER-001\", \"status\": \"CREATED\", \"quantity\": 2 }")));
	}

	public void stubCreateOrderMissingUserId(CreateOrderRequest request) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(request)))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"user id is not found\" }")));
	}

	public void stubCreateOrderInvalidQuantity(CreateOrderRequest request) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(request)))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Quantity should be greater than 0\" }")));
	}

	public void stubCreateOrderProductNotFound(CreateOrderRequest request) throws JsonProcessingException {

		WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/orders"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(request)))
			.willReturn(aResponse().withStatus(404)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Product not found\" }")));
	}

	// ---------- GET ORDER ----------

	public void stubGetOrderSuccess(String orderId) {

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/orders/" + orderId))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"order\": { \"id\": \"MOCK-ORDER-001\", \"status\": \"CREATED\" }, "
						+ "\"item\": { \"productId\": 555, \"quantity\": 2 }, "
						+ "\"shipping\": { \"addressId\": 3001 } }")));
	}

	public void stubGetOrderInvalidId(String orderId) {

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/orders/" + orderId))
			.willReturn(aResponse().withStatus(400)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Invalid order Id\" }")));
	}

	public void stubGetOrderNotFound(String orderId) {

		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/orders/" + orderId))
			.willReturn(aResponse().withStatus(404)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Order not found\" }")));
	}

	// ---------- PUT / PATCH ----------

	public void stubPutOrder(String orderId, PutOrderRequest request) throws JsonProcessingException {

		WireMock.stubFor(WireMock.put(WireMock.urlEqualTo("/orders/" + orderId))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(request)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Order updated successfully\" }")));
	}

	public void stubPatchOrderStatus(OrderStatusRequest request) throws JsonProcessingException {

		WireMock.stubFor(WireMock.patch(WireMock.urlEqualTo("/orders/status"))
			.withRequestBody(WireMock.equalToJson(objectMapper.writeValueAsString(request)))
			.willReturn(aResponse().withStatus(200)
				.withHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON_UTF8)
				.withBody("{ \"message\": \"Order partially updated successfully\" }")));
	}

}
