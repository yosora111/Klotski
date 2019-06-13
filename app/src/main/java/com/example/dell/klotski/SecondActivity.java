package com.example.dell.klotski;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "DRAG";

    public int matrix[][];
    public int game[];
    public int imageli[];
    private int height;
    private int width;
    public int unitX,unitY;
    private GridLayout myGridLayout;
    public ArrayList<ImageView> imageList;
    //public DragGridLayout dragGridLayout;
    public ImageView LiuBei,GuanYu,ZhangFei,ZhuGeLiang,ZhaoYun,MaChao,Zu1,Zu2,Zu3,Zu4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        game = intent.getIntArrayExtra("game");
        imageli = intent.getIntArrayExtra("list");
        System.out.println();
        myGridLayout = findViewById(R.id.myGridLayout);
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        imageList = new ArrayList<>();
        width = dm.widthPixels;
        height = dm.heightPixels;
        LiuBei = findViewById(R.id.liubei);//1
        GuanYu = findViewById(R.id.guanyu);//2
        ZhangFei = findViewById(R.id.zhangfei);//3
        ZhuGeLiang = findViewById(R.id.zhugeliang);//4
        ZhaoYun = findViewById(R.id.zhaoyun);//5
        MaChao = findViewById(R.id.machao);//6
        Zu1 = findViewById(R.id.zu1);//7
        Zu2 = findViewById(R.id.zu2);//8
        Zu3 = findViewById(R.id.zu3);//9
        Zu4 = findViewById(R.id.zu4);//10
        initImage();
        for (int i=0;i<imageList.size();i++){
            final ImageView image = imageList.get(i);
            image.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent();
                    ClipData clipData = ClipData.newIntent("label",intent);
                    view.startDrag(null,new View.DragShadowBuilder(view),image,0);
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                    return true;
                }
            });
        }

        setNewMatrix();
        myGridLayout.removeAllViews();
        for (int i=0;i<12;i++){
            if (imageli[i]>0)
                myGridLayout.addView(imageList.get(imageli[i]-1));
            else{
                ImageView voidView = new ImageView(this);
                voidView.setAdjustViewBounds(true);
                voidView.setMaxHeight(height/5);
                voidView.setMaxWidth(width/4);
                voidView.setImageResource(R.drawable.kong);
                myGridLayout.addView(voidView);
            }
        }
        myGridLayout.setOnDragListener(new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                float posX=0,posY=0;
                int X,Y;
                ImageView localState = (ImageView) event.getLocalState();
                X = (int)localState.getLeft()*4/myGridLayout.getWidth();
                Y = (int)localState.getTop()*5/myGridLayout.getHeight();
                unitX = myGridLayout.getWidth()/4-10;
                unitY = myGridLayout.getHeight()/5-10;
                draw();
                Log.i(TAG, "X: "+X+"   Y: "+Y);
                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.i(TAG, "开始拖拽");
                        //localState.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.i(TAG, "结束拖拽");
                        //localState.setVisibility(View.VISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.i(TAG, "拖拽的view进入监听的view时");
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.i(TAG, "拖拽的view离开监听的view时");
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        posX = event.getX();
                        posY = event.getY();
                        Log.i(TAG, "拖拽的view在BLUE中的位置:x ="+posX);
                        break;
                    case DragEvent.ACTION_DROP:
                        posX = event.getX();
                        posY = event.getY();
                        posX = posX - localState.getWidth()/4;
                        posY = posY - localState.getHeight()/4;
                        int x,y;
                        x = (int)(posX*4/myGridLayout.getWidth());
                        y = (int)(posY*5/myGridLayout.getHeight());
                        Log.i(TAG, "释放拖拽的view x: "+x+"  y:"+y);
//                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                        params.columnSpec = GridLayout.spec(x, 1);
//                        params.rowSpec = GridLayout.spec(y, 1);
//                        ((ViewGroup)localState.getParent()).removeView(localState);
//                        myGridLayout.addView(localState,params);
                        int sizeX,sizeY;
                        sizeX = localState.getWidth()/unitX;
                        sizeY = localState.getHeight()/unitY;
                        Log.i(TAG,"SIZE: "+sizeX+" "+sizeY);
                        if (canDrag(sizeX,sizeY,X,Y,x,y)){
                            swap(sizeX,sizeY,X,Y,x,y);
                            print();
                            draw();
                            isWin();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "不能移动！",
                                    Toast.LENGTH_SHORT).show();
                        }

                        break;
                }
                return true;
            }
        });
        //draw(tempx,tempy);

