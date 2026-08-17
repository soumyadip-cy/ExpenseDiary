package org.soumyadip.expensediary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.soumyadip.expensediary.dto.*;
import org.soumyadip.expensediary.entity.ImplementedUserDetails;
import org.soumyadip.expensediary.exception.UserCreationException;
import org.soumyadip.expensediary.service.EasyUserCreationService;
import org.soumyadip.expensediary.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final EasyUserCreationService easyUserCreationService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserInfo>> getSelfProfile(
            @AuthenticationPrincipal ImplementedUserDetails userDetails
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        userService.getUser(userDetails.getUsername()),
                        HttpStatus.OK.value(),
                        Instant.now())
        );
    }

    @GetMapping("/access-times")
    public ResponseEntity<ApiResponse<AccessTimes>> getAccessTimes() {
        return ResponseEntity.ok(new ApiResponse<>(true, userService.getAccessTimes(), HttpStatus.OK.value(), Instant.now()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping("/register-user")
    public ResponseEntity<ApiResponse<ApiMessage>> registerUser(
            @RequestBody
            @Valid
            CreateUserRequest createUserRequest
    ) throws UserCreationException {

        easyUserCreationService.createRegularUser(createUserRequest.username(), createUserRequest.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, new ApiMessage("User created successfully!"), HttpStatus.CREATED.value(), Instant.now())
        );

    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping("/register-admin-user")
    public ResponseEntity<ApiResponse<ApiMessage>> registerAdminUser(
            @RequestBody
            @Valid
            CreateUserRequest createUserRequest
    ) throws UserCreationException {

        easyUserCreationService.createAdminUser(createUserRequest.username(), createUserRequest.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, new ApiMessage("User created successfully!"), HttpStatus.CREATED.value(), Instant.now())
        );

    }

    @PatchMapping("/change-user-details")
    public ResponseEntity<ApiResponse<ApiMessage>> changeUserDetails(
            @RequestBody
            @Valid
            ChangeUsername changeUsername,
            @AuthenticationPrincipal ImplementedUserDetails userDetails
    ) {
        userService.editUsername(userDetails.getUsername(), changeUsername.username());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true, new ApiMessage("User successfully changed!"), HttpStatus.OK.value(), Instant.now())
        );
    }

    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<ApiMessage>> changePassword(
            @RequestBody
            @Valid
            ChangePasswordRequest changePasswordRequest,
            @AuthenticationPrincipal ImplementedUserDetails userDetails
    ) {

        userService.changePassword(userDetails.getUsername(), changePasswordRequest.oldPassword(), changePasswordRequest.newPassword());

        return ResponseEntity.ok(
                new ApiResponse<>(true, new ApiMessage("Password changed successfully!"), HttpStatus.OK.value(), Instant.now())
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ApiResponse<ApiMessage>> deleteUser(
            @PathVariable String id,
            @AuthenticationPrincipal ImplementedUserDetails userDetails
    ) {

        userService.deleteUser(userDetails.getUsername(), id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                new ApiResponse<>(true, new ApiMessage("User deleted successfully!"), HttpStatus.NO_CONTENT.value(), Instant.now())
        );
    }
}
