package service;

import model.GoogleAccount;
import utils.GoogleUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class GoogleCallbackService {
    public void processCallback(String code, HttpServletRequest request) throws IOException {
        // Lấy access token và thông tin người dùng từ Google
        String accessToken = GoogleUtils.getToken(code);
        GoogleAccount googlePojo = GoogleUtils.getUserInfo(accessToken);

        // Lưu thông tin vào request attribute (hoặc xử lý lưu vào DB nếu cần)
        request.setAttribute("id", googlePojo.getId());
        request.setAttribute("email", googlePojo.getEmail());
        request.setAttribute("verified_email", googlePojo.isVerified_email());
        request.setAttribute("name", googlePojo.getName());
        request.setAttribute("given_name", googlePojo.getGiven_name());
        request.setAttribute("family_name", googlePojo.getFamily_name());
        request.setAttribute("link", googlePojo.getLink());
        request.setAttribute("picture", googlePojo.getPicture());

    }
}

