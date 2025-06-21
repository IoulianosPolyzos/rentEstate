package gr.hua.dit.rentEstate.config;

import gr.hua.dit.rentEstate.entities.User;
import gr.hua.dit.rentEstate.service.EmailService;
import gr.hua.dit.rentEstate.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();

        User user = userService.getUserByUsername(username);

        if (user != null && user.getEmail() != null) {
            emailService.sendRegisterNotification(user.getEmail(), username);
        }

        response.sendRedirect("/index");
    }

}

