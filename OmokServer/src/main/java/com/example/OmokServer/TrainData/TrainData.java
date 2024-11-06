package com.example.OmokServer.TrainData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "train_data")
@Getter
@Setter
public class TrainData {
    @Id
    @Column(name = "state_code", length = 64)
    private String stateCode; // 상태 해시값

    @Column(name = "state", length = 675)
    private String state; // 오목판 상태 배열을 문자열로 저장

    @Column(name = "policy", length = 675)
    private String policy; // 정책 배열을 문자열로 저장

    @Column(name = "blackwin")
    private int blackWin; // 흑 승리 횟수

    @Column(name = "whitewin")
    private int whiteWin; // 백 승리 횟수

    @Column(name = "draw")
    private int draw; // 무승부 횟수

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(state).append('\n');
        sb.append(policy).append('\n');
        sb.append(blackWin).append(' ');
        sb.append(whiteWin).append(' ');
        sb.append(draw);
        return sb.toString();
    }
}
