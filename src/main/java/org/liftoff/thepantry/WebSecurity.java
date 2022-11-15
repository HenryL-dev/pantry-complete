package org.liftoff.thepantry;

import org.liftoff.thepantry.data.UserRepository;
import org.liftoff.thepantry.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public WebSecurity(@Qualifier("userService") UserService userDetailsService,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http
                .cors().and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(SecurityConstants.H2_CONSOLE)
                .permitAll()
                .anyRequest().authenticated().and()

                // User Authentication with custom login URL path
                .addFilter(getAuthenticationFilter(authenticationManager))

                // User Authorization with JWT
                .addFilter(new AuthorizationFilter(authenticationManager, userRepository))
                .authenticationManager(authenticationManager)

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.headers().frameOptions().disable();

        return http.build();
    }

    // Configure custom login URL path
    protected AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager);
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
