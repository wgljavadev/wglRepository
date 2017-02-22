package com.chinaweather.android;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaweather.android.gson.Forecast;
import com.chinaweather.android.gson.Weather;
import com.chinaweather.android.util.Concast;
import com.chinaweather.android.util.HttpUtil;
import com.chinaweather.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各控件实例
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        /**
         * 尝试从本地缓存中读取天气信息。那么第一次肯定是没有的，
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null) {
            Log.d("WeatherActivity",weatherString);
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else {
            //缓存时去服务器查询天气
            String weatherId = getIntent().getStringExtra("weather_id");
            Log.d("WeatherweatherId",weatherId);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);  //从服务器获取数据。请求数据，先将天气界面隐藏了， 否则空数据看起来奇怪。
        }
    }

    /**
     * 根据天气id请求城市天气信息。
     * @param weatherId
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+
                weatherId+"&key="+ Concast.key;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this , "获取天气信息失败", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                //将请求的结果转换为Weather对象。
                final Weather weather = Utility.handleWeatherResponse(responseText);
                Log.d("Weather",weather.toString());
                //将当前线程切换到主线程中。
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            //如果返回成功， 则将返回的数据缓存到SharedPreferences当中。
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this)
                                            .edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            //进行内容显示。
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 处理并展示实体中的数据， 从Weather实体中获取数据， 显示在相应的控件上。
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews(); //从这个现实布局中移除所有的视图。
//        //展示未来几天的天气信息。
//        for (Forecast forecast : weather.forecastList) {
//            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
//            TextView dateText  = (TextView)view.findViewById(R.id.date_text);
//            TextView infoText = (TextView)view.findViewById(R.id.info_text);
//            TextView maxText = (TextView)view.findViewById(R.id.max_text);
//            TextView minText = (TextView) findViewById(R.id.min_text);
//            dateText.setText(forecast.data);
//            infoText.setText(forecast.more.info);
//            maxText.setText(forecast.temperature.max);
//            minText.setText(forecast.temperature.min);
//            forecastLayout.addView(view);
//        }
        if (weather.aqi != null ) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数："+weather.suggestion.carWash.info;
        String sport = "运动建议："+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        //设置完数据后， 将天气布局设置为可见。
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
