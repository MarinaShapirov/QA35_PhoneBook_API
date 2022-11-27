package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.ContactRespDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

public class DeleteAllContactsRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWExNDEwQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjcwMTcyMTM4LCJpYXQiOjE2Njk1NzIxMzh9.7G0pBIxT_LRp89dyr7_ZnD7oBbH6A1gfC90wIZFCx-0";
    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void deleteAllContactsSuccess() {
        ContactRespDto respDto = given()
                        .header("Authorization", token)
                        .when()
                        .delete("contacts/clear")
                        .then()
                        .assertThat().statusCode(200)
                        .extract().response().as(ContactRespDto.class);
        System.out.println(respDto.getMessage());
        Assert.assertEquals(respDto.getMessage(), "All contacts was deleted!");

    }

    @Test
    public void deleteAllContactsUnauthorized() {
        ErrorDto respDto = given()
                .header("Authorization", "qwerty")
                .when()
                .delete("contacts/clear")
                .then()
                .assertThat().statusCode(401)
                .extract().response().as(ErrorDto.class);
        System.out.println(respDto.toString());
        Assert.assertEquals(respDto.getStatus(), 401);
        Assert.assertEquals(respDto.getError(), "Unauthorized");

    }
}
