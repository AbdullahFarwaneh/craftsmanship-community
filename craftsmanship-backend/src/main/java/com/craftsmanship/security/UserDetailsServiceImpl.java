package com.craftsmanship.security;
import com.craftsmanship.repository.CraftsmanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CraftsmanRepository repo;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // identifier can be email or phone
        var c = repo.findByEmail(identifier)
                .or(() -> repo.findByPhoneNumber(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + identifier));
        return User.builder()
                .username(c.getEmail() != null ? c.getEmail() : c.getPhoneNumber())
                .password(c.getPassword())
                .roles("CRAFTSMAN").build();
    }
}
