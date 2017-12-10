package wenson.com.networktest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wenson.com.networktest.httpexercise.HttpURLActivity;
import wenson.com.networktest.httpexercise.OkHttpActivity;
import wenson.com.networktest.httpexercise.PullParseXmlActivity;
import wenson.com.networktest.httpexercise.SaxParseXmlActivity;

/**
 * buffer knife 的使用：
 * https://github.com/JakeWharton/butterknife/
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "luo---MainActivity";
    @BindView(R.id.btn_http)
    Button btnHttp;
    @BindView(R.id.btn_ok_http)
    Button btnOkHttp;
    @BindView(R.id.btn_pull_parse_xml)
    Button btnPullParseXml;
    @BindView(R.id.btn_sax_parse_xml)
    Button btnSaxParseXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_http, R.id.btn_ok_http, R.id.btn_pull_parse_xml, R.id.btn_sax_parse_xml})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_http:
                HttpURLActivity.actionStart(this);
                break;
            case R.id.btn_ok_http:
                OkHttpActivity.actionStart(this);
                break;
            case R.id.btn_pull_parse_xml:
                PullParseXmlActivity.actionStart(this);
                break;
            case R.id.btn_sax_parse_xml:
                SaxParseXmlActivity.actionStart(this);
                break;
        }
    }
}
