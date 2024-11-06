package com.example.OmokServer.GameData;

import com.example.OmokServer.DTO.PostGameDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameDataService {

    private final GameDataRepository gameDataRepository;

    @Autowired
    public GameDataService(GameDataRepository gameDataRepository){
        this.gameDataRepository = gameDataRepository;
    }

    public void addGameData(PostGameDataRequest request){
        GameData gameData = new GameData();
        gameData.setGameLog(request.getGameLog());
        gameData.setResultCode(request.getResultCode());
        gameDataRepository.save(gameData);
    }
}
