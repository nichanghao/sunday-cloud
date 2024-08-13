package net.sunday.cloud.system.util;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import net.sunday.cloud.base.common.exception.BusinessException;
import net.sunday.cloud.system.exception.SystemRespCodeEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    /**
     * JWT 密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT 过期时间，单位：小时
     */
    @Getter
    @Value("${jwt.expiration:24}")
    private Integer expiration;

    // 生成JWT令牌
    public String generateToken(String subject, Map<String, Object> claims) {
        Date now = new Date();

        return Jwts.builder()
                .claims()
                .add(claims == null ? Collections.emptyMap() : claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(DateUtil.offsetHour(now, expiration))
                .and().signWith(getKey()).compact();
    }

    // 从JWT中提取 subject
    public String extractSubject(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    // 从JWT中提取Claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 提取所有Claims
    private Claims extractAllClaims(String token) {

        return parseToken(token).getPayload();
    }

    // 检查JWT是否过期
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 提取JWT的过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 解析JWT并返回Jws对象 (包含 header, body, signature)
    public Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(SystemRespCodeEnum.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new BusinessException(SystemRespCodeEnum.TOKEN_INVALID);
        }
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
