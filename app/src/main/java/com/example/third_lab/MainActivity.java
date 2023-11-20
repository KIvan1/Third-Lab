package com.example.third_lab;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<Currency> currency;
    private static String valTo;
    private static String valFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnectionCheck();
        Spinner from = findViewById(R.id.spinnerFrom);
        Spinner to = findViewById(R.id.spinnerTo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String str_xml = downloadXml();
                    from.post(new Runnable() {
                        @Override
                        public void run() {
                            xmlParser parser = new xmlParser();
                            if(parser.parse(str_xml)) {
                                ArrayAdapter<String> adapter = new ArrayAdapter(getBaseContext(),
                                        android.R.layout.simple_spinner_item, parser.getCurrency());
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                from.setAdapter(adapter);
                            }
                        }
                    });
                    to.post(new Runnable() {
                        @Override
                        public void run() {
                            xmlParser parser = new xmlParser();
                            if(parser.parse(str_xml))
                            {
                                currency = parser.getValue();
                                ArrayAdapter<String> adapter = new ArrayAdapter(getBaseContext(),
                                        android.R.layout.simple_spinner_item, parser.getCurrency());
                                valTo = parser.getCurrency().get(0);
                                valFrom = parser.getCurrency().get(0);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                to.setAdapter(adapter);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        onButtonClick();
    }

    private void checkConnectionCheck(){
        ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = false;
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        connected = true;
                    }
        }
        if (!connected)
        {
            showAlert("No connection");
        }
    }

    private String downloadXml() throws IOException {
        StringBuilder xmlResult = new StringBuilder();
        BufferedReader reader = null;
        InputStream stream = null;
        HttpsURLConnection connection = null;
        try {
            URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
            connection = (HttpsURLConnection)url.openConnection();
            stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line=reader.readLine()) != null) {
                xmlResult.append(line);
            }
            return xmlResult.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void showAlert(String massage) {
        AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);
        alert_builder.setMessage(massage);
        alert_builder.setCancelable(false);
        alert_builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                finish();
            }
        });
        AlertDialog alert = alert_builder.create();
        alert.setTitle("Error status");
        alert.show();
    }

    private void onButtonClick() {
        Button btn0 = (Button)findViewById(R.id.b0);
        Button btn1 = (Button)findViewById(R.id.b1);
        Button btn2 = (Button)findViewById(R.id.b2);
        Button btn3 = (Button)findViewById(R.id.b3);
        Button btn4 = (Button)findViewById(R.id.b4);
        Button btn5 = (Button)findViewById(R.id.b5);
        Button btn6 = (Button)findViewById(R.id.b6);
        Button btn7 = (Button)findViewById(R.id.b7);
        Button btn8 = (Button)findViewById(R.id.b8);
        Button btn9 = (Button)findViewById(R.id.b9);
        Spinner to = findViewById(R.id.spinnerTo);
        Button btn_convert = (Button)findViewById(R.id.delate);
        TextView input = (TextView)findViewById(R.id.Input);
        TextView output = (TextView)findViewById(R.id.Output);
        Spinner from = findViewById(R.id.spinnerFrom);
        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                valTo = item;
                String cur_num = input.getText().toString();
                if (!cur_num.equals("")) {
                    Double numb = Double.parseDouble(cur_num);
                    for (int j = 0; j < currency.size(); j++)
                        if (currency.get(j).getName().equals(valTo))
                            numb *= currency.get(j).getValue();
                    for (int j = 0; j < currency.size(); j++)
                        if (currency.get(j).getName().equals(valFrom))
                            numb /= currency.get(j).getValue();
                    output.setText(numb.toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String)adapterView.getItemAtPosition(i);
                valFrom = item;
                String cur_num = input.getText().toString();
                if (!cur_num.equals("")) {
                    Double numb = Double.parseDouble(cur_num);
                    for (int j = 0; j < currency.size(); j++)
                        if (currency.get(j).getName().equals(valTo))
                            numb *= currency.get(j).getValue();
                    for (int j = 0; j < currency.size(); j++)
                        if (currency.get(j).getName().equals(valFrom))
                            numb /= currency.get(j).getValue();
                    output.setText(numb.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "0");
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(input, output, "9");
            }
        });
        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cur_num = input.getText().toString();
                if(!cur_num.equals("")) {
                    input.setText(cur_num.substring(0, cur_num.length() - 1));
                    cur_num = input.getText().toString();
                    if(!cur_num.equals("")) {
                        Double numb = Double.parseDouble(cur_num);
                        for (int i = 0; i < currency.size(); i++)
                            if (currency.get(i).getName().equals(valTo))
                                numb *= currency.get(i).getValue();
                        for (int i = 0; i < currency.size(); i++)
                            if (currency.get(i).getName().equals(valFrom))
                                numb /= currency.get(i).getValue();
                        output.setText(numb.toString());
                    }
                    else
                        output.setText(cur_num);
                }
            }
        });
    }

    private void add(TextView input, TextView output, String num) {
        String cur_num = input.getText().toString();
        if(cur_num.equals("0")) {
            input.setText(num);
            output.setText(num);
        }
        else {
            input.setText(cur_num + num);
            Double numb = Double.parseDouble(cur_num + num);
            Double mn1 = 1.0;
            Double mn2 = 1.0;
            for (int i = 0; i < currency.size(); i++)
                if (currency.get(i).getName().equals(valTo))
                    mn1 = currency.get(i).getValue();
            for (int i = 0; i < currency.size(); i++)
                if (currency.get(i).getName().equals(valFrom))
                    mn2 = currency.get(i).getValue();
            numb *= mn1 / mn2;
            output.setText(numb.toString());
        }
    }
}