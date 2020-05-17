package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/processlogin")
                .usernameParameter("correo")
                .defaultSuccessUrl("/redirectByRol", true);

        http.logout().logoutSuccessUrl("/artesano/inventario")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
        ;

        http.authorizeRequests().antMatchers("/categoria","/categoria/**").hasAuthority("Administrador");
         http.authorizeRequests().antMatchers("/comunidad","/comunidad/**").hasAnyAuthority("Administrador","Gestor principal");
         http.authorizeRequests().anyRequest().permitAll();
    }

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select correo, contrasena, enable from usuario where correo = ?")
                .authoritiesByUsernameQuery("select u.correo, r.nombre " +
                                            "from usuario u inner join rol r on r.idrol=u.rol_idrol" +
                                            " where u.correo=? and u.enable=1");
     }
}
