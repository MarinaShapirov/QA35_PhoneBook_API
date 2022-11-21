package okhttp3;

import config.Provider;
import dto.ContactDto;
import dto.GetAllContactsDto;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContacts {
    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic29ueWFAZ21haWwuY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2Njk2NTY1MDUsImlhdCI6MTY2OTA1NjUwNX0.gaIDbPL5-m8iEJ5yvGiGehZEcnXstL9bCcB2JYMnLQo";
    @Test
    public void getAllContacts() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .get()
                .build();

        Response response = Provider.getInstance().getClient().newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        GetAllContactsDto contactsDto = Provider.getInstance().getGson().fromJson(response.body().string(), GetAllContactsDto.class);
        List<ContactDto> list = contactsDto.getContacts();
        for(ContactDto el:list) {
            System.out.println(el.getId());
            System.out.println("***********");
        }
    }
}
