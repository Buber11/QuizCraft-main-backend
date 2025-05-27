package main.QuizCraft.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import main.QuizCraft.model.User;
import main.QuizCraft.response.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret.key}")
    private String secretKey;

    @Value("${security.jwt.expiration.time}")
    private long jwtExpiration;


    public String extractUsername(String token) {
        return extractClaim(token,
                Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return extractClaim(token,
                claims -> claims.get("user_id", Long.class));
    }

    public String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    public String extractToken(HttpServletRequest request, String tokenName){
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(e -> e.getName().equals(tokenName))
                .map(e -> e.getValue())
                .findAny()
                .orElse(null);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token,
                                UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public Cookie createJwtCookie(Map<String, Object> extraClaims, User user, String nameOfCookie) {
        String jwtToken = generateToken(extraClaims, (UserDetails) user);
        Cookie cookie = new Cookie(nameOfCookie, jwtToken);
        configureCookie(cookie);
        return cookie;
    }

    @Override
    public MessageResponse validateToken(HttpServletRequest request) {
        String token = extractToken(request,"jwt_token");
        Boolean isValid = ! isTokenExpired(token);
        return new MessageResponse(isValid.toString());
    }

    private void configureCookie(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(5 * 3600);
    }
}

