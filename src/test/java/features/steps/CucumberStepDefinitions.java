package features.steps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import com.ig.eval.model.Customer;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features")
public class CucumberStepDefinitions {

    private static final String ADD_CUSTOMER_PATH = "/addCustomer";
    private static final String APPLICATION_JSON = "application/json";



    private final WireMockServer wireMockServer = new WireMockServer(Options.DYNAMIC_PORT);
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    @When("^Post Request with below customer values is requested to get \"([^\"]+)\" http status response$")
    public void postCustomerAdd(String expectedStatus, List<Customer> customerList) throws IOException {
        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("sample_customer_add.json");
        String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();

        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(customerList.get(0).getCustomerName()))
                .withRequestBody(containing(customerList.get(0).getPhoneNumber()))
                .willReturn(aResponse().withStatus(200)));

        HttpPost request = new HttpPost("http://localhost:" + wireMockServer.port() + ADD_CUSTOMER_PATH);
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.addHeader("content-type", APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);

        assertEquals(Integer.valueOf(expectedStatus).intValue(), response.getStatusLine().getStatusCode());
        verify(postRequestedFor(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON)));

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid phone number value is requested to get \"([^\"]+)\" http status response$")
    public void postCustomerInvalidPhoneNumer(String expectedStatus, List<Customer> customerList) throws IOException {
        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("sample_customer_ph_invalid.json");
        String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();

        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(customerList.get(0).getCustomerName()))
                .withRequestBody(containing(customerList.get(0).getPhoneNumber()))
                .willReturn(aResponse().withStatus(400)));

        HttpPost request = new HttpPost("http://localhost:" + wireMockServer.port() + ADD_CUSTOMER_PATH);
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.addHeader("content-type", APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);

        assertEquals(Integer.valueOf(expectedStatus).intValue(), response.getStatusLine().getStatusCode());
        verify(postRequestedFor(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON)));

        wireMockServer.stop();

    }

    @When("^Post Request with below invalid customer name value is requested to get \"([^\"]+)\" http status response$")
    public void postCustomerInvalidCustomerName(String expectedStatus, List<Customer> customerList) throws IOException {
        InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("sample_customer_customer_name_invalid.json");
        String jsonString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();

        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());
        stubFor(post(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(customerList.get(0).getCustomerName()))
                .withRequestBody(containing(customerList.get(0).getPhoneNumber()))
                .willReturn(aResponse().withStatus(400)));

        HttpPost request = new HttpPost("http://localhost:" + wireMockServer.port() + ADD_CUSTOMER_PATH);
        StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
        request.addHeader("content-type", APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);

        assertEquals(Integer.valueOf(expectedStatus).intValue(), response.getStatusLine().getStatusCode());
        verify(postRequestedFor(urlEqualTo(ADD_CUSTOMER_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON)));

        wireMockServer.stop();

    }

}
