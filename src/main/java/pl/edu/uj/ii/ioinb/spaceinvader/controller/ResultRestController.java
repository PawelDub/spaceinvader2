package pl.edu.uj.ii.ioinb.spaceinvader.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.edu.uj.ii.ioinb.spaceinvader.exception.UserNotFoundException;
import pl.edu.uj.ii.ioinb.spaceinvader.model.GameResult;
import pl.edu.uj.ii.ioinb.spaceinvader.model.Role;
import pl.edu.uj.ii.ioinb.spaceinvader.model.RoleType;
import pl.edu.uj.ii.ioinb.spaceinvader.model.User;
import pl.edu.uj.ii.ioinb.spaceinvader.service.GameService;
import pl.edu.uj.ii.ioinb.spaceinvader.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalTime;

@RestController
@RequestMapping("/user/game")
@Api(value = "Result", description = "Operations of manage of game results")
public class ResultRestController {
    Logger logger = LogManager.getLogger(ResultRestController.class);
    private GameService gameService;
    private UserService userService;

    @Autowired
    public ResultRestController(@Qualifier("gameService") GameService gameService, @Qualifier("userService") UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping("/result")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Save game result")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully saved new game result"),
            @ApiResponse(code = 401, message = "You are not authorized to save the result"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 400, message = "Data which you are trying to save is incorrect")
    })
    public void saveGameResult(@RequestBody @Valid GameResult result, Principal principal) throws Exception {
        checkUser(principal);
        User user = userService.findUserByEmail(result.getUserEmail());

        if (user == null) {
            logger.error("User not exists in database");
            throw new UserNotFoundException("You have to register this user");
        }

        for (Role role : user.getRoles()) {
            if (role.getRole().equals(RoleType.ADMIN)) {
                logger.error("Trying to write ADMIN result ");
                throw new UserNotFoundException("You are ADMIN, admin can't write result");
            }
        }

        logger.info("Game result: " + result.toString());
        gameService.save(result);
    }

    @GetMapping("/result")
    @ApiOperation(value = "View list of all game results")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned list of game results"),
            @ApiResponse(code = 401, message = "You are not authorized to get the results"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    public Iterable<GameResult> gameResults(Principal principal) throws Exception {
        checkUser(principal);
        return gameService.findAllOrderByResultAndResultTime();
    }

    @GetMapping("/bestresulttime")
    @ApiOperation(value = "View the best time result")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned the best time result"),
            @ApiResponse(code = 401, message = "You are not authorized to get the result"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    public LocalTime gameBestResultTime(Principal principal) throws Exception {
        checkUser(principal);
        return gameService.findBestResultTime();
    }

    @GetMapping("/bestresult")
    @ApiOperation(value = "View the best result")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned the best result"),
            @ApiResponse(code = 401, message = "You are not authorized to get the result"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
    })
    public Long gameBestResult(Principal principal) throws Exception {
        checkUser(principal);
        return gameService.findBestResult();
    }

    private void checkUser(Principal principal) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        logger.info("User: " + user.getEmail()+ " is logged with role: " + user.getRoles().stream().findFirst().get().getRole());
        if (principal != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().equals(RoleType.ADMIN)) {
                    logger.error("Admin is logged");
                    throw new UserNotFoundException("You are not user");
                }
            }
        } else {
            logger.error("User " + user.getEmail() + " is not logged");
            throw new UserNotFoundException("You have to be logIn");
        }
    }
}
