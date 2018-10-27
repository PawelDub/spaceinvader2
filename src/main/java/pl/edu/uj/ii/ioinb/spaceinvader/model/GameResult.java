package pl.edu.uj.ii.ioinb.spaceinvader.model;

import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Entity
@Table(name = "game_result")
public class GameResult {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "user_email")
    @Email
    @NotNull
    private String userEmail;

    @Column(name = "result_time")
    @DateTimeFormat(pattern = "kk:mm:ss")
    private LocalTime resultTime;

    @Column(name = "result")
    @NotNull
    private Long result;

    public GameResult() {
    }

    public GameResult(@Email @NotNull String userEmail, @Size(max = 8, min = 8, message = "size should contain 8 chars e.g: '11:12:05'") LocalTime resultTime, @NotNull Long result) {
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
}
