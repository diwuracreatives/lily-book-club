package com.lilybookclub.service.impl;

import com.lilybookclub.config.JwtConfiguration;
import com.lilybookclub.entity.RefreshToken;
import com.lilybookclub.entity.User;
import com.lilybookclub.exception.BadRequestException;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.repository.RefreshTokenRepository;
import com.lilybookclub.security.UserDetailsServiceImpl;
import com.lilybookclub.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfiguration jwtConfig;
    private final UserDetailsServiceImpl userDetailsService;


    private RefreshToken checkIfTokenExists(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Refresh token is invalid"));
    }


    public String createRefreshToken(User user){
          Optional<RefreshToken> presentRefreshToken = refreshTokenRepository.findByUser(user);

          if (presentRefreshToken.isPresent()){
              return resetRefreshToken(presentRefreshToken.get());
          } else {
              Instant expiryTime = Instant.now().plusMillis(jwtConfig.getRefreshTokenExpiryDuration());

              RefreshToken refreshToken = RefreshToken.builder()
                    .user(user)
                    .token(UUID.randomUUID().toString())
                    .expiryTime(expiryTime)
                    .build();
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getToken();
        }
    }

    public String resetRefreshToken(RefreshToken refreshToken){

        Instant expiryTime = Instant.now().plusMillis(jwtConfig.getRefreshTokenExpiryDuration());

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryTime(expiryTime);
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public RefreshToken isRefreshTokenValid(String token) {
        RefreshToken refreshToken = checkIfTokenExists(token);
        if (refreshToken.getExpiryTime().isBefore(Instant.now())) {
            log.info("Refresh token is expired: {}", token);
            throw new BadRequestException("Refresh token is expired");
        } else {
             return refreshToken;
        }
    }

    public void deleteRefreshToken(){
        log.info("In Delete refresh token");
        User user = userDetailsService.getLoggedInUser();
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);
        refreshToken.ifPresent(refreshTokenRepository::delete);
    }

}
