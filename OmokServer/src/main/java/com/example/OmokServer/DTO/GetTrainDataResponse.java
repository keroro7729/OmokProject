package com.example.OmokServer.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetTrainDataResponse {
    private List<TData> items;
    public GetTrainDataResponse(){}
}
