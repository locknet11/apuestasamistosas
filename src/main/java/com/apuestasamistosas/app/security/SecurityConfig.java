package com.apuestasamistosas.app.security;

import com.apuestasamistosas.app.services.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@Configuration
@EnableAsync
@EnableScheduling
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("usuarioServicio")
    public UsuarioServicio usuarioServicio;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/css/*", "/js/*", "/images/*", "/**").permitAll()
                .and().formLogin()
                .loginPage("/user/login")
                .loginProcessingUrl("/user/logincheck")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/user/dashboard")
                .failureUrl("/user/login?error=error")
                .permitAll()
                .and().logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .addLogoutHandler(new HeaderWriterLogoutHandler(
                        new ClearSiteDataHeaderWriter(
                                ClearSiteDataHeaderWriter.Directive.CACHE,
                                ClearSiteDataHeaderWriter.Directive.COOKIES,
                                ClearSiteDataHeaderWriter.Directive.STORAGE)))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll().and().csrf().disable();

        http.sessionManagement().maximumSessions(1);

        /*  Si por alguna razon fallan los metodos POST hay que agregar CRFS.disable */
    }
}
