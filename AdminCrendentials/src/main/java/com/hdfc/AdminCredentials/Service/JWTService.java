package com.hdfc.AdminCredentials.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hdfc.AdminCredentials.Exception.HandleBadRequest;
import com.hdfc.AdminCredentials.Model.Admin;
import com.hdfc.AdminCredentials.Repository.AdminRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTService {
	
	private static final String SECRET = "f03434d154a068bc0549ac810c3d5802199323b60ebc9cd317854332e6ad80da";

	@Autowired
	private AdminRepository adminRepository;
	
	public String extractUsername(String token) {
		 
		 String name = null;
		 
		 try {
			name = extractClaim(token, Claims::getSubject);
		} catch (Exception e) {
			throw new HandleBadRequest("Invalid token");
		}
		 
	        return name;
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return  Jwts.parserBuilder()
		                .setSigningKey(getSignKey())
		                .build()
		                .parseClaimsJws(token)
		                .getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public Boolean validateToken(String token,UserDetails userDetails) {
	    	
	    	String username = null ;
	    	try {
	    		username = extractUsername(token);
			} catch (Exception e) {
				throw new HandleBadRequest("Invalid tiken");
			}
	    	
	        
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	
	
	public Map<String,String> generateJWTToken(String username) {
		
		Admin user = adminRepository.findByName(username);
		Map<String,Object> claims = new HashMap<>();
		Map<String,String> token = new HashMap<String, String>();
		token.put("Token", createJWTToken(claims,username));
		token.put("Role", user.getRole());
		token.put("BranchId", user.getBranchid().toString());
		return token;
	}

	private String createJWTToken(Map<String, Object> claims, String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+1000*24*60*60))
				.signWith(getSignKey(),SignatureAlgorithm.HS256)
				.compact();
	}

	private Key getSignKey() {
		byte[] keyBites = Decoders.BASE64.decode(SECRET);
		return  Keys.hmacShaKeyFor(keyBites);
		
	}

}
