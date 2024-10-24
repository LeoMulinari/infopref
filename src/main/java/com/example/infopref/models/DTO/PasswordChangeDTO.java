package com.example.infopref.models.DTO;

import lombok.Data;

@Data
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;
}
