package wenson.com.networktest.httpexercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wenson.com.networktest.R;

public class HttpURLActivity extends AppCompatActivity {

    @BindView(R.id.send_request)
    Button sendRequest;
    @BindView(R.id.show_response)
    TextView showResponse;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, HttpURLActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_url);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.send_request)
    public void OnViewClick() {
        sendRequestWithHttpURLConnection();
    }

    //开启线程发起网络请求
    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    InputStream in = conn.getInputStream();
                    //读取输入流
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //从子线程切换到主线程，更新UI，显示结果
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showResponse.setText("响应结果：" + "\n" + response);
            }
        });
    }
}
