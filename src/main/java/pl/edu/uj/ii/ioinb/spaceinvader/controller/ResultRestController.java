package pl.edu.uj.ii.ioinb.spaceinvader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.uj.ii.ioinb.spaceinvader.model.GameResult;
import pl.edu.uj.ii.ioinb.spaceinvader.model.Role;
import pl.edu.uj.ii.ioinb.spaceinvader.model.User;
import pl.edu.uj.ii.ioinb.spaceinvader.repository.GameResultRepository;
import pl.edu.uj.ii.ioinb.spaceinvader.service.GameService;
import pl.edu.uj.ii.ioinb.spaceinvader.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalTime;

@RestController
@RequestMapping("/user/game")
public class ResultRestController {

    private GameService gameService;
    private UserService userService;

    @Autowired
    public ResultRestController(@Qualifier("gameService") GameService gameService, @Qualifier("userService") UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping("/result")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResult createNewUser(@RequestBody @Valid GameResult result, Principal principal) throws Exception {
        checkUser(principal);
        return gameService.save(result);
    }

    @GetMapping("/result")
    public Iterable<GameResult> gameResults(Principal principal) throws Exception {
        checkUser(principal);
        return gameService.findAllOrderByResultTimeAndResult();
    }

    @GetMapping("/bestresulttime")
    public LocalTime gameBestResultTime(Principal principal) throws Exception {
        checkUser(principal);
        return gameService.findBestResultTime();
    }

    @GetMapping("/bestresult")
    public Long gameBestResult(Principal principal) throws Exception {
        checkUser(principal);
        return gameService.findBestResult();
    }

    private void checkUser(Principal principal) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals("ADMIN")) {
                    throw new Exception("You are not user");
                }
            }
        } else {
            throw new Exception("You have to be logIn");
        }
    }
}
