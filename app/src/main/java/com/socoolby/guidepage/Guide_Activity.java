package com.socoolby.guidepage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.socoolby.guidepage.unit.Func;
import com.socoolby.guidepage.unit.FuncInt;

/**
 * Created by socoolby on 12/18/13.
 */
public class Guide_Activity extends Activity implements View.OnClickListener{
    private ImageView[] tips;
    private View[] Views;
    private int lastX = 0;
    private int currentIndex;
    private boolean isGoToMain=false;
    ViewPager viewPager=null;
    private int[] colors={0xecf0f1,0x3998db,0x5bbc9d,0x34495e,0x34495e};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuncInt.init(this);

        setContentView(R.layout.activity_guid);
        Button btn_guid_gotomain = (Button) findViewById(R.id.btn_guid_gotomain);
        btn_guid_gotomain.setOnClickListener(this);
        ViewGroup group = (ViewGroup)findViewById(R.id.vg_guid_main);
        viewPager = (ViewPager)findViewById(R.id.vp_guild_main);

        tips = new ImageView[4];
        Views=new View[4];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            LinearLayout.LayoutParams params1 =
                    new LinearLayout.LayoutParams(10, 10);
            params1.setMargins(0,0,10,0);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            imageView.setLayoutParams(params1);
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            group.addView(imageView);
        }
        Views[0]=(View) LayoutInflater.from(this).inflate(R.layout.activity_guid_fram1,null);
        Views[1]=(View) LayoutInflater.from(this).inflate(R.layout.activity_guid_fram2,null);
        Views[2]=(View) LayoutInflater.from(this).inflate(R.layout.activity_guid_fram3,null);
        Views[3]=(View) LayoutInflater.from(this).inflate(R.layout.activity_guid_fram4,null);

        LinearLayout lay_guid_fram4_buttom=(LinearLayout)Views[3].findViewById(R.id.lay_guid_fram4_buttom);


        RelativeLayout.LayoutParams relParams=new  RelativeLayout.LayoutParams(FuncInt.screenWidth,FuncInt.screenHeight-FuncInt.screenWidth);
        relParams.setMargins(0,FuncInt.screenWidth,0,0);

        lay_guid_fram4_buttom.setLayoutParams(relParams );

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:

//                        Func.Sysout("GuidActivity viewPage Move X"+event.getX()+" Y:"+event.getY()+"currIndex:"+ currentIndex);
                        if ((lastX - event.getX()) > 100 && (currentIndex == Views.length - 1)) {
//                            gotoMain();
                        }

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        viewPager.setAdapter(new ListTitleAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                Func.Sysout("viewPage i"+i+" v:"+v+" i2:"+i2);
                int startColor=colors[i];
                int stopColor=colors[i+1];

                int c=calcColor(startColor, stopColor, (int) (v * 100));
                viewPager.setBackgroundColor(c);
            }

            @Override
            public void onPageSelected(int i) {
                Func.Sysout("viewPage  viewPage Pagechange:"+i);
                currentIndex=i;
                setImageBackground(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                Func.Sysout("viewPage       onPageScrollStateChanged:"+i);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_guid_gotomain:
                    gotoMain();
                break;
        }
    }

    private synchronized void gotoMain()
    {
        if(!isGoToMain)
        {
            isGoToMain=true;
            startActivity(new Intent(Guide_Activity.this, MainActivity.class));
            finish();
        }

    }
    private class ListTitleAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(Views[position]);

        }
        @Override
        public Object instantiateItem(View container, int position) {
            View view=(Views[position]);
            ((ViewPager)container).addView(view, 0);
            return view;
        }

    }
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    private  int calcColor(int startColor,int stopColor,int percent)
    {
        Func.Sysout("viewPage startColor:" + startColor + " stopColor:" + stopColor+" percent:"+percent);
        int tempColor=0;
        float r=0,g=0,b=0;
        int start_rgb[]=retrieveRGBComponent(startColor);
        int stop_rgb[]=retrieveRGBComponent(stopColor);
        r=((float)stop_rgb[0]-(float)start_rgb[0])/100f;
        g=((float)stop_rgb[1]-(float)start_rgb[1])/100f;
        b=((float)stop_rgb[2]-(float)start_rgb[2])/100f;
        Func.Sysout("r:"+r+" g:"+g+" b:"+b);
        stop_rgb[0] =(int)(start_rgb[0]+r*percent);
        stop_rgb[1] =(int)(start_rgb[1]+g*percent);
        stop_rgb[2] =(int)(start_rgb[2]+b*percent);
        Func.Sysout("result r:"+stop_rgb[0]+" g:"+stop_rgb[1]+" b:"+stop_rgb[2]);
        tempColor=generateFromRGBComponent(stop_rgb);
        Func.Sysout("viewPage Return Color:" + tempColor);

        return Color.argb(255,stop_rgb[0],stop_rgb[1],stop_rgb[2]);

    }
    private  int[] retrieveRGBComponent(int color) {
        int[] rgb = new int[3];
        rgb[0] = (color & 0x00ff0000) >> 16;
        rgb[1] = (color & 0x0000ff00) >> 8;
        rgb[2] = (color & 0x000000ff);
        return rgb;
    }
    private  int generateFromRGBComponent(int[] rgb) {
        if (rgb == null || rgb.length != 3 || rgb[0] < 0 || rgb[0] > 255 || rgb[1] < 0
                || rgb[1] > 255 || rgb[2] < 0 || rgb[2] > 255)
            return 0xfffff;
        return rgb[0] << 16 | rgb[1] << 8 | rgb[2];
    }

}
