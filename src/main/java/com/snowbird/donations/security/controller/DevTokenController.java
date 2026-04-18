package com.snowbird.donations.security.controller;

import com.snowbird.donations.security.dto.DevTokenRequest;
import com.snowbird.donations.security.util.JwtTokenService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dev")
@RequiredArgsConstructor
@Profile({"local", "dev"})
public class DevTokenController {

    private final JwtTokenService jwtTokenService;

    @PostMapping("/token")
    public Map<String, Object> generateToken(@RequestBody DevTokenRequest request) {
        List<String> roles = request.getRoles() == null || request.getRoles().isEmpty()
                ? List.of("ROLE_USER")
                : request.getRoles();

        String token = jwtTokenService.generateToken(
                request.getUserId(),
                request.getName(),
                request.getEmail(),
                roles
        );

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("token", token);
        response.put("tokenType", "Bearer");
        response.put("userId", request.getUserId());
        response.put("roles", roles);
        return response;
    }
}
