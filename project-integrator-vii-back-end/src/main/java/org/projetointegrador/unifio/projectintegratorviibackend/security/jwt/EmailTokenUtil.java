package org.projetointegrador.unifio.projectintegratorviibackend.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class EmailTokenUtil {
    private final static long EXPIRATION_TIME = 8640000;
    private final static SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateValidationToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    public boolean validateEmailToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

//   private Date extractExpiration(String token) {
//        JwtParser jwtParser = Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build();
//        return jwtParser.parseClaimsJwt(token)
//                .getBody()
//                .getExpiration();
//    }

    //    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }

//    public boolean validateEmailToken(String token) {
//        return !isTokenExpired(token);
//    }

//    public String extractEmail(String token) {
//        JwtParser jwtParser = Jwts.parserBuilder()
//                .setSigningKey(SECRET_KEY)
//                .build();
//        return jwtParser.parseClaimsJwt(token)
//                .getBody()
//                .getSubject();
//    }


}
