package com.example.imagepro;

import android.os.Bundle;


import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class page1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.page1);



        Button btn1 = (Button)findViewById(R.id.pbtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;
                switch (btn.getId()) {
                    case R.id.pbtn:
                        System.out.println("666");
                        page1.this.finish();
                    break;
                }

            }
        });

    }

//    public void pbtnc(View view) {
//        this.setContentView(R.layout.activity_main);
//    }

//    @Override
//    public boolean onBackPressed() {
//        return true;
//    }

}