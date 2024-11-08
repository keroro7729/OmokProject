package com.example.OmokServer.Controller;

import com.example.OmokServer.DTO.AIRequest;
import com.example.OmokServer.DTO.AddOmokDataRequest;
import com.example.OmokServer.DTO.PostGameDataRequest;
import com.example.OmokServer.Engine.RenjuEngine;
import com.example.OmokServer.MonteCarlo.MTCS;
import com.example.OmokServer.SingleTon.FileStorage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ApiController {

    private FileStorage storage = FileStorage.getInstance();
    private final String OMOK_DATA = "omok_data";

    @PostMapping("/add_omok_data")
    public ResponseEntity<Void> name(@RequestBody AddOmokDataRequest request){
        try{
            storage.append(OMOK_DATA, request.getDataFile());
            return ResponseEntity.ok().build();
        } catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/get_omok_data")
    public ResponseEntity<String> getOmokData(){
        try{
            String result = storage.read(OMOK_DATA);
            return ResponseEntity.ok(result);
        } catch(IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/ai_put")
    public ResponseEntity<Integer> aiPut(@RequestBody AIRequest request){
        RenjuEngine env = new RenjuEngine(request.getGameLog());
        MTCS mcts = new MTCS();
        int result = mcts.bestAction(env, 1000);
        return ResponseEntity.ok(result);
    }
}
