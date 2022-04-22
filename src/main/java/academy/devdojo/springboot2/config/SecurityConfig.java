package academy.devdojo.springboot2.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //O que estamos protegendo com Protocolo HTTP
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Todas requisições HTTP passam pela autorização
        http.csrf().disable() //CSRF ficara desabilitado apenas para facilitar no curso.
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //Permite que o Front nao seja obrigado a sempre enviar o HttpOnly como True
                //.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()//cada requisição precisa estar autenticada
                .and()
                .httpBasic();//forma de autenticação
        }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Criptografando a Senha/Password
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {}", passwordEncoder.encode("test"));

       //Criando Usuarios em Memória
        auth.inMemoryAuthentication()
                .withUser("gonini") //usuario
                .password(passwordEncoder.encode("teste123")) //senha criptografada
                .roles("USER", "ADMIN") //Grupos de Acesso
                .and()
                .withUser("devdojo") //usuario
                .password(passwordEncoder.encode("teste123")) //senha criptografada
                .roles("USER"); //Grupos de Acesso
    }

}
