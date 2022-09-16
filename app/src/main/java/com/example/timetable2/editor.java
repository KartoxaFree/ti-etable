package com.example.timetable2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.timetable2.MainActivity.buttonBackgroundColor;
import static com.example.timetable2.MainActivity.buttonTextColor;
import static com.example.timetable2.MainActivity.today;
import static com.example.timetable2.Settings.color_PREFERENCES;
import static com.example.timetable2.Settings.color_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.downloadBase_PREFERENCES_COUNTER;
import static com.example.timetable2.build.addPredmet;

public class editor extends AppCompatActivity {

    public static final String editorSwift_PREFERENCES = "editorSwift";
    public static final String editorSwift_PREFERENCES_COUNTER = "editorSwift2";
    private SharedPreferences editorSwift;

    private SharedPreferences color;

    Button add, saveAll, clear;
    ListView listView;

    final Context context = this;

    Switch sw1,sw2,sw3,sw4,sw5;
    final Integer s[] = new Integer[]{0,0,0,0,0};
    final Integer sCheck[] = new Integer[]{-1,0,7,31,365};
    final Integer skol[] = new Integer[]{5,2,3,3,4};
    final String sName[] = new String[]{"Единожды","Ежеденевно","Еженедельно","Ежемесячно","Ежегодно"};

    final String dateMonth[] = new String[]{"Январь","Февраль","Март","Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
    final String dataName[] = new String[]{"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота" ,"Воскресенье"};

    public static List<List> fullList = new ArrayList<List>();
    public static List<List> fullListSort = new ArrayList<List>();

    private ArrayAdapter<String> mAdapter;

    int defSize=20;
    int obchKol=0,nKol=0;
    public int k=-1;
    public int addMonth, addDataNed;
    public int delName;

    //номер строки в list - номер в массиве - 2 или 1 значение (1-название, 2-начало и количество)
    public static List<List> netKol = new ArrayList<List>();
    List setAdapter = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        View aView = getWindow().getDecorView();
        aView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        listView = findViewById(R.id.listView);
        editorSwift = getSharedPreferences(editorSwift_PREFERENCES, Context.MODE_PRIVATE);
        color = getSharedPreferences(color_PREFERENCES, Context.MODE_PRIVATE);
        add = (Button) findViewById(R.id.add);
        saveAll = (Button) findViewById(R.id.saveAll);
        clear = (Button) findViewById(R.id.clear);

        sw1 = (Switch) findViewById(R.id.sw1);
        sw2 = (Switch) findViewById(R.id.sw2);
        sw3 = (Switch) findViewById(R.id.sw3);
        sw4 = (Switch) findViewById(R.id.sw4);
        sw5 = (Switch) findViewById(R.id.sw5);

        //установить
        if (editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)%10==1) {
            sw1.setChecked(true);
            s[0]=1;
        }
        if (editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/10%10==1) {
            sw2.setChecked(true);
            s[1]=1;
        }
        if (editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/100%10==1) {
            sw3.setChecked(true);
            s[2]=1;
        }
        if (editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/1000%10==1) {
            sw4.setChecked(true);
            s[3]=1;
        }
        if (editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/10000==1) {
            sw5.setChecked(true);
            s[4]=1;
        }

        setArrayList();
        set();
        check();

