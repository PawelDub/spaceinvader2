package pl.edu.uj.ii.ioinb.spaceinvader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.uj.ii.ioinb.spaceinvader.model.Role;
import pl.edu.uj.ii.ioinb.spaceinvader.model.User;
import pl.edu.uj.ii.ioinb.spaceinvader.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class GameController {

    private UserService userService;

    @Autowired
    public GameController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String userHome(Principal principal, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        String view = "user/game";

        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals("ADMIN")) {
                    return "redirect:/admin/home";
                }
            }
        }

        model.addAttribute("name", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        model.addAttribute("user", user);
        model.addAttribute("message", "Game is Available Only for Users with " + user.getRoles() + " Role");
        model.addAttribute("principal", principal.getName());

        return view;
    }
}