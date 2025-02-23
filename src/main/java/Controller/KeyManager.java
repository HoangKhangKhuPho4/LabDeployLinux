package Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class KeyManager {
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

    public static void savePrivateKeyToFile(PrivateKey privateKey, String filePath) throws IOException {
        String privateKeyEncoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        Files.write(Paths.get(filePath), privateKeyEncoded.getBytes());
    }


}

