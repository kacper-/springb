package com.km;

import com.km.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {

    private static final UserDetails[] USERS = {
            User.withUsername("user").password("{noop}user").roles("USER").build(),
            User.withUsername("admin").password("{noop}admin").roles("ADMIN").build()
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher("/stats/**");
        http.authorizeHttpRequests(auth -> auth.requestMatchers(matcher).anonymous());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
    public UserDetailsManager userDetailsService() {
        return new InMemoryUserDetailsManager(USERS);
    }
}
