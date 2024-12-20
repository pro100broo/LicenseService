package ru.mtuci.license_service.security;

import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.utils.LicenseServiceException;

import javax.crypto.SecretKey;


@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSigningKey;

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    public String generateToken(UserDetailsImpl userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole());

        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(HttpServletRequest request, UserDetailsImpl user) throws LicenseServiceException {
        final String userName = extractUserName(request);
        return (userName.equals(user.getUsername())) && !isTokenExpired(request);
    }

    private boolean isTokenExpired(HttpServletRequest request) throws LicenseServiceException {
        return extractExpiration(request).before(new Date());
    }

    public String extractUserName(HttpServletRequest request) throws LicenseServiceException {
        return extractClaim(extractToken(request), Claims::getSubject);
    }

    private Date extractExpiration(HttpServletRequest request) throws LicenseServiceException {
        return extractClaim(extractToken(request), Claims::getExpiration);
    }

    private String extractToken(HttpServletRequest request) throws LicenseServiceException {
        String authHeader = request.getHeader(HEADER_NAME);
        if (authHeader == null){
            throw new LicenseServiceException("Bearer token not specified");
        }
        return authHeader.substring(BEARER_PREFIX.length());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) throws LicenseServiceException {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }


    private Claims extractAllClaims(String token) throws LicenseServiceException {
        try {
            return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            throw new LicenseServiceException("Invalid token data");
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
