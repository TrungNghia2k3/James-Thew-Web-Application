package com.filter;

import com.response.ApiResponse;
import com.google.gson.Gson;
import com.service.JwtService;
import com.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/api/protected/*")
public class JwtFilter implements Filter {
    private static final Gson gson = new Gson();
    private final JwtService jwtService;
    private final UserService userService;

    public JwtFilter() {
        this.jwtService = new JwtService();
        this.userService = new UserService();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json");

        // Extract Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendResponse(response, new ApiResponse<>(401, "Missing or invalid Authorization header"));
            return;
        }

        // Extract JWT
        String jwt = authHeader.substring(7);
        try {
            // Validate JWT
            Claims claims = jwtService.validateJwt(jwt);

            // Set user details in request
            request.setAttribute("user_id", claims.get("user_id", Integer.class));
            request.setAttribute("username", claims.getSubject());
            request.setAttribute("role", claims.get("role", String.class));

            // Check subscription for members
            if ("member".equals(claims.get("role"))) {
                boolean isSubscriptionValid = userService.isSubscriptionValid(claims.get("user_id", Integer.class));
                if (!isSubscriptionValid) {
                    sendResponse(response, new ApiResponse<>(403, "Subscription expired or user not found"));
                    return;
                }
            }

            // Check role-based access
            if (!jwtService.hasRequiredRole(request, claims.get("role", String.class))) {
                sendResponse(response, new ApiResponse<>(403, "Insufficient permissions"));
                return;
            }

            // Proceed to the servlet
            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            sendResponse(response, new ApiResponse<>(401, "Token expired"));
        } catch (JwtException e) {
            sendResponse(response, new ApiResponse<>(401, "Invalid token"));
        } catch (Exception e) {
            sendResponse(response, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }

    private void sendResponse(HttpServletResponse response, ApiResponse<?> apiResponse) throws IOException {
        response.setStatus(apiResponse.getStatus());
        response.getWriter().write(gson.toJson(apiResponse));
    }
}