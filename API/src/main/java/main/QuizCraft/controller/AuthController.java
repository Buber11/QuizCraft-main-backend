package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.model.user.AuthRequest;
import main.QuizCraft.response.Response;
import main.QuizCraft.service.Auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public <T extends Response>  ResponseEntity<T> register(@RequestBody AuthRequest authRequst){
        final T response = authService.register(authRequst);
        return ResponseEntity.status(response.getCodeHttp()).body(response);
    }

    @PostMapping("/login")
    public <T extends Response> ResponseEntity<T> login(@RequestBody AuthRequest authRequst,
                                                        HttpServletResponse httpServletResponse){
        final T response = authService.authenticate(authRequst, httpServletResponse);
        return ResponseEntity.status(response.getCodeHttp()).body(response);
    }

    @PostMapping("/new-cookie")
    public <T extends  Response> ResponseEntity<T> renewCookie(HttpServletRequest request,
                                                               HttpServletResponse httpResponse){
        final T response = authService.renewCookie(request, httpResponse);
        return ResponseEntity.status(response.getCodeHttp()).body(response);
    }

}
