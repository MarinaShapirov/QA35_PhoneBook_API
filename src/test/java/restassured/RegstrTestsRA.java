package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthReqDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RegstrTestsRA {
    @BeforeMethod
    public void setUp() {
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }
    @Test
    public void registrSuccess(){
        int i  = new Random().nextInt(1000)+1000;
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonya"+ +i+"@gmail.com")
                .password("Ssonya12345$")
                .build();

        String token = given()
                        .body(auth)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("user/registration/usernamepassword")
                        .then()
                        .assertThat().statusCode(200)
                        .extract().path("token");
        System.out.println(token);
    }

    @Test
    public void registrWrongUserName(){
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonyagmail.com")
                .password("Ssonya12345$")
                .build();

       given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post("user/registration/usernamepassword")
                .then()
                .assertThat().statusCode(400)
               .assertThat().body("message.username", containsString("must be a well-formed email address"));

    }
}
