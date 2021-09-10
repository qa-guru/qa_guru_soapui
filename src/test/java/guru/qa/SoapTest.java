package guru.qa;

import guru.qa.domain.GetCountryResponse;
import io.restassured.RestAssured;
import io.restassured.internal.util.IOUtils;
import io.restassured.response.Response;
import io.spring.guides.gs_producing_web_service.Country;
import io.spring.guides.gs_producing_web_service.GetCountryRequest;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SoapTest {

    @Test
    void getCountriesTest() throws Exception {
        JAXBContext jbCtx = JAXBContext.newInstance(GetCountryResponse.class);
        Unmarshaller unmarshaller = jbCtx.createUnmarshaller();

        InputStream is = SoapTest.class.getClassLoader().getResourceAsStream("getCountryRequest.xml");
        final String request = new String(IOUtils.toByteArray(is));

        RestAssured.baseURI = "http://localhost:8080/ws";

        Response response=given()
                .header("Content-Type", "text/xml")
                .and()
                .body(request)
                .when()
                .post("/getCountry")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println(response.asString());
        assertTrue(response.asString().contains("Madrid"));
        // TODO implement jaxb unmarshalling
    }
}
