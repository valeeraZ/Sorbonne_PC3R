package tools;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.util.Date;

import static tools.Constants.*;

public class JWTTokenUtils {
    private static final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(TOKEN_SECRET);
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(apiKeySecretBytes);

    public static String createToken(String id_user, String email) {
        long expiration = TOKEN_EXPIRATION;
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        String tokenPrefix = Jwts.builder()
                .setId(id_user)
                .setHeaderParam("type", TOKEN_TYPE)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setIssuer("News-PC3R")
                .setIssuedAt(createdDate)
                .setSubject(email)
                .setExpiration(expirationDate)
                .compact();
        return TOKEN_PREFIX + tokenPrefix;
    }

    public static String getIdUser(String token) throws SignatureException, ExpiredJwtException, MalformedJwtException{
        return getTokenBody(token).getId();
    }

    public static String getEmailByToken(String token) throws SignatureException, ExpiredJwtException, MalformedJwtException {
        return getTokenBody(token).getSubject();
    }

    private static Claims getTokenBody(String token) throws SignatureException, ExpiredJwtException, MalformedJwtException {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}