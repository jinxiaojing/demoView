package com.talkplus.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.talkplus.mylibrary.demoView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private com.talkplus.mylibrary.demoView demoView;
    private EditText et_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        demoView = findViewById(R.id.dv_view);
        et_view = findViewById(R.id.et_view);
        findViewById(R.id.bt_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_view.getText() != null && !et_view.getText().toString().equals("")) {
                    demoView.setSelect(Integer.parseInt(et_view.getText().toString()));
                }
            }
        });
        List<String> oLists = new ArrayList<>();
        List<String> iLists = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            oLists.add(String.valueOf(i));
            iLists.add("$ " + i);
        }
        demoView.setOutsideList(oLists);
        demoView.setInsideList(iLists);
        demoView.setSelect(3);
    }
}
