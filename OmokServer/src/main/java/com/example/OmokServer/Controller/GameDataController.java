package com.example.OmokServer.Controller;

import com.example.OmokServer.DTO.GetTrainDataResponse;
import com.example.OmokServer.DTO.PostGameDataRequest;
import com.example.OmokServer.DTO.TData;
import com.example.OmokServer.GameData.GameDataRepository;
import com.example.OmokServer.GameData.GameDataService;
import com.example.OmokServer.SingleTon.Converter;
import com.example.OmokServer.TrainData.TrainData;
import com.example.OmokServer.TrainData.TrainDataRepository;
import com.example.OmokServer.TrainData.TrainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Controller
@RequestMapping("api/data")
public class GameDataController {
    private final GameDataService gameDataService;
    private final TrainDataService trainDataService;

    @Autowired
    public GameDataController(GameDataService gameDataService, TrainDataService trainDataService){
        this.gameDataService = gameDataService;
        this.trainDataService = trainDataService;
    }

    @PostMapping("/post_game_data")
    public ResponseEntity<Void> postGameData(@RequestBody PostGameDataRequest request){
        gameDataService.addGameData(request);
        List<Integer> gameLogList = toList(request.getGameLog());
        trainDataService.saveOmokData(gameLogList, request.getResultCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get_train_data")
    public ResponseEntity<GetTrainDataResponse> getTrainData(){
        List<TrainData> list = trainDataService.getAllOmokData();
        List<TData> items = new ArrayList<>();
        for(TrainData data : list){
            TData item = new TData();
            item.setState(Converter.toList(data.getState()));
            item.setPolicy(Converter.toList(data.getPolicy()));
            item.setWins(new ArrayList<>());
            item.getWins().add(data.getBlackWin());
            item.getWins().add(data.getWhiteWin());
            item.getWins().add(data.getDraw());
            items.add(item);
        }
        return ResponseEntity.ok(new GetTrainDataResponse(items));
    }

    private List<Integer> toList(String logStr){
        StringTokenizer st = new StringTokenizer(logStr);
        List<Integer> list = new ArrayList<>();
        while(st.hasMoreTokens())
            list.add(Integer.parseInt(st.nextToken()));
        return list;
    }
}
