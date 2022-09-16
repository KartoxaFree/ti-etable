package com.example.timetable2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.timetable2.MainActivity.buttonBackgroundColor;
import static com.example.timetable2.MainActivity.buttonTextColor;
import static com.example.timetable2.Settings.color_PREFERENCES_COUNTER;

public class build extends AppCompatActivity {
    //build

    EditText name, textCh1, textCh2, textMin1, textMin2;
    Spinner prome,spinnerMonth, spinnerWeek;
    EditText textDay, textDay2, textYear;
    Button save, addButton, delButton;
    TableLayout tableLayout;
    Switch vedSw;

    public int r,g,b,colorText;
    public int xWeek,tch1=0,tch2=0,tmin1=0,tmin2=0;
    public int addDataNed, addProm=-1, addMonth;

    //формат: еженедельно-название(str)-доп значение-день недели-начало-конец
    public static List addPredmet = new ArrayList();

    final String dataName[] = new String[]{"Пн", "Вт", "Ср", "Чт", "Пт", "Сб" ,"Вс"};
    final String dateMonth[] = new String[]{"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    //List dateMonth = Arrays.asList(new String[] {"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"});

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        View aView = getWindow().getDecorView();
        aView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        
        name = (EditText) findViewById(R.id.name);
        textCh1 = (EditText) findViewById(R.id.textCh1);
        textCh2 = (EditText) findViewById(R.id.textCh2);
        textMin1 = (EditText) findViewById(R.id.textMin1);
        textMin2 = (EditText) findViewById(R.id.textMin2);
        prome = (Spinner) findViewById(R.id.prome);

        spinnerWeek = (Spinner) findViewById(R.id.spinnerWeek);
        spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        textDay = (EditText) findViewById(R.id.textDay);
        textDay2 = (EditText) findViewById(R.id.textDay2);
        textYear = (EditText) findViewById(R.id.textYear);

        save = (Button) findViewById(R.id.save);
        addButton = (Button) findViewById(R.id.addButton);
        delButton = (Button) findViewById(R.id.delButton);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        vedSw = (Switch) findViewById(R.id.vedSw);

        save.setTextColor(buttonTextColor);
        save.setBackgroundColor(buttonBackgroundColor);
        addButton.setTextColor(buttonTextColor);
        addButton.setBackgroundColor(buttonBackgroundColor);
        delButton.setTextColor(buttonTextColor);
        delButton.setBackgroundColor(buttonBackgroundColor);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.spinProm,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prome.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinDataNed,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeek.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.month,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        textYear.setHint("def:"+simpleDateFormat.format(myCalendar.getTime()));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tch1<25 && tch2<25 && tmin1<60 && tmin2<60) {
                    if (textCh1.getText().length() != 0 || textMin1.getText().length() != 0 ||
                            textCh2.getText().length() != 0 || textMin2.getText().length() != 0) {
                        switch (xWeek){
                            case 0:
                                setOne();
                                break;
                            case 1:
                                setAllDay();
                                break;
                            case 2:
                                setWeek();
                                break;
                            case 3:
                                setMonth();
                                break;
                            case 4:
                                setYear();
                                break;
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Введи хоть что-то", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Превышение временного промежутка", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (xWeek){
                    case 0:
                        delete(5);
                        break;
                    case 1:
                        delete(2);
                        break;
                    case 2:
                        delete(3);
                        break;
                    case 3:
                        delete(3);
                        break;
                    case 4:
                        delete(4);
                        break;
                }
            }
        });

