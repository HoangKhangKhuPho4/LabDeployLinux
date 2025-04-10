package facebook;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.User;
import facebook4j.conf.ConfigurationBuilder;
import utils.ConstantsFacebook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// Import Gson để parse JSON
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RestFB {

    /**
     * Lấy access token từ Facebook thông qua code trả về khi người dùng xác thực.
     *
     * @param code Mã code được trả về từ Facebook sau khi người dùng cho phép ứng dụng.
     * @return Access token dưới dạng chuỗi.
     * @throws Exception Nếu không lấy được access token từ phản hồi của Facebook.
     */
    public static String getToken(final String code) throws Exception {
        // Mã hóa redirect_uri để đảm bảo URL được truyền đi đúng định dạng theo RFC 3986
        String encodedRedirectUri = URLEncoder.encode(ConstantsFacebook.FACEBOOK_REDIRECT_URL, "UTF-8");

        // Xây dựng URL hoàn chỉnh để lấy access token theo định dạng của Facebook
        String tokenUrl = String.format(ConstantsFacebook.FACEBOOK_LINK_GET_TOKEN,
                ConstantsFacebook.FACEBOOK_APP_ID,
                ConstantsFacebook.FACEBOOK_APP_SECRET,
                encodedRedirectUri,
                code);

        // Gửi yêu cầu GET đến tokenUrl
        URL url = new URL(tokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        // Đọc phản hồi từ Facebook
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder responseBuilder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            responseBuilder.append(inputLine);
        }
        in.close();
        String response = responseBuilder.toString();

        // In ra response để kiểm tra định dạng phản hồi (debug)
        System.out.println("Response từ Facebook token endpoint: " + response);

        String token = null;
        // Kiểm tra định dạng của response: nếu bắt đầu với '{' thì là JSON, ngược lại là URL-encoded.
        if (response.trim().startsWith("{")) {
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            if (jsonObject.has("access_token")) {
                token = jsonObject.get("access_token").getAsString();
            } else {
                throw new Exception("Không lấy được access token từ phản hồi JSON: " + response);
            }
        } else {
            // Xử lý response dạng URL-encoded: access_token=XXX&expires=YYY
            String[] params = response.split("&");
            for (String param : params) {
                if (param.startsWith("access_token=")) {
                    token = param.split("=")[1];
                    break;
                }
            }
            if (token == null) {
                throw new Exception("Không lấy được access token từ phản hồi: " + response);
            }
        }
        return token;
    }

    /**
     * Lấy thông tin người dùng từ Facebook với access token đã có.
     *
     * @param accessToken Access token đã lấy được từ phương thức getToken.
     * @return Đối tượng User của facebook4j chứa thông tin người dùng.
     * @throws Exception Nếu có lỗi trong quá trình lấy thông tin người dùng.
     */
    public static User getUserInfo(String accessToken) throws Exception {
        // Cấu hình facebook4j với các thông tin cần thiết, bao gồm app id, app secret và access token
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthAppId(ConstantsFacebook.FACEBOOK_APP_ID)
                .setOAuthAppSecret(ConstantsFacebook.FACEBOOK_APP_SECRET)
                .setOAuthAccessToken(accessToken);
        FacebookFactory ff = new FacebookFactory(cb.build());
        Facebook facebook = ff.getInstance();
        // Yêu cầu Facebook trả về các trường: id, name, email, picture
        return facebook.getMe(new Reading().fields("id", "name", "email", "picture"));
    }
}
