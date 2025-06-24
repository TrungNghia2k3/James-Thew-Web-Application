package com.ntn.culinary.filter;

import com.ntn.culinary.response.ApiResponse;
import com.ntn.culinary.service.JwtService;
import com.ntn.culinary.service.UserService;
import com.ntn.culinary.util.ResponseUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebFilter(urlPatterns = "/api/protected/*")
public class JwtFilter implements Filter {
    private static JwtService jwtService = new JwtService();
    private static UserService userService = new UserService();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setContentType("application/json");

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ResponseUtil.sendResponse(response, new ApiResponse<>(401, "Missing or invalid Authorization header"));
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            Claims claims = jwtService.validateJwt(jwt);

            Integer userId = claims.get("user_id", Integer.class);
            String username = claims.getSubject();

            List<?> rolesRaw = claims.get("roles", List.class);
            List<String> roles = rolesRaw != null ? rolesRaw.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString) // an toàn ép kiểu
                    .collect(Collectors.toList()) : Collections.emptyList();

            List<?> permissionsRaw = claims.get("permissions", List.class);
            List<String> permissions = permissionsRaw != null ? permissionsRaw.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .collect(Collectors.toList()) : Collections.emptyList();

            request.setAttribute("user_id", userId);
            request.setAttribute("username", username);
            request.setAttribute("roles", roles);
            request.setAttribute("permissions", permissions);

            // Nếu là MEMBER thì kiểm tra subscription
//            if (roles.contains("MEMBER")) {
//                boolean isSubscriptionValid = userService.isSubscriptionValid(userId);
//                if (!isSubscriptionValid) {
//                    ResponseUtil.sendResponse(response, new ApiResponse<>(403, "Subscription expired or user not found"));
//                    return;
//                }
//            }

            // Kiểm tra role có đủ quyền truy cập không
            if (!jwtService.hasRequiredRole(request, roles)) {
                ResponseUtil.sendResponse(response, new ApiResponse<>(403, "Insufficient role access"));
                return;
            }

            // (Optional) Nếu muốn kiểm tra permission:
            // if (!jwtService.hasPermission(claims, "MANAGE_CONTESTS")) {
            //     ResponseUtil.sendResponse(response, new ApiResponse<>(403, "Permission denied"));
            //     return;
            // }

            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
            ResponseUtil.sendResponse(response, new ApiResponse<>(401, "Token expired"));
        } catch (JwtException e) {
            ResponseUtil.sendResponse(response, new ApiResponse<>(401, "Invalid token"));
        } catch (Exception e) {
            ResponseUtil.sendResponse(response, new ApiResponse<>(500, "Server error: " + e.getMessage()));
        }
    }
}
