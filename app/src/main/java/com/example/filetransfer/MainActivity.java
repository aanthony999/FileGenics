package com.example.filetransfer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements Runnable{

    private ImageView bgapp;

    private String hiduke="TT";
    private int id=1;
    private String errmsg="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgapp = (ImageView) findViewById(R.id.bgapp);
        scaleView(bgapp, 1f, .58f);
//        bgapp.animate().translationY(-1000).setDuration(800).setStartDelay(300);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                new Fragment_Home()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new Fragment_Home();
                            break;
                        case R.id.nav_chat:
                            selectedFragment = new Fragment_Chat();
                            break;
                        case R.id.nav_bluetooth:
                            selectedFragment = new Fragment_Transfer();
                            break;
                        case R.id.nav_qrcode:
                            selectedFragment = new Fragment_QR();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new Fragment_Settings();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, -1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    //connecting mysql db
    public void run() {
        System.out.println("Select Records Example by using the Prepared Statement!");
        Connection con = null;
        int count = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection
                    ("jdbc:mysql://XX.XX.XX.XX:3306/stasha_db?useUnicode=YES&characterEncoding=utf-8","YYYY","YYYY");
            try{
                String sql;
                sql
                        = "SELECT * FROM pet";
                PreparedStatement prest = con.prepareStatement(sql);
                //prest.setInt(1,1980);
                //prest.setInt(2,2004);
                ResultSet rs = prest.executeQuery();

                while (rs.next()){
                    //hiduke = rs.getString(1);
                    id = rs.getInt(1);
                    count++;
                    System.out.println(id + "\t" + "- " + hiduke);

                }
                System.out.println("Number of records: " + count);
                prest.close();
                con.close();
            }
            catch (SQLException s){
                System.out.println("SQL statement is not executed!");
                errmsg=errmsg+s.getMessage();
                id = -1;

            }
        }
        catch (Exception e){
            e.printStackTrace();
            errmsg=errmsg+e.getMessage();
            id = -2;
        }

        handler.sendEmptyMessage(0);

    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            TextView textView = (TextView) findViewById(R.id.textView0);
            textView.setText("hiduke="+hiduke+" price="+id+" "+errmsg);
        }
    };


}
