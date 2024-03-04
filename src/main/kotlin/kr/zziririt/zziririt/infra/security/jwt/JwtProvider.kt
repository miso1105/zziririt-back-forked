package kr.zziririt.zziririt.infra.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

@Component
class JwtProvider (
    @Value("\${auth.jwt.issuer}") private val issuer: String,
    @Value("\${auth.jwt.secret}") private val secret: String,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val accessTokenExpirationHour : Long,
    @Value("\${auth.jwt.accessTokenExpirationHour}") private val refreshTokenExpirationHour : Long,
) {

    companion object {
        private const val BEARER_TYPE = "bearer"
    }

    private val key: Key by lazy {
        val secretKey : String = secret
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey))
    }

    fun validateToken(jwt: String): Result<Jws<Claims>> {
        return kotlin.runCatching {
            val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
            Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt)

        }
    }
    fun generateJwtDto(email: String) : JwtDto {
        val now = Date().time
        val accessTokenExpiresIn = Date(now + accessTokenExpirationHour)

        val accessToken = Jwts.builder()
            .subject(email)
            .expiration(accessTokenExpiresIn)
            .signWith(key)
            .compact()

        val refreshToken = Jwts.builder()
            .expiration(Date(now + refreshTokenExpirationHour))
            .signWith(key)
            .compact()

        return JwtDto(
            grantType = BEARER_TYPE,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessTokenExpiresIn = accessTokenExpiresIn.time
        )
    }
}