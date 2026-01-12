package com.example.urlshortner.controller;

import com.example.urlshortner.dto.request.RoleAssignRequest;
import com.example.urlshortner.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.urlshortner.UrlShortenerConstants.ROLE;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "admin")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users/{user}/roles")
    public ResponseEntity<String> assignRole(@RequestBody RoleAssignRequest request, @PathVariable String user) {
        String roleName = request.getRoleName();
        if (!roleName.startsWith(ROLE))      roleName = ROLE + roleName;
        adminService.assignRole(user, roleName);

        return ResponseEntity.ok("Assigned role successfully");
    }
}
