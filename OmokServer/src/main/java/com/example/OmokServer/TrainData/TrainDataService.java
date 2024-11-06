package com.example.OmokServer.TrainData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class TrainDataService {

    private final TrainDataRepository omokDataRepository;

    @Autowired
    public TrainDataService(TrainDataRepository omokDataRepository) {
        this.omokDataRepository = omokDataRepository;
    }

    // 데이터 저장
    public void saveOmokData(List<Integer> gameLog, int gameResult) {
        int[][] states = new int[8][225];
        int player = 1;
        for (int i=0; i<gameLog.size()-2; i++) {
            for(int j=0; j<8; j++){
                int cur = rotflip(gameLog.get(i), j), next = rotflip(gameLog.get(i+1), j);
                updateOmokData(states[j], player, cur, next, gameResult);
            }
            player = player == 1 ? 2 : 1;
        }
    }

    private void updateOmokData(int state[], int player, int cur, int next, int gameResult){
        state[cur] = player;
        String stateStr = arrayToString(state);
        String stateCode = generateStateCode(stateStr);
        Optional<TrainData> omokDataOpt = omokDataRepository.findById(stateCode);

        TrainData omokData;
        if (omokDataOpt.isPresent()) {
            omokData = omokDataOpt.get();
        } else {
            omokData = new TrainData();
            omokData.setStateCode(stateCode);
            omokData.setState(stateStr);
            omokData.setPolicy(arrayToString(new int[225]));
            omokData.setBlackWin(0);
            omokData.setWhiteWin(0);
            omokData.setDraw(0);
        }

        omokData.setPolicy(addPolicy(omokData.getPolicy(), next));
        switch(gameResult){
            case 3: omokData.setBlackWin(omokData.getBlackWin()+1); break;
            case 4: omokData.setWhiteWin(omokData.getWhiteWin()+1); break;
            case 5: omokData.setDraw(omokData.getDraw()+1); break;
        }
        omokDataRepository.save(omokData);
    }

    // 모든 데이터 조회
    public List<TrainData> getAllOmokData() {
        return omokDataRepository.findAll();
    }


    private int rotflip(int coor, int n){
        switch(n) {
            case 0:
                return coor;
            case 1:
                return rot(coor);
            case 2:
                return rot(rot(coor));
            case 3:
                return rot(rot(rot(coor)));
            case 4:
                return flip(coor);
            case 5:
                return flip(rot(coor));
            case 6:
                return flip(rot(rot(coor)));
            case 7:
                return flip(rot(rot(rot(coor))));
            default: return -1;
        }
    }
    private int rot(int coor){
        int x = coor / 15;
        int y = coor % 15;
        return y * 15 + (14 - x);
    }
    private int flip(int coor){
        int x = coor / 15;
        int y = coor % 15;
        return x * 15 + (14 - y);
    }
    private String arrayToString(int[] arr){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<arr.length; i++)
            sb.append(arr[i]).append(' ');
        return sb.toString();
    }
    private int[] stringToArray(String str){
        StringTokenizer st = new StringTokenizer(str);
        int[] arr = new int[225];
        for(int i=0; i<225; i++)
            arr[i] = Integer.parseInt(st.nextToken());
        return arr;
    }

    private String generateStateCode(String state) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(state.getBytes());
            return HexFormat.of().formatHex(hashBytes); // 해시 값을 16진수 문자열로 반환
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private String addPolicy(String policyStr, int coor){
        int[] policy = stringToArray(policyStr);
        policy[coor]++;
        return arrayToString(policy);
    }
}
