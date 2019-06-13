package com.example.dell.klotski;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SelectActivity extends AppCompatActivity {

    //public int game1[][],game2[][],game3[][];
    public int game1[],game2[],game3[];
    public int imageli[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
//        game1 = new int[][]{{4, 1, 1, 5}, {4, 1, 1, 5}, {3, 2, 2, 6}, {3, 7, 8, 6}, {9, 0, 0, 10}};
//        game2 = new int[][]{{1,1,4,0},{1,1,4,0},{7,5,2,2},{3,5,8,6},{3,9,10,6}};
//        game3 = new int[][]{{9,1,1,10},{4,1,1,5},{4,2,2,5},{3,7,8,6},{3,0,0,6}};
        game1 = new int[]{4,1,1,5,4,1,1,5,3,2,2,6,3,7,8,6,9,0,0,10};
        game2 = new int[]{1,1,4,0,1,1,4,0,7,5,2,2,3,5,8,6,3,9,10,6};
        game3 = new int[]{9,1,1,10,4,1,1,5,4,2,2,5,3,7,8,6,3,0,0,6};
    }

    public void gameStart1(View view) {
        imageli = new int[]{4,1,5,3,2,6,7,8,9,0,0,10};
        Intent intent = new Intent();
        intent.setClass(SelectActivity.this,SecondActivity.class);
        intent.putExtra("game",game1);
        intent.putExtra("list",imageli);
        startActivity(intent);
    }

    public void gameStart2(View view) {
        imageli = new int[]{1,4,0,0,7,5,2,3,8,6,9,10};
        Intent intent = new Intent();
        intent.setClass(SelectActivity.this,SecondActivity.class);
        intent.putExtra("game",game2);
        intent.putExtra("list",imageli);
        startActivity(intent);
    }

    public void gameStart3(View view) {
        imageli = new int[]{9,1,10,4,5,2,3,7,8,6,0,0};
        Intent intent = new Intent();
        intent.setClass(SelectActivity.this,SecondActivity.class);
        intent.putExtra("game",game3);
        intent.putExtra("list",imageli);
        startActivity(intent);
    }
}
