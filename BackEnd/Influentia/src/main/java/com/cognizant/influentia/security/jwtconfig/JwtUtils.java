package com.cognizant.influentia.security.jwtconfig;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.KeyGenerator;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
	
	public String generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
		keyGen.init(512);
		return keyGen.generateKey().toString();
	}
	
	private long expirationMS = 86400000;
	
	private Key getKey() throws NoSuchAlgorithmException {
		String secret = generateSecretKey();
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String generateToken(String username, String fullName) throws InvalidKeyException, NoSuchAlgorithmException {
		Date date = new Date();
		Date expiryDate = new Date(date.getTime() + expirationMS);
		
		return Jwts.builder()
				.claim("fullName", fullName)
				.setSubject(username)
				.setIssuedAt(date)
				.setExpiration(expiryDate)
				.signWith(getKey())
				.compact();
	}
	
	public String extractUsername(String token) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, NoSuchAlgorithmException {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public String extractFullName(String token) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, NoSuchAlgorithmException {
		return Jwts.parserBuilder()
				.setSigningKey(getKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("fullName", String.class);
	}
	
	public boolean validateToken(String token, UserDetails userDetails) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, NoSuchAlgorithmException {
		String username = extractUsername(token);
		
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) throws SignatureException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, NoSuchAlgorithmException {
        Date expirationDate = Jwts
        		.parserBuilder()
        		.setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expirationDate.before(new Date());
    }
}