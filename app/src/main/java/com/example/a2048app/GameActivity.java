package com.example.a2048app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    public GameActivity() {
        gameActivity = this;
    }

    private double probability;
    private int id;
    private TextView gameScore;
    private int score;
    private TextView maxScore;
    private int max;
    private static GameActivity gameActivity = null;//不加null会空指针
    private Button reStart;

    private List<Map<String, Object>> totalList = new ArrayList<Map<String, Object>>();

    //    SharedPreferences myPreference;
//    SharedPreferences.Editor editor;
    private MySQLiteOpenHelper dbHelper = null;

    public static GameActivity getGameActivity() {
        return gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbHelper = new MySQLiteOpenHelper(this);

        Bundle bundle = getIntent().getExtras();
        probability = bundle.getDouble("probability");
        id = bundle.getInt("id");

        gameScore = (TextView) findViewById(R.id.game_score);
        score = 0;
        maxScore = (TextView) findViewById(R.id.max_score);

        reStart = (Button) findViewById(R.id.restart);

//        myPreference = getSharedPreferences("myPreference", Context.MODE_PRIVATE);
//        editor = myPreference.edit();
//        maxScore.setText(myPreference.getInt("max", 0) + "");
        String sql = "insert into tb_mycontacts(maxscore, id) values(?,?)";
        dbHelper.execData(sql, new Object[]{0, id});

        totalList.addAll(getMaxScore());
        max = Integer.parseInt(totalList.get(0).get("maxscore").toString());//从数据库中读出历史最高分
        maxScore.setText(totalList.get(0).get("maxscore").toString());

        reStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putDouble("probability", probability);
                bundle.putInt("id", id);
                intent.putExtras(bundle);//传递数据
                intent.setClass(GameActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }

    public double getProbability() {
        return probability;
    }

    public void showScore() {
        gameScore.setText(score + "");
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void addScore(int s) {
        score += s;
        showScore();
        if (score > max) {
            setMaxScore(score);
        }
    }

    private void setMaxScore(int m) {
        max = m;
//        editor.putInt("max", m);
        String sql = "update tb_mycontacts set maxscore=? where id =?";
        dbHelper.execData(sql, new Object[]{max, id});
        maxScore.setText(max + "");
    }

    private List<Map<String, Object>> getMaxScore() {
        return dbHelper.selectList("select * from tb_mycontacts where id = " + id, null);
    }

    public void play() {
        RingtoneManager rm = new RingtoneManager(this);//初始化系统声音
        Uri uri = rm.getDefaultUri(rm.TYPE_NOTIFICATION);//获取系统声音路径
        Ringtone mRingtone = rm.getRingtone(this, uri);//通过Uri来获取提示音的实例对象
        mRingtone.play();//播放
    }
}
