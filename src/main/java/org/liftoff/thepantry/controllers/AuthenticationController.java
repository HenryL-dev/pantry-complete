package org.liftoff.thepantry.controllers;

import org.liftoff.thepantry.data.UserRepository;
import org.liftoff.thepantry.models.User;
import org.liftoff.thepantry.models.dto.LoginFormDTO;
import org.liftoff.thepantry.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    UserRepository userRepository;


    private static final String userSessionKey = "user";

    public User getUserFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }

        Optional<User> user = userRepository.findById(Integer.valueOf(String.valueOf(userId)));

        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }


    @PostMapping("register")
    public String processRegistrationForm(Model model, @ModelAttribute @Valid RegisterFormDTO newUser, Errors errors, RedirectAttributes ra,
                                           HttpServletRequest request) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Register");
            return "register";
        }

        User existingUser = userRepository.findByUsername(newUser.getUsername());

        if (existingUser != null) {


            errors.rejectValue("username", "username.alreadyexists", "Username in use, please choose another.");
            model.addAttribute("title", "Register");
            ra.addFlashAttribute("class", "alert alert-danger");
            ra.addFlashAttribute("message", "Username '" + newUser.getUsername() + "' already exists!");
            return "register";
        }
// 12345678
        String password = newUser.getPassword();
        String verifyPassword = newUser.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Please enter matching passwords");
            model.addAttribute("title", "Register");
            ra.addFlashAttribute("class", "alert alert-danger");
            ra.addFlashAttribute("message", "Passwords do not match!");
            return "register";
        }

        User addUser = new User(newUser.getUsername(), newUser.getPassword());
        userRepository.save(addUser);
        setUserInSession(request.getSession(), addUser);


        return "redirect:/";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }

        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());

        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword();

        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }

        setUserInSession(request.getSession(), theUser);

        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }
}