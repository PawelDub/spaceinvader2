package pl.edu.uj.ii.ioinb.spaceinvader.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Entity
@Table(name = "game_result")
public class GameResult {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Email
    @NotEmpty
    @Column(name = "user_email")
    private String userEmail;

    @NotNull
    @DateTimeFormat(pattern = "kk:mm:ss")
    @Column(name = "result_time")
    private LocalTime resultTime;

    @NotNull
    @Column(name = "result")
    private Long result;

    public GameResult() {
    }

    public GameResult(@Valid String userEmail, @Valid LocalTime resultTime, @Valid Long result) {
        this.userEmail = userEmail;
        this.resultTime = resultTime;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalTime getResultTime() {
        return resultTime;
    }

    public void setResultTime(LocalTime resultTime) {
        this.resultTime = resultTime;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", resultTime=" + resultTime +
                ", result=" + result +
                '}';
    }
}
