package com.example.OmokServer.GameData;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "game_data")
@Getter
@Setter
public class GameData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "game_log")
    private String gameLog;

    @Column(name = "result_code")
    private int resultCode;

    // player1, player2, pre gameLog to List<Integer>
}