package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.ContactRespDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactById {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2Njk2NTY1MDUsImlhdCI6MTY2OTA1NjUwNX0.gaIDbPL5-m8iEJ5yvGiGehZEcnXstL9bCcB2JYMnLQo";
    String id;
    @BeforeMethod
    public void addNewContact() throws IOException {
        int i  = new Random().nextInt(1000)+1000;
        ContactDto contact = ContactDto.builder()
                .name("name"+i)
                .lastName("lastName"+i)
                .email("email"+i +"@gmail.com")
                .phone("1234567890")
                .address("address")
                .description("descr")
                .build();

        RequestBody body = RequestBody.create(Provider.getInstance().getGson().toJson(contact), Provider.getInstance().getJson());
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body)
                .build();
        //send request
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        ContactRespDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(), ContactRespDto.class);
        System.out.println(responseDto.getMessage());

        Assert.assertTrue(responseDto.getMessage().contains("Contact was added"));
        String msg = responseDto.getMessage();
        String [] arr = msg .split("ID: ");
        String id = arr[1];
        System.out.println(id);
    }

    @Test
    public void deleteContactById() throws IOException {

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .addHeader("Authorization", token)
                .delete()
                .build();
        //send request
        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());

        ContactRespDto responseDto = Provider.getInstance().getGson().fromJson(response.body().string(), ContactRespDto.class);
        System.out.println(responseDto.getMessage());
        Assert.assertEquals(responseDto.getMessage(), "Contact was deleted");

    }
}
