package ch.teko.ft.task_management_backend.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import ch.teko.ft.task_management_backend.entity.User;
import ch.teko.ft.task_management_backend.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtil {

  private final UserRepository userRepository;

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String userName = extractUserName(token);
    return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 604800000))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public User getLoggedInUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      User user = (User) authentication.getPrincipal();
      Optional<User> userOptional = userRepository.findById(user.getId());
      return userOptional.orElse(null);
    }
    return null;
  }
}