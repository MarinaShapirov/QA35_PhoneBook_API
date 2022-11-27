package okhttp3;

import config.Provider;
import dto.AuthReqDto;
import dto.AuthRespDto;
import dto.ErrorDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrTests {
    @Test
    public void registrnSuccess() throws IOException {
        int i  = new Random().nextInt(1000)+1000;
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonya"+ i + "@gmail.com")
                .password("Ssonya12345$")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
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
    public void registrnWrongEmail() throws IOException {
        int i  = new Random().nextInt(1000)+1000;
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonyagmail.com")
                .password("Ssonya12345$")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        //send request
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        System.out.println(responseDto.getError());
        Assert.assertEquals(responseDto.getStatus(), 400);
        Assert.assertEquals(responseDto.getError(), "Bad Request");
        Object errMsg = responseDto.getMessage();
        Assert.assertTrue(errMsg.toString().contains("must be a well-formed email address"));
    }

    @Test
    public void registrnWrongPsw() throws IOException {
        int i  = new Random().nextInt(1000)+1000;
        AuthReqDto auth = AuthReqDto.builder()
                .username("sonya@gmail.com")
                .password("Ssonya")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(auth), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        //send request
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(), 400);

        ErrorDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(), ErrorDto.class);
        System.out.println(responseDto.getError());
        Assert.assertEquals(responseDto.getStatus(), 400);
        Assert.assertEquals(responseDto.getError(), "Bad Request");
        Object errMsg = responseDto.getMessage();
        Assert.assertTrue(errMsg.toString().contains(" At least 8 characters; Must contain at least 1 uppercase letter"));
    }
}
