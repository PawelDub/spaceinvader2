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
import pl.edu.uj.ii.ioinb.spaceinvader.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/home")
public class HomeController {
    Logger logger = LogManager.getLogger(HomeController.class);
    private UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userHome(Principal principal, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        String view = "user/userhome";

        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals(RoleType.ADMIN)) {
                    logger.info("User has role: ADMIN");
                    return "redirect:/home/admin";
                }
            }
        } else {
            return "redirect:/";

        }

        model.addAttribute("name", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        model.addAttribute("message", "Content Available Only for Users with " + user.getRoles().stream().findFirst().get().getRole() + " Role");
        model.addAttribute("principal", principal.getName());

        return view;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminhome(Principal principal, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        String view = "admin/adminhome";

        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals(RoleType.USER)) {
                    view = "redirect:/home/user";
                    logger.info("User has role: USER");
                }
            }
        } else {
            return "redirect:/";
        }
        model.addAttribute("name", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        model.addAttribute("user", user);
        model.addAttribute("message", "Content Available Only for Users with " + user.getRoles().stream().findFirst().get().getRole() + " Role");
        model.addAttribute("principal", principal.getName());
        return view;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Principal principal) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals(RoleType.ADMIN)) {
                    logger.info("User has role: ADMIN");
                    return "redirect:/home/admin";
                }
            }
            logger.info("User has role: USER");
            return "redirect:/home/user";
        }
        return "redirect:/login";
    }
}