//        dragGridLayout = new DragGridLayout(this,imageList);
//        GridLayout myGridLayout = findViewById(R.id.myGridLayout);
//        dragGridLayout.initImage();
//        dragGridLayout.setCanDrag();
//        myGridLayout.removeView(Zu3);
//        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//        params.columnSpec = GridLayout.spec(2, 1);
//        params.rowSpec = GridLayout.spec(4, 1);
        //myGridLayout.addView(Zu3,params);
    }
    public void initImage(){
        matrix = new int[][]{{4, 1, 1, 5}, {4, 1, 1, 5}, {3, 2, 2, 6}, {3, 7, 8, 6}, {9, 0, 0, 10}};
        LiuBei.setAdjustViewBounds(true);
        LiuBei.setMaxHeight(2*height/5);
        LiuBei.setMaxWidth(width/2);
        imageList.add(LiuBei);
        GuanYu.setAdjustViewBounds(true);
        GuanYu.setMaxHeight(height/5);
        GuanYu.setMaxWidth(width/2);
        imageList.add(GuanYu);
        ZhangFei.setAdjustViewBounds(true);
        ZhangFei.setMaxHeight(2*height/5);
        ZhangFei.setMaxWidth(width/4);
        imageList.add(ZhangFei);
        ZhuGeLiang.setAdjustViewBounds(true);
        ZhuGeLiang.setMaxHeight(2*height/5);
        ZhuGeLiang.setMaxWidth(width/4);
        imageList.add(ZhuGeLiang);
        ZhaoYun.setAdjustViewBounds(true);
        ZhaoYun.setMaxHeight(2*height/5);
        ZhaoYun.setMaxWidth(width/4);
        imageList.add(ZhaoYun);
        MaChao.setAdjustViewBounds(true);
        MaChao.setMaxHeight(2*height/5);
        MaChao.setMaxWidth(width/4);
        imageList.add(MaChao);
        Zu1.setAdjustViewBounds(true);
        Zu1.setMaxHeight(height/5);
        Zu1.setMaxWidth(width/4);
        imageList.add(Zu1);
        Zu2.setAdjustViewBounds(true);
        Zu2.setMaxHeight(height/5);
        Zu2.setMaxWidth(width/4);
        imageList.add(Zu2);
        Zu3.setAdjustViewBounds(true);
        Zu3.setMaxHeight(height/5);
        Zu3.setMaxWidth(width/4);
        imageList.add(Zu3);
        Zu4.setAdjustViewBounds(true);
        Zu4.setMaxHeight(height/5);
        Zu4.setMaxWidth(width/4);
        imageList.add(Zu4);
    }

    public void setNewMatrix(){
        for (int i=0;i<5;i++)
            for (int j=0;j<4;j++){
                matrix[i][j] = game[i*4+j];
            }
    }

    public void swap(int sizeX,int sizeY,int X,int Y,int x,int y){//X初始，x改变
        int temp[][] = new int[sizeY][sizeX];
        for (int i=0;i<sizeY;i++)
            for (int j=0;j<sizeX;j++)
                temp[i][j] = matrix[Y+i][X+j];
        for (int i=0;i<sizeY;i++){
            for (int j=0;j<sizeX;j++){
                matrix[Y+i][X+j] = 0;
            }
        }
        for (int i=0;i<sizeY;i++)
            for (int j=0;j<sizeX;j++)
                matrix[y+i][x+j] = temp[i][j];
    }

    public void draw(){
        int item_weight,item_height;
        myGridLayout.removeAllViews();
        int zeros[][] = new int[5][4];
        for (int i=0;i<5;i++){
            for (int j=0;j<4;j++){
                if(zeros[i][j]==0){
                    if(matrix[i][j]==0){
                        ImageView voidView = new ImageView(this);
                        voidView.setAdjustViewBounds(true);
                        voidView.setMaxHeight(height/5);
                        voidView.setMaxWidth(width/4);
                        voidView.setImageResource(R.drawable.kong);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.columnSpec = GridLayout.spec(j,1);
                        params.rowSpec = GridLayout.spec(i,1);
                        myGridLayout.addView(voidView,params);
                    }
                    else{
                        item_weight = imageList.get(matrix[i][j]-1).getWidth()/unitX;
                        item_height = imageList.get(matrix[i][j]-1).getHeight()/unitY;
                        ImageView imageView = imageList.get(matrix[i][j]-1);
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.columnSpec = GridLayout.spec(j,item_weight);
                        params.rowSpec = GridLayout.spec(i,item_height);
                        myGridLayout.addView(imageView,params);
                        for (int m=0;m<item_height;m++)
                            for (int n=0;n<item_weight;n++)
                                zeros[i+m][j+n] = matrix[i][j];
                    }
                }
            }
        }
    }

    public boolean canDrag(int sizeX,int sizeY,int X,int Y,int x,int y){
        boolean isCanDrag = true;
        int num = matrix[Y][X];
        for (int i=0;i<sizeY;i++)
            for (int j=0;j<sizeX;j++){
                if (matrix[y+i][x+j]==0||matrix[y+i][x+j]==num){

                }
                else{
                    return false;
                }
            }
        return isCanDrag;
    }

    public void isWin(){
        if (matrix[3][1]==1&&matrix[4][2]==1){
            AlertDialog.Builder ab=new AlertDialog.Builder(this);
            ab.setTitle("恭喜");
            ab.setMessage("成功通关！");
            ab.setPositiveButton("text",new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ab.show();
        }
    }

    public void print(){
        for(int i=0;i<5;i++){
            for (int j=0;j<4;j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }
}
