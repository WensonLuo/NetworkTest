package wenson.com.networktest.httpexercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wenson.com.networktest.R;

public class PullParseXmlActivity extends AppCompatActivity {
    private static final String TAG = "luo---PullParseXml";
    @BindView(R.id.pull_parse)
    Button pullParse;
    @BindView(R.id.show_result)
    TextView showResult;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PullParseXmlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_parse_xml);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.pull_parse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pull_parse:
                sendRequestWithOkHttpPullParse();
                break;
        }
    }

    private void sendRequestWithOkHttpPullParse() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器地址是电脑本机
                            .url("http://192.168.0.142/get_data.xml")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseXMLWithPull(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithPull(String xmlData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String id = "";
            String name = "";
            String version = "";
            StringBuilder sb = new StringBuilder();
            String responseData  = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("id".equals(nodeName)) {
                            id = xmlPullParser.nextText();
                        } else if ("name".equals(nodeName)) {
                            name = xmlPullParser.nextText();
                        } else if ("version".equals(nodeName)) {
                            version = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("app".equals(nodeName)) {
                            Log.d(TAG, "parseXMLWithPull: id is " + id);
                            Log.d(TAG, "parseXMLWithPull: name is " + name);
                            Log.d(TAG, "parseXMLWithPull: version is " + version);
                            sb.append("parseXMLWithPull: id is " + id + "\n");
                            sb.append("parseXMLWithPull: name is " + name + "\n");
                            sb.append("parseXMLWithPull: version is " + version + "\n");
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            responseData = sb.toString();
            showResult(responseData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showResult.setText(result);
            }
        });
    }

}
