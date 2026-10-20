package com.example.job_finder.controller;

import com.example.job_finder.model.User;
import com.example.job_finder.repository.UserRepository;
import com.example.job_finder.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ✅ REGISTER
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            return "EMAIL ALREADY EXISTS";
        }

        userRepository.save(user);
        return "REGISTERED";
    }

    // ✅ LOGIN (JWT) - FIXED to return {token, user}
    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        User u = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(u != null && u.getPassword().equals(user.getPassword())){
            String token = JwtUtil.generateToken(u.getEmail());
            Map<String, Object> res = new HashMap<>();
            res.put("token", token);
            res.put("user", u);
            return res;
        }
        return "INVALID";
    }

    // TEST
    @GetMapping("/test")
    public String test() {
        return "Backend Working 🚀";
    }
}
