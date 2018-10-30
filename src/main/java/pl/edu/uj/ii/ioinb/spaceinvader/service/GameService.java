package pl.edu.uj.ii.ioinb.spaceinvader.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.edu.uj.ii.ioinb.spaceinvader.model.GameResult;
import pl.edu.uj.ii.ioinb.spaceinvader.repository.GameResultRepository;

import javax.annotation.security.RolesAllowed;
import java.time.LocalTime;

@Service("gameService")

public class GameService {

    private GameResultRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameResultRepository") GameResultRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Iterable<GameResult> findAllOrderByResultTimeAndResult() {
        return gameRepository.findAllOrderByResultTimeAndResult();
    }

    public void save(GameResult gameResult) {
        gameRepository.save(gameResult);
    }

    public LocalTime findBestResultTime() {
        return gameRepository.findBestResultTime();
    }

    public Long findBestResult() {
        return gameRepository.findBestResult();
    }

}