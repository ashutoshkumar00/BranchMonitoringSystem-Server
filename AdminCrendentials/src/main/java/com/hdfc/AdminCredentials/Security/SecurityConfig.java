package com.hdfc.AdminCredentials.Security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.hdfc.AdminCredentials.Filter.JWTAuthFilter;
import com.hdfc.AdminCredentials.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

	@Autowired
	private JWTAuthFilter jwtAuthFilter;
	
	//Authentication
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserService();
	}
	
	
	
	//Authorization
	//@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		
		
		//System.out.println("in securityconfig");
		return 	http.csrf(csrf -> csrf.disable()).cors().disable()
//				.cors().configurationSource(new CorsConfigurationSource() {
//			
//					@Override
//					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//						CorsConfiguration cfg = new CorsConfiguration();
//						cfg.setAllowedOrigins(Arrays.asList("https://branch-monitoring-system.vercel.app"));
//						//cfg.setAllowedMethods(Collections.singletonList("*"));
//						cfg.setAllowCredentials(true);
//						cfg.setExposedHeaders(Arrays.asList("Authorization"));
//						return cfg;
//					}
//				})
//				.and()
                .authorizeHttpRequests((authorizeHttpRequests) ->authorizeHttpRequests
                .requestMatchers("/admin/getalladmin","/admin/deleteadmin/**","/admin/validatetoken").authenticated()
                .requestMatchers("/admin/welcome", "/admin/authenticate", "/admin/addadmin").permitAll())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public AuthenticationProvider authenticationProvider(){
		
		//we need to pass who is UserDetails service and what is password encoder, to authenticationProvider.
		//then authenticationProvider will create authentication object.
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return authenticationProvider;
    }
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
//	@Bean
//	public WebSecurityCustomizer webSecurityCustomizer() {
//	    return (web) -> web.ignoring().requestMatchers("/admin/welcome", "/admin/authenticate", "/admin/addadmin");
//	}
	
}
