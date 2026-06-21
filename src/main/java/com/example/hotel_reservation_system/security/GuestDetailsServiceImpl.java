package com.example.hotel_reservation_system.security;

import com.example.hotel_reservation_system.model.Guest;
import com.example.hotel_reservation_system.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuestDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Guest guest = guestRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Guest not found: " + email));
        return new User(
                guest.getEmail(),
                guest.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_GUEST")));
    }
}