        //сохранение
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().length()!=0){
                    if (addPredmet.size()>0){
                        addPredmet.add(0,addProm);
                        String editTextName=name.getText().toString();
                        editTextName= Pattern.compile("\n").matcher(editTextName).replaceAll("_");
                        addPredmet.add(1,String.valueOf(editTextName.replace(" ","_")));
                        //Доп
                        if (vedSw.isChecked()==false){
                            addPredmet.add(2,0);
                        } else {
                            addPredmet.add(2,1);
                        }

                        try {
                            // отрываем поток для записи
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                    openFileOutput("time", MODE_APPEND)));
                            // пишем данные
                            String asd="";
                            for (int i=0;i<addPredmet.size();i++) {
                                asd+=addPredmet.get(i)+" ";
                            }
                            bw.write(asd);
                            bw.newLine();

                            // закрываем поток
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Успешно добавлено ", Toast.LENGTH_SHORT);
                        toast.show();

                        addPredmet = new ArrayList();
                        tableLayout.removeAllViews();

                        Intent intent = new Intent(build.this,    MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Добавь ради приличия хоть одно событие", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не введено имя", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                addMonth=selectedItemPosition;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                addDataNed=selectedItemPosition;
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        prome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                xWeek = selectedItemPosition;
                switch (xWeek){
                    case 0:
                        scrollNull();
                        spinnerWeek.setVisibility(View.GONE);
                        spinnerMonth.setVisibility(View.VISIBLE);
                        textYear.setVisibility(View.VISIBLE);
                        textDay.setVisibility(View.GONE);
                        textDay2.setVisibility(View.VISIBLE);
                        addProm=-1;
                        break;
                    case 1:
                        scrollNull();
                        spinnerWeek.setVisibility(View.GONE);
                        spinnerMonth.setVisibility(View.GONE);
                        textYear.setVisibility(View.GONE);
                        textDay.setVisibility(View.GONE);
                        textDay2.setVisibility(View.GONE);
                        addProm=0;
                        break;
                    case 2:
                        scrollNull();
                        spinnerWeek.setVisibility(View.VISIBLE);
                        spinnerMonth.setVisibility(View.GONE);
                        textYear.setVisibility(View.GONE);
                        textDay.setVisibility(View.GONE);
                        textDay2.setVisibility(View.GONE);
                        addProm=7;
                        break;
                    case 3:
                        scrollNull();
                        spinnerWeek.setVisibility(View.GONE);
                        spinnerMonth.setVisibility(View.GONE);
                        textYear.setVisibility(View.GONE);
                        textDay.setVisibility(View.VISIBLE);
                        textDay2.setVisibility(View.GONE);
                        addProm=31;
                        break;
                    case 4:
                        scrollNull();
                        spinnerWeek.setVisibility(View.GONE);
                        spinnerMonth.setVisibility(View.VISIBLE);
                        textYear.setVisibility(View.GONE);
                        textDay.setVisibility(View.GONE);
                        textDay2.setVisibility(View.VISIBLE);
                        addProm=365;
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setOne() {
        setDefault();
        String y;
        if (textYear.length() == 0) {
            Calendar myCalendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            y=simpleDateFormat.format(myCalendar.getTime());
        } else {
            y=textYear.getText().toString();
        }
        if (textDay2.length()!=0) {
            if (Integer.valueOf(String.valueOf(textDay2.getText())) < 32) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView text1 = new TextView(this);
                text1.setText("Год: " + y);
                tableRow.addView(text1, 0);

                TextView text2 = new TextView(this);
                text2.setText(dateMonth[Integer.valueOf(addMonth)]);
                tableRow.addView(text2, 1);

                TextView text3 = new TextView(this);
                text3.setText("Дата: " + textDay2.getText().toString());
                tableRow.addView(text3, 2);

                TextView text4 = new TextView(this);
                text4.setText("C: " + String.valueOf(tch1) + " ч. " + String.valueOf(tmin1) + " м.");
                tableRow.addView(text4, 3);

                TextView text5 = new TextView(this);
                text5.setText("ДО: " + String.valueOf(tch2) + " ч. " + String.valueOf(tmin2) + " м.");
                tableRow.addView(text5, 4);

                tableLayout.addView(tableRow);

                addPredmet.add(String.valueOf(y));
                addPredmet.add(String.valueOf(addMonth));
                addPredmet.add(String.valueOf(textDay2.getText()));
                addPredmet.add(String.valueOf(tch1 * 60 + tmin1));
                addPredmet.add(String.valueOf(tch2 * 60 + tmin2));
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Этот день никогда не наступит", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введи день", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private void setAllDay() {
        setDefault();
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView text1 = new TextView(this);
        text1.setText("C   "+String.valueOf(tch1)+" ч.   "+String.valueOf(tmin1)+" м.");
        tableRow.addView(text1, 0);

        TextView text2 = new TextView(this);
        text2.setText("ДО   "+String.valueOf(tch2)+" ч.   "+String.valueOf(tmin2)+" м.");
        tableRow.addView(text2, 1);

        tableLayout.addView(tableRow);

        addPredmet.add(String.valueOf(tch1*60+tmin1));
        addPredmet.add(String.valueOf(tch2*60+tmin2));


    }

    private void setWeek() {
        setDefault();
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView text1 = new TextView(this);
        text1.setText(dataName[Integer.valueOf(addDataNed)]);
        tableRow.addView(text1, 0);

        TextView text2 = new TextView(this);
        text2.setText("C   "+String.valueOf(tch1)+" ч.   "+String.valueOf(tmin1)+" м.");
        tableRow.addView(text2, 1);

        TextView text3 = new TextView(this);
        text3.setText("ДО   "+String.valueOf(tch2)+" ч.   "+String.valueOf(tmin2)+" м.");
        tableRow.addView(text3, 2);

        tableLayout.addView(tableRow);

        addPredmet.add(String.valueOf(addDataNed));
        addPredmet.add(String.valueOf(tch1*60+tmin1));
        addPredmet.add(String.valueOf(tch2*60+tmin2));
    }

    private void setMonth() {
        if (textDay.length()!=0) {
            if (Integer.valueOf(textDay.getText().toString()) < 31) {
                setDefault();
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView text1 = new TextView(this);
                text1.setText("День: " + textDay.getText().toString());
                tableRow.addView(text1, 0);

                TextView text2 = new TextView(this);
                text2.setText("C   " + String.valueOf(tch1) + " ч.   " + String.valueOf(tmin1) + " м.");
                tableRow.addView(text2, 1);

                TextView text3 = new TextView(this);
                text3.setText("ДО   " + String.valueOf(tch2) + " ч.   " + String.valueOf(tmin2) + " м.");
                tableRow.addView(text3, 2);

                tableLayout.addView(tableRow);

                addPredmet.add(textDay.getText().toString());
                addPredmet.add(String.valueOf(tch1 * 60 + tmin1));
                addPredmet.add(String.valueOf(tch2 * 60 + tmin2));
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Этот день никогда не наступит", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите день", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void setYear() {
        setDefault();
        if (Integer.valueOf(textDay2.getText().toString()) < 32) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView text1 = new TextView(this);
            text1.setText(dateMonth[Integer.valueOf(addMonth)]);
            tableRow.addView(text1, 0);

            TextView text2 = new TextView(this);
            text2.setText(textDay2.getText().toString());
            tableRow.addView(text2, 1);

            TextView text3 = new TextView(this);
            text3.setText("C   " + String.valueOf(tch1) + " ч.   " + String.valueOf(tmin1) + " м.");
            tableRow.addView(text3, 2);

            TextView text4 = new TextView(this);
            text4.setText("ДО   " + String.valueOf(tch2) + " ч.   " + String.valueOf(tmin2) + " м.");
            tableRow.addView(text4, 3);

            tableLayout.addView(tableRow);

            addPredmet.add(addMonth);
            addPredmet.add(textDay2.getText().toString());
            addPredmet.add(String.valueOf(tch1 * 60 + tmin1));
            addPredmet.add(String.valueOf(tch2 * 60 + tmin2));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Этот день никогда не наступит", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void setDefault() {
        if (textCh1.getText().length()!=0){
            tch1 = Integer.valueOf(textCh1.getText().toString());
        } else {
            tch1=0;
        }
        if (textMin1.getText().length()!=0){
            tmin1 = Integer.valueOf(textMin1.getText().toString());
        } else {
            tmin1=0;
        }
        if (textCh2.getText().length()!=0){
            tch2 = Integer.valueOf(textCh2.getText().toString());
        }else {
            tch2=0;
        }
        if (textMin2.getText().length()!=0) {
            tmin2 = Integer.valueOf(textMin2.getText().toString());
        } else {
            tmin2=0;
        }
        if (textCh1.getText().length()==0 && textMin1.getText().length()==0) {
            tch1=tch2;
            tmin1=tmin2;
        }
        if (textCh2.getText().length()==0 && textMin2.getText().length()==0) {
            tch2=tch1;
            tmin2=tmin1;
        }
        if (tch1*60+tmin1>tch2*60+tmin2){
            int k;
            k=tch2;
            tch2=tch1;
            tch1=k;
            k=tmin2;
            tmin2=tmin1;
            tmin1=k;
        }
    }

    private void scrollNull() {
        int childCount = tableLayout.getChildCount();
        addPredmet = new ArrayList();
        for(int i=0;i<childCount;i++) {
            tableLayout.removeViewAt(0);
        }
    }

    private void delete(int kolDel) {
        int childCount = tableLayout.getChildCount();
        if (addPredmet.size()!=0){
            tableLayout.removeViewAt(childCount-1);
            for (int i=0;i<kolDel;i++) {
                addPredmet.remove(addPredmet.size() - 1);
            }
        }
    }
}

/*
save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // открываем поток для чтения
                    BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                            openFileInput("time")));
                    String str = "";
                    // читаем содержимое
                    while ((str = timeRead.readLine()) != null) {

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("time", MODE_APPEND)));
                    // пишем данные
                    bw.write(editText.getText().toString());
                    bw.newLine();
                    // закрываем поток
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("name", MODE_APPEND)));
                    // пишем данные
                    bw.write(editText2.getText().toString()+" ");
                    // закрываем поток
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(build.this,    MainActivity.class);
                startActivity(intent);
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("time", MODE_PRIVATE)));
                    // закрываем поток
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("name", MODE_PRIVATE)));
                    // закрываем поток
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(build.this,    MainActivity.class);
                startActivity(intent);
            }
        });
 */
/*
if (name.getText().length()!=0){
                    if (addPredmet.size()>3){
                        addPredmet.set(1,String.valueOf(name.getText()));
                        addPredmet.set(0,"7");
                        try {
                            // отрываем поток для записи
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                    openFileOutput("time", MODE_APPEND)));
                            // пишем данные
                            String asd="";
                            for (int i=0;i<addPredmet.size();i++) {
                                asd+=addPredmet.get(i)+" ";
                            }
                            bw.write(asd);
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    String.valueOf(asd), Toast.LENGTH_SHORT);
                            toast.show();
                            bw.newLine();
                            // закрываем поток
                            bw.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(build.this,    MainActivity.class);
                        startActivity(intent);

                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Успешно добавлено", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Добавь ради приличия хоть одно событие", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не введено имя", Toast.LENGTH_SHORT);
                    toast.show();
                }
 */