package com.moneyflow.backend.security;
import javax.crypto.SecretKey; import javax.crypto.spec.SecretKeySpec; import java.nio.charset.StandardCharsets; import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.*; import org.springframework.security.config.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.oauth2.jwt.*; import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecretKey jwtKey(@Value("${app.jwt.secret}") String secret){return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256");}
 @Bean JwtEncoder jwtEncoder(SecretKey key){return new NimbusJwtEncoder(new ImmutableSecret<>(key));}
 @Bean JwtDecoder jwtDecoder(SecretKey key){return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();}
 @Bean SecurityFilterChain security(HttpSecurity h)throws Exception{return h.csrf(c->c.disable()).authorizeHttpRequests(a->a.requestMatchers("/api/health","/api/auth/**").permitAll().anyRequest().authenticated()).oauth2ResourceServer(o->o.jwt(Customizer.withDefaults())).build();}
}
