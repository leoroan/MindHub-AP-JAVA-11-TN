package com.mindhub.homebanking.configurations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
@Configuration
/**
 * Spring Security usa el objeto WebSecurityConfigurerAdapter para configurar la
 * autorización, es por ello que la clase
 * WebAuthorization hereda del mismo para sobrescribir el método configure
 * indicando la nueva configuración de
 * autorización
 */
class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /**
         * Se debe tener en cuenta que el orden de las reglas es importante ya que en
         * este caso si se recibe una
         * petición con la URL /data, se verifica primero contra el
         * antMatcher(“/admin/**”) y si este no se cumple
         * entonces pasa al siguiente, por lo que se valida contra antMatcher(“/**”) y
         * como se cumple entonces
         * Spring Security verificará que exista una sesión iniciada y que ese usuario
         * tenga el rol USER para poder
         * acceder a /data.
         */
        http.authorizeRequests()
                .antMatchers("/web/**").permitAll()

                .antMatchers("/manager.html",
                        "/rest/**",
                        "/accounts/{id}",
                        "/h2-console/**").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.POST,
                        "/api/login",
                        "/api/logout",
                        "/api/clients").permitAll()

                .antMatchers(HttpMethod.POST,
                        "/transactions",
                        "/clients/current/accounts",
                        "/clients/current/cards").hasAnyAuthority("CLIENT", "ADMIN")

                .antMatchers(HttpMethod.GET,
                        "/**",
                        "/transactions",
                        "/api/clients/current/accounts",
                        "/api/clients/current/cards",
                        "/api/clients/current/Loans",
                        "/api/transactions").hasAnyAuthority("ADMIN", "CLIENT");


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF tokens

        http.csrf().disable();

        // disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling()
                .authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
        return http.build();

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}