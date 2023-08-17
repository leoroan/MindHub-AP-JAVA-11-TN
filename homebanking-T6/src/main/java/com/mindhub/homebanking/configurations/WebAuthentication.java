package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/***
 * GlobalAuthenticationConfigurerAdapter que es el objeto que utiliza el
 * Spring Security para saber cómo buscará los detalles del usuario. La
 * anotación @Configuation le indica a spring que debe crear un objeto de
 * este tipo cuando se está iniciando la aplicación para que cuando se
 * configure el módulo de spring utilice ese objeto ya creado.
 */
@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClientRepository clientRepository;

    /***
     * La clase WebAuthentication se extiende de GlobalAuthenticationConfigurerAdapter
     * para poder sobrescribir su método init. En este método se debe cambiar el servicio
     * de detalles de usuario por uno nuevo que implemente la búsqueda de los detalles
     * del usuario utilizando el repositorio de clientes, en otras palabras se indicará
     * a Spring que para esta aplicación los usuarios son clientes.
     */
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputMail -> {
            Client client = clientRepository.findByEmail(inputMail);
            if (client != null) {
                return new User(client.getEmail(), client.getPassword(),
                        AuthorityUtils.createAuthorityList("CLIENT"));
            } else {
                throw new UsernameNotFoundException("Unknown email: " + inputMail);
            }
        });
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

    }
}
