package com.example.timetable2;

import static com.example.timetable2.MainActivity.buttonBackgroundColor;
import static com.example.timetable2.MainActivity.buttonTextColor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class syncingOnline extends AppCompatActivity {

    EditText editTextLessons, editTextLessons2, editTextLessons3, editTextLessons4, editTextLessons5, editTextLessons6, editTextLessons7, editTextLessons8;
    EditText timeHs, timeHs2, timeHs3, timeHs4, timeHs5, timeHs6, timeHs7, timeMs, timeMs2, timeMs3, timeMs4, timeMs5, timeMs6, timeMs7, timeHdo, timeHdo2,
            timeHdo3, timeHdo4, timeHdo5, timeHdo6, timeHdo7, timeMdo, timeMdo2, timeMdo3, timeMdo4, timeMdo5, timeMdo6, timeMdo7, timeHs8, timeMs8, timeHdo8, timeMdo8;
    Spinner spinnerLessons;
    Button buttonPaste, buttonCopy, buttonSave;
    ScrollView scrollView;

    public List addWeek = new ArrayList();
    public List addDay = new ArrayList();
    public List addWeekTime = new ArrayList();
    public List addDayTime = new ArrayList();
    public List addHomeWork = new ArrayList();
    public ArrayList copyList = new ArrayList();
    public int nowItemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syncing_online);
        View aView = getWindow().getDecorView();
        aView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        editTextLessons = (EditText) findViewById(R.id.editTextLessons);
        editTextLessons2 = (EditText) findViewById(R.id.editTextLessons2);
        editTextLessons3 = (EditText) findViewById(R.id.editTextLessons3);
        editTextLessons4 = (EditText) findViewById(R.id.editTextLessons4);
        editTextLessons5 = (EditText) findViewById(R.id.editTextLessons5);
        editTextLessons6 = (EditText) findViewById(R.id.editTextLessons6);
        editTextLessons7 = (EditText) findViewById(R.id.editTextLessons7);
        editTextLessons8 = (EditText) findViewById(R.id.editTextLessons8);
        spinnerLessons = (Spinner) findViewById(R.id.spinnerLessons);
        timeHs = (EditText) findViewById(R.id.timeHs);
        timeHs2 = (EditText) findViewById(R.id.timeHs2);
        timeHs3 = (EditText) findViewById(R.id.timeHs3);
        timeHs4 = (EditText) findViewById(R.id.timeHs4);
        timeHs5 = (EditText) findViewById(R.id.timeHs5);
        timeHs6 = (EditText) findViewById(R.id.timeHs6);
        timeHs7 = (EditText) findViewById(R.id.timeHs7);
        timeHs8 = (EditText) findViewById(R.id.timeHs8);
        timeMs = (EditText) findViewById(R.id.timeMs);
        timeMs2 = (EditText) findViewById(R.id.timeMs2);
        timeMs3 = (EditText) findViewById(R.id.timeMs3);
        timeMs4 = (EditText) findViewById(R.id.timeMs4);
        timeMs5 = (EditText) findViewById(R.id.timeMs5);
        timeMs6 = (EditText) findViewById(R.id.timeMs6);
        timeMs7 = (EditText) findViewById(R.id.timeMs7);
        timeMs8 = (EditText) findViewById(R.id.timeMs8);
        timeHdo = (EditText) findViewById(R.id.timeHdo);
        timeHdo2 = (EditText) findViewById(R.id.timeHdo2);
        timeHdo3 = (EditText) findViewById(R.id.timeHdo3);
        timeHdo4 = (EditText) findViewById(R.id.timeHdo4);
        timeHdo5 = (EditText) findViewById(R.id.timeHdo5);
        timeHdo6 = (EditText) findViewById(R.id.timeHdo6);
        timeHdo7 = (EditText) findViewById(R.id.timeHdo7);
        timeHdo8 = (EditText) findViewById(R.id.timeHdo8);
        timeMdo = (EditText) findViewById(R.id.timeMdo);
        timeMdo2 = (EditText) findViewById(R.id.timeMdo2);
        timeMdo3 = (EditText) findViewById(R.id.timeMdo3);
        timeMdo4 = (EditText) findViewById(R.id.timeMdo4);
        timeMdo5 = (EditText) findViewById(R.id.timeMdo5);
        timeMdo6 = (EditText) findViewById(R.id.timeMdo6);
        timeMdo7 = (EditText) findViewById(R.id.timeMdo7);
        timeMdo8 = (EditText) findViewById(R.id.timeMdo8);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonPaste = (Button) findViewById(R.id.buttonPaste);
        buttonCopy = (Button) findViewById(R.id.buttonCopy);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        // Узнаем размеры экрана из ресурсов
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        // установить размеры для listView
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        ViewGroup.LayoutParams par = scrollView.getLayoutParams();
        par.height = displaymetrics.heightPixels-20;
        scrollView.setLayoutParams(par);

        //установка цвета
        buttonSave.setTextColor(buttonTextColor);
        buttonSave.setBackgroundColor(buttonBackgroundColor);
        buttonPaste.setTextColor(buttonTextColor);
        buttonPaste.setBackgroundColor(buttonBackgroundColor);
        buttonCopy.setTextColor(buttonTextColor);
        buttonCopy.setBackgroundColor(buttonBackgroundColor);

        for(int i = 0;i<7;i++) {
            addWeek.add(new ArrayList());
            addWeekTime.add(new ArrayList());
        }

        try {
            // открываем поток для чтения
            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                    openFileInput("timeBase")));
            String str = "";
            int kStr=0, k=0;
            // читаем содержимое
            while ((str = timeRead.readLine()) != null) {
                ArrayList addReadStr = new ArrayList();
                String[] strSplit = str.split(" ");
                if (str.equals("@")) {
                    kStr++;
                    k=0;
                } else {
                    //расписание
                    if (kStr == 0) {
                        for (int i = 0; i < strSplit.length; i++) {
                            addReadStr.add(strSplit[i]);
                        }
                        //Log.i("0", String.valueOf(str));
                        addWeek.set(k, addReadStr);
                        k++;
                    }
                    //звонки
                    if (kStr == 1) {
                        for (int i = 0; i < strSplit.length; i++) {
                            addReadStr.add(strSplit[i]);
                        }
                        //Log.i("1", String.valueOf(str));
                        addWeekTime.set(k, addReadStr);
                        k++;
                    }
                    //дз
                    if (kStr == 2) {
                        if (!(str.equals(""))) {
                            addHomeWork.add(str);
                        }
                        k++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("addWeek",String.valueOf(addWeek));
        Log.i("addWeekTime",String.valueOf(addWeekTime));
        Log.i("addHomeWork",String.valueOf(addHomeWork));

        setNum(0);
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNum();
                copyList = (ArrayList) addWeekTime.get(nowItemPosition);
                Toast.makeText(getApplicationContext(), "Скопированно", Toast.LENGTH_SHORT).show();
            }
        });
        buttonPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeekTime.set(nowItemPosition, copyList);
                setNum(nowItemPosition);
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNum();
                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("timeBase", MODE_PRIVATE)));
                    // пишем данные
                    String strWrite="";
                    for(int i = 0;i<addWeek.size();i++) {
                        ArrayList addReadStr = (ArrayList) addWeek.get(i);
                        for(int j = 0;j<addReadStr.size();j++) {
                            strWrite+=addReadStr.get(j)+" ";
                        }
                        strWrite+='\n';
                    }
                    strWrite+="@"+'\n';
                    for(int i = 0;i<addWeekTime.size();i++) {
                        ArrayList addReadStr = (ArrayList) addWeekTime.get(i);
                        for(int j = 0;j<addReadStr.size();j++) {
                            strWrite+=addReadStr.get(j)+" ";
                        }
                        strWrite+='\n';
                    }
                    strWrite+="@"+'\n';
                    for(int i = 0;i<addHomeWork.size();i++) {
                        strWrite+=addHomeWork.get(i);
                        if (i!=addHomeWork.size()-1) {
                            strWrite += '\n';
                        }
                    }
                    bw.write(strWrite);
                    bw.newLine();

                    // закрываем поток
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(syncingOnline.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.spinDataNed,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLessons.setAdapter(adapter);

        spinnerLessons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                //сохранение данных
                saveNum();

                //обнуление
                setNull();

                //установка сохранённого текста
                setNum(selectedItemPosition);

                //смена
                nowItemPosition=selectedItemPosition;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            };
        });
    }

    void saveNum() {
        addDay = new ArrayList();
        addDay.add(editTextLessons.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons2.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons3.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons4.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons5.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons6.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons7.getText().toString().replace(" ","_"));
        addDay.add(editTextLessons8.getText().toString().replace(" ","_"));
        addWeek.set(nowItemPosition,addDay);

        addDayTime = new ArrayList();
        int k;
        //1
        k = 0;
        if (timeHs.getText().length()!=0) {
            k+=Integer.valueOf(timeHs.getText().toString())*60;
        }
        if (timeMs.getText().length()!=0) {
            k+=Integer.valueOf(timeMs.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo.getText().toString())*60;
        }
        if (timeMdo.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo.getText().toString());
        }
        addDayTime.add(k);
        //2
        k = 0;
        if (timeHs2.getText().length()!=0) {
            k+=Integer.valueOf(timeHs2.getText().toString())*60;
        }
        if (timeMs2.getText().length()!=0) {
            k+=Integer.valueOf(timeMs2.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo2.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo2.getText().toString())*60;
        }
        if (timeMdo2.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo2.getText().toString());
        }
        addDayTime.add(k);
        //3
        k = 0;
        if (timeHs3.getText().length()!=0) {
            k+=Integer.valueOf(timeHs3.getText().toString())*60;
        }
        if (timeMs3.getText().length()!=0) {
            k+=Integer.valueOf(timeMs3.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo3.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo3.getText().toString())*60;
        }
        if (timeMdo3.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo3.getText().toString());
        }
        addDayTime.add(k);
        //4
        k = 0;
        if (timeHs4.getText().length()!=0) {
            k+=Integer.valueOf(timeHs4.getText().toString())*60;
        }
        if (timeMs4.getText().length()!=0) {
            k+=Integer.valueOf(timeMs4.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo4.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo4.getText().toString())*60;
        }
        if (timeMdo4.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo4.getText().toString());
        }
        addDayTime.add(k);
        //5
        k = 0;
        if (timeHs5.getText().length()!=0) {
            k+=Integer.valueOf(timeHs5.getText().toString())*60;
        }
        if (timeMs5.getText().length()!=0) {
            k+=Integer.valueOf(timeMs5.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo5.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo5.getText().toString())*60;
        }
        if (timeMdo5.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo5.getText().toString());
        }
        addDayTime.add(k);
        //6
        k = 0;
        if (timeHs6.getText().length()!=0) {
            k+=Integer.valueOf(timeHs6.getText().toString())*60;
        }
        if (timeMs6.getText().length()!=0) {
            k+=Integer.valueOf(timeMs6.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo6.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo6.getText().toString())*60;
        }
        if (timeMdo6.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo6.getText().toString());
        }
        addDayTime.add(k);
        //7
        k = 0;
        if (timeHs7.getText().length()!=0) {
            k+=Integer.valueOf(timeHs7.getText().toString())*60;
        }
        if (timeMs7.getText().length()!=0) {
            k+=Integer.valueOf(timeMs7.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo7.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo7.getText().toString())*60;
        }
        if (timeMdo7.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo7.getText().toString());
        }
        addDayTime.add(k);
        //8
        k = 0;
        if (timeHs8.getText().length()!=0) {
            k+=Integer.valueOf(timeHs8.getText().toString())*60;
        }
        if (timeMs8.getText().length()!=0) {
            k+=Integer.valueOf(timeMs8.getText().toString());
        }
        addDayTime.add(k);
        k = 0;
        if (timeHdo8.getText().length()!=0) {
            k+=Integer.valueOf(timeHdo8.getText().toString())*60;
        }
        if (timeMdo8.getText().length()!=0) {
            k+=Integer.valueOf(timeMdo8.getText().toString());
        }
        addDayTime.add(k);
        addWeekTime.set(nowItemPosition,addDayTime);
    }

    void setNull () {
        editTextLessons.setText("");
        editTextLessons2.setText("");
        editTextLessons3.setText("");
        editTextLessons4.setText("");
        editTextLessons5.setText("");
        editTextLessons6.setText("");
        editTextLessons7.setText("");
        editTextLessons8.setText("");
        timeHs.setText("");
        timeHs2.setText("");
        timeHs3.setText("");
        timeHs4.setText("");
        timeHs5.setText("");
        timeHs6.setText("");
        timeHs7.setText("");
        timeHs8.setText("");
        timeMs.setText("");
        timeMs2.setText("");
        timeMs3.setText("");
        timeMs4.setText("");
        timeMs5.setText("");
        timeMs6.setText("");
        timeMs7.setText("");
        timeMs8.setText("");
        timeHdo.setText("");
        timeHdo2.setText("");
        timeHdo3.setText("");
        timeHdo4.setText("");
        timeHdo5.setText("");
        timeHdo6.setText("");
        timeHdo7.setText("");
        timeHdo8.setText("");
        timeMdo.setText("");
        timeMdo2.setText("");
        timeMdo3.setText("");
        timeMdo4.setText("");
        timeMdo5.setText("");
        timeMdo6.setText("");
        timeMdo7.setText("");
        timeMdo8.setText("");
    }

    void setNum(int selectedItemPositionD) {
        String ar = String.valueOf(addWeek.get(selectedItemPositionD));
        ar = ar.replace("[","");
        ar = ar.replace("]","");
        ar = ar.replace(" ","");
        String [] arSplit = ar.split(",");
        for (int i = 0;i<arSplit.length;i++){
            String asd = String.valueOf(arSplit[i]).replace("_"," ");
            switch (i) {
                case 0:
                    editTextLessons.setText(asd);
                    break;
                case 1:
                    editTextLessons2.setText(asd);
                    break;
                case 2:
                    editTextLessons3.setText(asd);
                    break;
                case 3:
                    editTextLessons4.setText(asd);
                    break;
                case 4:
                    editTextLessons5.setText(asd);
                    break;
                case 5:
                    editTextLessons6.setText(asd);
                    break;
                case 6:
                    editTextLessons7.setText(asd);
                    break;
                case 7:
                    editTextLessons8.setText(asd);
                    break;
            }
        }

        ar = String.valueOf(addWeekTime.get(selectedItemPositionD));
        ar = ar.replace("[","");
        ar = ar.replace("]","");
        ar = ar.replace(" ","");
        arSplit = ar.split(",");
        for (int i = 0;i<arSplit.length;i++){
            int asd;
            if (!(arSplit[i].equals(""))){
                asd = Integer.valueOf(arSplit[i]);
            } else {
                asd=0;
            }
            switch (i) {
                case 0:
                    if (asd!=0) {
                        timeHs.setText(String.valueOf(asd/60));
                        timeMs.setText(String.valueOf(asd%60));
                    }
                    break;
                case 1:
                    if (asd!=0) {
                        timeHdo.setText(String.valueOf(asd / 60));
                        timeMdo.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 2:
                    if (asd!=0) {
                        timeHs2.setText(String.valueOf(asd/60));
                        timeMs2.setText(String.valueOf(asd%60));
                    }
                    break;
                case 3:
                    if (asd!=0) {
                        timeHdo2.setText(String.valueOf(asd / 60));
                        timeMdo2.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 4:
                    if (asd!=0) {
                        timeHs3.setText(String.valueOf(asd/60));
                        timeMs3.setText(String.valueOf(asd%60));
                    }
                    break;
                case 5:
                    if (asd!=0) {
                        timeHdo3.setText(String.valueOf(asd / 60));
                        timeMdo3.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 6:
                    if (asd!=0) {
                        timeHs4.setText(String.valueOf(asd/60));
                        timeMs4.setText(String.valueOf(asd%60));
                    }
                    break;
                case 7:
                    if (asd!=0) {
                        timeHdo4.setText(String.valueOf(asd / 60));
                        timeMdo4.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 8:
                    if (asd!=0) {
                        timeHs5.setText(String.valueOf(asd/60));
                        timeMs5.setText(String.valueOf(asd%60));
                    }
                    break;
                case 9:
                    if (asd!=0) {
                        timeHdo5.setText(String.valueOf(asd / 60));
                        timeMdo5.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 10:
                    if (asd!=0) {
                        timeHs6.setText(String.valueOf(asd/60));
                        timeMs6.setText(String.valueOf(asd%60));
                    }
                    break;
                case 11:
                    if (asd!=0) {
                        timeHdo6.setText(String.valueOf(asd / 60));
                        timeMdo6.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 12:
                    if (asd!=0) {
                        timeHs7.setText(String.valueOf(asd/60));
                        timeMs7.setText(String.valueOf(asd%60));
                    }
                    break;
                case 13:
                    if (asd!=0) {
                        timeHdo7.setText(String.valueOf(asd / 60));
                        timeMdo7.setText(String.valueOf(asd % 60));
                    }
                    break;
                case 14:
                    if (asd!=0) {
                        timeHs8.setText(String.valueOf(asd/60));
                        timeMs8.setText(String.valueOf(asd%60));
                    }
                    break;
                case 15:
                    if (asd!=0) {
                        timeHdo8.setText(String.valueOf(asd / 60));
                        timeMdo8.setText(String.valueOf(asd % 60));
                    }
                    break;
            }
        }
    }
}