package okhttp3;

import com.google.gson.Gson;
import dto.AuthReqDto;
import dto.AuthRespDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import config.Provider;

import java.io.IOException;

public class LoginTests {

    @Test
    public void loginSuccess() throws IOException {
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonya@gmail.com")
                .password("Ssonya12345$")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();
        //send request
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AuthRespDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(), AuthRespDto.class);
        System.out.println(responseDto.getToken());
    }

    @Test
    public void loginWrongPsw() throws IOException {
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonyagmail.com")
                .password("Ssonya12345$")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body)
                .build();
        //send request
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 401);

        ErrorDto errDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        Object message = errDto.getMessage();
        Assert.assertEquals(message, "Login or Password incorrect");
        Assert.assertEquals(errDto.getStatus(), 401);

    }
}
