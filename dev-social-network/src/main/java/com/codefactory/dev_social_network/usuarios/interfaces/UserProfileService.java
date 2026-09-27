package com.codefactory.dev_social_network.usuarios.interfaces;

import java.util.UUID;

import com.codefactory.dev_social_network.usuarios.dto.UserProfileEditionRequestDTO;
import com.codefactory.dev_social_network.usuarios.dto.UserProfileResponseDTO;

public interface UserProfileService {

    UserProfileResponseDTO updateUserProfile(UUID userId, UserProfileEditionRequestDTO request);

    UserProfileResponseDTO getUserProfile(UUID userId);
}
