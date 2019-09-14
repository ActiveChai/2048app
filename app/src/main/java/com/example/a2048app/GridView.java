package com.example.a2048app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class GridView extends GridLayout {

    public GridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridView(Context context) {
        super(context);
        init();
    }

    private void init() {

        setColumnCount(4);

        setOnTouchListener(new OnTouchListener() {

            private float startX, startY;//按下坐标
            private float offsetX, offsetY;//偏移值

            @Override
            public boolean onTouch(View v, MotionEvent event) {//手势判断

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {//左右滑动
                            if (offsetX < 0) {
                                slideLeft();
                                System.out.println("left");
                            } else if (offsetX > 0) {
                                slideRight();
                                System.out.println("right");
                            }
                        } else if (Math.abs(offsetX) < Math.abs(offsetY)) {//上下滑动（安卓坐标系y轴向下为正）
                            if (offsetY < 0) {
                                slideUp();
                                System.out.println("up");
                            } else if (offsetY > 0) {
                                slideDown();
                                System.out.println("down");
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int cardWidth = (w - 8) / 4;
        int cardHeight = (w - 8) / 4;
        addCards(cardWidth, cardHeight);
        startGame();
    }

    private Grid[][] cardsMap = new Grid[4][4];

    private void addCards(int cardWidth, int cardHeight) {

        Grid grid;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                grid = new Grid(getContext());
                grid.setNum(0);
                addView(grid, cardWidth, cardHeight);

                cardsMap[x][y] = grid;
            }

        }
    }

    private List<Point> emptyPoints = new ArrayList<Point>();//空卡片坐标

    private void addRandomNum() {//添加随机数

        emptyPoints.clear();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));//随机移除一个空卡片并返回
        cardsMap[p.x][p.y].setNum(Math.random() > GameActivity.getGameActivity().getProbability() ? 2 : 4);
    }

    public void startGame() {
        GameActivity.getGameActivity().clearScore();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();//开始游戏时添加两个随机数
    }

    private void slideUp() {
        boolean flag = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                boolean mid = false;//判断中间是否有值
                for (int y1 = y + 1; y1 < 4; y1++) {//往下依次遍历
                    if (cardsMap[x][y1].getNum() > 0) {//遍历值非空
                        if (cardsMap[x][y].getNum() == 0) {//当前 y 值为空
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());//交换，此时不需要判断，否则弹框有问题
                            cardsMap[x][y1].setNum(0);
                            flag = true;
                        } else if (!mid && cardsMap[x][y].isEqual(cardsMap[x][y1])) {
                            GameActivity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            flag = true;
                            break;//只能合并一次
                        } else {
                            mid = true;
                        }
                    }
                }
            }
        }
        if (flag) {
            GameActivity.getGameActivity().play();
            addRandomNum();
            judgeEnd();//一定要放这，弹框个数问题
        }
    }

    private void slideDown() {
        boolean flag = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                boolean mid = false;//判断中间是否有值
                for (int y1 = y - 1; y1 >= 0; y1--) {//往下依次遍历
                    if (cardsMap[x][y1].getNum() > 0) {//遍历值非空
                        if (cardsMap[x][y].getNum() == 0) {//当前 y 值为空
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());//交换
                            cardsMap[x][y1].setNum(0);
                            flag = true;
                        } else if (!mid && cardsMap[x][y].isEqual(cardsMap[x][y1])) {
                            GameActivity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            flag = true;
                            break;
                        } else {
                            mid = true;
                        }
                    }
                }
            }
        }
        if (flag) {
            GameActivity.getGameActivity().play();
            addRandomNum();
            judgeEnd();
        }
    }

    private void slideLeft() {
        boolean flag = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                boolean mid = false;//判断中间是否有值
                for (int x1 = x + 1; x1 < 4; x1++) {//往右依次遍历
                    if (cardsMap[x1][y].getNum() > 0) {//遍历值非空
                        if (cardsMap[x][y].getNum() == 0) {//当前 x 值为空
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());//交换
                            cardsMap[x1][y].setNum(0);
                            flag = true;
                        } else if (!mid && cardsMap[x][y].isEqual(cardsMap[x1][y])) {
                            GameActivity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            flag = true;
                            break;
                        } else {
                            mid = true;
                        }
                    }
                }
            }
        }
        if (flag) {
            GameActivity.getGameActivity().play();
            addRandomNum();
            judgeEnd();
        }
    }

    private void slideRight() {
        boolean flag = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                boolean mid = false;//判断中间是否有值
                for (int x1 = x - 1; x1 >= 0; x1--) {//往左依次遍历
                    if (cardsMap[x1][y].getNum() > 0) {//遍历值非空
                        if (cardsMap[x][y].getNum() == 0) {//当前 x 值为空
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());//交换
                            cardsMap[x1][y].setNum(0);
                            flag = true;
                        } else if (!mid && cardsMap[x][y].isEqual(cardsMap[x1][y])) {
                            GameActivity.getGameActivity().addScore(cardsMap[x][y].getNum());
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            flag = true;
                            break;
                        } else {
                            mid = true;
                        }
                    }
                }
            }
        }
        if (flag) {
            GameActivity.getGameActivity().play();
            addRandomNum();
            judgeEnd();
        }
    }

    private boolean isEnd(int x, int y) {
        return cardsMap[x][y].getNum() == 32;
    }

    private void judgeEnd() {
        boolean end = false;
        for (int y = 0; y < 4; y++) {
            if (!end) {
                for (int x = 0; x < 4; x++) {
                    if (!end && isEnd(x, y) || emptyPoints.size() == 0) {
                        end = true;
                        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                .setTitle("游戏结束")
                                .setPositiveButton("重新开始游戏", new DialogInterface.OnClickListener() {//添加普通按钮
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startGame();
                                    }
                                })
                                .create();
                        alertDialog.show();
                        break;
                    }
                }
            }
        }
    }
}
