package com.example.testspring.controller;


import com.example.testspring.JwtCore;
import com.example.testspring.SigninRequest;
import com.example.testspring.SignupRequest;
import com.example.testspring.model.AppUser;
import com.example.testspring.repository.UserRepository;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    private JwtCore jwtCore;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    public void setJwtCore(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }


    @PostMapping("/login")
    ResponseEntity<?> login (@RequestBody SigninRequest signinRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }
    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest)  {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            String hashed = passwordEncoder.encode(signupRequest.getPassword());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        }
        AppUser user = new AppUser();
        user.setUsername(signupRequest.getUsername());
        String hashed = passwordEncoder.encode(signupRequest.getPassword());
        user.setPassword(hashed);
        userRepository.save(user);
        return ResponseEntity.ok("success");
    }


}
