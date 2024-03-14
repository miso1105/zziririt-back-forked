package kr.zziririt.zziririt.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*

@Component
class JwtProvider(
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour: Long,
    @Value("\${auth.jwt.refreshTokenExpirationHour}") private val refreshTokenExpirationHour: Long,
) {

    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)
        }
    }

    fun generateAccessToken(subject: Long, email: String, providerId: String, role: String, status: String): String {
        return generateToken(subject, email, providerId, role, status, Duration.ofHours(accessTokenExpirationHour))
    }

    fun generateToken(
        subject: Long,
        email: String,
        providerId: String,
        role: String,
        status: String,
        expirationPeriod: Duration
    ): String {
        val claims: Claims = Jwts.claims()
            .add(mapOf("role" to role, "email" to email, "providerId" to providerId, "status" to status))
            .build()

        val now = Instant.now()
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

        return Jwts.builder()
            .subject(subject.toString())
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expirationPeriod)))
            .claims(claims)
            .signWith(key)
            .compact()

    }
}