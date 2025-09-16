package com.lilybookclub.security;

import com.lilybookclub.entity.User;
import com.lilybookclub.exception.NotFoundException;
import com.lilybookclub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
     private final UserRepository userRepository;

     @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
         return new UserDetailsImpl(user);
     }

    public User getLoggedInUser(){
        String loggedInUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(loggedInUser)
                .orElseThrow(()
                        -> new NotFoundException("Logged in User not found"));
    }
}
