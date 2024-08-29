package com.th.pm.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.th.pm.exceptions.EntityNotFoundException;
import com.th.pm.model.User;
import com.th.pm.repository.UserRepository;

@Service
public class PmUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()){
            return new UserDetailsImpl(user.get());
        }
        throw new UsernameNotFoundException("username not found "+username);
    }

    public UserDetails loadUserByEmail(String email) throws EntityNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            return new UserDetailsImpl(user.get());
        }
        throw new EntityNotFoundException("User not found with email of "+email);
        
    }
    
}
