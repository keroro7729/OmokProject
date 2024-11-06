package com.example.OmokServer.MonteCarlo;

import com.example.OmokServer.Engine.RenjuEngine;

import java.util.List;
import java.util.Random;

public class MTCS {
    private static final Random random = new Random();
    public int bestAction(RenjuEngine initEnv, int iterations){
        Node root = new Node(initEnv, null);
        for(int i=0; i<iterations; i++){
            Node node = treePolicy(root);
            double reward = defaultPolicy(node.getEnv());
            backpropagate(node, reward);
        }
        return root.selectChild().getLastAction();
    }

    private Node treePolicy(Node node){
        while(!node.getEnv().isEnd()){
            if(node.getVisits() == 0) // 첫 방문
                return node;
            else node = node.selectChild();
        }
        return node;
    }

    private double defaultPolicy(RenjuEngine env){
        while(!env.isEnd()){
            List<Integer> actions = env.getValidActions();
            int randomAction = actions.get(random.nextInt(actions.size()));
            env.makeMove(randomAction);
        }
        switch(env.getWinner()){
            case 3: return 1.0;
            case 4: return -1.0;
            case 5: return 0.0;
            default: throw new RuntimeException("defaultPolicy result is not ended");
        }
    }

    private void backpropagate(Node node, double result){
        while(node != null){
            node.update(result);
            node = node.getParent();
        }
    }
}
