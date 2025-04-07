package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.GoogleAccount;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class GoogleUtils {
    public static String getToken(final String code) throws ClientProtocolException, IOException {
        System.out.println("Request token with parameters:");
        System.out.println("client_id: " + Constants.GOOGLE_CLIENT_ID);
        System.out.println("client_secret: " + Constants.GOOGLE_CLIENT_SECRET);
        System.out.println("redirect_uri: " + Constants.GOOGLE_REDIRECT_URI);
        System.out.println("code: " + code);
        System.out.println("grant_type: " + Constants.GOOGLE_GRANT_TYPE);

        String response = Request.Post(Constants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", Constants.GOOGLE_CLIENT_ID)
                        .add("client_secret", Constants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", Constants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", Constants.GOOGLE_GRANT_TYPE)
                        .build())
                .execute().returnContent().asString();
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
        System.out.println("Access Token: " + accessToken);
        return accessToken;
    }

    public static GoogleAccount getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = Constants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        GoogleAccount googlePojo = new Gson().fromJson(response, GoogleAccount.class);
        System.out.println(googlePojo);
        return googlePojo;
    }
}
