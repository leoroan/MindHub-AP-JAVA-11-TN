package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
/**
 * Spring Security usa el objeto WebSecurityConfigurerAdapter para configurar la autorización, es por ello que la clase
 * WebAuthorization hereda del mismo para sobrescribir el método configure indicando la nueva configuración de
 * autorización
 */
class WebAuthorization extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * Se debe tener en cuenta que el orden de las reglas es importante ya que en este caso si se recibe una
         * petición con la URL /data, se verifica primero contra el antMatcher(“/admin/**”) y si este no se cumple
         * entonces pasa al siguiente, por lo que se valida contra antMatcher(“/**”) y como se cumple entonces
         * Spring Security verificará que exista una sesión iniciada y que ese usuario tenga el rol USER para poder
         * acceder a /data.
         */
        http.authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/**").hasAuthority("USER");
        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/app/login");
        http.logout().logoutUrl("/app/logout");
    }
}