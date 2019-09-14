package com.example.a2048app;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Grid extends FrameLayout {

    public Grid(Context context) {
        super(context);

        label = new TextView(getContext());
        label.setTextSize(32);
        label.setGravity(Gravity.CENTER);//文字居中
        label.setBackgroundColor(0xffffffff);

        LayoutParams lp = new LayoutParams(-1, -1);//FrameLayout布局参数
        lp.setMargins(8, 8, 8, 8);

        addView(label, lp);//添加控件
    }

    private int num = 0;
    private TextView label;

    public void setNum(int num) {
        this.num = num;

        if (num == 0) {
            label.setText("");
        } else {
            label.setText(num + "");
        }
    }

    public int getNum() {
        return num;
    }

    public boolean isEqual(Grid grid) {
        return getNum() == grid.getNum();
    }

}
