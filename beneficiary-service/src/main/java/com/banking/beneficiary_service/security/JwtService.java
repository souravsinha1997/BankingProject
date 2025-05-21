package com.banking.beneficiary_service.security;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JwtService {

	 private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	    private final String SECRET_KEY = "b33611cb858fa9c4f9e381e7f00dcc623285e0f3a3e01b4369347f910eff911b";

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public String extractRole(String token) {
	        return extractClaim(token, claims -> claims.get("role", String.class));
	    }


	    public boolean isValid(String token) {
	        try {
	            return !isTokenExpired(token);
	        } catch (ExpiredJwtException e) {
	            return false;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    private boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    private Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
	        Claims claims = extractAllClaims(token);
	        return resolver.apply(claims);
	    }

	    public int getCustomerId(String token) {
	        try {
	            int extractedId = extractClaim(token, claims -> claims.get("customerId", Integer.class));

	            if (extractedId <0) {
	                return 0;
	            }
	            return extractedId;
	        } catch (Exception e) {
	            return 0;
	        }
	    }
	    
	    private Claims extractAllClaims(String token) {
	        try {
	            if (token == null || token.trim().isEmpty()) {
	                throw new RuntimeException("JWT token is missing");
	            }

	            return Jwts.parserBuilder()
	                    .setSigningKey(getSigninKey())
	                    .build()
	                    .parseClaimsJws(token)
	                    .getBody();
	        } catch (Exception e) {
	            throw new RuntimeException("Invalid JWT token");
	        }
	    }

	    private SecretKey getSigninKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }
}


