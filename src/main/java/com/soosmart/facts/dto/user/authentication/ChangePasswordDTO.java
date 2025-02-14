package com.soosmart.facts.dto.user.authentication;

public record ChangePasswordDTO(
        String oldPassword,
        String newPassword
) {
}
