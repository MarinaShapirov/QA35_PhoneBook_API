package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import dto.ContactRespDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.http.HttpResponse;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class AddNewContactTestsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2Njk2NTY1MDUsImlhdCI6MTY2OTA1NjUwNX0.gaIDbPL5-m8iEJ5yvGiGehZEcnXstL9bCcB2JYMnLQo";

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test//bug - return status 0 instead 200
    public void AddNewContactSuccess() {
        ContactDto contact = createContact();
        Assert.assertTrue(addNewContact(contact));
    }

    @Test
    public void AddNewContactNegativeUnauthorized() {
        ContactDto contact = createContact();

        ErrorDto respDto = given()
                .header("Authorization", "qwert")
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .contentType(ContentType.JSON)
                .assertThat().statusCode(401)
                .assertThat().body("error", containsString("Unauthorized"))
                .extract().response().as(ErrorDto.class);
        System.out.println(respDto.toString());
    }

    @Test(enabled = false)//bug - add contact with duplicated fields
    public void AddNewContactNegativeDuplicateField() {
        int i = new Random().nextInt(1000) + 1000;
        ContactDto contact = createContact();
        addNewContact(contact);

        ContactDto contactTest = ContactDto.builder()
                .name(contact.getName())
                .lastName("lastNmae"+i)
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .address("address")
                .description("descr")
                .build();

        ErrorDto respDto = given()
                .header("Authorization", token)
                .body(contactTest)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .contentType(ContentType.JSON)
                .assertThat().statusCode(409)
                //.assertThat().body("message", containsString(""))
                .extract().response().as(ErrorDto.class);
        System.out.println(respDto.toString());;
    }


    private ContactDto createContact() {
        int i = new Random().nextInt(1000) + 1000;
        return ContactDto.builder()
                .name("name" + i)
                .lastName("lastName" + i)
                .email("email" + i + "@gmail.com")
                .phone("1234567890")
                .address("address")
                .description("descr")
                .build();
    }

    private boolean addNewContact(ContactDto contact) {
        boolean res = false;
        ErrorDto respDto = given()
                .header("Authorization", token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .extract().response().as(ErrorDto.class);
        System.out.println(respDto.toString());
        if (respDto.getStatus() == 200 &&
                respDto.getMessage().toString().contains("Contact was added!"))
            res = true;
        return res;
    }
}
