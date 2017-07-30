package com.fandean.testactivity.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.fandean.testactivity.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener{
    //用于显示地图的View

    private MapView mMapView;
    //地图View中的地图对象
    private AMap mAMap;

    //定位需要的声明
    //定位客户端
    private AMapLocationClient mLocationClient;
    //定位参数
    private AMapLocationClientOption mLocationClientOption;
    //定位监听器
    private OnLocationChangedListener mLocationChangedListener;
    private boolean isFirstLoc=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

//        [Android开发之高德地图实现定位 - 简书](http://www.jianshu.com/p/c3dc0cea0a2d "Android开发之高德地图实现定位 - 简书")
//        [利用高德地图API打造基于LBS的Android应用 - 简书](http://www.jianshu.com/p/6a4d17a342b7 "利用高德地图API打造基于LBS的Android应用 - 简书")
        mMapView = (MapView) findViewById(R.id.map);
        //获取地图对象
        mAMap = mMapView.getMap();

        //设置显示定位按钮 并且可以点击
        UiSettings settings = mAMap.getUiSettings();
        //设置定位监听
        mAMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        mAMap.setMyLocationEnabled(true);

        //开始定位
        initLoc();
    }

    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationClientOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationClientOption);
        //启动定位
        mLocationClient.startLocation();
    }


    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mLocationChangedListener.onLocationChanged(amapLocation);
                    //添加图钉
                    mAMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("这里好火");
        //设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangedListener = onLocationChangedListener;
    }
    //停止定位
    @Override
    public void deactivate() {
        mLocationChangedListener = null;
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
