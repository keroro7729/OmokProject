package com.example.OmokServer.MonteCarlo;

import com.example.OmokServer.Engine.RenjuEngine;
import com.example.OmokServer.Enums.GameState;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private RenjuEngine env;
    private Node parent;
    private List<Node> children;
    private int visits;
    private double reward;

    public Node(RenjuEngine env, Node parent){
        this.env = env;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.visits = 0;
        this.reward = 0;
    }

    public void expand(){
        for(int coor : env.getValidActions()){
            RenjuEngine childEnv = new RenjuEngine(env);
            childEnv.makeMove(coor);
            Node child = new Node(childEnv, this);
            children.add(child);
        }
    }

    public Node selectChild(){
        if(children.isEmpty())
            expand();
        Node best = null;
        double bestValue = env.getState() == GameState.black_playing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for(Node child : children){
            double uctValue = child.reward / (child.visits + 1) + Math.sqrt(2 * Math.log(visits + 1) / (child.visits + 1));
            if((env.getState() == GameState.black_playing && uctValue > bestValue) ||
                    (env.getState() == GameState.white_playing && uctValue < bestValue)){
                bestValue = uctValue;
                best = child;
            }
        }
        return best;
    }

    public void update(double value){
        visits++;
        reward += value;
    }

    public int getVisits(){ return visits; }
    public RenjuEngine getEnv(){ return env; }
    public Node getParent(){ return parent; }
    public int getLastAction(){
        List<Integer> history = env.getHistory();
        return history.get(history.size()-1);
    }
}
