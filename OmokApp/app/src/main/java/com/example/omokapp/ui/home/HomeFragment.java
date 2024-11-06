package com.example.omokapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.omokapp.Api.ApiManager;
import com.example.omokapp.DTO.AIRequest;
import com.example.omokapp.Enums.GameState;
import com.example.omokapp.Enums.PutError;
import com.example.omokapp.OmokBoards.OnBoardTouchListener;
import com.example.omokapp.OmokRules.PracticeEngine;
import com.example.omokapp.OmokRules.RenjuRule;
import com.example.omokapp.R;
import com.example.omokapp.Tools.Storage;
import com.example.omokapp.databinding.FragmentHomeBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;
    private PracticeEngine engine;
    private ApiManager apiManager;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Storage.getInstance(context);
        engine = new PracticeEngine(new RenjuRule());
        apiManager = new ApiManager(getString(R.string.base_url));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        setListener();
        refresh();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void refresh(){
        binding.testBoard.refresh(engine.getBoard());
        viewModel.setNumber(engine.getCurIndex()+1);
        binding.numberView.setText(viewModel.getNumber());
    }

    private void setListener(){
        binding.testBoard.setOnTouchListener(new OnBoardTouchListener() {
            @Override
            public void onTouch(int coordinate) {
                int code = engine.put(coordinate);
                if (code < 0)
                    Log.d("HomeFragment", "put error: " + PutError.fromCode(code));
                else if (code > 2)
                    Log.d("HomeFragment", "game end: " + GameState.fromCode(code));
                else Log.d("HomeFragment", "code: " + GameState.fromCode(code));
                refresh();
            }
        });

        binding.aiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AIRequest request = new AIRequest(engine.getHistory());
                Call<Integer> call = apiManager.getService().aiPut(request);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()){
                            engine.put(response.body());
                        }
                        else{
                            Log.e("aiput error", "code "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.e("aiput error", "err msg: "+t.getMessage());
                    }
                });
            }
        });
        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String history = engine.getHistoryString();
                //GameState state = engine.getState();
                //apiManager.sendGameHistory(history, state);
                apiManager.postGameData(engine.getHistory(), engine.getState());
            }
        });
        binding.prevprevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.moveCurIndex(-10);
                refresh();
            }
        });
        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.moveCurIndex(-1);
                refresh();
            }
        });
        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.moveCurIndex(1);
                refresh();
            }
        });
        binding.nextnextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                engine.moveCurIndex(10);
                refresh();
            }
        });
    }
}