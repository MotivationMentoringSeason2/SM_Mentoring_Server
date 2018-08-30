package net.skhu.mentoring.config;

import net.skhu.mentoring.component.JwtTokenProvider;
import net.skhu.mentoring.component.SecurityAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityAuthenticationProvider securityAuthenticationProvider;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/AccountAPI/resource/**");
        web.ignoring().antMatchers(HttpMethod.GET, "/AccountAPI/guest/**");
        web.ignoring().antMatchers(HttpMethod.POST, "/AccountAPI/guest/**");
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/AccountAPI/admin/**").hasRole("ADMIN")
                .antMatchers("/AccountAPI/user/**").hasRole("USER")
                .antMatchers("/AccountAPI/common/**").hasAnyRole("ADMIN", "USER");

        http.exceptionHandling().accessDeniedPage("/UserAPI/auth/common/denied");

        http.apply(new JwtTokenFilterConfig(this.jwtTokenProvider));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.securityAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }
}

