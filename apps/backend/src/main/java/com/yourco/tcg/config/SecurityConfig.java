package com.yourco.tcg.config;

/**
 * TODO (class):
 * - Define SecurityFilterChain bean.
 * - Public routes: catalog, auth. Authenticated: cart, orders. Admin: /admin/**.
 * - Configure OAuth2 login (Google, Discord) and register JwtAuthFilter.
 * - Stateless sessions, CSRF handling for API.
 *
 * Annotations / types: @Configuration, @EnableWebSecurity
 */
public class SecurityConfig {
}
