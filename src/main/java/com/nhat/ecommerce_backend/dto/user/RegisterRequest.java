package com.nhat.ecommerce_backend.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Họ không được để trống")
    @Schema(example = "Nguyễn")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    @Schema(example = "Minh")
    private String lastName;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải là trong quá khứ")
    @Schema(example = "1990-01-01")
    private LocalDate birthDate;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại không hợp lệ")
    @Schema(example = "0925706495")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Schema(example = "Hà Nội, Việt Nam")
    private String address;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Schema(example = "nguyenminh@example.com")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
    @Schema(example = "password123")
    private String password;
}
