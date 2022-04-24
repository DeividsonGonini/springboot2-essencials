package academy.devdojo.springboot2.config;

import academy.devdojo.springboot2.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true) //Habilita o PreAuthorize utilizado no controller
@RequiredArgsConstructor
@SuppressWarnings("java:S5344")//Desconsiderando Warnings
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final DevDojoUserDetailsService devDojoUserDetailsService;

    /*
    *** Filtros do Security ***
    BasicAuthenticationFilter - Token basic em Base64
    UsernamePasswordFilter
    DefaultLoginPageGeneratingFilter - Tele de login
    DefaultLogoutPageGeneratingFilter
    FilterSecurityInterceptor - checa se esta autorizado
    Authentication -> Authorization
     */

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
                .formLogin()//exige que seja preenchido um formulario para logar
                .and()
                .httpBasic();//forma de autenticação
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //Criptografando a Senha/Password
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {}", passwordEncoder.encode("teste123"));

        //Autentica por usuarios em memoria
        //Criando Usuarios em Memória
        auth.inMemoryAuthentication()
                .withUser("gonini2") //usuario
                .password(passwordEncoder.encode("teste123")) //senha criptografada
                .roles("USER", "ADMIN") //Grupos de Acesso
                .and()
                .withUser("devdojo2") //usuario
                .password(passwordEncoder.encode("teste123")) //senha criptografada
                .roles("USER"); //Grupos de Acesso

        //Autentica por usuarios no banco de dados
        auth.userDetailsService(devDojoUserDetailsService).passwordEncoder(passwordEncoder);
    }
}
