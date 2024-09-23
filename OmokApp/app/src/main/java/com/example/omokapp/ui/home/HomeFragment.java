package com.example.omokapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.omokapp.Enums.GameState;
import com.example.omokapp.Enums.PutError;
import com.example.omokapp.OmokBoards.BoardView;
import com.example.omokapp.OmokBoards.OnBoardTouchListener;
import com.example.omokapp.OmokRules.RenjuRule;
import com.example.omokapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RenjuRule engine;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        engine = new RenjuRule();
        BoardView boardView = binding.testBoard;

        boardView.setOnTouchListener(new OnBoardTouchListener() {
            @Override
            public void onTouch(int coordinate) {
                int code = engine.put(coordinate);
                if(code < 0)
                    Log.d("HomeFragment", "put error: "+ PutError.fromCode(code));
                else if(code > 2)
                    Log.d("HomeFragment", "game end: "+ GameState.fromCode(code));
                boardView.refresh(engine.getBoard());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}