package com.example.OmokServer.SingleTon;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Converter {
    public static List<Integer> toList(String logStr){
        StringTokenizer st = new StringTokenizer(logStr);
        List<Integer> list = new ArrayList<>();
        while(st.hasMoreTokens())
            list.add(Integer.parseInt(st.nextToken()));
        return list;
    }

    private static String filter = "[],";
    public static String toString(List<Integer> list){
        String str = list.toString();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<str.length(); i++){
            if(filter.indexOf(str.charAt(i)) == -1)
                sb.append(str.charAt(i));
        }
        return sb.toString();
    }
}
