package com.fandean.testactivity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fandean.testactivity.R;
import com.fandean.testactivity.ui.dummy.DummyContent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ItemFragment.OnListFragmentInteractionListener {

    FragmentManager mManager;
    android.support.v4.app.Fragment mFragment;
    //定位服务类
    //AMapLocationClient是定位*服务*类，可以启动定位、停止定位以及销毁定位
    private AMapLocationClient mAMapLocationClient;
    //定位参数
    private AMapLocationClientOption mAMapLocationClientOption;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //用于显示定位信息
        final TextView textView = (TextView) findViewById(R.id.map_text);

//        初始化定位
        mAMapLocationClient = new AMapLocationClient(getApplicationContext());
        mAMapLocationClient.setLocationListener(new AMapLocationListener() {
            //获取定位结果
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null){
                    if (aMapLocation.getErrorCode() == 0){
                        textView.setText("纬度：" + aMapLocation.getLatitude() + "\n" +
                        "经度："  + aMapLocation.getLongitude() + "\n" +
                        aMapLocation.getAddress() + "\n" +
                        aMapLocation.getProvince() + "\n" +
                        aMapLocation.getCity());

                    } else {
                        textView.setText("定位失败， ErroCode: "
                                + aMapLocation.getErrorCode() + ", errInfo: "
                        + aMapLocation.getErrorInfo());
                    }
                }

            }
        });


        //定位参数设置
        mAMapLocationClientOption = new AMapLocationClientOption();
        //高准确性
        mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
        //调用方法startLocation()后，开始异步获取定位数据
        mAMapLocationClient.startLocation();


        mManager = getSupportFragmentManager();
        mFragment = mManager.findFragmentById(R.id.fragment_container);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.fragment_container);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);

/*        String url =
                "https://img3.doubanio.com/view/movie_poster_cover/spst/public/p2459944375.webp";
        Glide.with(this)
                .load(url)
                .thumbnail(0.5f)
                .into(imageView);*/


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this,ScrollingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this,Main2Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this,MapActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            if (mFragment == null){
                mCoordinatorLayout.removeAllViews();
                mFragment = new ItemFragment();
                mManager.beginTransaction()
                        .add(R.id.fragment_container,mFragment)
                        .commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAMapLocationClient != null){
            mAMapLocationClient.onDestroy();
            mAMapLocationClient = null;
            mAMapLocationClientOption = null;
        }
    }
}
