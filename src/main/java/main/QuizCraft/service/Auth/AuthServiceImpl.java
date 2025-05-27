package main.QuizCraft.service.Auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.exception.InvalidCredentialsException;
import main.QuizCraft.exception.TokenAccessDeniedException;
import main.QuizCraft.exception.UserNotFoundException;
import main.QuizCraft.model.Token;
import main.QuizCraft.repository.TokenRepository;
import main.QuizCraft.request.AuthRequest;
import main.QuizCraft.model.User;
import main.QuizCraft.repository.UserRepository;
import main.QuizCraft.security.JwtServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserVerificationService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtServiceImpl jwtService;
    private final TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public void authenticate(AuthRequest authRequest,
                             HttpServletRequest request,
                             HttpServletResponse response) {

        String username = authRequest.username();
        String password = authRequest.password();

        logger.info("Attempting authentication for username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User not found: {}", username);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return new UserNotFoundException();
                });

        if (!encoder.matches(password, user.getPassword())) {
            logger.warn("Invalid password for username: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new InvalidCredentialsException();
        }

        logger.info("Authentication successful for username: {}", username);
        addJwtCookie(response, user);
        addRefreshTokenCookie(request, response, user);
    }

    private void addJwtCookie(HttpServletResponse response, User user) {
        Cookie jwtCookie = jwtService.createJwtCookie(
                Map.of("user_id", user.getId()),
                user,
                "jwt_token"
        );
        response.addCookie(jwtCookie);
    }

    private void addRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, User user) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = tokenRepository.findByUserId(user.getId())
                .orElse(new Token());
        token.setToken(tokenValue);
        token.setUserId(user.getId());
        token.setIp(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        tokenRepository.save(token);

        logger.info("Refresh token created for user {}: {}", user.getUsername(), tokenValue);

        Cookie refreshCookie = new Cookie("jwt_refresh_token", tokenValue);
        response.addCookie(refreshCookie);
    }


    @Override
    public void register(AuthRequest authRequst) {
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
            throw new UserNotFoundException();
        }
    }


    @Override
    public void renewCookie(HttpServletRequest request, HttpServletResponse response) {
        long userId = (long) request.getAttribute("user_id");
        String refreshToken = (String) request.getAttribute("refresh_token");

        logger.info("Renewing token for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Token token = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenAccessDeniedException("Invalid refresh token"));

        if (!token.getUserId().equals(userId)) {
            throw new TokenAccessDeniedException("Token does not belong to user");
        }

        if (token.getExpiresAt().isBefore(ZonedDateTime.now(ZoneId.of("UTC")))) {
            tokenRepository.delete(token);
            throw new TokenAccessDeniedException("Refresh token expired");
        }

        Cookie jwtCookie = jwtService.createJwtCookie(
                Map.of("user_id", user.getId()), user, "jwt_token"
        );
        response.addCookie(jwtCookie);
    }

    @Override
    public void logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("user_id");
        String refreshToken = (String) request.getAttribute("refresh_token");

        logger.info("Logging out user: {}", userId);

        if (refreshToken == null) {
            logger.warn("No refresh token found for user: {}", userId);
            throw new TokenAccessDeniedException("No refresh token found");
        }

        Token token = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new TokenAccessDeniedException("Invalid refresh token"));

        if (!token.getUserId().equals(userId)) {
            throw new TokenAccessDeniedException("Token does not belong to user");
        }

        tokenRepository.delete(token);
        logger.info("User {} logged out successfully", userId);
    }


    @Override
    public void verifyOwner(HttpServletRequest request, Long ownerId) {
        Long currentUserId = getCurrentUserId(request);
        if (!currentUserId.equals(ownerId)) {
            logger.error("Access denied: User with id {} is not authorized to access this deck.", currentUserId);
            throw new AccessDeniedException("You are not the owner of this deck");
        }
        logger.info("Access granted for user with id: {}", currentUserId);
    }

    private Long getCurrentUserId(HttpServletRequest httpServletRequest){
        Long userId = (Long) httpServletRequest.getAttribute("user_id");
        logger.debug("Current user id from request: {}", userId);
        return userId;
    }

}
