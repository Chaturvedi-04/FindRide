package com.alpha.FindRide.security;

import com.alpha.FindRide.Entity.AppUser;
import com.alpha.FindRide.Repository.AppUserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String mobileno)
            throws UsernameNotFoundException {

        AppUser user = appUserRepo.findByMobileno(Long.parseLong(mobileno))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with mobile: " + mobileno)
                );

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getMobileno()),   // 👈 username
                user.getPassword(),                   // 👈 encrypted password
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole())
                )
        );
    }
}
