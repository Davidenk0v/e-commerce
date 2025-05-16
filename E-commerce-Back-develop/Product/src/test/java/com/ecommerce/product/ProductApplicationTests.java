package com.ecommerce.product;

import static java.util.Map.entry;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.ecommerce.product.entity.ModelState;

import io.restassured.RestAssured;

@TestPropertySource(properties = "spring.cloud.config.enabled=false")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ProductApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class ProductApplicationTests {
	
	// Database is populated from TestSeeder to fulfill the requirements of the tests
	@SuppressWarnings("rawtypes")
	@ServiceConnection
	static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:alpine");
    
	@LocalServerPort
	private Integer port;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private static final Map<String, String> CONTROLLER_URLS = Map.ofEntries(
			entry("spec", "/api/v1/product/spec"), 
			entry("category", "/api/v1/product/category"), 
			entry("product", "/api/v1/product/product"),
			entry("model", "/api/v1/product/model"),
			entry("rating", "/api/v1/product/rating"),
			entry("commentary", "/api/v1/product/commentary"),
			entry("like", "/api/v1/product/like"),
			entry("image", "/api/v1/product/image"));

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	
	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
		registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
	}
	
	@BeforeAll
	static void beforeAll() {
		postgreSQLContainer.start();
	}
	
	@AfterAll
	static void afterAll() {
		postgreSQLContainer.stop();
	}
	
	
	// CategoryController Tests **********************************
	
	// Legal call to create a category
	@Test
	@Order(1)
	void shouldCreateCategory() {
		String requestBody = """
				{
					"title": "sobremesa"
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("category"))
			.then()
			.statusCode(201)
			.body("title", Matchers.equalTo("sobremesa"))
			.body("id", Matchers.notNullValue());
	}
	
	// Illegal call to create a category
	@Test
	@Order(2)
	void shouldThrowIllegalArgumentExceptionWhenCreatingCategory() {		
		String requestBody = """
				{
					"title": "sobremesa"
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("category"))
			.then()
			.statusCode(400)
			.body("message", equalTo("Category with title 'sobremesa' already exists"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("category")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to get a category
	@Test
	@Order(3)
	void shouldGetCategory() {
		RestAssured
			.get(CONTROLLER_URLS.get("category") + "/sobremesa")
			.then()
			.statusCode(200)
			.body("title", Matchers.equalTo("sobremesa"))
			.body("id", Matchers.notNullValue());
	}
	
	// Call that should throw a NotFoundException
	@Test
	@Order(4)
	void shouldThrowNotFoundExceptionWhenGettingCategory() {
		RestAssured
			.get(CONTROLLER_URLS.get("category") + "/pantalones")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Legal call to get all categories
	@Test
	@Order(5)
	void shouldGetAllCategories() {
		RestAssured
			.get(CONTROLLER_URLS.get("category"))
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("title", everyItem(notNullValue()));
	}
	
	// Legal call to update a category
	@Test
	@Order(6)
	void shouldUpdateCategory() {
		String requestBody = """
				{
				    "title": "Sobremesa"
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("category") + "/3")
			.then()
			.statusCode(200)
			.body("title", Matchers.equalTo("Sobremesa"))
			.body("id", Matchers.equalTo(3));
	}
	
	// Illegal call to update a category that does not exist
	@Test
	@Order(7)
	void shouldThrowNotFoundExceptionWhenUpdatingCategory() {
		String requestBody = """
				{
				    "title": "sobremesa"
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("category") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Illegal call to update a category with an invalid request body
	@Test
	@Order(8)
	void shouldThrowBadRequestExceptionWhenUpdatingCategory() {
		String requestBody = """
				{
				    "title": "Sobremesa"
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("category") + "/3")
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Category with title 'Sobremesa' already exists"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("category") + "/3"))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to delete a category
	@Test
	@Order(9)
	void shouldDeleteCategory() {
		RestAssured
			.delete(CONTROLLER_URLS.get("category") + "/Sobremesa")
			.then()
			.statusCode(204);
	}
	
	// Illegal call to delete a category that does not exist
	@Test
	@Order(10)
	void shouldThrowNotFoundExceptionWhenDeletingCategory() {
		RestAssured
			.delete(CONTROLLER_URLS.get("category") + "/Sobremesa")
			.then()
			.statusCode(404);
	}
	
	// SpecController Tests **********************************
	
	// Legal call to create a spec
	@Test
	@Order(11)
	void shouldCreateSpec() {
		String requestBody = """
				{
					"name": "color",
					"value": "rojo"
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("spec"))
			.then()
			.statusCode(201)
			.body("name", Matchers.equalTo("color"))
			.body("value", Matchers.equalTo("rojo"))
			.body("id", Matchers.notNullValue());
	}
	
	// Illegal call to create a spec
	@Test
	@Order(12)
	void shouldThrowIllegalArgumentExceptionWhenCreatingSpec() {		
		String requestBody = """
				{
					"name": "color",
					"value": "rojo"
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("spec"))
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Spec with name 'color' and value 'rojo' already exists"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("spec")))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to get a spec
	@Test
	@Order(13)
	void shouldGetSpec() {
		RestAssured
			.get(CONTROLLER_URLS.get("spec") + "/3")
			.then()
			.statusCode(200)
			.body("name", Matchers.equalTo("color"))
			.body("value", Matchers.equalTo("rojo"))
			.body("id", Matchers.equalTo(3));
	}
	
	// Call that should throw a NotFoundException
	@Test
	@Order(14)
	void shouldThrowNotFoundExceptionWhenGettingSpec() {
		RestAssured
			.get(CONTROLLER_URLS.get("spec") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Legal call to get all specs
	@Test
	@Order(15)
	void shouldGetAllSpecs() {
		RestAssured
			.get(CONTROLLER_URLS.get("spec"))
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("name", everyItem(notNullValue()))
			.body("value", everyItem(notNullValue()));
	}
	
	// Legal call to update a spec
	@Test
	@Order(16)
	void shouldUpdateSpec() {
		String requestBody = """
				{
				    "name": "color",
				    "value": "azul"
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("spec") + "/3")
			.then()
			.statusCode(200)
			.body("name", Matchers.equalTo("color"))
			.body("value", Matchers.equalTo("azul"))
			.body("id", Matchers.equalTo(3));
	}
	
	// Illegal call to update a spec that does not exist
	@Test
	@Order(17)
	void shouldThrowNotFoundExceptionWhenUpdatingSpec() {
		String requestBody = """
				{
				    "name": "color",
				    "value": "naranja"
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("spec") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Illegal call to update a spec with an invalid request body
	@Test
	@Order(18)
	void shouldThrowBadRequestExceptionWhenUpdatingSpec() {
		String requestBody = """
				{
				    "name": "color",
				    "value": "azul"
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("spec") + "/3")
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Spec with name 'color' and value 'azul' already exists"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("spec") + "/3"))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to delete a spec
	@Test
	@Order(19)
	void shouldDeleteSpec() {
		RestAssured
			.delete(CONTROLLER_URLS.get("spec") + "/3")
			.then()
			.statusCode(204);
	}
	
	// Illegal call to delete a spec that does not exist
	@Test
	@Order(20)
	void shouldThrowNotFoundExceptionWhenDeletingSpec() {
		RestAssured
			.delete(CONTROLLER_URLS.get("spec") + "/3")
			.then()
			.statusCode(404);
	}
	
	// ProductController Tests **********************************
	
	// Legal call to create a product
	@Test
	@Order(21)
	void shouldCreateProduct() {
		String requestBody = """
				{
					"title": "Iphone 11",
					"sellerId": "569f67de-36e6-4552-ac54-e52085109818",
					"manufacturer": "Apple",
					"categoryIds": [2]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("product"))
			.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("title", equalTo("Iphone 11"))
			.body("sellerId", equalTo("569f67de-36e6-4552-ac54-e52085109818"))
			.body("manufacturer", equalTo("Apple"))
			.body("categoryIds", hasItem(2))
			.body("modelIds", empty())
			.body("currentModelId", nullValue())
			.body("imageUrl", nullValue())
			.body("price", nullValue())
			.body("description", nullValue())
			.body("rating", nullValue())
			.body("opinionCount", nullValue());
	}
	
	// Illegal call to create a product with already existing title for the given user
	@Test
	@Order(22)
	void shouldThrowIllegalArgumentUserAlreadyHasProductRegisteredExceptionWhenCreatingProduct() {		
		String requestBody = """
				{
					"title": "Iphone 11",
					"sellerId": "569f67de-36e6-4552-ac54-e52085109818",
					"manufacturer": "Apple",
					"categoryIds": [2]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("product"))
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("User already has a product registered with title 'Iphone 11'"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("product")))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
	
	// Illegal call to create a product with none existing category
	@Test
	@Order(23)
	void shouldThrowIllegalArgumentNonExistingCategoryExceptionWhenCreatingProduct() {		
		String requestBody = """
				{
					"title": "Iphone 12",
					"sellerId": "569f67de-36e6-4552-ac54-e52085109818",
					"manufacturer": "Apple",
					"categoryIds": [100]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("product"))
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Category with id '100' not found"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("product")))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
		
	// Legal call to get a product
	@Test
	@Order(24)
	void shouldGetProduct() {
		RestAssured
			.get(CONTROLLER_URLS.get("product") + "/3")
			.then()
			.statusCode(200)
			.body("id", Matchers.notNullValue())
			.body("title", Matchers.equalTo("Iphone 11"))
			.body("sellerId", Matchers.equalTo("569f67de-36e6-4552-ac54-e52085109818"))
			.body("manufacturer", Matchers.equalTo("Apple"))
			.body("categoryIds.size()", Matchers.equalTo(1))
			.body("categoryIds", Matchers.hasItem(2))
			.body("modelIds", Matchers.empty())
			.body("currentModelId", nullValue())
			.body("imageUrl", Matchers.nullValue())
			.body("price", Matchers.nullValue())
			.body("description", Matchers.nullValue())
			.body("rating", Matchers.nullValue())
			.body("opinionCount", Matchers.nullValue());
	}
		
	// Call that should throw a NotFoundException
	@Test
	@Order(25)
	void shouldThrowNotFoundExceptionWhenGettingProduct() {
		RestAssured
			.get(CONTROLLER_URLS.get("product") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Legal call to get all products
	@Test
	@Order(26)
	void shouldGetAllProducts() {
		RestAssured
			.get(CONTROLLER_URLS.get("product"))
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("title", everyItem(notNullValue()))
			.body("sellerId", everyItem(notNullValue()))
			.body("manufacturer", everyItem(notNullValue()))
			.body("categoryIds", everyItem(notNullValue()));
	}
		
	// Legal call to update a product
	@Test
	@Order(27)
	void shouldUpdateProduct() {
		String requestBody = """
				{
					"title": "Iphone 11",
					"sellerId": "569f67de-36e6-4552-ac54-e52085109818",
					"manufacturer": "Samsung",
					"categoryIds": [2]
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("product") + "/3")
			.then()
			.statusCode(200)
			.body("id", Matchers.notNullValue())
			.body("title", Matchers.equalTo("Iphone 11"))
			.body("sellerId", Matchers.equalTo("569f67de-36e6-4552-ac54-e52085109818"))
			.body("manufacturer", Matchers.equalTo("Samsung"))
			.body("categoryIds.size()", Matchers.equalTo(1))
			.body("categoryIds", Matchers.hasItem(2))
			.body("modelIds", Matchers.empty())
			.body("currentModelId", nullValue())
			.body("imageUrl", Matchers.nullValue())
			.body("price", Matchers.nullValue())
			.body("description", Matchers.nullValue())
			.body("rating", Matchers.nullValue())
			.body("opinionCount", Matchers.nullValue());
	}
		
	// Illegal call to update a product that does not exist
	@Test
	@Order(28)
	void shouldThrowNotFoundExceptionWhenUpdatingProduct() {
		String requestBody = """
				{
					"title": "Iphone 100",
					"sellerId": "569f67de-36e6-4552-ac54-e52085109818",
					"manufacturer": "Apple",
					"categoryIds": [2]
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("product") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
		
	// Illegal call to update a product with a title already used in another product of the same seller
	@Test
	@Order(29)
	void shouldThrowBadRequestExceptionWhenUpdatingProductWithAlreadyUsedTitleForSeller() {
		String requestBody = """
				{
					"title": "iPhone 13",
					"sellerId": "569g65de-35y6-4552-ac54-e52085109818",
					"manufacturer": "Apple",
					"categoryIds": [2]
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("product") + "/1")
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Product with title 'iPhone 13' and sellerId '569g65de-35y6-4552-ac54-e52085109818' already exists"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("product") + "/1"))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
		
	// Illegal call to update a product with a none existing category
	@Test
	@Order(30)
	void shouldThrowBadRequestExceptionWhenUpdatingProductWithNonExistingCategory() {
		String requestBody = """
				{
					"title": "iPhone 13",
					"sellerId": "569g65de-35y6-4552-ac54-e52085109818",
					"manufacturer": "Apple",
					"categoryIds": [200]
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("product") + "/2")
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Category with id '200' not found"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("product") + "/2"))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
		
	// Legal call to delete a product
	@Test
	@Order(31)
	void shouldDeleteProduct() {
		RestAssured
			.delete(CONTROLLER_URLS.get("product") + "/3")
			.then()
			.statusCode(204);
	}
	
	// Illegal call to delete a product that does not exist
	@Test
	@Order(32)
	void shouldThrowNotFoundExceptionWhenDeletingProduct() {
		RestAssured
			.delete(CONTROLLER_URLS.get("product") + "/3")
			.then()
			.statusCode(404);
	}
	
	// Get all products for a given category
	@Test
	@Order(70)
	void shouldGetAllProductsForCategory() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?categories=Portátiles")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(1))
				.body("content[0].categoryIds", hasItem(1))
				.body("totalElements", equalTo(1))
				.body("totalPages", equalTo(1))
				.body("size", equalTo(5));

	}
	
	// Get all products for a set of given category
	@Test
	@Order(71)
	void shouldGetAllProductsForSetOfCategories() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?categories=Portátiles,Smartphones")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(2))
				.body("content.categoryIds.flatten()", hasItems(1,2))
				.body("totalElements", equalTo(2))
				.body("totalPages", equalTo(1))
				.body("size", equalTo(5));

	}
	
	// Get all products changing pagination size
	@Test
	@Order(72)
	void shouldGetAllProductsPaginatedWithSizeOfTwo() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?size=1")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(1))
				.body("totalElements", equalTo(2))
				.body("totalPages", equalTo(2))
				.body("size", equalTo(1));

	}
	
	// Get all products in page number 2 
	@Test
	@Order(73)
	void shouldGetAllProductsPaginatedPageOne() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?size=1&page=1")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(1))
				.body("totalElements", equalTo(2))
				.body("totalPages", equalTo(2))
				.body("size", equalTo(1))
				.body("number", equalTo(1));
	}
	
	// Get all products ordered by title in ascending order
	@Test
	@Order(74)
	void shouldGetAllProductsOrderedByTitleAscending() {
		String decodedSort = URLDecoder.decode("%5B%7B%22field%22%3A%22title%22%2C%22direction%22%3A%22asc%22%7D%5D", StandardCharsets.UTF_8);
		
		RestAssured
				.given()
				.param("sort", decodedSort)
				.get(CONTROLLER_URLS.get("product") + "/search")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(2))
				.body("totalElements", equalTo(2))
				.body("content[0].title", equalTo("Macbook Pro 2021"))
				.body("content[1].title", equalTo("iPhone 13"));
	}
	
	// Get all products ordered by title in descending order
	@Test
	@Order(81)
	void shouldGetAllProductsOrderedByTitleDescending() {
		String decodedSort = URLDecoder.decode("%5B%7B%22field%22%3A%22title%22%2C%22direction%22%3A%22desc%22%7D%5D", StandardCharsets.UTF_8);
		
		RestAssured
				.given()
				.param("sort", decodedSort)
				.get(CONTROLLER_URLS.get("product") + "/search")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(2))
				.body("totalElements", equalTo(2))
				.body("content[0].title", equalTo("iPhone 13"))
				.body("content[1].title", equalTo("Macbook Pro 2021"));
	}
	
	// Get all products which have a model whose price is higher than a given minimum value
	@Test
	@Order(75)
	void shouldGetAllProductsWithModelPriceHigherThanMinimum() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?min_price=1000")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(1))
				.body("content[0].price", Matchers.greaterThanOrEqualTo((float) 1000.00));
	}
	
	// Get all products which have a model whose price is lower than a given maximum value
	@Test
	@Order(76)
	void shouldGetAllProductsWithModelPriceLowerThanMaximum() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?max_price=1000")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(1))
				.body("content[0].price", Matchers.lessThanOrEqualTo((float) 1000.00));
	}
	
	// Get no products which have a model whose price is between the minimum and maximum value provided
	@Test
	@Order(77)
	void shouldGetNoProductsWithModelPriceBetweenMinimumAndMaximum() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?max_price=1500&min_price=1000")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(0));
	}
	
	// Get both products which have a model whose price is between the minimum and maximum value provided
	@Test
	@Order(78)
	void shouldGetBothProductsWithModelPriceBetweenMinimumAndMaximum() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?max_price=3000&min_price=500")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(2));
	}
	
	// Get all products which have an average rating higher than the given value
	@Test
	@Order(79)
	void shouldGetAllProductsWithAvgRatingHigherThanValue() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?min_rating=3")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(2));
	}
	
	// Get no products which have an average rating higher than the given value
	@Test
	@Order(80)
	void shouldGetNoProductsWithAvgRatingHigherThanValue() {
		RestAssured
				.get(CONTROLLER_URLS.get("product") + "/search?min_rating=5")
				.then()
				.statusCode(200)
				.body("content.size()", equalTo(0));
	}
		
	// ModelController Tests **********************************
	
	// Legal call to create a model
	@Test
	@Order(33)
	void shouldCreateModel() {
		String requestBody = """
				{
					"description": "Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro.",
					"price": "900.00",
					"productId": "2",
					"specIds": [1]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("model"))
			.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("productId", equalTo(2))
			.body("price", equalTo((float) 900.00))
			.body("imagesUrl", empty())
			.body("description", equalTo("Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro."))
			.body("state", equalTo(ModelState.PENDING_APPROVAL.toString()))
			.body("commentaries", empty())
			.body("specs.size()", equalTo(1))
			.body("specs[0].id", equalTo(1))
			.body("specs[0].name", equalTo("Color"))
			.body("specs[0].value", equalTo("Negro"));
	}
	
	// Illegal call to create a model with non existing product
	@Test
	@Order(34)
	void shouldThrowIllegalArgumentProductDoesNotExistExceptionWhenCreatingProduct() {		
		String requestBody = """
				{
					"description": "Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro.",
					"price": "900.00",
					"productId": "100",
					"specIds": [1]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("model"))
			.then()
			.statusCode(400)
			.body("message", equalTo("Product with id '100' does not exist"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("model")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
	
	// Illegal call to create a model with a non existing Spec
	@Test
	@Order(35)
	void shouldThrowIllegalArgumentSpecNotFoundExceptionWhenCreatingProduct() {		
		String requestBody = """
				{
					"description": "Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro.",
					"price": "900.00",
					"productId": "2",
					"specIds": [100]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("model"))
			.then()
			.statusCode(400)
			.body("message", equalTo("Spec with id '100' not found"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("model")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
		
	// Legal call to get a model
	@Test
	@Order(36)
	void shouldGetModel() {
		RestAssured
			.get(CONTROLLER_URLS.get("model") + "/3")
			.then()
			.statusCode(200)
			.body("id", equalTo(3))
			.body("productId", equalTo(2))
			.body("price", equalTo((float) 900.00))
			.body("imagesUrl", empty())
			.body("description", equalTo("Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro."))
			.body("state", equalTo(ModelState.PENDING_APPROVAL.toString()))
			.body("commentaries", empty())
			.body("specs.size()", equalTo(1))
			.body("specs[0].id", equalTo(1))
			.body("specs[0].name", equalTo("Color"))
			.body("specs[0].value", equalTo("Negro"));
	}
		
	// Call that should throw a NotFoundException
	@Test
	@Order(37)
	void shouldThrowNotFoundExceptionWhenGettingModel() {
		RestAssured
			.get(CONTROLLER_URLS.get("model") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
		
	// Legal call to get all models
	@Test
	@Order(38)
	void shouldGetAllModels() {
		RestAssured
			.get(CONTROLLER_URLS.get("model"))
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("productId", everyItem(notNullValue()))
			.body("price", everyItem(notNullValue()))
			.body("description", everyItem(notNullValue()))
			.body("state", everyItem(equalTo(ModelState.PENDING_APPROVAL.toString())))
			.body("specs", everyItem(notNullValue()));
	}
		
	// Legal call to update a model
	@Test
	@Order(39)
	void shouldUpdateModel() {
		String requestBody = """
				{
					"description": "Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro.",
					"price": "920.00",
					"productId": "2",
					"specIds": [1]
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("model") + "/3")
			.then()
			.statusCode(200)
			.body("id", equalTo(3))
			.body("productId", equalTo(2))
			.body("price", equalTo((float) 920.00))
			.body("imagesUrl", empty())
			.body("description", equalTo("Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro."))
			.body("state", equalTo(ModelState.PENDING_APPROVAL.toString()))
			.body("commentaries", empty())
			.body("specs.size()", equalTo(1))
			.body("specs[0].id", equalTo(1))
			.body("specs[0].name", equalTo("Color"))
			.body("specs[0].value", equalTo("Negro"));
	}
		
	// Illegal call to update a model that does not exist
	@Test
	@Order(40)
	void shouldThrowNotFoundExceptionWhenUpdatingModel() {
		String requestBody = """
				{
					"description": "Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro.",
					"price": "920.00",
					"productId": "2",
					"specIds": [1]
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("model") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
		
	// Illegal call to update a model with a spec that does not exist
	@Test
	@Order(41)
	void shouldThrowBadRequestExceptionWhenUpdatingModelWithNonExistingSpec() {
		String requestBody = """
				{
					"description": "Smartphone de última generación con 256GB de almacenamiento y diseño elegante en negro.",
					"price": "920.00",
					"productId": "2",
					"specIds": [100]
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("model") + "/3")
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Spec with id '100' not found"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("model") + "/3"))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
		
	// Legal call to delete a model
	@Test
	@Order(42)
	void shouldDeleteModel() {
		RestAssured
			.delete(CONTROLLER_URLS.get("model") + "/3")
			.then()
			.statusCode(204);
	}
	
	// Illegal call to delete a model that does not exist
	@Test
	@Order(43)
	void shouldThrowNotFoundExceptionWhenDeletingModel() {
		RestAssured
			.delete(CONTROLLER_URLS.get("model") + "/3")
			.then()
			.statusCode(404);
	}
		
	// RatingController Tests **********************************

	// Legal call to create a rating
	@Test
	@Order(44)
	void shouldCreateRating() {
		String requestBody = """
				{
					"modelId": 1,
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"value": 4,
					"optCommentaryCreationDto": null
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("rating"))
			.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("modelId", equalTo(1))
			.body("userId", equalTo("432u67je-36r6-4532-ac52-e43246489998"))
			.body("value", equalTo(4))
			.body("commentaryId", equalTo(null));
	}
		
	// Illegal call to create a rating
	@Test
	@Order(45)
	void shouldThrowIllegalArgumentExceptionWhenCreatingRating() {		
		String requestBody = """
				{
					"modelId": 1,
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"value": 4,
					"optCommentaryCreationDto": null
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("rating"))
			.then()
			.statusCode(400)
			.body("message", equalTo("Rating with modelId '1' and userId '432u67je-36r6-4532-ac52-e43246489998' already exists"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("rating")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
		
	// Legal call to get all ratings for the given model
	@Test
	@Order(46)
	void shouldGetAllRatingsForModel() {
		RestAssured
			.get(CONTROLLER_URLS.get("rating") + "/model/1")
			.then()
			.statusCode(200)
			.body("size()", equalTo(3))
			.body("id", everyItem(notNullValue()))
			.body("modelId", everyItem(equalTo(1)))
			.body("userId", everyItem(notNullValue()))
			.body("value", everyItem(notNullValue()));
	}
		
	// Illegal call to get all ratings for a non existing model
	@Test
	@Order(47)
	void shouldThrowNotFoundExceptionWhenGettingAllRatingsForNonExistingModel() {
		RestAssured
			.get(CONTROLLER_URLS.get("rating") + "/model/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
		
	// Legal call to update a rating
	@Test
	@Order(48)
	void shouldUpdateRating() {
		String requestBody = """
				{
					"modelId": 1,
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"value": 5,
					"optCommentaryCreationDto": null
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("rating") + "/5")
			.then()
			.statusCode(200)
			.body("id", equalTo(5))
			.body("modelId", equalTo(1))
			.body("userId", equalTo("432u67je-36r6-4532-ac52-e43246489998"))
			.body("value", equalTo(5))
			.body("commentaryId", equalTo(null));
	}
		
	// Illegal call to update a rating that does not exist
	@Test
	@Order(49)
	void shouldThrowNotFoundExceptionWhenUpdatingRating() {
		String requestBody = """
				{
					"modelId": 1,
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"value": 2,
					"optCommentaryCreationDto": null
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("rating") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
		
	// Legal call to delete a rating
	@Test
	@Order(50)
	void shouldDeleteRating() {
		RestAssured
			.delete(CONTROLLER_URLS.get("rating") + "/5")
			.then()
			.statusCode(204);
	}
		
	// Illegal call to delete a rating that does not exist
	@Test
	@Order(51)
	void shouldThrowNotFoundExceptionWhenDeletingRating() {
		RestAssured
			.delete(CONTROLLER_URLS.get("rating") + "/5")
			.then()
			.statusCode(404);
	}
		
	// CommentaryController Tests **********************************
	// This test involves date comparisons, it might fail if day date changes during execution
	
	// Legal call to create a commentary
	@Test
	@Order(52)
	void shouldCreateCommentary() {
		LocalDateTime now = LocalDateTime.now();
        
		String requestBody = """
				{
					"modelId": 1,
					"text": "Gran relación calidad precio. Muy satisfecho con mi compra.",
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"parentCommentaryId": null
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("commentary"))
			.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("modelId", equalTo(1))
			.body("userId", equalTo("432u67je-36r6-4532-ac52-e43246489998"))
			.body("text", equalTo("Gran relación calidad precio. Muy satisfecho con mi compra."))
			.body("creationDate", equalTo(now.format(formatter)))
			.body("images", empty())
			.body("likes", equalTo(0))
			.body("parentCommentaryId", nullValue())
			.body("childCommentaryIds", empty());
	}
		
	// Illegal call to create a commentary with non existing model
	@Test
	@Order(53)
	void shouldThrowIllegalArgumentExceptionModelNonExistingWhenCreatingCommentary() {		
		String requestBody = """
				{
					"modelId": 100,
					"text": "Gran relación calidad precio. Muy satisfecho con mi compra.",
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"parentCommentaryId": null
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("commentary"))
			.then()
			.statusCode(400)
			.body("message", equalTo("Model with id '100' does not exist"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("commentary")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
	
	// Illegal call to create a commentary on a commentary the user already commented on
	@Test
	@Order(54)
	void shouldThrowIllegalArgumentExceptionUserAlreadyCommentedOnCommentaryWhenCreatingCommentary() {		
		String requestBody = """
				{
					"modelId": 1,
					"text": "Gran relación calidad precio. Muy satisfecho con mi compra.",
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"parentCommentaryId": 2
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("commentary"))
			.then()
			.statusCode(400)
			.body("message", equalTo("User has aldready commented on the Commentary with id '2'"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("commentary")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
	
	// Illegal call to create a commentary on a model that the user already commented on
	@Test
	@Order(55)
	void shouldThrowIllegalArgumentExceptionUserAlreadyCommentedOnModelWhenCreatingCommentary() {		
		String requestBody = """
				{
					"modelId": 1,
					"text": "Gran relación calidad precio. Lo recomiendo.",
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"parentCommentaryId": null
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("commentary"))
			.then()
			.statusCode(400)
			.body("message", equalTo("User has already registered a Commentary for the Model with id '1'"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("commentary")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to get a commentary
	@Test
	@Order(56)
	void shouldGetCommentary() {
		LocalDateTime now = LocalDateTime.now();
		
		RestAssured
			.get(CONTROLLER_URLS.get("commentary") + "/4")
			.then()
			.statusCode(200)
			.body("id", equalTo(4))
			.body("modelId", equalTo(1))
			.body("userId", equalTo("432u67je-36r6-4532-ac52-e43246489998"))
			.body("text", equalTo("Gran relación calidad precio. Muy satisfecho con mi compra."))
			.body("creationDate", equalTo(now.format(formatter)))
			.body("images", empty())
			.body("likes", equalTo(0))
			.body("parentCommentaryId", nullValue())
			.body("childCommentaryIds", empty());
	}
	
	// Call that should throw a NotFoundException
	@Test
	@Order(57)
	void shouldThrowNotFoundExceptionWhenGettingCommentary() {
		RestAssured
			.get(CONTROLLER_URLS.get("commentary") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Legal call to update a commentary
	@Test
	@Order(58)
	void shouldUpdateCommentary() {
		String requestBody = """
				{
					"modelId": 1,
					"text": "Gran relación calidad precio. Lo volvería a comprar.",
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"parentCommentaryId": null
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("commentary") + "/4")
			.then()
			.statusCode(200)
			.body("id", equalTo(4))
			.body("modelId", equalTo(1))
			.body("text", equalTo("Gran relación calidad precio. Lo volvería a comprar."))
			.body("userId", equalTo("432u67je-36r6-4532-ac52-e43246489998"))
			.body("parentCommentaryId", equalTo(null));
	}
		
	// Illegal call to update a commentary that does not exist
	@Test
	@Order(59)
	void shouldThrowNotFoundExceptionWhenUpdatingCommentary() {
		String requestBody = """
				{
					"modelId": 1,
					"text": "Gran relación calidad precio. Lo volvería a comprar.",
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"parentCommentaryId": null
				}
				""";

		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.put(CONTROLLER_URLS.get("commentary") + "/100")
			.then()
			.statusCode(404)
			.body(emptyOrNullString());
	}
	
	// Legal call to delete a commentary
	@Test
	@Order(60)
	void shouldDeleteCommentary() {
		RestAssured
			.delete(CONTROLLER_URLS.get("commentary") + "/4")
			.then()
			.statusCode(204);
	}
		
	// Illegal call to delete a commentary that does not exist
	@Test
	@Order(61)
	void shouldThrowNotFoundExceptionWhenDeletingCommentary() {
		RestAssured
			.delete(CONTROLLER_URLS.get("commentary") + "/4")
			.then()
			.statusCode(404);
	}
	
	// LikeController Tests **********************************
	// This test involves date comparisons, it might fail if day date changes during execution
	
	// Legal call to create a like
	@Test
	@Order(62)
	void shouldCreateLike() {
		LocalDateTime now = LocalDateTime.now();
        
		String requestBody = """
				{
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"commentaryId": 1
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("like"))
			.then()
			.statusCode(201)
			.body("id", notNullValue())
			.body("userId", equalTo("432u67je-36r6-4532-ac52-e43246489998"))
			.body("commentaryId", equalTo(1))
			.body("creationDate", equalTo(now.format(formatter)));
	}
		
	// Illegal call to create a like with non existing commentary
	@Test
	@Order(63)
	void shouldThrowIllegalArgumentExceptionCommentaryNonExistingWhenCreatingLike() {		
		String requestBody = """
				{
					"userId": "432u67je-36r6-4532-ac52-e43246489998",
					"commentaryId": 100
				}
				""";
		
		RestAssured.given()
			.body(requestBody)
			.contentType("application/json")
			.post(CONTROLLER_URLS.get("like"))
			.then()
			.statusCode(400)
			.body("message", equalTo("Commentary with id '100' not found"))
			.body("details", equalTo("uri=" + CONTROLLER_URLS.get("like")))
			.body("status", equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to get all likes
	@Test
	@Order(64)
	void shouldGetAllLikes() {
		RestAssured
			.get(CONTROLLER_URLS.get("like"))
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("userId", everyItem(notNullValue()))
			.body("commentaryId", everyItem(notNullValue()))
			.body("creationDate", everyItem(notNullValue()));
	}
	
	// Legal call to get all likes of the given user
	@Test
	@Order(65)
	void shouldGetAllLikesOfUser() {
		RestAssured
			.get(CONTROLLER_URLS.get("like") + "/user/432u67je-36r6-4532-ac52-e43246489998")
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("userId", everyItem(notNullValue()))
			.body("commentaryId", everyItem(notNullValue()))
			.body("creationDate", everyItem(notNullValue()));
	}
	
	// Legal call to get all likes of the given commentary
	@Test
	@Order(66)
	void shouldGetAllLikesOfCommentary() {
		RestAssured
			.get(CONTROLLER_URLS.get("like") + "/commentary/1")
			.then()
			.statusCode(200)
			.body("size()", greaterThan(0))
			.body("id", everyItem(notNullValue()))
			.body("userId", everyItem(notNullValue()))
			.body("commentaryId", everyItem(notNullValue()))
			.body("creationDate", everyItem(notNullValue()));
	}
	
	// Illegal call to get all likes of a non existing commentary
	@Test
	@Order(67)
	void shouldThrowIllegalArgumentExceptionNonExistingCommentaryWhenCreatingSpec() {			
		RestAssured
			.get(CONTROLLER_URLS.get("like") + "/commentary/100")
			.then()
			.statusCode(400)
			.body("message", Matchers.equalTo("Commentary with id '100' not found"))
			.body("details", Matchers.equalTo("uri=" + CONTROLLER_URLS.get("like") + "/commentary/100"))
			.body("status", Matchers.equalTo("400 BAD_REQUEST"));
	}
	
	// Legal call to delete a like
	@Test
	@Order(68)
	void shouldDeleteLike() {
		RestAssured
			.delete(CONTROLLER_URLS.get("like") + "/3")
			.then()
			.statusCode(204);
	}
	
	// Illegal call to delete a like that does not exist
	@Test
	@Order(69)
	void shouldThrowNotFoundExceptionWhenDeletingLike() {
		RestAssured
			.delete(CONTROLLER_URLS.get("like") + "/3")
			.then()
			.statusCode(404);
	}
}
