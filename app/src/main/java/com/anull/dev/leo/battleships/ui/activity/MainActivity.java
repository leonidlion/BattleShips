package com.anull.dev.leo.battleships.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.anull.dev.leo.battleships.R;
import com.anull.dev.leo.battleships.field.GameCellInfo;
import com.anull.dev.leo.battleships.view.GameBoardView;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String COLOR_DIALOG_TAG = "COLOR_DIALOG_TAG";
    private static final int COLOR_FOR_CELL = 0;
    private static final int COLOR_FOR_SHIP = 1;
    private static final int COLOR_FOR_LINE = 2;
    private static final int COLOR_FOR_DEAD = 3;
    private static final int COLOR_FOR_TEXT = 4;

    private ColorPickerDialog mColorPickerDialog;
    private int mColorChosenType;
    @BindView(R.id.game_board)
    GameBoardView mGameBoardView;
    @BindView(R.id.ship_name)
    TextView mShipName;
    @BindView(R.id.ship_size)
    TextView mShipSize;
    @BindView(R.id.ship_health)
    TextView mShipHealth;
    @BindView(R.id.ship_alive)
    TextView mShipAlive;
    @BindView(R.id.current_cell_coordinate)
    TextView mCellCoordinate;

    @OnClick({
            R.id.reload_field,
            R.id.cell_color_picker,
            R.id.ship_color_picker,
            R.id.line_color_picker,
            R.id.dead_color_picker,
            R.id.text_color_picker})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.reload_field:
                mGameBoardView.reloadField();
                break;
            case R.id.cell_color_picker:
                mColorChosenType = COLOR_FOR_CELL;
                mColorPickerDialog.show(getFragmentManager(), COLOR_DIALOG_TAG);
                break;
            case R.id.ship_color_picker:
                mColorChosenType = COLOR_FOR_SHIP;
                mColorPickerDialog.show(getFragmentManager(), COLOR_DIALOG_TAG);
                break;
            case R.id.line_color_picker:
                mColorChosenType = COLOR_FOR_LINE;
                mColorPickerDialog.show(getFragmentManager(), COLOR_DIALOG_TAG);
                break;
            case R.id.dead_color_picker:
                mColorChosenType = COLOR_FOR_DEAD;
                mColorPickerDialog.show(getFragmentManager(), COLOR_DIALOG_TAG);
                break;
            case R.id.text_color_picker:
                mColorChosenType = COLOR_FOR_TEXT;
                mColorPickerDialog.show(getFragmentManager(), COLOR_DIALOG_TAG);
                break;

        }
    }

    @OnCheckedChanged(R.id.attack_ship_cb)
    public void onCheckedChange(boolean isCheck){
        mGameBoardView.setAttackMode(isCheck);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initGameBoardView();

        initColorPickerDialog();
    }

    private void initGameBoardView() {
        mGameBoardView.setOnCellClickListener(new GameBoardView.OnCellClickListener() {
            @Override
            public void onCellClick(GameCellInfo gameCellInfo) {
                mCellCoordinate.setText(getString(
                        R.string.current_cell_coordinate,
                        gameCellInfo.getI(),
                        gameCellInfo.getJ()));
                if (gameCellInfo.getShip() != null) {
                    mShipName.setText(getString(
                            R.string.ship_name,
                            gameCellInfo.getShip().getShipName()));
                    mShipSize.setText(getString(
                            R.string.ship_size,
                            gameCellInfo.getShip().getShipSize()));
                    mShipHealth.setText(getString(
                            R.string.ship_health,
                            gameCellInfo.getShip().getDamagedCellsCount(),
                            gameCellInfo.getShip().getShipSize()));
                    mShipAlive.setText(getString(
                            R.string.is_ship_alive,
                            String.valueOf(gameCellInfo.getShip().isShipAlive())));
                }else {
                    mShipName.setText(gameCellInfo.getCellType());
                    mShipSize.setText(R.string.empty_data);
                    mShipHealth.setText(R.string.empty_data);
                    mShipAlive.setText(R.string.empty_data);
                }
            }
        });
    }

    private void initColorPickerDialog(){
        int[] colors = getResources().getIntArray(R.array.default_rainbow);
        mColorPickerDialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                colors,
                R.color.flamingo,
                4,
                ColorPickerDialog.SIZE_SMALL, true);
        mColorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                switch (mColorChosenType){
                    case COLOR_FOR_CELL:
                        mGameBoardView.setCellColor(color);
                        break;
                    case COLOR_FOR_SHIP:
                        mGameBoardView.setShipColor(color);
                        break;
                    case COLOR_FOR_LINE:
                        mGameBoardView.setBoardLineColor(color);
                        break;
                    case COLOR_FOR_DEAD:
                        mGameBoardView.setShipDamagedCellColor(color);
                        break;
                    case COLOR_FOR_TEXT:
                        mGameBoardView.setTextColor(color);
                        break;
                }
            }
        });
    }

}
