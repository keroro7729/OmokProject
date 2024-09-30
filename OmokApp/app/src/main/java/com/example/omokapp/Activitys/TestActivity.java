package com.example.omokapp.Activitys;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.omokapp.OmokBoards.BoardView;
import com.example.omokapp.OmokBoards.OnBoardTouchListener;
import com.example.omokapp.OmokRules.OpenRule;
import com.example.omokapp.R;
import com.example.omokapp.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;
    private BoardView boardView;
    private OpenRule engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boardView = binding.boardView;
        engine = new OpenRule();
        refreshBoard();

        boardView.setOnTouchListener(new OnBoardTouchListener() {
            @Override
            public void onTouch(int coordinate) {

            }
        });
    }

    private void refreshBoard(){
        int[] d = engine.getLastHistory();
        boardView.refresh(d[0], d[1], d[2]);
    }
}