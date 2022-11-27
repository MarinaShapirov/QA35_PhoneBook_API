package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthReqDto;
import dto.ContactDto;
import dto.ContactRespDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class UpdateContactRA {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWExNDEwQGdtYWlsLmNvbSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjcwMTcyMTM4LCJpYXQiOjE2Njk1NzIxMzh9.7G0pBIxT_LRp89dyr7_ZnD7oBbH6A1gfC90wIZFCx-0";
    ContactDto contact;
    String id;

    @BeforeMethod
    public void setUp(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";

        ContactDto contact = createContact();
        addNewContact(contact);
    }

    @Test(enabled = false)
    public void UpdateContactSuccess(){
        ContactDto updContact = contact;
        updContact.setId(id);
        updContact.setName("NewName");

        given()
                .body(updContact)
                .contentType(ContentType.JSON)
                .when()
                .put("contact")
                .then()
                .assertThat().statusCode(200)
                .assertThat().body("message", containsString(""));

    }

    private ContactDto createContact(){
        int i  = new Random().nextInt(1000)+1000;
        return ContactDto.builder()
                .name("name"+i)
                .lastName("lastName"+i)
                .email("email"+i +"@gmail.com")
                .phone("1234567890")
                .address("address")
                .description("descr")
                .build();
    }
    private boolean addNewContact(ContactDto contact){
        boolean res = false;
        ErrorDto respDto = given()
                .header("Authorization", token)
                .body(contact)
                .contentType(ContentType.JSON)
                .when()
                .post("contacts")
                .then()
                .extract().response().as(ErrorDto.class);
        if(respDto.getStatus() == 200 &&
                respDto.getMessage().equals("Contact was added"))
        {res = true;}

        id = respDto.getMessage().toString().split("ID: ")[1];
        System.out.println("ID: "+ id);

        return res;
    }

}
