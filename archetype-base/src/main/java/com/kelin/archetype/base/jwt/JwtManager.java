// Copyright 2019 Kelin Inc. All rights reserved.

package com.kelin.archetype.base.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * @author Kelin Tan
 */
@Component
@ConditionalOnExpression(value = "${jwt.enable}")
@RequiredArgsConstructor
public class JwtManager {
    private final JwtRSAKeyProvider rsaKeyProvider;

    private static final String ISSUER = "archetype.boot";
    private static final String AUDIENCE = "user";
    private static final int DEFAULT_EXPIRE_DAY = 30;

    public DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();

        return verifier.verify(token);
    }

    public String generateToken(String subject) {
        Algorithm algorithm = Algorithm.RSA256(rsaKeyProvider);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withAudience(AUDIENCE)
                .withExpiresAt(Date.from(Instant.now().plus(DEFAULT_EXPIRE_DAY, ChronoUnit.DAYS)))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);
    }
}
