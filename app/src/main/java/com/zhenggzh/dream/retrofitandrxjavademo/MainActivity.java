package com.zhenggzh.dream.retrofitandrxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhenggzh.dream.retrofitandrxjavademo.bean.BaseRequestBean;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherRequestBean;
import com.zhenggzh.dream.retrofitandrxjavademo.bean.WeatherResponseBean;
import com.zhenggzh.dream.retrofitandrxjavademo.download.DownloadActivity;
import com.zhenggzh.dream.retrofitandrxjavademo.netsubscribe.WeatherSubscribe;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultListener;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.OnSuccessAndFaultSub;
import com.zhenggzh.dream.retrofitandrxjavademo.netutils.RetrofitFactory;
import com.zhenggzh.dream.retrofitandrxjavademo.operator.RxOperatorMainActivity;
import com.zhenggzh.dream.retrofitandrxjavademo.utils.GsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_getDate)
    Button btnGetDate;
    @BindView(R.id.et_input_city)
    EditText etInputCity;
    @BindView(R.id.tv_city_wather)
    TextView tvCityWather;
    @BindView(R.id.btn_Operator)
    Button btnOperator;
    @BindView(R.id.btn_download)
    AppCompatButton btnDownload;

    private String mCityName = "北京";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mCityName = etInputCity.getText().toString();
    }

    @OnClick({R.id.btn_getDate, R.id.btn_Operator, R.id.btn_download})
    public void onClickButton(Button button) {
        switch (button.getId()) {
            case R.id.btn_getDate:
                getWeatherData();
                break;
            case R.id.btn_Operator:
                startActivity(new Intent(MainActivity.this, RxOperatorMainActivity.class));
                break;
            case R.id.btn_download:
                startActivity(new Intent(MainActivity.this, DownloadActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 请求天气预报的数据
     * OnSuccessAndFaultSub 我只是加了错误处理和请求的loading，可以自己根据项目的业务修改
     * new OnSuccessAndFaultSub（第一个参数:成功or失败的回调，第二个参数:上下文，可以不填，控制dialog的）
     */
    private void getWeatherData() {
        WeatherSubscribe.getHomeDate(new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFault(String errorMsg) {

            }
        }));

        WeatherSubscribe.getWeatherDataForBody(mCityName, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                //成功
                Toast.makeText(MainActivity.this, "请求成功~", Toast.LENGTH_SHORT).show();
                WeatherResponseBean weather = GsonUtils.fromJson(result,
                        WeatherResponseBean.class);
                tvCityWather.setText(weather.toString());
            }

            @Override
            public void onFault(String errorMsg) {
                //失败
                Toast.makeText(MainActivity.this, "请求失败：" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        }, MainActivity.this));
    }

}
