package com.example.task21p;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    List<String> menu=new ArrayList<>();

    String [] m_l =new String[]{"Meter","Kilometer","Inch","Foot", "Mile"};
    String [] m_t =new String[]{"Centigrade","Fahrenheit","Kelvin","Rankine","Reaumur"};
    String [] m_w =new String[]{"Kilogram","Gram","Pound","Carat"};

    List<Info> values=new ArrayList<>();

    InfoAdapter adapter;

    //What kind of unit this time convert
    int what=0;
    //Source unit
    int flag_s=0;
    double src=1;

    void Convert(){
        values.clear();
        switch (what){
            case 0:
                for (int i=0;i<m_l.length;i++){
                    Info info=new Info();
                    info.name=m_l[i];
                    info.value=ConvertToTarget(i,ConvertToBase(flag_s,src));
                    values.add(info);
                }
                break;
            case 1:
                for (int i=0;i<m_t.length;i++){
                    Info info=new Info();
                    info.name=m_t[i];
                    info.value=ConvertToTarget(i,ConvertToBase(flag_s,src));
                    values.add(info);
                }
                break;
            case 2:
                for (int i=0;i<m_w.length;i++){
                    Info info=new Info();
                    info.name=m_w[i];
                    info.value=ConvertToTarget(i,ConvertToBase(flag_s,src));
                    values.add(info);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    void HideKeyBoard(){
        InputMethodManager manager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    double ConvertToBase(int flag,double source){
        if (flag==0)
            //Source is Base
            return source;
        switch (what){
            case 0:
                //To Meter m
                switch (flag){
                    case 1:
                        return source*1000;
                    case 2:
                        return source/12*0.3048;
                    case 3:
                        return source*0.3048;
                    case 4:
                        return source*1760*3*0.3048;
                    default:
                        break;
                }
                break;
            case 1:
                //To Centigrade °C
                switch (flag){
                    case 1:
                        return (source-32)/1.8;
                    case 2:
                        return source-273.15;
                    case 3:
                        return (source-32-459.67)/1.8;
                    case 4:
                        return source*1.25;
                }
                break;
            case 2:
                //To Kilogram kg
                switch (flag){
                    case 1:
                        return source/1000;
                    case 2:
                        return source*0.45359237;
                    case 3:
                        return source*0.2/1000;
                }
                break;
            default:
                break;
        }
        return 0;
    }

    double ConvertToTarget(int flag,double source){
        if (flag==0)
            //Source is Target
            return source;
        switch (what){
            case 0:
                switch (flag){
                    case 1:
                        return source/1000;
                    case 2:
                        return source/0.3048*12;
                    case 3:
                        return source/0.3048;
                    case 4:
                        return source/1760/3/0.3048;
                }
                break;
            case 1:
                switch (flag){
                    case 1:
                        return source*1.8+32;
                    case 2:
                        return source+273.15;
                    case 3:
                        return source*1.8+32+459.67;
                    case 4:
                        return source*0.8;
                }
                break;
            case 2:
                switch (flag){
                    case 1:
                        return source*1000;
                    case 2:
                        return source/0.45359237;
                    case 3:
                        return source*5000;
                }
                break;
            default:
                break;
        }
        return 0;
    }

    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,menu);
        adapter=new InfoAdapter(values);
        Spinner spinner=findViewById(R.id.sp_menu);
        ListView listView=findViewById(R.id.li_info);
        EditText input=findViewById(R.id.tv_input);
        spinner.setAdapter(arrayAdapter);
        listView.setAdapter(adapter);
        menu.addAll(Arrays.asList(m_l));
        arrayAdapter.notifyDataSetChanged();
        findViewById(R.id.btn_l).setOnClickListener(this);
        findViewById(R.id.btn_t).setOnClickListener(this);
        findViewById(R.id.btn_w).setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                flag_s=position;
                Convert();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=s.toString();
                if (str.indexOf(".")!=str.lastIndexOf(".")){
                    str=str.substring(0,str.lastIndexOf("."));
                    input.setText(str);
                    input.setSelection(str.length());
                }
                if (str.length()!=0)
                    src=Double.parseDouble(str);
                else
                    src=0;
                Convert();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HideKeyBoard();
            }
        });
        input.setSelection(1);
        Convert();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_l:
                menu.clear();
                menu.addAll(Arrays.asList(m_l));
                arrayAdapter.notifyDataSetChanged();
                what=0;
                break;
            case R.id.btn_t:
                menu.clear();
                menu.addAll(Arrays.asList(m_t));
                arrayAdapter.notifyDataSetChanged();
                what=1;
                break;
            case R.id.btn_w:
                menu.clear();
                menu.addAll(Arrays.asList(m_w));
                arrayAdapter.notifyDataSetChanged();
                what=2;
                break;
        }
        HideKeyBoard();
        Convert();
    }

    class Info{
        String name;
        double value;
    }

    class InfoAdapter extends BaseAdapter{
        private List<Info> list;
        InfoAdapter(List<Info> list){
            this.list=list;
        }
        @Override
        public int getCount() {
            if (list==null)
            return 0;
            return list.size();
        }

        @Override
        public Info getItem(int position) {
            if (list==null)
                return null;
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView!=null){
                holder= (ViewHolder) convertView.getTag();
            }else{
                convertView= LayoutInflater.from(MainActivity.this).inflate(R.layout.li_info,parent,false);
                holder=new ViewHolder();
                holder.name=convertView.findViewById(R.id.name);
                holder.value=convertView.findViewById(R.id.value);
                convertView.setTag(holder);
            }
            holder.name.setText(getItem(position).name);
            holder.value.setText(String.valueOf(getItem(position).value));
            return convertView;
        }

        class ViewHolder{
            TextView name;
            TextView value;
        }
    }
}