package com.example.a2048app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button level1 = (Button) findViewById(R.id.level1);
        Button level2 = (Button) findViewById(R.id.level2);

        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putDouble("probability", 0.2);
                bundle.putInt("id", 1);
                intent.putExtras(bundle);//传递数据
                intent.setClass(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putDouble("probability", 1);
                bundle.putInt("id", 2);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
