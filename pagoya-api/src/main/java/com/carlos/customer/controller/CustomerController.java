package com.carlos.customer.controller;

import com.carlos.customer.dto.CreateCustomerRequest;
import com.carlos.customer.dto.CustomerResponse;
import com.carlos.customer.dto.UpdateCustomerRequest;
import com.carlos.customer.service.ICustomerService;
import com.carlos.shared.pagination.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CustomerResponse>> findAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(
                PageResponse.from(customerService.findAll(pageable)));
    }
    @Operation(summary = "Obtener mi perfil")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil del usuario autenticado"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> findMe(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(customerService.findByEmail(email));
    }

    @Operation(summary = "Actualizar mi perfil (fullName, phone)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @PutMapping("/me")
    public ResponseEntity<CustomerResponse> updateMe(
            Authentication authentication,
            @Valid @RequestBody UpdateCustomerRequest request) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(customerService.updateByEmail(email, request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}