package api.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import api.models.request.order.CreateOrderRequest;
import api.models.request.order.OrderStatusRequest;
import api.models.request.order.PutOrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient {

	public Response createOrder(CreateOrderRequest createOrderRequest) {
		return given().contentType(ContentType.JSON).body(createOrderRequest).when().post("/orders");
	}

	public Response getOrder(String orderId) {
		return given().contentType(ContentType.JSON).when().get("/orders/" + orderId);
	}

	public Response updateOrder(String orderId, PutOrderRequest putOrderRequest) {
		return given().contentType(ContentType.JSON).body(putOrderRequest).when().put("/orders/" + orderId);
	}

	public Response updateOrderStatus(OrderStatusRequest orderStatusRequest) {
		return given().contentType(ContentType.JSON).body(orderStatusRequest).when().patch("/orders/status");
	}

}
