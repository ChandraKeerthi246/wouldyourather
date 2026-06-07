package com.keerthi.wouldyourather;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String resultTitle;
    private String threatAssessment;
    private String dangerLevel;
    

    @Column(length = 2000)
    private String answers;

    private LocalDateTime playedAt;

    public GameResult() {
    }

    public GameResult(String nickname, String resultTitle, String threatAssessment,
                      String dangerLevel, String answers) {
        this.nickname = nickname;
        this.resultTitle = resultTitle;
        this.threatAssessment = threatAssessment;
        this.dangerLevel = dangerLevel;
        
        this.answers = answers;
        this.playedAt = LocalDateTime.now();
    }
}