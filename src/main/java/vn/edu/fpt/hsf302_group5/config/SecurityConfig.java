package vn.edu.fpt.hsf302_group5.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.util.AntPathMatcher;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.service.impl.user.CustomOAuth2UserService;
import vn.edu.fpt.hsf302_group5.service.user.CustomUserDetailsService;

@Configuration
@EnableWebSecurity // Kích hoạt cơ chế bảo mật của Spring Security , nên dùng cách method
@EnableMethodSecurity // bật phân quyền ở level phương thức với @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${remember-me.key}")
    private String rememberMeKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean //chịu trách nhiệm thực hiện quá trình xác thực (Authentication) thông tin đăng nhập từ cơ sở dữ liệu.
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(authenticationProvider(customUserDetailsService, passwordEncoder()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home/**", "/login", "/register", "/register-success", "/resend-verification", "/privacy-policy", "/register-recruiter", "/verify", "/forgot-password", "/reset-password", "/css/**", "/js/**", "/images/**", "/assets/**", "/api/load-administrator/**", "/do-login", "/verify-reset-password").permitAll() // Cho phép truy cập tài nguyên tĩnh và các trang không cần xác thực
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/do-login") // Post
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler(((request, response, exception) -> {
                            if (exception instanceof DisabledException) {
                                response.sendRedirect("/login?error=accountInactive");
                            } else if (exception instanceof UsernameNotFoundException) {
                                response.sendRedirect("/login?error=userNotFound");
                            } else {
                                response.sendRedirect("/login?error=badCredentials");
                            }
                        }))
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            String redirectUrl = "/";
                            for (var authority : authorities) {
                                if (authority.getAuthority().equals(UserRole.CANDIDATE.toString())) {
                                    redirectUrl = "/";
                                    break;
                                } else if (authority.getAuthority().equals(UserRole.RECRUITER.toString())) {
                                    redirectUrl = "/recruiter/company-profile";
                                    break;
                                } else if (authority.getAuthority().equals(UserRole.ADMIN.toString())) {
                                    redirectUrl = "/admin/dashboard";
                                    break;
                                }
                            }
                            response.sendRedirect(redirectUrl);
                        })
                )
                .oauth2Login((oauth) -> oauth
                        .loginPage("/login")
                        .redirectionEndpoint((redirection) -> redirection.baseUri("/login/oauth2/code/*")) // * có thể là gg hoặc github...
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        ) //Sau khi lấy được thông tin user từ Google đưa nó cho customOAuth2UserService xử lý
                        .successHandler((request, response, authentication) -> {
                            var authorities = authentication.getAuthorities();
                            String redirectUrl = "/";
                            for (var authority : authorities) {
                                if (authority.getAuthority().equals(UserRole.CANDIDATE.toString())) {
                                    redirectUrl = "/";
                                    break;
                                } else if (authority.getAuthority().equals(UserRole.RECRUITER.toString())) {
                                    redirectUrl = "/recruiter/company-profile";
                                    break;
                                } else if (authority.getAuthority().equals(UserRole.ADMIN.toString())) {
                                    redirectUrl = "/admin/dashboard";
                                    break;
                                }
                            }
                            response.sendRedirect(redirectUrl);
                        })
                )
                .rememberMe(httpSecurityRememberMeConfigurer -> {
                    httpSecurityRememberMeConfigurer.key(rememberMeKey);
                    httpSecurityRememberMeConfigurer.rememberMeParameter("remember-me");
                    httpSecurityRememberMeConfigurer.tokenValiditySeconds(60 * 60 * 24 * 30);
                })
                .logout(logout -> {
                            logout.logoutUrl("/logout"); // POST
                            logout.logoutSuccessUrl("/");
                            logout.clearAuthentication(true);
                        }
                )
                .build();
    }
}