        add.setTextColor(buttonTextColor);
        add.setBackgroundColor(buttonBackgroundColor);
        saveAll.setTextColor(buttonTextColor);
        saveAll.setBackgroundColor(buttonBackgroundColor);
        clear.setTextColor(buttonTextColor);
        clear.setBackgroundColor(buttonBackgroundColor);
    }

    //установить Switch
    private void setEditorSwift(int num, int s){
        int ss1=editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)%10;
        int ss2=editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/10%10;
        int ss3=editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/100%10;
        int ss4=editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/1000%10;
        int ss5=editorSwift.getInt(editorSwift_PREFERENCES_COUNTER, 0)/10000%10;

        switch(num) {
            case 0:
                ss1=s;
                break;
            case 1:
                ss2=s;
                break;
            case 2:
                ss3=s;
                break;
            case 3:
                ss4=s;
                break;
            case 4:
                ss5=s;
                break;
        }
        SharedPreferences.Editor editor = editorSwift.edit();
        editor.putInt(editorSwift_PREFERENCES_COUNTER, ss5*10000+ss4*1000+ss3*100+ss2*10+ss1);
        editor.apply();

        set();
    }

    //выпадающий список
    private void createDialog(List strS) {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.edit_drop_list, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);
        final AlertDialog dlg = mDialogBuilder.create();

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText name, textCh1, textCh2, textMin1, textMin2;
        final TextView textView7, textView8, textView9, textView1;
        final Spinner spinnerMonth, spinnerWeek;
        final EditText textDay, textDay2, textYear;
        final Button save;

        name = (EditText) promptsView.findViewById(R.id.name);
        textCh1 = (EditText) promptsView.findViewById(R.id.textCh1);
        textCh2 = (EditText) promptsView.findViewById(R.id.textCh2);
        textMin1 = (EditText) promptsView.findViewById(R.id.textMin1);
        textMin2 = (EditText) promptsView.findViewById(R.id.textMin2);
        textView7 = (TextView) promptsView.findViewById(R.id.textView7);
        textView8 = (TextView) promptsView.findViewById(R.id.textView8);
        textView9 = (TextView) promptsView.findViewById(R.id.textView9);
        textView1 = (TextView) promptsView.findViewById(R.id.textView1);
        spinnerMonth = (Spinner) promptsView.findViewById(R.id.spinnerMonth);
        textDay = (EditText) promptsView.findViewById(R.id.textDay);
        textDay2 = (EditText) promptsView.findViewById(R.id.textDay2);
        textYear = (EditText) promptsView.findViewById(R.id.textYear);
        spinnerWeek = (Spinner) promptsView.findViewById(R.id.spinnerWeek);
        save = (Button) promptsView.findViewById(R.id.save);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.spinDataNed,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeek.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinDataNed,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);
        adapter = ArrayAdapter.createFromResource(this, R.array.month,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        k=-1;
        if (strS.size()<=3){
            k=0;
            textCh1.setVisibility(View.GONE);
            textCh2.setVisibility(View.GONE);
            textMin1.setVisibility(View.GONE);
            textMin2.setVisibility(View.GONE);
            textView7.setVisibility(View.GONE);
            textView8.setVisibility(View.GONE);
            textView9.setVisibility(View.GONE);
            textView1.setVisibility(View.GONE);
            spinnerMonth.setVisibility(View.GONE);
            textDay.setVisibility(View.GONE);
            textDay2.setVisibility(View.GONE);
            textYear.setVisibility(View.GONE);
            spinnerWeek.setVisibility(View.GONE);

            name.setVisibility(View.VISIBLE);
            name.setText(String.valueOf(fullList.get((int)strS.get(1)).get(1)));
        } else {
            name.setVisibility(View.GONE);
            setTime(textCh1, textMin1, textCh2, textMin2, strS);
            switch (Integer.valueOf((int)strS.get(2))){
                case -1:
                    k=1;
                    spinnerWeek.setVisibility(View.GONE);
                    spinnerMonth.setVisibility(View.VISIBLE);
                    textYear.setVisibility(View.VISIBLE);
                    textDay.setVisibility(View.GONE);
                    textDay2.setVisibility(View.VISIBLE);

                    textYear.setText(String.valueOf(fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)))));
                    spinnerMonth.setSelection(Integer.valueOf((int) fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)+1))));
                    textDay2.setText(String.valueOf(fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)+2))));
                    addMonth=Integer.valueOf((int) fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)+1)));
                    break;
                case 0:
                    k=2;
                    spinnerWeek.setVisibility(View.GONE);
                    spinnerMonth.setVisibility(View.GONE);
                    textYear.setVisibility(View.GONE);
                    textDay.setVisibility(View.GONE);
                    textDay2.setVisibility(View.GONE);
                    break;
                case 7:
                    k=3;
                    spinnerWeek.setVisibility(View.VISIBLE);
                    spinnerMonth.setVisibility(View.GONE);
                    textYear.setVisibility(View.GONE);
                    textDay.setVisibility(View.GONE);
                    textDay2.setVisibility(View.GONE);

                    spinnerWeek.setSelection(Integer.valueOf((int) fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)))));
                    addDataNed=Integer.valueOf((int) fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3))));
                    break;
                case 31:
                    k=4;
                    spinnerWeek.setVisibility(View.GONE);
                    spinnerMonth.setVisibility(View.GONE);
                    textYear.setVisibility(View.GONE);
                    textDay.setVisibility(View.VISIBLE);
                    textDay2.setVisibility(View.GONE);

                    textDay.setText(String.valueOf(fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)))));
                    break;
                case 365:
                    k=5;
                    spinnerWeek.setVisibility(View.GONE);
                    spinnerMonth.setVisibility(View.VISIBLE);
                    textYear.setVisibility(View.GONE);
                    textDay.setVisibility(View.GONE);
                    textDay2.setVisibility(View.VISIBLE);

                    spinnerMonth.setSelection(Integer.valueOf((int) fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)))));
                    textDay2.setText(String.valueOf(fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3)+1))));
                    addMonth=Integer.valueOf((int) fullList.get(Integer.valueOf((int) strS.get(1))).get(Integer.valueOf((int) strS.get(3))));
                    break;
            }

        }
        delName = 0;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast;
                List k2;

                if (k==0 || Integer.valueOf(textCh1.getText().toString())<25 && Integer.valueOf(textCh2.getText().toString())<25 && Integer.valueOf(textMin1.getText().toString())<60 && Integer.valueOf(textMin2.getText().toString())<60) {
                    if (textCh1.getText().length() != 0 || textMin1.getText().length() != 0 ||
                            textCh2.getText().length() != 0 || textMin2.getText().length() != 0 || k == 0) {
                        switch (k) {
                            case 0:
                                if (name.getText().length() != 0) {
                                    k2 = fullList.get(Integer.valueOf((int) strS.get(1)));
                                    Log.i("k",String.valueOf(k2));
                                    k2.set(1, String.valueOf(name.getText().toString()).replace(" ","_"));
                                    Log.i("k",String.valueOf(k2));
                                    fullList.set(Integer.valueOf((int) strS.get(1)), k2);
                                } else {
                                    delName+=1;
                                    if (delName==1) {
                                        toast = Toast.makeText(getApplicationContext(), "Поле пустует\nнажмите ещё раз для удаления", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                    if (delName>=2) {
                                        fullList.remove(Integer.valueOf((int) strS.get(1))*1);
                                        toast = Toast.makeText(getApplicationContext(), "удалено", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                }
                                break;
                            case 1:
                                k = 2;
                                if (textYear.length() != 0) {
                                    if (textDay2.length() != 0) {
                                        if (Integer.valueOf(textDay2.getText().toString()) <32) {
                                            k2 = fullList.get(Integer.valueOf((int) strS.get(1)));
                                            k2.set((int) strS.get(3), textYear.getText().toString());
                                            k2.set((int) strS.get(3) + 1, addMonth);
                                            k2.set((int) strS.get(3) + 2, textDay2.getText().toString());
                                            k2.set((int) strS.get(3) + 3, Integer.valueOf(textCh1.getText().toString()) * 60 + Integer.valueOf(textMin1.getText().toString()));
                                            k2.set((int) strS.get(3) + 4, Integer.valueOf(textCh2.getText().toString()) * 60 + Integer.valueOf(textMin2.getText().toString()));
                                            fullList.set(Integer.valueOf((int) strS.get(1)), k2);
                                        } else {
                                            toast = Toast.makeText(getApplicationContext(),
                                                    "Этот день никогда не наступит", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    } else {
                                        toast = Toast.makeText(getApplicationContext(),
                                                "Введите день", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    toast = Toast.makeText(getApplicationContext(),
                                            "Введите год", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                            case 2:
                                k2 = fullList.get(Integer.valueOf((int) strS.get(1)));
                                k2.set((int) strS.get(3), Integer.valueOf(textCh1.getText().toString())*60+Integer.valueOf(textMin1.getText().toString()));
                                k2.set((int) strS.get(3) + 1, Integer.valueOf(textCh2.getText().toString())*60+Integer.valueOf(textMin2.getText().toString()));
                                fullList.set(Integer.valueOf((int) strS.get(1)), k2);
                                break;
                            case 3:
                                k2 = fullList.get(Integer.valueOf((int) strS.get(1)));
                                k2.set((int) strS.get(3), addDataNed);
                                k2.set((int) strS.get(3) + 1, Integer.valueOf(textCh1.getText().toString())*60+Integer.valueOf(textMin1.getText().toString()));
                                k2.set((int) strS.get(3) + 2, Integer.valueOf(textCh2.getText().toString())*60+Integer.valueOf(textMin2.getText().toString()));
                                fullList.set(Integer.valueOf((int) strS.get(1)), k2);
                                break;
                            case 4:
                                if (textDay.length() != 0) {
                                    if (Integer.valueOf(textDay.getText().toString()) <32) {
                                        k2 = fullList.get(Integer.valueOf((int) strS.get(1)));
                                        k2.set((int) strS.get(3), Integer.valueOf(textDay.getText().toString()));
                                        k2.set((int) strS.get(3) + 1, Integer.valueOf(textCh1.getText().toString()) * 60 + Integer.valueOf(textMin1.getText().toString()));
                                        k2.set((int) strS.get(3) + 2, Integer.valueOf(textCh2.getText().toString()) * 60 + Integer.valueOf(textMin2.getText().toString()));
                                        fullList.set(Integer.valueOf((int) strS.get(1)), k2);
                                    } else {
                                        toast = Toast.makeText(getApplicationContext(),
                                                "Этот день никогда не наступит", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    toast = Toast.makeText(getApplicationContext(),
                                            "Введите день", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                            case 5:
                                if (textDay2.length() != 0) {
                                    if (Integer.valueOf(textDay2.getText().toString()) <32) {
                                        k2 = fullList.get(Integer.valueOf((int) strS.get(1)));
                                        k2.set((int) strS.get(3), addMonth);
                                        k2.set((int) strS.get(3) + 1, textDay2.getText().toString());
                                        k2.set((int) strS.get(3) + 2, Integer.valueOf(textCh1.getText().toString()) * 60 + Integer.valueOf(textMin1.getText().toString()));
                                        k2.set((int) strS.get(3) + 3, Integer.valueOf(textCh2.getText().toString()) * 60 + Integer.valueOf(textMin2.getText().toString()));
                                        fullList.set(Integer.valueOf((int) strS.get(1)), k2);
                                    } else {
                                        toast = Toast.makeText(getApplicationContext(),
                                                "Этот день никогда не наступит", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } else {
                                    toast = Toast.makeText(getApplicationContext(),
                                            "Введите день", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                                break;
                        }
                        if (delName!=1) {
                            set();
                            dlg.dismiss();
                        }
                    } else {
                        toast = Toast.makeText(getApplicationContext(),
                                "Введите время", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    toast = Toast.makeText(getApplicationContext(),
                            "Некоректное время", Toast.LENGTH_SHORT);
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

        //и отображаем его:
        dlg.show();
    }
    private void setTime(EditText text, EditText text2, EditText text3, EditText text4, List i){
        //установаить время из fullList
        text.setText(String.valueOf((int) fullList.get((int)i.get(1)).get((int)i.get(3)+(int)i.get(4)-2)/60));
        text2.setText(String.valueOf((int) fullList.get((int)i.get(1)).get((int)i.get(3)+(int)i.get(4)-2)%60));
        text3.setText(String.valueOf((int) fullList.get((int)i.get(1)).get((int)i.get(3)+(int)i.get(4)-1)/60));
        text4.setText(String.valueOf((int) fullList.get((int)i.get(1)).get((int)i.get(3)+(int)i.get(4)-1)%60));
    }

    //кнопки
    private void check() {
        //отклик нажатия
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int q=-1;
                for(int i=0;i<netKol.size();i++){
                    if(Integer.valueOf((int) netKol.get(i).get(0))==position){
                        q=i;
                    }
                }
                if (q!=-1){
                    Log.i("q",String.valueOf(netKol.get(q)));
                    createDialog(netKol.get(q));
                    //Log.i("Myapp",String.valueOf(fullList.get()));
                }
            }
        });
        //единожды
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int u=0;
                s[u]=0;
                if (isChecked==true){
                    s[u]=1;
                }
                setEditorSwift(u,s[u]);
            }
        });
        //ежеденевно
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int u=1;
                s[u]=0;
                if (isChecked==true){
                    s[u]=1;
                }
                setEditorSwift(u,s[u]);
            }
        });
        //еженедельно
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int u=2;
                s[u]=0;
                if (isChecked==true){
                    s[u]=1;
                }
                setEditorSwift(u,s[u]);
            }
        });
        //ежемесячно
        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int u=3;
                s[u]=0;
                if (isChecked==true){
                    s[u]=1;
                }
                setEditorSwift(u,s[u]);
            }
        });
        //ежегодно
        sw5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int u=4;
                s[u]=0;
                if (isChecked==true){
                    s[u]=1;
                }
                setEditorSwift(u,s[u]);
            }
        });
        //добавить
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editor.this,    build.class);
                startActivity(intent);
            }
        });
        //сохранение
        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("time", MODE_PRIVATE)));
                    // пишем данные
                    for (int i=0;i<fullList.size();i++) {
                        String asd="";
                        for (int j=0;j<fullList.get(i).size();j++) {
                            if (j==1) {
                                asd += String.valueOf(fullList.get(i).get(j)).replace(" ","_") + " ";
                            } else {
                                asd += String.valueOf(fullList.get(i).get(j)) + " ";
                            }
                        }
                        bw.write(asd);
                        bw.newLine();
                    }

                    // закрываем поток
                    bw.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.i("fullList",String.valueOf(fullList));
                Intent intent = new Intent(editor.this,    MainActivity.class);
                startActivity(intent);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Успешно сохранено", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //очистка
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.imports, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText tv = (EditText) promptsView.findViewById(R.id.tv);
                tv.setFocusable(false);
                tv.setLongClickable(false);
                tv.setText("Удалить все события?");
                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder.setCancelable(false).setPositiveButton("да",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //действие после нажатия:
                                try {
                                    // отрываем поток для записи
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                            openFileOutput("time", MODE_PRIVATE)));
                                    // пишем данные
                                    bw.write("");
                                    // закрываем поток
                                    bw.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                today = new ArrayList<>();

                                Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(editor.this,    MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("нет",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();
            }
        });
    }

    //установка listview
    public void set(){
        obchKol=0;
        nKol=0;
        netKol = new ArrayList();
        //обнуление
        setAdapter = new ArrayList();

        for (int i = 0; i < s.length; i++) {
            if (s[i]==1) {
                //setAdapter
                setAdapter.add("         "+String.valueOf(sName[i]));
                obchKol++;
                nKol++;
                boolean f=false;
                for (int j = 0; j < fullList.size(); j++) {
                    //НАФИГА?!?!?!?!?!?! а главное зачем!??!?!?!?!
                    /*if (fullList.size()==1){
                        break;
                    }*/
                    if (sCheck[i]==(int) fullList.get(j).get(0)){
                        //setAdapter
                        setAdapter.add("⬥ "+String.valueOf(fullList.get(j).get(1))+" ⬥");
                        List list1 = new ArrayList();
                        list1.add(obchKol);
                        list1.add(j);
                        netKol.add(list1);

                        obchKol++;
                        nKol++;
                        int h=skol[Arrays.asList(sCheck).indexOf((int) fullList.get(j).get(0))];
                        for (int w = 3; w < fullList.get(j).size(); w+=h) {
                            List list = new ArrayList();
                            for (int wkol = 0; wkol < h; wkol++) {
                                list.add(fullList.get(j).get(w+wkol));
                            }
                            setText(list,(int) fullList.get(j).get(0));
                            list1 = new ArrayList();
                            list1.add(obchKol);
                            list1.add(j);
                            list1.add(fullList.get(j).get(0));
                            list1.add(w);
                            list1.add(h);
                            netKol.add(list1);

                            obchKol++;
                        }
                        f=true;
                    }
                }
                if (f==false) {
                    //setAdapter
                    setAdapter.add("Таких событий нет");
                    obchKol++;
                    nKol++;
                }
            }
        }
        if (fullList.size()==0 && s[0]==0 && s[1]==0 && s[2]==0 && s[3]==0 && s[4]==0) {
            //setAdapter
            setAdapter.add("Тут пустовато, заполни меня");
        }

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, setAdapter);
        listView.setAdapter(mAdapter);
    }
    private void setText(List listSet, int lSize) {
        String line="";
        if (lSize==sCheck[0]){
            line+=String.valueOf(listSet.get(0))+" год, ";
            line+=String.valueOf(dateMonth[(int)listSet.get(1)])+", ";
            line+=String.valueOf(listSet.get(2))+" день, ";
            line+=clock(listSet,3);
        }
        if (lSize==sCheck[1]){
            line+=clock(listSet,0);
        }
        if (lSize==sCheck[2]){
            line+=String.valueOf(dataName[(int)listSet.get(0)])+", ";
            line+=clock(listSet,1);
        }
        if (lSize==sCheck[3]){
            line+=String.valueOf((int)listSet.get(0))+" день, ";
            line+=clock(listSet,1);
        }
        //clock()
        if (lSize==sCheck[4]){
            line+=String.valueOf(dateMonth[(int)listSet.get(0)])+" ";
            line+=String.valueOf(listSet.get(1))+" день, ";
            line+=clock(listSet,2);
        }
        //setAdapter
        setAdapter.add(""+String.valueOf(line));
    }
    private String clock(List lS, int q) {
        String str="";
        str+="время: c "+((int)lS.get(q)/60 < 10 ? "" + String.valueOf((int)lS.get(q)/60) : String.valueOf((int)lS.get(q)/60))+
                ":"+ ((int) lS.get(q) % 60 < 10 ? "0" + String.valueOf((int)lS.get(q)%60) : String.valueOf((int)lS.get(q)%60))+" до ";
        q++;
        str+=((int)lS.get(q)/60 < 10 ? "" + String.valueOf((int)lS.get(q)/60) : String.valueOf((int)lS.get(q)/60))+
                ":"+ ((int) lS.get(q) % 60 < 10 ? "0" + String.valueOf((int)lS.get(q)%60) : String.valueOf((int)lS.get(q)%60));
        return str;
    }

    private void setArrayList(){
        fullList = new ArrayList<List>();
        //создание расписания на день
        try {
            // открываем поток для чтения
            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                    openFileInput("time")));
            String str = "";
            // читаем содержимое
            while ((str = timeRead.readLine()) != null) {
                String[] strSplit = str.split(" ");
                //для одноразовых
                //название-доп-год-месяц-день-(начало-конец)
                List list1 = new ArrayList();
                for (int j = 0; j<strSplit.length; j++) {
                    if (j==1) {
                        list1.add(String.valueOf(strSplit[1].replace("_", " ")));
                    } else {
                        list1.add(Integer.valueOf(strSplit[j]));
                    }
                }
                fullList.add(list1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fullListSort = new ArrayList<List>();
    }
}