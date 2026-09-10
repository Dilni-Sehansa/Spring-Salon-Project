package com.example.Spring_Salon_Project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers(HttpMethod.POST, "/v1/test/login").permitAll()
////                        .requestMatchers(HttpMethod.GET,"/v1/test/users").hasAnyRole("CUSTOMER")
////                        .anyRequest().authenticated()
//
//                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                                .requestMatchers("/", "/login.html", "/singIn.html", "/dashboard.html","/user.html").permitAll()
//                                .requestMatchers(HttpMethod.POST, "/v1/user/login").permitAll()
//                                .requestMatchers("/v1/user/**").authenticated()
//                                .anyRequest().authenticated()

//                                .anyRequest().authenticated()
//                                .requestMatchers(HttpMethod.POST, "/v1/user/user_saved").permitAll()
//                                .requestMatchers("/*.html", "/css/**", "/js/**", "/images/**").permitAll()
////                                .requestMatchers("/v1/user/**").hasAnyRole("ADMIN", "STAFF")
//                                .requestMatchers("/v1/user/**").permitAll()
//                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/*.html", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                        .requestMatchers("/login.html", "/singIn.html", "/dashboard.html", "/user.html", "/verify-email.html").permitAll()


                        .requestMatchers("/favicon.ico").permitAll()

                        //-------------------------USER----------------------------------------
                        .requestMatchers(HttpMethod.POST, "/v1/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/v1/user/user_saved").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/v1/user/verify-email").permitAll()

                        //-------------------- Authenticated user endpoints ------------------------
                        .requestMatchers("/v1/user/**").authenticated()
                        .requestMatchers("/v1/notification/**").authenticated()

                        //--------------------Public read endpoints----------------------------------
                        .requestMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/service/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/staffService/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/staffSchedule/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/feedback/**").permitAll()

                        // -------------------- Customer + Staff + Admin ---------------------------------
                        .requestMatchers(HttpMethod.GET,  "/v1/customer/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PUT,  "/v1/customer/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET,  "/v1/product/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET,  "/v1/payment/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/v1/appointment/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET,  "/v1/appointment/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET,  "/v1/appointmentDetail/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.POST, "/v1/feedback/**").hasAnyRole("CUSTOMER", "ADMIN")


                        // ---------------------------- Staff + Admin --------------------------------------------
                        .requestMatchers("/v1/customer/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/product/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/payment/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.DELETE, "/v1/appointment/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.PATCH,  "/v1/appointment/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/appointmentDetail/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/v1/staffService/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/v1/staff/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers(HttpMethod.GET, "/v1/supplier/**").hasAnyRole("ADMIN", "STAFF")

                        //----------------------------------- Admin only -----------------------------------------------
                        .requestMatchers("/v1/category/**").hasRole("ADMIN")
                        .requestMatchers("/v1/service/**").hasRole("ADMIN")
                        .requestMatchers("/v1/staffSchedule/**").hasRole("ADMIN")
                        .requestMatchers("/v1/staff/**").hasRole("ADMIN")
                        .requestMatchers("/v1/feedback/**").hasRole("ADMIN")
                        .requestMatchers("/v1/auditLog/**").hasRole("ADMIN")
                        .requestMatchers("/v1/supplier/**").hasRole("ADMIN")

                          //-------------------------CUSTOMER----------------------------------------

//                        .requestMatchers(HttpMethod.GET, "/v1/customer/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.PUT, "/v1/customer/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers("/v1/customer/**").hasAnyRole("ADMIN", "STAFF")
//
//                         //-------------------------CATEGORY----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
//                        .requestMatchers("/v1/category/**").hasAnyRole("ADMIN", "STAFF")
//
//                                //-------------------------SERVICE----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/service/**").permitAll()
//                        .requestMatchers("/v1/service/**").hasAnyRole("ADMIN", "STAFF")
//
//                                //-------------------------STAFF SERVICE----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/staffService/**").permitAll()
//                        .requestMatchers("/v1/staffService/**").hasAnyRole("ADMIN", "STAFF")
//
//                                //-------------------------STAFF SCHEDULE----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/staffSchedule/**").permitAll()
//                        .requestMatchers("/v1/staffSchedule/**").hasAnyRole("ADMIN")
//
//
//
//                                //-------------------------SERVICE----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/service/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/v1/service/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.PUT, "/v1/service/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.PATCH, "/v1/service/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers("/v1/service/**").hasAnyRole("ADMIN")
//
//                                //-------------------------CATEGORY----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/category/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/v1/category/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.PUT, "/v1/category/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.PATCH, "/v1/category/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers("/v1/category/**").hasAnyRole("ADMIN")
//
//                        //-------------------------PRODUCT----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/product/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers("/v1/product/**").hasAnyRole("ADMIN", "STAFF")
//
//                        //-------------------------PAYMENT----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/payment/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers("/v1/payment/**").hasAnyRole("ADMIN", "STAFF")
//
//                        //-------------------------APPOINTMENT----------------------------------------
//                        .requestMatchers(HttpMethod.POST, "/v1/appointment/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.GET, "/v1/appointment/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/appointment/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers(HttpMethod.PATCH, "/v1/appointment/**").hasAnyRole("ADMIN", "STAFF")
//
//                        //-------------------------APPOINTMENT DETAILS----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/appointmentDetail/**").hasAnyRole("CUSTOMER", "ADMIN", "STAFF")
//                        .requestMatchers("/v1/appointmentDetail/**").hasAnyRole("ADMIN", "STAFF")
//
//                        //-------------------------NOTIFICATION----------------------------------------
//                        .requestMatchers("/v1/notification/**").authenticated()
//
//                        //-------------------------STAFF----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/staff/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers("/v1/staff/**").hasRole("ADMIN")
//
//
//                        //-------------------------FEEDBACK----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/feedback/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/v1/feedback/**").hasAnyRole("ADMIN", "CUSTOMER")
//                        .requestMatchers("/v1/feedback/**").hasAnyRole("ADMIN")
//
//                        //-------------------------NOTIFICATION----------------------------------------
//                        .requestMatchers("/v1/notification/**").authenticated()
//
//                        //-------------------------AUDIT LOG----------------------------------------
//                        .requestMatchers("/v1/auditLog/**").hasRole("ADMIN")
//
//                        //-------------------------SUPPLIER----------------------------------------
//                        .requestMatchers(HttpMethod.GET, "/v1/supplier/**").hasAnyRole("ADMIN", "STAFF")
//                        .requestMatchers("/v1/supplier/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
