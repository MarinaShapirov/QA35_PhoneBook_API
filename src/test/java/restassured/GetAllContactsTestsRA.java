package restassured;

import com.jayway.restassured.RestAssured;
import dto.ContactDto;
import dto.GetAllContactsDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetAllContactsTestsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2Njk2NTY1MDUsImlhdCI6MTY2OTA1NjUwNX0.gaIDbPL5-m8iEJ5yvGiGehZEcnXstL9bCcB2JYMnLQo";

    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void getAllContactsSuccess() {

        GetAllContactsDto contactsDto = given()
                .header("Authorization", token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDto.class);

        List<ContactDto> list = contactsDto.getContacts();
        for(ContactDto el:list) {
            System.out.println(el.getId());
            System.out.println("***********");
        }
    }

    @Test
    public void getAllContactsUnAuthorized() {

        given()
                .header("Authorization", "asdfg")
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(401);
    }
}
