package com.lilybookclub.security;

import com.lilybookclub.entity.User;
import com.lilybookclub.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
     private final UserRepository userRepository;

     public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
     }

     @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
         User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
         return new UserDetailsImpl(user);
     }
}
