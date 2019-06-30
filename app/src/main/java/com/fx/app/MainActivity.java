package com.fx.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fx.switcher.Switcher;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switcher switcher = findViewById(R.id.mySwitch);
        switcher.setSwitchBackgroundColor(getResources().getColor(R.color.colorPrimary));
        switcher.setState(true);
        switcher.setOnClickSwitch(new Switcher.OnClickSwitch() {

            @Override
            public void switchOn() {
                System.out.println("On");
            }

            @Override
            public void switchOff() {
                System.out.println("Off");
            }
        });


    }
}
