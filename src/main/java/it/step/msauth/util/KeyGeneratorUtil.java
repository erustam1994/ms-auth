package it.step.msauth.util;

import org.springframework.stereotype.Component;

import java.security.*;

@Component
public class KeyGeneratorUtil {

    private final KeyPair keyPair;

    public KeyGeneratorUtil() throws NoSuchAlgorithmException {
        this.keyPair = getKeyPair();
    }

    private KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }


}
