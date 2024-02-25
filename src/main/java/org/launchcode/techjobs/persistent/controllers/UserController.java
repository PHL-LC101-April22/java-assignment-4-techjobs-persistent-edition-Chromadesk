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

    String index = "users/index";

    private static final String userSessionKey = "user";

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
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return index;
    }

    @GetMapping("signup")
    public String displaySignupForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Sign Up");
        return index;
    }

    //TODO Make login form method
//    @PostMapping("login")
//    public String processLoginForm(
//            @ModelAttribute @Valid User newUser, Errors errors, Model model) {
//
//        if (errors.hasErrors()) {
//            return "users/index";
//        }
//        System.out.println(newUser.getPwHash());
//
//        return "redirect:";
//    }

    @PostMapping("signup")
    public String processSignupForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                    Errors errors, HttpServletRequest request, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Sign Up");
            return index;
        }

        User existingUser = userRepository.findByName(loginFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "Username is taken.");
            model.addAttribute("title", "Register");
            return "register";
        }

        User newUser = new User(loginFormDTO.getUsername(), loginFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";
    }
}
