package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.model.user.request.AuthRequest;
import main.QuizCraft.security.JwtService;
import main.QuizCraft.service.Auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthRequest authRequst){
        authService.register(authRequst);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthRequest authRequst,
                                                        HttpServletResponse httpServletResponse){
        authService.authenticate(authRequst, httpServletResponse);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/new-cookie")
    public ResponseEntity renewCookie(HttpServletRequest request,
                                                               HttpServletResponse httpResponse){
        authService.renewCookie(request, httpResponse);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/token-validation")
    public ResponseEntity validateToken(HttpServletRequest request){
        jwtService.validateToken(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
