package com.register.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.CharacterEncodingFilter;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Profile("!test")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REMEMBER_ME = "remember-me";
    @Autowired
    PersistentTokenRepository tokenRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler = new CustomAuthenticationFailureHandler();

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("administrator").password("administrator").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/register**", "/login**", "/register/**", "/resetPassword**", "/api/**", "/greetings/**")
                .permitAll()
                // .antMatchers(HttpMethod.POST,"/api/**").hasAuthority("ADMIN")
                // .antMatchers(HttpMethod.PUT,"/api/**").hasAuthority("ADMIN")
                // .antMatchers(HttpMethod.DELETE,"/api/**").hasAuthority("ADMIN")
                .anyRequest().fullyAuthenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .failureHandler(authenticationFailureHandler)
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeParameter(REMEMBER_ME)
                .tokenRepository(tokenRepository)
                .tokenValiditySeconds(360000)
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies(REMEMBER_ME)
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf().disable().headers().frameOptions().disable();

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(REMEMBER_ME, userDetailsService, tokenRepository);
    }

    @Bean
    public Docket api() {
        // Dokumentacja do API jest dostępna pod adresem http://localhost:8080/swagger-ui.html
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(path -> path.startsWith("/api/"))
                .build();
    }

    @Bean
    public SimpleJaxWsServiceExporter simpleJaxWsServiceExporter() {
        SimpleJaxWsServiceExporter simpleJaxWsServiceExporter = new SimpleJaxWsServiceExporter();
        simpleJaxWsServiceExporter.setBaseAddress("http://localhost:8888/services/");
        return simpleJaxWsServiceExporter;
    }


}
