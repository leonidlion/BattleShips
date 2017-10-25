package com.anull.dev.leo.battleships.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.anull.dev.leo.battleships.R;
import com.anull.dev.leo.battleships.field.GameCellInfo;
import com.anull.dev.leo.battleships.field.GameField;

import static com.anull.dev.leo.battleships.Constants.LETTERS;
import static com.anull.dev.leo.battleships.field.CellTypes.DEAD_SHIP_CELL;
import static com.anull.dev.leo.battleships.field.CellTypes.EMPTY;
import static com.anull.dev.leo.battleships.field.CellTypes.NEAR_SHIP_CELL;
import static com.anull.dev.leo.battleships.field.CellTypes.SHIP;

public class GameBoardView extends View {
    private static final String GAME_FIELD_STATE = "GAME_FIELD_STATE";
    private static final String CELL_COLOR_KEY = "CELL_COLOR_KEY";
    private static final String SHIP_COLOR_KEY = "SHIP_COLOR_KEY";
    private static final String DEAD_COLOR_KEY = "DEAD_COLOR_KEY";
    private static final String LINE_COLOR_KEY = "LINE_COLOR_KEY";
    private static final String TEXT_COLOR_KEY = "TEXT_COLOR_KEY";
    private static final int DEFAULT_BOARD_LINE_WIDTH = 2;
    private static final int DEFAULT_BOARD_LINE_COLOR = Color.GRAY;
    private static final int DEFAULT_SHIP_CELL_COLOR = Color.RED;
    private static final int DEFAULT_EMPTY_CELL_COLOR = Color.BLUE;
    private static final int DEFAULT_SHIP_DAMAGED_CELL_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_SIZE = 22;

    private static final int MIN_SIZE_VIEW = 500;
    private static final int CELL_COUNT = 11;

    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mCellPaint;

    private int mBoardLineWidth;
    private int mBoardLineColor;
    private int mBoardCellColor;
    private int mShipCellColor;
    private int mShipDamagedCellColor;
    private int mTextColor;
    private float mTextSize;
    private int mCellSize;

    private GameField mGameField;
    private OnCellClickListener mOnCellClickListener;
    private boolean mIsAttackMode;

    public interface OnCellClickListener {
        void onCellClick(GameCellInfo gameCellInfo);
    }

    public GameBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaints();

