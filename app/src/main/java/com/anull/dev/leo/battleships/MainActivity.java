package com.anull.dev.leo.battleships;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anull.dev.leo.battleships.view.GameBoard;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.game_board)
    GameBoard mGameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

    }
}
