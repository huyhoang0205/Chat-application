package com.huyhoang25.chatapp.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.huyhoang25.chatapp.exception.AppException;
import com.huyhoang25.chatapp.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import static com.huyhoang25.chatapp.constant.AppConstant.AUTHORITIES;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateAccessToken(String userId, Set<String> authorities) {
        //fl jwsObject(header: {algorimth}, {payload: JWSclaimSet{subject, issueTime, ExpiredTime, JWTID - available / claim - custom }})
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(algorithm);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now().plus(2, ChronoUnit.HOURS).toEpochMilli());

        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet ClaimSet = new JWTClaimsSet.Builder()
        .subject(userId)
        .issueTime(issueTime)
        .expirationTime(expiredTime)
        .jwtID(jwtId)
        .claim(AUTHORITIES, authorities)
        .build();

        Payload payload = new Payload(ClaimSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }

        return jwsObject.serialize();
    }


    public String generateRefreshToken(String userId) {
        JWSAlgorithm algorithm = JWSAlgorithm.HS512;
        JWSHeader header = new JWSHeader(algorithm);

        Date issueTime = new Date();
        Date expiredTime = new Date(Instant.now().plus(2, ChronoUnit.DAYS).toEpochMilli());

        String jwtId = UUID.randomUUID().toString();

        JWTClaimsSet jwsClaimset = new JWTClaimsSet.Builder()
        .subject(userId)
        .issueTime(issueTime)
        .expirationTime(expiredTime)
        .jwtID(jwtId)
        .build();

        Payload payload = new Payload(jwsClaimset.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_GENERATION_FAILED);
        }

        return jwsObject.serialize();
    }
}