        if (attrs != null){
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoardView, 0,0);
            setBoardLineWidth(a.getInt(R.styleable.GameBoardView_board_line_width, DEFAULT_BOARD_LINE_WIDTH));
            setBoardLineColor(a.getInt(R.styleable.GameBoardView_board_line_color, DEFAULT_BOARD_LINE_COLOR));
            setCellColor(a.getInt(R.styleable.GameBoardView_cell_color, DEFAULT_EMPTY_CELL_COLOR));
            setShipColor(a.getInt(R.styleable.GameBoardView_ship_cell_color, DEFAULT_SHIP_CELL_COLOR));
            setShipDamagedCellColor(a.getInt(R.styleable.GameBoardView_ship_dead_cell_color, DEFAULT_SHIP_DAMAGED_CELL_COLOR));
            setTextColor(a.getInt(R.styleable.GameBoardView_text_color, DEFAULT_TEXT_COLOR));
            setTextSize(a.getDimension(R.styleable.GameBoardView_text_size, DEFAULT_TEXT_SIZE));
            a.recycle();
        }
    }

    private void initPaints(){
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mBoardLineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);

        mCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaintColors();
        if (mGameField == null){
            mGameField = new GameField();
        }
        mCellSize = getMeasuredWidth() / CELL_COUNT;
        drawLetters(canvas);
        drawNumbers(canvas);
        drawGameBoard(canvas);
        drawShips(canvas);
    }

    private void initPaintColors(){
        mTextPaint.setColor(mTextColor);
        mLinePaint.setColor(mBoardLineColor);
        mCellPaint.setColor(mBoardCellColor);
    }

    private void drawLetters(Canvas canvas){
        for (int i = 0; i < 10; i++){
            Rect rect = new Rect();
            mTextPaint.getTextBounds(LETTERS[i], 0, LETTERS[i].length(), rect);
            int textWidth = rect.width();
            int halfCell = mCellSize / 2;
            int startX = (mCellSize * (i + 1)) + halfCell - textWidth / 2;
            canvas.drawText(LETTERS[i], startX, halfCell, mTextPaint);
        }
    }

    private void drawNumbers(Canvas canvas){
        int startNumber = 1;
        int numberCount = 10;
        for (int i = startNumber; i <= numberCount; i++){
            Rect rect = new Rect();
            String number = String.valueOf(i);
            mTextPaint.getTextBounds(number, 0, number.length(), rect);
            int textWidth = rect.width();
            int halfCell = mCellSize / 2;
            int startX = halfCell - textWidth / 2;
            int startY = mCellSize * (i + 1) - halfCell + rect.height()/2;
            canvas.drawText(number, startX, startY, mTextPaint);
        }
    }

    private void drawGameBoard(Canvas canvas){
        for (int i = 1; i < 11; i++){
            for (int j = 1; j < 11; j++){
                int left = j * mCellSize;
                int top = i * mCellSize;
                int right = (j + 1) * mCellSize;
                int bottom = (i + 1) * mCellSize;
                Rect rect = new Rect(left, top, right, bottom);
                canvas.drawRect(rect, mLinePaint);
            }
        }
    }

    private void drawShips(Canvas canvas){
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                int left = j * mCellSize + mBoardLineWidth;
                int top = i * mCellSize + mBoardLineWidth;
                int right = (j + 1) * mCellSize - mBoardLineWidth;
                int bottom = (i + 1) * mCellSize - mBoardLineWidth;
                int color;
                switch (mGameField.getCellType(i - 1, j - 1)) {
                    case EMPTY:
                        color = mBoardCellColor;
                        break;
                    case NEAR_SHIP_CELL:
                        color = mBoardCellColor;
                        break;
                    case SHIP:
                        color = mShipCellColor;
                        break;
                    case DEAD_SHIP_CELL:
                        color = mShipDamagedCellColor;
                        break;
                    default:
                        color = mBoardCellColor;
                }
                mCellPaint.setColor(color);
                canvas.drawRect(new Rect(left, top, right, bottom), mCellPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasuredSpec, int heightMeasuredSpec) {
        super.onMeasure(widthMeasuredSpec, heightMeasuredSpec);
        int w = resolveSize(MIN_SIZE_VIEW, widthMeasuredSpec);
        int h = resolveSize(MIN_SIZE_VIEW, heightMeasuredSpec);

        int size = w > h ? h : w;

        setMeasuredDimension(size, size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int cellSize = getMeasuredWidth() / CELL_COUNT;
                int i = (int) (event.getX() / cellSize) - 1;
                int j = (int) (event.getY() / cellSize) - 1;
                if ((i | j) >= 0){
                    if (mOnCellClickListener != null){
                        if (mIsAttackMode) {
                            mGameField.attack(j, i);
                            invalidate();
                            requestLayout();
                        }
                        mOnCellClickListener.onCellClick(mGameField.getCellInfo(j,i));
                    }
                }
                return true;
            default: return false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mOnCellClickListener = null;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(GameBoardView.class.getSimpleName(), super.onSaveInstanceState());
        bundle.putParcelable(GAME_FIELD_STATE, mGameField);
        bundle.putInt(CELL_COLOR_KEY, mBoardCellColor);
        bundle.putInt(SHIP_COLOR_KEY, mShipCellColor);
        bundle.putInt(LINE_COLOR_KEY, mBoardLineColor);
        bundle.putInt(DEAD_COLOR_KEY, mShipDamagedCellColor);
        bundle.putInt(TEXT_COLOR_KEY, mTextColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mGameField = bundle.getParcelable(GAME_FIELD_STATE);
            mBoardCellColor = bundle.getInt(CELL_COLOR_KEY);
            mShipCellColor = bundle.getInt(SHIP_COLOR_KEY);
            mShipDamagedCellColor = bundle.getInt(DEAD_COLOR_KEY);
            mBoardLineColor = bundle.getInt(LINE_COLOR_KEY);
            mTextColor = bundle.getInt(TEXT_COLOR_KEY);
            state = bundle.getParcelable(GameBoardView.class.getSimpleName());
        }
        super.onRestoreInstanceState(state);
    }

    public void reloadField(){
        mGameField = new GameField();
        invalidate();
        requestLayout();
    }

    public void setAttackMode(boolean isAttackMode){
        mIsAttackMode = isAttackMode;
    }

    public void setOnCellClickListener(OnCellClickListener clickListener){
        mOnCellClickListener = clickListener;
    }

    public void setBoardLineWidth(int boardLineWidth) {
        if (boardLineWidth > 0){
            mBoardLineWidth = boardLineWidth;
            mLinePaint.setStrokeWidth(mBoardLineWidth);
            invalidate();
            requestLayout();
        }
    }

    public void setBoardLineColor(int boardLineColor) {
        mBoardLineColor = boardLineColor;
        mLinePaint.setColor(mBoardLineColor);
        invalidate();
        requestLayout();
    }

    public void setCellColor(int cellColor) {
        mBoardCellColor = cellColor;
        mCellPaint.setColor(mBoardCellColor);
        invalidate();
        requestLayout();
    }

    public void setShipColor(int shipColor) {
        mShipCellColor = shipColor;
        invalidate();
        requestLayout();
    }

    public void setShipDamagedCellColor(int deadCellColor) {
        mShipDamagedCellColor = deadCellColor;
        invalidate();
        requestLayout();
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mTextPaint.setColor(mTextColor);
        invalidate();
        requestLayout();
    }

    public void setTextSize(float textSize){
        mTextSize = textSize;
        mTextPaint.setTextSize(textSize);
        invalidate();
        requestLayout();
    }
}
