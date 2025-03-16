package com.example.laptop;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAssuredCodeApplicationTests {

	private static final int laptopId = 22;

	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8080";
	}

	@Test
	@Order(1)
	@DisplayName("Test GET /laptops - Fetch all laptops")
	public void testGetAllLaptops() {
		given()
				.when()
				.get("/laptops")
				.then()
				.statusCode(200)
				.contentType(ContentType.JSON);
	}

	@Test
	@Order(2)
	@DisplayName("Test POST /laptops - Insert new laptop")
	@Description("Test to add a new laptop and verify response")
	public void testAddLaptop() {
		String requestBody = """
            {
				         "laptopBrand": "Mac Book Pro",
				         "modelName": "M2",
				         "laptopTag": "Apple"
				     }
        """;

		// Sending POST request
		Response response = given()
				.contentType(ContentType.JSON)
				.body(requestBody)
				.when()
				.post("/laptops")
				.then()
				.log().all()  // Logs full response
				.assertThat()
				.statusCode(201)  // Ensure API returns 200, adjust if needed
				.extract()
				.response();

		// Extract ID from response safely
		Integer laptopId = response.path("id");
		if (laptopId == null) {
			throw new AssertionError("Laptop ID is null, API might not be returning expected response!");
		}

		System.out.println("Created Laptop ID: " + laptopId);
	}

	@Test
	@Order(3)
	@DisplayName("Test PUT /laptops/{id} - Update existing laptop")
	@Description("Test to update an existing laptop by ID")
	public void testUpdateLaptop() {
		String updatedRequestBody = """
            {
                "laptopBrand": "Mac Book Pro Version Update",
                "modelName": "M3 New Model Update",
                "laptopTag": "Apple"
            }
        """;

		given()
				.contentType(ContentType.JSON)
				.body(updatedRequestBody)
				.when()
				.put("/laptops/" + laptopId)
				.then()
				.log().all()
				.assertThat()
				.statusCode(200);
				//.body("modelName", org.hamcrest.Matchers.equalTo("M3"));  // Verify update
	}

//	@Test
//	@Order(4)
//	@DisplayName("Test DELETE /laptops/{id} - Remove laptop")
//	@Description("Test to delete a laptop by ID and verify deletion")
//	public void testDeleteLaptop() {
//		given()
//				.when()
//				.delete("/laptops/" + laptopId)
//				.then()
//				.log().all()
//				.assertThat()
//				.statusCode(200);  // Expecting No Content on successful delete
//
//		// Verify deletion
//		given()
//				.when()
//				.get("/laptops/" + laptopId)
//				.then()
//				.statusCode(404);  // Expecting Not Found
//	}
}
