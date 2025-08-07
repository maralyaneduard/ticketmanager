package com.example.ticketmanagement.api.configuration.security;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Map;

public class JweUtils {
    private static final String SECRET = "12345678901234567890123456789012";

    public static String generateToken(String userId, String role) throws JOSEException {
        byte[] secretKey = SECRET.getBytes(StandardCharsets.UTF_8);
        if (secretKey.length != 32) {
            throw new IllegalArgumentException("SECRET must be exactly 32 bytes for A256GCM encryption");
        }

        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .claim("userId", userId)
                .claim("role", role)
                .build();

        Payload payload = new Payload(claims.toJSONObject());
        JWEObject jwe = new JWEObject(header, payload);
        jwe.encrypt(new DirectEncrypter(secretKey));

        return jwe.serialize();
    }

    public static Map<String, Object> decrypt(String token) throws ParseException, JOSEException {
        JWEObject jwe = JWEObject.parse(token);
        jwe.decrypt(new DirectDecrypter(SECRET.getBytes()));
        return jwe.getPayload().toJSONObject();
    }
}
