package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactDto;
import dto.ContactRespDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class DeleteContactByIdRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWExNDEwQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjcwMTcyMTM4LCJpYXQiOjE2Njk1NzIxMzh9.7G0pBIxT_LRp89dyr7_ZnD7oBbH6A1gfC90wIZFCx-0";
    String id;

    @BeforeMethod
    public void addNewContact() throws IOException {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        int i = new Random().nextInt(1000) + 1000;
        ContactDto contact = ContactDto.builder()
                .name("name" + i)
                .lastName("lastName" + i)
                .email("email" + i + "@gmail.com")
                .phone("1234567890")
                .address("address")
                .description("descr")
                .build();

        ContactRespDto respDto = given()
                                .header("Authorization", token)
                                .body(contact)
                                .contentType(ContentType.JSON)
                                .when()
                                .post("contacts")
                                .then()
                                .assertThat().statusCode(200)
                                .extract().response().as(ContactRespDto.class);
        id = respDto.getMessage().split("ID: ")[1];
        System.out.println("ID: "+ id);

    }

    @Test
    public void deleteContactById(){
        given()
                .header("Authorization", token)
                .when()
                .delete("contacts/" + id)
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString("Contact was deleted"));

    }
    @Test
    public void deleteContactByWrongId(){

        ErrorDto respDto = given()
                .header("Authorization", token)
                .when()
                .delete("contacts/" + id+100)
                .then()
                .contentType(ContentType.JSON)
                .assertThat().statusCode(400)
                .assertThat().body("status", equalTo(400))
                .extract().response().as(ErrorDto.class);
        System.out.println(respDto.toString());
        Assert.assertTrue(respDto.getMessage().toString().contains(id+100+" not found in your contacts!"));


    }
    @Test
    public void deleteContactByIdUnauthorized(){
        given()
                .header("Authorization", "qwerty")
                .when()
                .delete("contacts/" + id)
                .then()
                .contentType(ContentType.JSON)
                .assertThat().statusCode(401)
                .assertThat().body("status", equalTo(401))
                .assertThat().body("error", containsString("Unauthorized"));
    }
}
