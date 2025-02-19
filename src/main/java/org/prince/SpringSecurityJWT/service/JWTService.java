package org.prince.SpringSecurityJWT.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

@Service
public class JWTService {

    private static final Logger logger = Logger.getLogger(JWTService.class.getName());
    private String secretKey = "";

    // Constructor to generate secret key
    public JWTService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            logger.severe("Failed to generate secret key: " + e.getMessage());
        }
    }

    // Token generation methods
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 600 * 100 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    // Key generation method
    private SecretKey getKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    public String extractUsername(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    // Token extraction methods
//    public String extractUsername(String token) {
//        logger.info("Extracting username from token...");
//        try {
//            String username = extractClaim(token, Claims::getSubject);
//            logger.info("Username extracted successfully: " + username);
//            return username;
//        } catch (JwtException e) {
//            logger.log(Level.SEVERE, "Failed to extract username: " + e.getMessage(), e);
//            return null;
//        }
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        logger.info("Extracting claim from token...");
//        final Claims claims = extractAllClaims(token);
//        if (claims == null) {
//            logger.severe("Claims are null. Token is invalid.");
//            throw new JwtException("Invalid token");
//        }
//        logger.info("Claims extracted successfully.");
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        logger.info("Parsing token to extract all claims...");
//        try {
//            Jws<Claims> jws = Jwts.parser()
//                    .verifyWith(getKey())
//                    .build()
//                    .parseSignedClaims(token);
//            logger.info("Token parsed successfully. Claims extracted.");
//            return jws.getPayload();
//        } catch (JwtException e) {
//            logger.log(Level.SEVERE, "Token parsing failed: " + e.getMessage(), e);
//            throw new JwtException("Token parsing failed: " + e.getMessage());
//        }
//    }

//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//
//    }
//
//    private Claims extractAllClaims(String token) {
//        try {
//            return Jwts.parser()
//                    .setSigningKey(getKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (JwtException e) {
//            return null;    // if token is invalid
//        }
//    }

//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(getKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }

    // Token validation methods
    public boolean validateToken(String token, UserDetails userDetails) {
//        System.out.println("Token:00 " + token);
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
