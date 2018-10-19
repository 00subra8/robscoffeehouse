package features.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import com.ig.eval.model.CoffeeVariety;
import com.ig.eval.model.Customer;
import com.ig.eval.model.Order;
import com.ig.eval.model.OrderItem;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features")
public class CucumberStepDefinitions {

    private static final String ADD_CUSTOMER_PATH = "/addCustomer";
    private static final String ADD_COFFEE_VARIETY_PATH = "/addVariety";
    private static final String ORDER_PATH = "/order";
    private static final String APPLICATION_JSON = "application/json";


    private final WireMockServer wireMockServer = new WireMockServer(Options.DYNAMIC_PORT);
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    @When("^Post Request with below customer values is requested to get \"([^\"]+)\" http status response$")
    public void postCustomerAdd(String expectedStatus, List<Customer> customerList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_customer_add.json");

        wireMockServer.start();

        configureAndStubWireMockForCustomerAdd(customerList, 200);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_CUSTOMER_PATH);

        assertResponse(expectedStatus, response, ADD_CUSTOMER_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with below coffee variety values is requested to get \"([^\"]+)\" http status response$")
    public void postCoffeeVarietyAdd(String expectedStatus, List<CoffeeVariety> coffeeVarietyList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_coffee_variety_add.json");

        wireMockServer.start();

        configureAndStubWireMockForCoffeeVarietyAdd(coffeeVarietyList, 200);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_COFFEE_VARIETY_PATH);

        assertResponse(expectedStatus, response, ADD_COFFEE_VARIETY_PATH);

