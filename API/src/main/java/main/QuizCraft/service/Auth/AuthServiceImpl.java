package main.QuizCraft.service.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.model.user.AuthRequest;
import main.QuizCraft.model.user.User;
import main.QuizCraft.repository.UserRepository;
import main.QuizCraft.response.AuthResponse;
import main.QuizCraft.security.JwtServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtServiceImpl jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public AuthResponse authenticate(AuthRequest authRequest, HttpServletResponse response) {
        logger.info("Attempting authentication for username: {}", authRequest.username());
        Optional<User> userOpt = userRepository.findByUsername(authRequest.username());

        if (userOpt.isEmpty()) {
            logger.warn("Authentication failed: User not found for username: {}", authRequest.username());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return new AuthResponse(
                    "http://QuizCraft/problems/authenticate",
                    404,
                    "User not found",
                    "The username provided does not exist in the system.",
                    null
            );
        }

        User user = userOpt.get();
        if (!encoder.matches(authRequest.password(), user.getPassword())) {
            logger.warn("Authentication failed: Invalid password for username: {}", authRequest.username());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return new AuthResponse(
                    "http://QuizCraft/problems/authenticate",
                    401,
                    "Invalid credentials",
                    "The password provided is incorrect.",
                    null
            );
        }

        logger.info("Authentication successful for username: {}", authRequest.username());

        Cookie cookie = jwtService.createJwtCookie(Map.of("user_id", user.getId()),
                user,
                "jwt_token");
        response.addCookie(cookie);

        return new AuthResponse();
    }

    @Override
    public AuthResponse register(AuthRequest authRequst) {
        logger.info("Attempting to register a new user: {}", authRequst.username());

        User newUser = new User(
                authRequst.username(),
                encoder.encode(authRequst.password()),
                true,
                false
        );
        try {
            userRepository.save(newUser);
            logger.info("User successfully registered: {}", authRequst.username());
        } catch (DataAccessException ex) {
            logger.error("Registration failed: User with username {} already exists.", authRequst.username(), ex);
            return new AuthResponse(
                    "http://QuizCraft/problems/change-your-username",
                    400,
                    "Your username isn't unique",
                    "The provided username already exists in the system.",
                    null
            );
        }
        return new AuthResponse();
    }

    @Override
    public AuthResponse renewCookie(HttpServletRequest request, HttpServletResponse response) {
        long userId = (long) request.getAttribute("user_id");
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            logger.warn("Authentication failed: User not found for id: {}", userId );
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            return new AuthResponse(
                    "http://QuizCraft/problems/authenticate",
                    404,
                    "User not found",
                    "The username provided does not exist in the system.",
                    null
            );
        }
        User user = userOpt.get();
        Cookie cookie = jwtService.createJwtCookie(Map.of("user_id", user.getId()),
                user,
                "jwt_token");
        response.addCookie(cookie);

        return new AuthResponse();
    }

}
