package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;

import api.client.AuthClient;
import api.client.OrderClient;

import api.models.request.auth.LoginRequest;
import api.models.request.order.CreateOrderRequest;
import api.models.request.order.ProductDetails;
import api.models.request.order.ItemRequest;
import api.models.request.order.OrderStatusRequest;
import api.models.request.order.PutOrderRequest;
import api.models.request.order.ShippingRequest;

import api.models.response.auth.LoginFailedResponse;
import api.models.response.auth.LoginSuccessfulResponse;
import api.models.response.order.CreateOrderFailedResponse;
import api.models.response.order.CreateOrderResponse;
import api.models.response.order.GetOrderFailedResponse;
import api.models.response.order.GetOrderResponse;
import api.models.response.order.PatchOrderResponse;
import api.models.response.order.PutOrderResponse;

import api.mock.AuthStub;
import api.mock.OrderStub;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApiIntegrationTest extends BaseTest {

	private final AuthStub authStub = new AuthStub();

	private final OrderStub orderStub = new OrderStub();

	private final AuthClient authClient = new AuthClient();

	private final OrderClient orderClient = new OrderClient();

	// ---------- AUTH TESTS ----------

	@Test
	public void testAuthLoginForSuccessfulUser() throws JsonProcessingException {

		LoginRequest loginRequest = LoginRequest.builder().username("testuser").password("password123").build();

		authStub.stubLoginSuccess(loginRequest);

		Response response = authClient.login(loginRequest);
		LoginSuccessfulResponse authResponse = response.as(LoginSuccessfulResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(authResponse.getToken()).isEqualTo("mock_jwt_token");
	}

	@Test
	public void testAuthLoginForFailedUser() throws JsonProcessingException {

		LoginRequest loginRequest = LoginRequest.builder()
			.username("testuserForFailed")
			.password("password123")
			.build();

		authStub.stubLoginFailure(loginRequest);

		Response response = authClient.login(loginRequest);
		LoginFailedResponse failedResponse = response.as(LoginFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(401);
		Assertions.assertThat(failedResponse.getMessage()).isEqualTo("Authentication failed");
	}

	// ---------- CREATE ORDER TESTS ----------

	@Test
	public void testCreateOrderRequestSuccessful() throws JsonProcessingException {

		CreateOrderRequest request = CreateOrderRequest.builder()
			.userId(1001)
			.addressId(3001)
			.productDetails(ProductDetails.builder().productId(555).quantity(2).build())
			.build();

		orderStub.stubCreateOrderSuccess(request);

		Response response = orderClient.createOrder(request);
		CreateOrderResponse createOrderResponse = response.as(CreateOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(201);
		Assertions.assertThat(createOrderResponse.getOrderId()).isEqualTo("MOCK-ORDER-001");
		Assertions.assertThat(createOrderResponse.getStatus()).isEqualTo("CREATED");
		Assertions.assertThat(createOrderResponse.getQuantity()).isEqualTo(2);
	}

	@Test
	public void testCreateOrderRequestWithMissingUserId() throws JsonProcessingException {

		CreateOrderRequest request = CreateOrderRequest.builder()
			.addressId(3001)
			.productDetails(ProductDetails.builder().productId(555).quantity(2).build())
			.build();

		orderStub.stubCreateOrderMissingUserId(request);

		Response response = orderClient.createOrder(request);
		CreateOrderFailedResponse failedResponse = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(failedResponse.getMessage()).isEqualTo("user id is not found");
	}

	@Test
	public void testCreateOrderRequestWithInvalidQuantity() throws JsonProcessingException {

		CreateOrderRequest request = CreateOrderRequest.builder()
			.userId(1001)
			.addressId(3001)
			.productDetails(ProductDetails.builder().productId(555).quantity(0).build())
			.build();

		orderStub.stubCreateOrderInvalidQuantity(request);

		Response response = orderClient.createOrder(request);
		CreateOrderFailedResponse failedResponse = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(failedResponse.getMessage()).isEqualTo("Quantity should be greater than 0");
	}

	@Test
	public void testCreateOrderRequestForNotFoundProduct() throws JsonProcessingException {

		CreateOrderRequest request = CreateOrderRequest.builder()
			.userId(1001)
			.addressId(3001)
			.productDetails(ProductDetails.builder().productId(1111).quantity(1).build())
			.build();

		orderStub.stubCreateOrderProductNotFound(request);

		Response response = orderClient.createOrder(request);
		CreateOrderFailedResponse failedResponse = response.as(CreateOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(404);
		Assertions.assertThat(failedResponse.getMessage()).isEqualTo("Product not found");
	}

	// ---------- GET ORDER TESTS ----------

	@Test
	public void testGetOrderRequest() {

		orderStub.stubGetOrderSuccess("MOCK-ORDER-001");

		Response response = orderClient.getOrder("MOCK-ORDER-001");
		GetOrderResponse getOrderResponse = response.as(GetOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(getOrderResponse.getOrder().getId()).isEqualTo("MOCK-ORDER-001");
		Assertions.assertThat(getOrderResponse.getOrder().getStatus()).isEqualTo("CREATED");
		Assertions.assertThat(getOrderResponse.getItem().getProductId()).isEqualTo(555);
		Assertions.assertThat(getOrderResponse.getItem().getQuantity()).isEqualTo(2);
		Assertions.assertThat(getOrderResponse.getShipping().getAddressId()).isEqualTo(3001);
	}

	@Test
	public void testGetOrderRequestWithInvalidOrderId() {

		orderStub.stubGetOrderInvalidId("INVALID-ORDER-ID");

		Response response = orderClient.getOrder("INVALID-ORDER-ID");
		GetOrderFailedResponse failedResponse = response.as(GetOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(400);
		Assertions.assertThat(failedResponse.getMessage()).isEqualTo("Invalid order Id");
	}

	@Test
	public void testGetOrderRequestForNotFoundOrder() {

		orderStub.stubGetOrderNotFound("NOT-FOUND-ORDER-ID");

		Response response = orderClient.getOrder("NOT-FOUND-ORDER-ID");
		GetOrderFailedResponse failedResponse = response.as(GetOrderFailedResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(404);
		Assertions.assertThat(failedResponse.getMessage()).isEqualTo("Order not found");
	}

	// ---------- PUT / PATCH TESTS ----------

	@Test
	public void testPutOrderRequest() throws JsonProcessingException {

		PutOrderRequest request = PutOrderRequest.builder()
			.item(ItemRequest.builder().productId(590).quantity(10).build())
			.shipping(ShippingRequest.builder().addressId(5001).build())
			.order(OrderStatusRequest.builder().id("MOCK-ORDER-001").status("UPDATED").build())
			.build();

		orderStub.stubPutOrder("MOCK-ORDER-001", request);

		Response response = orderClient.updateOrder("MOCK-ORDER-001", request);
		PutOrderResponse putOrderResponse = response.as(PutOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(putOrderResponse.getMessage()).isEqualTo("Order updated successfully");
	}

	@Test
	public void testPatchOrdersStatus() throws JsonProcessingException {

		OrderStatusRequest request = OrderStatusRequest.builder().id("MOCK-ORDER-001").status("CANCELLED").build();

		orderStub.stubPatchOrderStatus(request);

		Response response = orderClient.updateOrderStatus(request);
		PatchOrderResponse patchResponse = response.as(PatchOrderResponse.class);

		Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
		Assertions.assertThat(patchResponse.getMessage()).isEqualTo("Order partially updated successfully");
	}

}
