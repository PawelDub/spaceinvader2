package pl.edu.uj.ii.ioinb.spaceinvader.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.uj.ii.ioinb.spaceinvader.model.GameResult;

import java.time.LocalTime;

@Repository("gameResultRepository")
public interface GameResultRepository extends CrudRepository<GameResult, Long> {

    @Query(value = "SELECT * FROM GAME_RESULT order by result_time asc, result desc", nativeQuery = true)
    Iterable<GameResult> findAllOrderByResultTimeAndResult();

    @Query(value = "select result_time from game_result order by result_time limit 1", nativeQuery = true)
    LocalTime findBestResultTime();

    @Query(value = "select result from game_result order by result desc limit 1", nativeQuery = true)
    Long findBestResult();

}
