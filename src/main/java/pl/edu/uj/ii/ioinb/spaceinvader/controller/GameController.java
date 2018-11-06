package pl.edu.uj.ii.ioinb.spaceinvader.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.uj.ii.ioinb.spaceinvader.model.Role;
import pl.edu.uj.ii.ioinb.spaceinvader.model.RoleType;
import pl.edu.uj.ii.ioinb.spaceinvader.model.User;
import pl.edu.uj.ii.ioinb.spaceinvader.service.GameService;
import pl.edu.uj.ii.ioinb.spaceinvader.service.UserService;

import java.security.Principal;
import java.time.LocalTime;

@Controller
@RequestMapping("/user")
public class GameController {
    private Logger logger = LogManager.getLogger(GameController.class);
    private UserService userService;

    private GameService gameService;

    @Autowired
    public GameController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String userHome(Principal principal, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        String view = "user/game";
        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals(RoleType.ADMIN)) {
                    logger.info("User has role: ADMIN");
                    return "redirect:/home/admin";
                }
            }
        }

        LocalTime bestResultTime = gameService.findBestResultTime();
        Long bestResult = gameService.findBestResult();
        if (bestResultTime == null) {
            model.addAttribute("bestResultTime", "00:00:00");
        } else {
            model.addAttribute("bestResultTime", bestResultTime.toString());
        }

        if (bestResult == null) {
            model.addAttribute("bestResult", "0");
        } else {
            model.addAttribute("bestResult", bestResult.toString());
        }

        model.addAttribute("name", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        model.addAttribute("user", user);
        model.addAttribute("message", "Game is Available Only for Users with " + user.getRoles() + " Role");
        model.addAttribute("principal", principal.getName());
        return view;
    }
}