package com.example.timetable2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.timetable2.MainActivity.arrayToStr;
import static com.example.timetable2.MainActivity.dopInt;
import static com.example.timetable2.MainActivity.today;
import static com.example.timetable2.Settings.time_PREFERENCES_COUNTER;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class fullTable extends AppCompatActivity {

    private SharedPreferences size;

    final String dataName[] = new String[]{"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота" ,"Воскресенье",
            "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    final String dateMonth[] = new String[]{"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    final Integer dayMonth[] = new Integer[]{31,28,31,30,31,30,31,31,30,31,30,31};

    String k="";
    int year,month,day,nowDataNed;
    public int kolSet = 0;

    //год-месяц-неделя-день
    public static List<List> nowWeek = new ArrayList<List>();
    public static List<List> setNowWeek = new ArrayList<List>();
    public static List<List> setNowWeekSort = new ArrayList<List>();

    public List homeWorkListFull = new ArrayList();
    public List nameListFull = new ArrayList();
    public List timeListFull = new ArrayList();

    List list = new ArrayList();

    TableLayout tableLayout;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_table);
        /*View aView = getWindow().getDecorView();
        aView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        setNowWeek.clear();
        for(int i=0;i<7;i++) {
            List list1 = new ArrayList();
            setNowWeek.add(list1);
        }

        time();
        setList();
        //set();
    }

    //установка времени
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void time(){
        year=0;
        month=0;
        day=0;
        nowDataNed=0;
        //время на данный момент
        Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        switch (simpleDateFormat.format(myCalendar.getTime())){
            case"понедельник":
                nowDataNed = 0;
                break;
            case"вторник":
                nowDataNed = 1;
                break;
            case"среда":
                nowDataNed = 2;
                break;
            case"четверг":
                nowDataNed = 3;
                break;
            case"пятница":
                nowDataNed = 4;
                break;
            case"суббота":
                nowDataNed = 5;
                break;
            case"воскресенье":
                nowDataNed = 6;
                break;
            case"Monday":
                nowDataNed = 0;
                break;
            case"Tuesday":
                nowDataNed = 1;
                break;
            case"Wednesday":
                nowDataNed = 2;
                break;
            case"Thursday":
                nowDataNed = 3;
                break;
            case"Friday":
                nowDataNed = 4;
                break;
            case"Saturday":
                nowDataNed = 5;
                break;
            case"Sunday":
                nowDataNed = 6;
                break;
        }

        simpleDateFormat = new SimpleDateFormat("yyyy");
        k = simpleDateFormat.format(myCalendar.getTime());
        year=Integer.parseInt(k);

        simpleDateFormat = new SimpleDateFormat("MM");
        k = simpleDateFormat.format(myCalendar.getTime());
        month=Integer.parseInt(k);

        simpleDateFormat = new SimpleDateFormat("dd");
        k = simpleDateFormat.format(myCalendar.getTime());
        day=Integer.parseInt(k);


        //время на следующие 7 дней
        nowWeek = new ArrayList();
        for(int i=0;i<7;i++) {
            list = new ArrayList();
            list.add(year);
            list.add(month-1);
            list.add(day);
            list.add(nowDataNed);
            nowWeek.add(list);

            int d;
            d=dayMonth[month-1];
            if (month==2){
                if((2020-year)%4==0) {
                    d++;
                }
            }
            if (nowDataNed+1>6) {
                nowDataNed=0;
            } else {
                nowDataNed++;
            }
            if (day+1>d){
                day=1;
                if (month+1>12){
                    month=1;
                    year++;
                } else {
                    month++;
                }
            } else {
                day++;
            }
        }
    }

    private void setList() {
        //создание расписания на день из синхронизированных данных
        try {
            // открываем поток для чтения
            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                    openFileInput("timeBase")));
            String str = "";
            // читаем содержимое
            int kolStr = 0, k=0;
            while ((str = timeRead.readLine()) != null) {
                String[] strSplit = str.split(" ");
                if (str.equals("@")) {
                    k++;
                    kolStr=0;
                } else {
                    if (k==0) {
                        nameListFull.add(str);
                    }
                    if (k==1) {
                        timeListFull.add(str);
                    }
                    if (k==2) {
                        homeWorkListFull.add(str);
                    }
                    kolStr++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // открываем поток для чтения
            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                    openFileInput("time")));
            String str = "";
            // читаем содержимое
            while ((str = timeRead.readLine()) != null) {
                String[] strSplit = str.split(" ");
                //единожды
                if (Integer.valueOf(strSplit[0])==-1){
                    for(int i=2+dopInt;i<strSplit.length;i+=5) {
                        for(int k=0;k<7;k++) {
                            int f = 0;
                            if (Integer.valueOf(strSplit[i]) == (int) nowWeek.get(k).get(0)) {
                                f++;
                            }
                            if (Integer.valueOf(strSplit[i + 1]) == (int) nowWeek.get(k).get(1)) {
                                f++;
                            }
                            if (Integer.valueOf(strSplit[i + 2]) == (int) nowWeek.get(k).get(2)) {
                                f++;
                            }
                            if (f == 3) {
                                list = new ArrayList();
                                for(int s=0;s<setNowWeek.get(k).size();s++) {
                                    list.add(setNowWeek.get(k).get(s));
                                }
                                list.add(String.valueOf(strSplit[1]));
                                list.add(Integer.valueOf(strSplit[i+3]));
                                list.add(Integer.valueOf(strSplit[i+4]));
                                setNowWeek.set(k,list);
                            }
                        }
                    }
                }
                //ежедневно
                if (Integer.valueOf(strSplit[0])==0){
                    for(int k=0;k<7;k++) {
                        list = new ArrayList();
                        for(int s=3;s<strSplit.length;s+=2) {
                            list.add(strSplit[1]);
                            list.add(Integer.valueOf(strSplit[s]));
                            list.add(Integer.valueOf(strSplit[s+1]));
                        }
                        for(int s=0;s<setNowWeek.get(k).size();s++) {
                            list.add(setNowWeek.get(k).get(s));
                        }
                        setNowWeek.set(k,list);
                    }
                }
                //еженедельно
                if (Integer.valueOf(strSplit[0])==7){
                    for(int i=2+dopInt;i<strSplit.length;i+=3) {
                        for(int k=0;k<7;k++) {
                            int f = 0;
                            if (Integer.valueOf(strSplit[i]) == (int) nowWeek.get(k).get(3)) {
                                f++;
                            }
                            if (f == 1) {
                                list = new ArrayList();
                                for(int s=0;s<setNowWeek.get(k).size();s++) {
                                    list.add(setNowWeek.get(k).get(s));
                                }
                                list.add(String.valueOf(strSplit[1]));
                                list.add(Integer.valueOf(strSplit[i+1]));
                                list.add(Integer.valueOf(strSplit[i+2]));
                                setNowWeek.set(k,list);
                            }
                        }
                    }
                }
                //ежемесячно
                if (Integer.valueOf(strSplit[0])==31){
                    for(int i=2+dopInt;i<strSplit.length;i+=3) {
                        for(int k=0;k<7;k++) {
                            int f = 0;
                            if (Integer.valueOf(strSplit[i]) == (int) nowWeek.get(k).get(2)) {
                                f++;
                            }
                            if (f == 1) {
                                list = new ArrayList();
                                for(int s=0;s<setNowWeek.get(k).size();s++) {
                                    list.add(setNowWeek.get(k).get(s));
                                }
                                list.add(String.valueOf(strSplit[1]));
                                list.add(Integer.valueOf(strSplit[i+1]));
                                list.add(Integer.valueOf(strSplit[i+2]));
                                setNowWeek.set(k,list);
                            }
                        }
                    }
                }
                //ежегодно
                if (Integer.valueOf(strSplit[0])==365){
                    for(int i=2+dopInt;i<strSplit.length;i+=4) {
                        for(int k=0;k<7;k++) {
                            int f = 0;
                            if (Integer.valueOf(strSplit[i]) == (int) nowWeek.get(k).get(1)) {
                                f++;
                            }
                            if (Integer.valueOf(strSplit[i + 1]) == (int) nowWeek.get(k).get(2)) {
                                f++;
                            }
                            if (f == 2) {
                                list = new ArrayList();
                                for(int s=0;s<setNowWeek.get(k).size();s++) {
                                    list.add(setNowWeek.get(k).get(s));
                                }
                                list.add(String.valueOf(strSplit[1]));
                                list.add(Integer.valueOf(strSplit[i+2]));
                                list.add(Integer.valueOf(strSplit[i+3]));
                                setNowWeek.set(k,list);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int nw = 0; nw<nowWeek.size();nw++) {
            //создание расписания на день из синхронизированных данных
            List nameList = new ArrayList();
            List timeList = new ArrayList();
            List homeWorkList = new ArrayList();
            try {
                // открываем поток для чтения
                BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                        openFileInput("timeBase")));
                String str = "";
                // читаем содержимое
                int kolStr = 0, k = 0;
                while ((str = timeRead.readLine()) != null) {
                    String[] strSplit = str.split(" ");
                    if (str.equals("@")) {
                        k++;
                        kolStr = 0;
                    } else {
                        if (k == 0) {
                            if (kolStr == (int) nowWeek.get(nw).get(3)) {
                                for (int i = 0; i < strSplit.length; i++) {
                                    nameList.add(strSplit[i]);
                                }
                            }
                        }
                        if (k == 1) {
                            if (kolStr == (int) nowWeek.get(nw).get(3)) {
                                for (int i = 0; i < strSplit.length; i++) {
                                    timeList.add(strSplit[i]);
                                }
                            }
                        }
                        if (k == 2) {
                            homeWorkList.add(str);
                        }
                        kolStr++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //формировка
            for (int i = 0; i < nameList.size(); i++) {
                if (!(nameList.get(i).equals("")) && !(nameList.get(i).equals(" "))) {
                    if (timeList.size() != 0 && timeList.size() / 2 >= i) {
                        if (!(timeList.get(i * 2).equals("0")) || !(timeList.get(i * 2).equals("0"))) {
                            List setNowWeekList = new ArrayList();
                            setNowWeekList = setNowWeek.get(nw);
                            setNowWeekList.add(String.valueOf(nameList.get(i)).replace("_", " "));
                            setNowWeekList.add(Integer.valueOf(min(Integer.valueOf(String.valueOf(timeList.get(i * 2))), Integer.valueOf(String.valueOf(timeList.get(i * 2 + 1))))));
                            setNowWeekList.add(Integer.valueOf(max(Integer.valueOf(String.valueOf(timeList.get(i * 2))), Integer.valueOf(String.valueOf(timeList.get(i * 2 + 1))))));
                            setNowWeek.set(nw,setNowWeekList);
                        }
                    }
                }
            }
        }

        //сортировочка
        setNowWeekSort = new ArrayList<List>();
        Log.i("setNowWeek",String.valueOf(setNowWeek));
        if (setNowWeek.size()!=0) {
            //проходка по строчкам
            for (int w = 0; w < setNowWeek.size(); w += 1) {
                List doSort = new ArrayList();
                for(int i=0;i<setNowWeek.get(w).size();i++){
                    doSort.add(setNowWeek.get(w).get(i));
                }
                List postSort = new ArrayList();
                //проходка по doSort
                for (int i1 = 1; i1 < doSort.size(); i1+=3) {
                    //Log.i("doSort",String.valueOf(doSort.get(i1))+":"+String.valueOf(doSort.get(i1-1)));
                    int f = 0;
                    //проходка по postSort
                    for (int i2 = 1; i2 < postSort.size(); i2+=3) {
                        if (Integer.valueOf(String.valueOf(doSort.get(i1)))<Integer.valueOf(String.valueOf(postSort.get(i2)))){
                            postSort.add(i2-1,doSort.get(i1+1));
                            postSort.add(i2-1,doSort.get(i1));
                            postSort.add(i2-1,doSort.get(i1-1));
                            f=1;
                            break;
                        }
                    }
                    if (f == 0) {
                        //Log.i(String.valueOf(w),String.valueOf(doSort.get(i1-1))+" "+String.valueOf(doSort.get(i1)));
                        postSort.add(doSort.get(i1-1));
                        postSort.add(doSort.get(i1));
                        postSort.add(doSort.get(i1+1));
                    }
                }
                setNowWeekSort.add(postSort);
            }

            setNowWeek = setNowWeekSort;
            Log.i("setNowWeek2",String.valueOf(setNowWeek));
            //Log.i("2",String.valueOf(setNowWeek));
        }
        set();
    }

    private void set(){
        int textSize = 30;
        for(int i=0;i<setNowWeek.size();i++) {
            TableRow tableRow2 = new TableRow(this);
            tableRow2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView text2 = new TextView(this);
            text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            text2.setTextColor(Color.rgb(255, 0, 0));
            text2.setText(String.valueOf(dataName[(int) nowWeek.get(i).get(3)]));
            tableRow2.addView(text2, 0);
            tableLayout.addView(tableRow2);

            for(int j=0;j<setNowWeek.get(i).size();j+=3) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView text = new TextView(this);
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                text.setText(String.valueOf(setNowWeek.get(i).get(j)).replace("_"," "));
                text.setId(kolSet);
                kolSet++;
                tableRow.addView(text, 0);

                SharedPreferences time = getSharedPreferences(Settings.time_PREFERENCES, Context.MODE_PRIVATE);
                if (time.getInt(time_PREFERENCES_COUNTER, 0) == 1) {
                    TextView text3 = new TextView(this);
                    text3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);

                    String l = String.valueOf((int) setNowWeek.get(i).get(j+1) / 60);
                    String l2 = String.valueOf((int) setNowWeek.get(i).get(j+1) % 60);
                    l2 = (Integer.valueOf(l2) < 10 ? "0" + l2 : l2);
                    String l3 = String.valueOf((int) setNowWeek.get(i).get(j+2) / 60);
                    String l4 = String.valueOf((int) setNowWeek.get(i).get(j+2) % 60);
                    l4 = (Integer.valueOf(l4) < 10 ? "0" + l4 : l4);

                    text3.setText(l + ":" + l2 + " - " + l3 + ":" + l4);
                    text3.setWidth(70);
                    tableRow.addView(text3, 1);
                }

                tableLayout.addView(tableRow);
            }
            if (setNowWeek.get(i).size()==0){
                TableRow tableRow3 = new TableRow(this);
                tableRow3.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView text = new TextView(this);
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
                text.setText("в этот день отдыхай");
                tableRow3.addView(text, 0);
                tableLayout.addView(tableRow3);
            }
        }
        setOnCHomework();
    }

    private void setOnCHomework() {
        for (int i = 0;i<kolSet;i++) {
            TextView setHomework = tableLayout.findViewById(i);
            setHomework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int k = -1;
                    for (int j = 0; j < homeWorkListFull.size(); j++) {
                        if (setHomework.getText().equals(String.valueOf(homeWorkListFull.get(j)).split(" ")[0].replace("_"," "))) {
                            k=j;
                            break;
                        }
                    }
                    boolean k2 = false;
                    for (int j = 0; j < nameListFull.size(); j++) {
                        String[] test = String.valueOf(nameListFull.get(j)).split(" ");
                        for (int l = 0; l < test.length; l++) {
                            if (setHomework.getText().toString().equals(test[l].replace("_"," "))) {
                                k2 = true;
                            }
                        }
                    }

                    if (k!=-1) {
                        createDialogHomeWork(String.valueOf(homeWorkListFull.get(k)));
                    } else {
                        if (k2) {
                            Toast.makeText(getApplicationContext(), "Домашнее задание отсутствует", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    //выпадающий список для дз
    private void createDialogHomeWork(String homework) {
        String name = homework.split(" ")[0];
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView2 = li.inflate(R.layout.drop_list_homework, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView2);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final TableLayout tableLayout2 = (TableLayout) promptsView2.findViewById(R.id.tableLayout2);
        final Button buttonSave = (Button) promptsView2.findViewById(R.id.buttonSave);
        final TextView textName = (TextView) promptsView2.findViewById(R.id.textName);

        //установка цвета
        buttonSave.setVisibility(View.GONE);

        textName.setText(name);

        kolSet = 0;

        String strPr = homework.replace(name+" ","");
        Log.i("strPr",String.valueOf(strPr));

        String[] strPrSplit = strPr.split(" ");

        for (int i = 0; i < strPrSplit.length-1; i+=2) {
            TableRow tableRowDropList = new TableRow(this);
            tableRowDropList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textDropList = new TextView(this);
            textDropList.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 7);
            textDropList.setText(String.valueOf(strPrSplit[i]));
            textDropList.setGravity(View.TEXT_ALIGNMENT_CENTER);
            textDropList.setId(kolSet);
            kolSet++;
            tableRowDropList.addView(textDropList, 0);
            tableLayout2.addView(tableRowDropList);

            tableRowDropList = new TableRow(this);
            tableRowDropList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            EditText editTextDropList = new EditText(this);
            editTextDropList.setFocusable(false);
            editTextDropList.setClickable(false);
            editTextDropList.setCursorVisible(false);
            editTextDropList.setBackground(null);
            editTextDropList.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
            editTextDropList.setText(String.valueOf(strPrSplit[i+1]).replace("_", " "));
            editTextDropList.setId(kolSet);
            kolSet++;
            tableRowDropList.addView(editTextDropList, 0);
            tableLayout2.addView(tableRowDropList);
        }

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();
    }
}