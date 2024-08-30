package com.th.pm.security;


import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.th.pm.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long tokenLifeTimeInMillis;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }


    public String generateToken(User user, Long tokenLifeTime){
        Instant tokenExpiration = Instant.now().plusMillis(tokenLifeTime);
        return Jwts
                    .builder()
                    .claim("email", user.getEmail())
                    .subject(user.getId().toString())
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(tokenExpiration))
                    .signWith(getSigningKey(), Jwts.SIG.HS256)
                    .compact();
    }

    public Claims extractAllClaims(String jwtToken){
        try{
            return Jwts
                        .parser()
                        .decryptWith(getSigningKey())
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();
        }catch(Exception e){
            log.error("Error parsing jwt token");
            throw new IllegalArgumentException("Invalid Jwt token", e);
        }
    }

    public <T> T extractClaim (String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date getExpiration(String token){
        if (token == null) {
            return null;
        }
        return extractClaim(token, Claims::getExpiration);
    }
    
    public String getSubject(String token){
        if(token == null){
            return null;
        }
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token){
        Date now = Date.from(Instant.now());
        Date expiration = getExpiration(token);
        return now.before(expiration);
    }

    private String getEmail(String token){
        if(token == null){
            return null;
        }
        Claims claims = extractAllClaims(token);
        return claims.get("email").toString();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        return !isTokenExpired(token) && userDetails.getUsername().toString().equals(getEmail(token));
    }
}
