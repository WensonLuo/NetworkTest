package wenson.com.networktest.httpexercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wenson.com.networktest.R;

public class SaxParseXmlActivity extends AppCompatActivity {
    private static final String TAG = "luo---SaxParseXml";
    @BindView(R.id.sax_parse)
    Button saxParse;
    @BindView(R.id.show_result)
    TextView showResult;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SaxParseXmlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sax_parse_xml);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sax_parse)
    public void onViewClicked() {
        sendRequestWithOkHttp();
    }

    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://192.168.0.142/get_data.xml")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseXMLWithSAX(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithSAX(String xmlData) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();
            ContentHandler handler = new ContentHandler();
            //将 ContentHandler 的实例设置到 XMLReader 中
            xmlReader.setContentHandler(handler);
            //开始执行解析
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class ContentHandler extends DefaultHandler {
        private String nodeName;
        private StringBuilder id;
        private StringBuilder name;
        private StringBuilder version;
        private StringBuilder sb;
        private String result;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            id = new StringBuilder();
            name = new StringBuilder();
            version = new StringBuilder();
            sb = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            nodeName = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if ("id".equals(nodeName)) {
                id.append(ch, start, length);
            } else if ("name".equals(nodeName)) {
                name.append(ch, start, length);
            } else if ("version".equals(nodeName)) {
                version.append(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if ("app".equals(localName)) {
                Log.d(TAG, "endElement: id is " + id.toString().trim());
                Log.d(TAG, "endElement: name is " + name.toString().trim());
                Log.d(TAG, "endElement: version is " + version.toString().trim());
                sb.append("endElement: id is " + id.toString().trim() + "\n");
                sb.append("endElement: name is " + name.toString().trim() + "\n");
                sb.append("endElement: version is " + version.toString().trim() + "\n");
                // 走完一个APP元素，清空一次StringBuilder
                id.setLength(0);
                name.setLength(0);
                version.setLength(0);
            }
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showResult.setText(sb.toString());
                }
            });
        }
    }
}
