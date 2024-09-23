package com.example.omokapp.OmokBoards;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BoardView extends View {
    private int[][] board;
    private int lastCoordinate, width;
    private float cellSize, stoneSize, padding;
    private Paint linePaint, blackPaint, whitePaint;
    private OnBoardTouchListener onTouchListener;
    private static final int BOARD_SIZE = 15;

    // dynamic constructer, when call from class
    public BoardView(Context context) {
        super(context);
        init();
    }

    // static constructer, when call from xml
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        board = new int[BOARD_SIZE][BOARD_SIZE];
        lastCoordinate = -1;

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(4);

        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
    }

    public void setOnTouchListener(OnBoardTouchListener listener){
        this.onTouchListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
        cellSize = width / (float)BOARD_SIZE;
        stoneSize = cellSize / 2.1f;
        padding = cellSize / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // set background color
        canvas.drawColor(Color.GRAY);

        // draw lines
        for (int i = 0; i < BOARD_SIZE; i++) {
            canvas.drawLine(padding, i * cellSize + padding, width - padding, i * cellSize + padding, linePaint);
            canvas.drawLine(i * cellSize + padding, padding, i * cellSize + padding, width - padding, linePaint);
        }

        // draw stones
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != 0) {
                    float centerX = i * cellSize + padding;
                    float centerY = j * cellSize + padding;
                    if (board[i][j] % 2 != 0)
                        canvas.drawCircle(centerX, centerY, stoneSize, blackPaint);
                    else canvas.drawCircle(centerX, centerY, stoneSize, whitePaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            int row = (int) ((x) / cellSize);
            int col = (int) ((y) / cellSize);
            Log.d("BoardView", "click: "+row+", "+col);
            lastCoordinate = row * BOARD_SIZE + col;
            if(onTouchListener != null)
                onTouchListener.onTouch(lastCoordinate);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public int getLastCoordinate(){ return lastCoordinate; }

    public void refresh(int[][] arr){
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                board[i][j] = arr[i][j];
        invalidate();
    }
    public void refresh(int x, int y, int v){
        board[x][y] = v;
        invalidate();
    }
    public void refresh(int coordinate, int v){
        refresh(coordinate/15, coordinate%15, v);
    }
}
