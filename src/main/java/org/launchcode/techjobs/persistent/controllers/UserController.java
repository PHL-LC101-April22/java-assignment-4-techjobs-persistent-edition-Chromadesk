package org.launchcode.techjobs.persistent.controllers;

import org.launchcode.techjobs.persistent.models.User;
import org.launchcode.techjobs.persistent.models.data.UserRepository;
import org.launchcode.techjobs.persistent.models.dto.LoginFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    String form = "users/form";
    String dashboard = "users/dashboard";

    public static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("login")
    public String displayLoginForm(Model model, HttpSession session) {
        model.addAttribute("title", "Log In");
        model.addAttribute("isLoggedOut", true);
        if (session.getAttribute(userSessionKey) != null) { return "redirect:"; }

        model.addAttribute(new LoginFormDTO());
        return form;
    }

    @GetMapping("signup")
    public String displaySignupForm(Model model, HttpSession session) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Sign Up");
        model.addAttribute("isLoggedOut", true);
        if (session.getAttribute(userSessionKey) != null) { return "redirect:"; }
        return form;
    }

    @PostMapping("login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request, Model model) {
        model.addAttribute("isLoggedOut", false);

        if (errors.hasErrors()) {
                model.addAttribute("title", "Log In");
                return form;
            }

            User user = userRepository.findByName(loginFormDTO.getUsername());
            String password = loginFormDTO.getPassword();

            if (user == null || !user.isMatchingPassword(password)) {
                errors.rejectValue("username", "user.invalid", "Invalid username or password.");
                model.addAttribute("title", "Log In");
                return form;
            }

            setUserInSession(request.getSession(), user);

            return "redirect:";
    }

    @PostMapping("signup")
    public String processSignupForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                    Errors errors, HttpServletRequest request, Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("isLoggedOut", true);

        if (errors.hasErrors()) { return form; }

        User existingUser = userRepository.findByName(loginFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "Username is taken.");
            return form;
        }

        User newUser = new User(loginFormDTO.getUsername(), loginFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";
    }

    @GetMapping("logout")
    public String logout(Model model, HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:";
    }

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("title", "hi");

        return dashboard;
    }
}
