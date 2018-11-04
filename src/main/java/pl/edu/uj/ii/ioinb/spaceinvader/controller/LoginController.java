package pl.edu.uj.ii.ioinb.spaceinvader.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.uj.ii.ioinb.spaceinvader.service.UserService;

import java.security.Principal;

@Controller
public class LoginController {
    Logger logger = LogManager.getLogger(LoginController.class);
    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @RequestMapping(value = "/access-denied", method = RequestMethod.GET)
    public String accessDenied() {
        return "access-denied";
    }


}