        wireMockServer.stop();

    }

    @When("^GET Request is sent for report url \"([^\"]+)\" then \"([^\"]+)\" http status response is received$")
    public void getReport(String path, String expectedStatus) throws IOException {
        wireMockServer.start();

        configureAndStubWireMockForReport(path, Integer.parseInt(expectedStatus));

        HttpResponse response = getRequestAndGetResponse(path);

        assertResponseForGet(expectedStatus, response, path);

        wireMockServer.stop();

    }

    @When("^Post Request with below order values is requested to get \"([^\"]+)\" http status response$")
    public void postOrderAdd(String expectedStatus, List<Map<String, String>> orderMapList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_order_add.json");

        Order order = convertOrderMapListToOrder(ListUtils.emptyIfNull(orderMapList));

        wireMockServer.start();

        configureAndStubWireMockForOrders(order, 200);

        HttpResponse response = postRequestAndGetResponse(jsonString, ORDER_PATH);

        assertResponse(expectedStatus, response, ORDER_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid phone number value is requested to get \"([^\"]+)\" http status response$")
    public void postCustomerInvalidPhoneNumer(String expectedStatus, List<Customer> customerList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_customer_ph_invalid.json");

        wireMockServer.start();

        configureAndStubWireMockForCustomerAdd(customerList, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_CUSTOMER_PATH);

        assertResponse(expectedStatus, response, ADD_CUSTOMER_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid variety name is requested to get \"([^\"]+)\" http status response$")
    public void postCoffeeVarietyWithInvalidName(String expectedStatus, List<CoffeeVariety> coffeeVarietyList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_coffee_variety_name_invalid.json");

        wireMockServer.start();

        configureAndStubWireMockForCoffeeVarietyAdd(coffeeVarietyList, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_COFFEE_VARIETY_PATH);

        assertResponse(expectedStatus, response, ADD_COFFEE_VARIETY_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid customer name value is requested to get \"([^\"]+)\" http status response$")
    public void postCustomerInvalidCustomerName(String expectedStatus, List<Customer> customerList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_customer_customer_name_invalid.json");

        wireMockServer.start();

        configureAndStubWireMockForCustomerAdd(customerList, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_CUSTOMER_PATH);

        assertResponse(expectedStatus, response, ADD_CUSTOMER_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid available quantity value is requested to get \"([^\"]+)\" http status response$")
    public void postCoffeeVarietyWithInvalidAvailablity(String expectedStatus, List<CoffeeVariety> coffeeVarietyList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_coffee_variety_available_quantity_invalid.json");

        wireMockServer.start();

        configureAndStubWireMockForCoffeeVarietyAdd(coffeeVarietyList, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_COFFEE_VARIETY_PATH);

        assertResponse(expectedStatus, response, ADD_COFFEE_VARIETY_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid price value is requested to get \"([^\"]+)\" http status response$")
    public void postCoffeeVarietyWithInvalidPrice(String expectedStatus, List<CoffeeVariety> coffeeVarietyList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_coffee_variety_price_invalid.json");

        wireMockServer.start();

        configureAndStubWireMockForCoffeeVarietyAdd(coffeeVarietyList, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ADD_COFFEE_VARIETY_PATH);

        assertResponse(expectedStatus, response, ADD_COFFEE_VARIETY_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with invalid customer phone number is requested to get \"([^\"]+)\" http status response$")
    public void postOrderInvalidCustomerPhoneNumber(String expectedStatus, List<Map<String, String>> orderMapList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_order_phone_number_invalid.json");

        Order order = convertOrderMapListToOrder(ListUtils.emptyIfNull(orderMapList));

        wireMockServer.start();

        configureAndStubWireMockForOrders(order, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ORDER_PATH);

        assertResponse(expectedStatus, response, ORDER_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with invalid quantity is requested to get \"([^\"]+)\" http status response$")
    public void postOrderInvalidQuantity(String expectedStatus, List<Map<String, String>> orderMapList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_order_quantity_invalid.json");

        Order order = convertOrderMapListToOrder(ListUtils.emptyIfNull(orderMapList));

        wireMockServer.start();

        configureAndStubWireMockForOrders(order, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ORDER_PATH);

        assertResponse(expectedStatus, response, ORDER_PATH);

        wireMockServer.stop();

    }

    @When("^Post Request with invalid customer variety is requested to get \"([^\"]+)\" http status response$")
    public void postOrderInvalidVariety(String expectedStatus, List<Map<String, String>> orderMapList) throws IOException {
        String jsonString = getJsonString("jsonSamples/sample_order_variety_invalid.json");

        Order order = convertOrderMapListToOrder(ListUtils.emptyIfNull(orderMapList));

        wireMockServer.start();

        configureAndStubWireMockForOrders(order, 400);

        HttpResponse response = postRequestAndGetResponse(jsonString, ORDER_PATH);

        assertResponse(expectedStatus, response, ORDER_PATH);

        wireMockServer.stop();

    }

    private HttpResponse postRequestAndGetResponse(String jsonString, String endpoint) throws IOException {
        HttpPost request = new HttpPost("http://localhost:" + wireMockServer.port() + endpoint);
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.addHeader("content-type", APPLICATION_JSON);
        request.setEntity(entity);
        return httpClient.execute(request);
    }

    private HttpResponse getRequestAndGetResponse(String endpoint) throws IOException {
        HttpGet request = new HttpGet("http://localhost:" + wireMockServer.port() + endpoint);
        return httpClient.execute(request);
    }

    private void configureAndStubWireMockForCustomerAdd(List<Customer> customerList, int status) {
        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(customerList.get(0).getCustomerName()))
                .withRequestBody(containing(customerList.get(0).getPhoneNumber()))
                .willReturn(aResponse().withStatus(status)));
    }

    private void configureAndStubWireMockForCoffeeVarietyAdd(List<CoffeeVariety> coffeeVarietyList, int status) {
        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo(ADD_COFFEE_VARIETY_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(coffeeVarietyList.get(0).getName()))
                .withRequestBody(containing(coffeeVarietyList.get(0).getDescription()))
                .withRequestBody(containing(coffeeVarietyList.get(0).getAvailableQuantity()))
                .withRequestBody(containing(coffeeVarietyList.get(0).getPrice()))
                .willReturn(aResponse().withStatus(status)));
    }

    private void configureAndStubWireMockForOrders(Order order, int status) {
        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo(ORDER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(order.getCustomerPhoneNumber()))
                .withRequestBody(containing(order.getItemList().get(0).getCoffeeVarietyName()))
                .withRequestBody(containing(order.getItemList().get(0).getQuantity()))
                .withRequestBody(containing(order.getItemList().get(1).getCoffeeVarietyName()))
                .withRequestBody(containing(order.getItemList().get(1).getCoffeeVarietyName()))
                .willReturn(aResponse().withStatus(status)));
    }

    private void configureAndStubWireMockForReport(String path, int status) {
        configureFor("localhost", wireMockServer.port());
        stubFor(get(urlEqualTo(path))
                .willReturn(aResponse().withStatus(status)));
    }

    private void assertResponse(String expectedStatus, HttpResponse response, String addCustomerPath) {
        assertEquals(Integer.valueOf(expectedStatus).intValue(), response.getStatusLine().getStatusCode());
        verify(postRequestedFor(urlEqualTo(addCustomerPath))
                .withHeader("content-type", equalTo(APPLICATION_JSON)));
    }

    private void assertResponseForGet(String expectedStatus, HttpResponse response, String addCustomerPath) {
        assertEquals(Integer.valueOf(expectedStatus).intValue(), response.getStatusLine().getStatusCode());
        verify(getRequestedFor(urlEqualTo(addCustomerPath)));
    }

    private String getJsonString(String s) {
        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream(s);
        return new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();
    }

    private Order convertOrderMapListToOrder(List<Map<String, String>> orderMapList) {
        Order order = new Order();
        if (!orderMapList.isEmpty()) {
            Map<String, String> orderMap = MapUtils.emptyIfNull(orderMapList.get(0));
            order.setCustomerPhoneNumber(orderMap.get("Customer Phone Number"));
            OrderItem orderItem1 = new OrderItem();
            String[] itemList1 = StringUtils.split(orderMap.get("Item List1"), ",");
            if (itemList1 != null && itemList1.length > 1) {
                orderItem1.setCoffeeVarietyName(itemList1[0]);
                orderItem1.setQuantity(itemList1[1]);
            }
            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(orderItem1);

            OrderItem orderItem2 = new OrderItem();
            String[] itemList2 = StringUtils.split(orderMap.get("Item List2"), ",");
            if (itemList2 != null && itemList2.length > 1) {
                orderItem2.setCoffeeVarietyName(itemList2[0]);
                orderItem2.setQuantity(itemList2[1]);
            }
            orderItems.add(orderItem2);

            order.setItemList(orderItems);
        }

        return order;
    }
}
