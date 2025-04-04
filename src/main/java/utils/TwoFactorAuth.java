package utils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

public class TwoFactorAuth {

    private static final GoogleAuthenticator gAuth = new GoogleAuthenticator();

    // Tạo secret key mới cho người dùng
    public static String generateSecretKey() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    // Tạo URL mã QR theo chuẩn otpauth cho người dùng quét với ứng dụng Authenticator
    public static String getQRCodeUrl(String secretKey, String userEmail, String appName) {
        return "otpauth://totp/" + appName + ":" + userEmail + "?secret="
                + secretKey + "&issuer=" + appName;
    }

    // Xác thực OTP nhập vào với secret key của người dùng
    public static boolean verifyOTP(String secretKey, String userOTP) {
        try {
            int otp = Integer.parseInt(userOTP);
            return gAuth.authorize(secretKey, otp);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}