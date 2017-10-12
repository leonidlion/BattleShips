package com.anull.dev.leo.battleships.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.anull.dev.leo.battleships.GameField;
import com.anull.dev.leo.battleships.R;

public class GameBoard extends View {
    private static final int DEFAULT_BOARD_LINE_WIDTH = 2;
    private static final int DEFAULT_BOARD_LINE_COLOR = Color.GRAY;
    private static final int DEFAULT_SHIP_CELL_COLOR = Color.RED;
    private static final int DEFAULT_EMPTY_CELL_COLOR = Color.BLUE;
    private static final int DEFAULT_TEXT_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_SIZE = 22;

    private static final int MIN_SIZE_VIEW = 500;
    private static final int CELL_COUNT = 11;
    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
    };

    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mCellPaint;

    private int mBoardLineWidth;
    private int mBoardLineColor;
    private int mBoardCellColor;
    private int mShipCellColor;
    private int mTextColor;
    private float mTextSize;

    private GameField mGameField;

    public GameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaints();

        if (attrs != null){
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoard, 0,0);
            setBoardLineWidth(a.getInt(R.styleable.GameBoard_board_line_width, DEFAULT_BOARD_LINE_WIDTH));
            setBoardLineColor(a.getInt(R.styleable.GameBoard_board_line_color, DEFAULT_BOARD_LINE_COLOR));
            setCellColor(a.getInt(R.styleable.GameBoard_cell_color, DEFAULT_EMPTY_CELL_COLOR));
            setShipColor(a.getInt(R.styleable.GameBoard_ship_cell_color, DEFAULT_SHIP_CELL_COLOR));
            setTextColor(a.getInt(R.styleable.GameBoard_text_color, DEFAULT_TEXT_COLOR));
            setTextSize(a.getDimension(R.styleable.GameBoard_text_size, DEFAULT_TEXT_SIZE));
            a.recycle();
        }

        initGameField();
    }

    private void initPaints(){
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mBoardLineColor);
        mLinePaint.setStrokeWidth(mBoardLineWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mCellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCellPaint.setColor(mBoardCellColor);
    }

    private void initGameField(){
        mGameField = new GameField();
        mGameField.setColors(mBoardCellColor, mShipCellColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cellSize = getMeasuredWidth() / CELL_COUNT;
        drawLetters(canvas, cellSize);
        drawNumbers(canvas, cellSize);
        drawGameBoard(canvas, cellSize);
        drawShips(canvas);
    }

    private void drawLetters(Canvas canvas, int cellSize){
        for (int i = 0; i < 10; i++){
            Rect rect = new Rect();
            mTextPaint.getTextBounds(LETTERS[i], 0, LETTERS[i].length(), rect);
            int textWidth = rect.width();
            int startX = (cellSize * (i + 1)) + cellSize / 2 - textWidth / 2;
            int startY = cellSize / 2;
            canvas.drawText(LETTERS[i], startX, startY, mTextPaint);
        }
    }

    private void drawNumbers(Canvas canvas, int cellSize){
        for (int i = 1; i <= 10; i++){
            Rect rect = new Rect();
            String number = String.valueOf(i);
            mTextPaint.getTextBounds(number, 0, number.length(), rect);
            int textWidth = rect.width();
            int startX = cellSize / 2 - textWidth / 2;
            int startY = cellSize * (i + 1) - cellSize / 2 + rect.height()/2;
            canvas.drawText(number, startX, startY, mTextPaint);
        }
    }

    private void drawGameBoard(Canvas canvas, int cellSize){
        for (int i = 1; i < 11; i++){
            for (int j = 1; j < 11; j++){
                int left = j * cellSize;
                int top = i * cellSize;
                int right = (j + 1) * cellSize;
                int bottom = (i + 1) * cellSize;
                Rect rect = new Rect(left, top, right, bottom);
                canvas.drawRect(rect, mLinePaint);
                mGameField.addCell(i-1, j-1);
            }
        }
    }

    private void drawShips(Canvas canvas){
        int cellSize = getMeasuredWidth() / CELL_COUNT;
        mGameField.generateShips();
        for (int i = 1; i < 11; i++){
            for (int j = 1; j < 11; j++){
                int left = j * cellSize + mBoardLineWidth;
                int top = i * cellSize + mBoardLineWidth;
                int right = (j + 1) * cellSize - mBoardLineWidth;
                int bottom = (i + 1) * cellSize - mBoardLineWidth;
                mCellPaint.setColor(mGameField.getCellColor(i-1, j-1));
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
