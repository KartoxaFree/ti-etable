package com.example.timetable2;
//очитска кеша
//File -> Invalidate Caches/Restart
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.jsoup.select.Elements;

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

import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;
import static android.view.View.GONE;
import static com.example.timetable2.Settings.autoSyncing_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.color_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.downloadBase_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.notify_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.pogr_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.simpleMode_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.size_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.time_PREFERENCES_COUNTER;
import static com.example.timetable2.Settings.sound_PREFERENCES_COUNTER;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainActivity extends AppCompatActivity {

    public static final String oldTime_PREFERENCES = "mysettings";
    public static final String oldTime_PREFERENCES_COUNTER = "counter";
    private SharedPreferences oldTime;

    public static final String zapros_PREFERENCES = "mysettings2";
    public static final String zapros_PREFERENCES_COUNTER = "counter2";
    private SharedPreferences zapros;

    public static final String lastSyncing_PREFERENCES = "lastSyncingPar";
    public static final String lastSyncing_PREFERENCES_COUNTER = "lastSyncingPar2";
    private SharedPreferences lastSyncing;

    private SharedPreferences size;
    private SharedPreferences time;
    private SharedPreferences pogr;
    private SharedPreferences color;
    private SharedPreferences notify;
    private SharedPreferences sound;
    private SharedPreferences simpleMode;
    private SharedPreferences autoSyncing;
    private SharedPreferences downloadBase;

    Button full;
    ImageButton menu;
    TableLayout tableLayout;

    //онлайн firebase
    private DatabaseReference ref;
    private FirebaseDatabase database;

    private NotificationManager notificationManager;
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;
    // Идентификатор канала
    private static String CHANNEL_ID = "Cat channel";

    public static int pogrGlobal;
    public static int buttonBackgroundColor, buttonTextColor;
    public int nowYrok = -1, nextYrok = -1;
    public static int nowTimeFormatSec, nowDataNed;
    private String k;
    public int red, green, blue, textColor;
    public static int dopInt = 1;
    int notifyF = 0;
    public int stringHour = 0;
    public boolean connectToFirebase = false;
    public int kolSet = 0;
    public String dataYMD="";

    double version = Double.parseDouble(Build.VERSION.RELEASE);

    final Context context = this;
    private ProgressBar pbHorizontal;
    private TextView tvProgressHorizontal;

    final String dataName[] = new String[]{"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье",
            "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"};
    List dateMonth = Arrays.asList(new String[]{"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"});
    //Log.i("Myapp",String.valueOf(strSplit.length));

    //формат: название(str)-доп значение-начало-конец
    public static List<List> today = new ArrayList<List>();
    public static List<List> todaySort = new ArrayList<List>();
    public static ArrayList nowYrok2 = new ArrayList<>();

    public List homeWorkList = new ArrayList();
    public List nameList = new ArrayList();
    public List timeList = new ArrayList();

    public List homeWorkListFull = new ArrayList();
    public List nameListFull = new ArrayList();
    public List timeListFull = new ArrayList();

    // благодоря этому классу мы будет разбирать данные на куски
    public Elements title;
    // то в чем будем хранить данные пока не передадим адаптеру
    public ArrayList<String> titleList = new ArrayList<String>();
    // Listview Adapter для вывода данных
    private ArrayAdapter<String> adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*View aView = getWindow().getDecorView();
        aView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        // | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN*/

        oldTime = getSharedPreferences(oldTime_PREFERENCES, Context.MODE_PRIVATE);
        zapros = getSharedPreferences(zapros_PREFERENCES, Context.MODE_PRIVATE);
        lastSyncing = getSharedPreferences(lastSyncing_PREFERENCES, Context.MODE_PRIVATE);
        size = getSharedPreferences(Settings.size_PREFERENCES, Context.MODE_PRIVATE);
        time = getSharedPreferences(Settings.time_PREFERENCES, Context.MODE_PRIVATE);
        pogr = getSharedPreferences(Settings.pogr_PREFERENCES, Context.MODE_PRIVATE);
        color = getSharedPreferences(Settings.color_PREFERENCES, Context.MODE_PRIVATE);
        notify = getSharedPreferences(Settings.notify_PREFERENCES, Context.MODE_PRIVATE);
        sound = getSharedPreferences(Settings.sound_PREFERENCES, Context.MODE_PRIVATE);
        simpleMode = getSharedPreferences(Settings.simpleMode_PREFERENCES, Context.MODE_PRIVATE);
        autoSyncing = getSharedPreferences(Settings.autoSyncing_PREFERENCES, Context.MODE_PRIVATE);
        downloadBase = getSharedPreferences(Settings.downloadBase_PREFERENCES, Context.MODE_PRIVATE);

        full = (Button) findViewById(R.id.full);
        pbHorizontal = (ProgressBar) findViewById(R.id.pb_horizontal);
        tvProgressHorizontal = (TextView) findViewById(R.id.tv_progress_horizontal);
        menu = (ImageButton) findViewById(R.id.menu);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        //определение времени
        time();
        //определение расписания на день
        setArrayList();
        //определение текцущего события
        techYrok();
        //установка расписания
        set();
        //время до начала или конца события
        postProgress();
        //постоянная проверка
        update();
        //переход на другие окна
        newActivity();

        //установка параметров
        full.setText(dataName[nowDataNed + 7]);

        red = color.getInt(color_PREFERENCES_COUNTER, 250) / 1000000;
        green = color.getInt(color_PREFERENCES_COUNTER, 238) / 1000 % 1000;
        blue = color.getInt(color_PREFERENCES_COUNTER, 221) % 1000;

        int s = (red + green + blue) / 3;
        if (s > 125) {
            textColor = 0;
        } else {
            textColor = 255;
        }
        full.setText(dataName[nowDataNed + 7]);
        buttonTextColor=Color.rgb(textColor, textColor, textColor);
        buttonBackgroundColor=Color.rgb(red, green, blue);
        full.setTextColor(buttonTextColor);
        full.setBackgroundColor(buttonBackgroundColor);
        //menu.setBackgroundColor(Color.rgb(r, g, b));

        //автосинхронизация
        if (autoSyncing.getInt(autoSyncing_PREFERENCES_COUNTER, 0)!=0) {
            if (stringHour-lastSyncing.getInt(lastSyncing_PREFERENCES_COUNTER, 0)>=autoSyncing.getInt(autoSyncing_PREFERENCES_COUNTER, 0)) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            snapshot.child("lessons").child(downloadBase.getString(downloadBase_PREFERENCES_COUNTER, "")).getValue().toString();
                            try {
                                // отрываем поток для записи
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                        openFileOutput("timeBase", MODE_PRIVATE)));
                                // пишем данные
                                bw.write(snapshot.child("lessons").child(downloadBase.getString(downloadBase_PREFERENCES_COUNTER, "")).getValue().toString());
                                bw.newLine();

                                // закрываем поток
                                bw.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getApplicationContext(), "Синхронизировано", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = lastSyncing.edit();
                            editor.putInt(lastSyncing_PREFERENCES_COUNTER, stringHour);
                            editor.apply();
                        } catch (NullPointerException e) {
                            Toast.makeText(getApplicationContext(), "Синхронизация провалена", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        }

        //проверка версии
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (version<Double.valueOf(snapshot.child("Admin").child("version").getValue().toString())) {
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
                    tv.setText("Обновить версию?");
                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder.setCancelable(false).setPositiveButton("да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //действие после нажатия:
                                    Intent browserIntent = new
                                            Intent(Intent.ACTION_VIEW, Uri.parse(snapshot.child("Admin").child("link").getValue().toString()));
                                    startActivity(browserIntent);
                                }
                            })
                            .setNegativeButton("нет",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    //Создаем AlertDialog:
                    AlertDialog alertDialog = mDialogBuilder.create();

                    //и отображаем его:
                    alertDialog.show();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //переход на другие Activity
    private void newActivity() {
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, fullTable.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    //выпадающий список
    private void createDialog() {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.droplist, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final ImageButton SettingsButton = (ImageButton) promptsView.findViewById(R.id.SettingsButton);
        final ImageButton buildButton = (ImageButton) promptsView.findViewById(R.id.buildButton);
        final ImageButton infoButton = (ImageButton) promptsView.findViewById(R.id.infoButton);
        final ImageButton syncingButton = (ImageButton) promptsView.findViewById(R.id.syncingButton);
        final ImageButton button = (ImageButton) promptsView.findViewById(R.id.button);
        final TextView infoText = (TextView) promptsView.findViewById(R.id.infoText);

        //установка простого режима
        if (simpleMode.getInt(simpleMode_PREFERENCES_COUNTER, 0)==1) {
            buildButton.setVisibility(GONE);
            button.setVisibility(GONE);
            infoText.setVisibility(GONE);
        }

        //настройки
        SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        //добавить в расписание
        buildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, editor.class);
                startActivity(intent);
            }
        });
        //редактировать уроки
        syncingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, syncingOnline.class);
                startActivity(intent);
            }
        });
        //информация о приложении и обо мне
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, info.class);
                startActivity(intent);
            }
        });

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        //и отображаем его:
        alertDialog.show();
    }

    //установка времени
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void time() {
        //yyyy-MM-dd HH:mm:ss || 2016-09-25 16:50:34
        Calendar myCalendar = Calendar.getInstance();
        nowTimeFormatSec = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");

        switch (simpleDateFormat.format(myCalendar.getTime())) {
            case "понедельник":
                nowDataNed = 0;
                break;
            case "вторник":
                nowDataNed = 1;
                break;
            case "среда":
                nowDataNed = 2;
                break;
            case "четверг":
                nowDataNed = 3;
                break;
            case "пятница":
                nowDataNed = 4;
                break;
            case "суббота":
                nowDataNed = 5;
                break;
            case "воскресенье":
                nowDataNed = 6;
                break;
            case "Monday":
                nowDataNed = 0;
                break;
            case "Tuesday":
                nowDataNed = 1;
                break;
            case "Wednesday":
                nowDataNed = 2;
                break;
            case "Thursday":
                nowDataNed = 3;
                break;
            case "Friday":
                nowDataNed = 4;
                break;
            case "Saturday":
                nowDataNed = 5;
                break;
            case "Sunday":
                nowDataNed = 6;
                break;
        }

        simpleDateFormat = new SimpleDateFormat("kk");
        k = simpleDateFormat.format(myCalendar.getTime());
        if (Integer.valueOf(k) != 24) {
            nowTimeFormatSec += Integer.parseInt(k) * 3600;
        }

        simpleDateFormat = new SimpleDateFormat("mm");
        k = simpleDateFormat.format(myCalendar.getTime());
        nowTimeFormatSec += Integer.parseInt(k) * 60;

        simpleDateFormat = new SimpleDateFormat("ss");
        k = simpleDateFormat.format(myCalendar.getTime());
        nowTimeFormatSec += Integer.parseInt(k);

        //не работает 29 февраля
        int[] monthDay = {31,28,31,30,31,30,31,31,30,31,30,31};
        simpleDateFormat = new SimpleDateFormat("yyyy");
        stringHour = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()))*24*60;
        dataYMD = simpleDateFormat.format(myCalendar.getTime())+":";
        simpleDateFormat = new SimpleDateFormat("MM");
        stringHour += Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()))*monthDay[Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()))];
        dataYMD += simpleDateFormat.format(myCalendar.getTime())+":";
        simpleDateFormat = new SimpleDateFormat("dd");
        stringHour += Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()))*24;
        dataYMD += simpleDateFormat.format(myCalendar.getTime());
        simpleDateFormat = new SimpleDateFormat("HH");
        stringHour += Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));

        //погрешность
        nowTimeFormatSec -= pogr.getInt(pogr_PREFERENCES_COUNTER, 0);
    }

    //заполнение окна
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void set() {
        //обнуление
        int childCount = tableLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            tableLayout.removeViewAt(0);
        }

        for (int i = 0; i < today.size(); i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView text = new TextView(this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size.getInt(size_PREFERENCES_COUNTER, 35));
            text.setText(String.valueOf(today.get(i).get(0)));
            text.setId(i);

            //подсветка текущего
            for (int j = 0; j < nowYrok2.size(); j++) {
                if (Integer.valueOf((int) nowYrok2.get(j)) == i) {
                    text.setTextColor(Color.rgb(0, 200, 0));
                }
            }
            //подсветка следующего
            if (nextYrok == i) {
                text.setTextColor(Color.rgb(255, 165, 0));
            }
            tableRow.addView(text, 0);

            if (time.getInt(time_PREFERENCES_COUNTER, 0) == 1) {
                TextView text2 = new TextView(this);
                text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                text2.setGravity(Gravity.TOP);
                String l = String.valueOf((int) today.get(i).get(1 + dopInt) / 60);
                String l2 = String.valueOf((int) today.get(i).get(1 + dopInt) % 60);
                l2 = (Integer.valueOf(l2) < 10 ? "0" + l2 : l2);
                String l3 = String.valueOf((int) today.get(i).get(2 + dopInt) / 60);
                String l4 = String.valueOf((int) today.get(i).get(2 + dopInt) % 60);
                l4 = (Integer.valueOf(l4) < 10 ? "0" + l4 : l4);
                text2.setWidth(70);

                text2.setText(l + ":" + l2 + " - " + l3 + ":" + l4);
                tableRow.addView(text2, 1);
            }

            tableLayout.addView(tableRow);
        }
        if (today.size() == 0) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView text = new TextView(this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size.getInt(size_PREFERENCES_COUNTER, 40));
            text.setText("Сегодня отдыхай");

            tableRow.addView(text, 0);

            tableLayout.addView(tableRow);
        }
        for(int i = 0;i<today.size();i++) {
            TextView chkBox = tableLayout.findViewById(i);
            chkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("chkBoxName",String.valueOf(chkBox.getText().toString()));
                    boolean checkHomeWork = false;
                    for(int hw = 0;hw<homeWorkList.size();hw++) {
                        String strHW = String.valueOf(homeWorkList.get(hw));
                        String[] strHWSplit = strHW.split(" ");
                        if (strHWSplit.length!=0) {
                            if (strHWSplit[0].equals(String.valueOf(chkBox.getText().toString()).replace(" ","_"))) {
                                checkHomeWork = true;
                                createDialogHomeWork(hw, String.valueOf(chkBox.getText().toString()));
                                break;
                            }
                        }
                    }
                    if (!checkHomeWork) {
                        checkHomeWork = false;
                        for(int hw = 0;hw<nameList.size();hw++) {
                            if (nameList.get(hw).equals(String.valueOf(chkBox.getText().toString()).replace(" ","_"))) {
                                checkHomeWork = true;
                                break;
                            }
                        }
                        if (checkHomeWork) {
                            createDialogHomeWork(-1, String.valueOf(chkBox.getText().toString()));
                        }
                    }
                }
            });
        }
    }

    //выпадающий список для дз
    private void createDialogHomeWork(int sumStrHomeWork, String name) {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView2 = li.inflate(R.layout.drop_list_homework, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView2);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final TableLayout tableLayout2 = (TableLayout) promptsView2.findViewById(R.id.tableLayout2);
        final Button buttonSave = (Button) promptsView2.findViewById(R.id.buttonSave);
        final TextView textName = (TextView) promptsView2.findViewById(R.id.textName);


        //установка цвета
        buttonSave.setTextColor(buttonTextColor);
        buttonSave.setBackgroundColor(buttonBackgroundColor);

        textName.setText(name);

        kolSet = 0;

        TableRow tableRowDropList = new TableRow(this);
        tableRowDropList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        EditText editTextDropList = new EditText(this);
        editTextDropList.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        editTextDropList.setText("");
        editTextDropList.setHint("Новое домашнее задание");
        editTextDropList.setId(kolSet);
        kolSet++;
        tableRowDropList.addView(editTextDropList, 0);
        tableLayout2.addView(tableRowDropList);

        if (sumStrHomeWork!=-1) {
            String strPr = String.valueOf(homeWorkList.get(sumStrHomeWork));
            String[] strPrSplit = strPr.split(" ");


            for (int i = 1; i < strPrSplit.length-1; i+=2) {
                tableRowDropList = new TableRow(this);
                tableRowDropList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView textDropList = new TextView(this);
                textDropList.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                textDropList.setText(String.valueOf(strPrSplit[i]));
                textDropList.setId(kolSet);
                kolSet++;
                tableRowDropList.addView(textDropList, 0);
                tableLayout2.addView(tableRowDropList);

                tableRowDropList = new TableRow(this);
                tableRowDropList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                editTextDropList = new EditText(this);
                editTextDropList.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                editTextDropList.setText(String.valueOf(strPrSplit[i+1]).replace("_", " "));
                editTextDropList.setId(kolSet);
                kolSet++;
                tableRowDropList.addView(editTextDropList, 0);
                tableLayout2.addView(tableRowDropList);
            }
        }

        //Создаем AlertDialog:
        AlertDialog alertDialog = mDialogBuilder.create();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                List writeHomework = new ArrayList();
                writeHomework.add(name.replace(" ","_"));
                for (int i = 1; i < kolSet; i+=2) {
                    TextView setTime = tableLayout2.findViewById(i);
                    EditText setHomework = tableLayout2.findViewById(i+1);
                    if (!setHomework.getText().toString().isEmpty()) {
                        writeHomework.add(setTime.getText().toString());
                        writeHomework.add(setHomework.getText().toString().replace(" ","_").replace("\n","_"));
                    }
                }
                EditText setHomework = tableLayout2.findViewById(0);
                if (!setHomework.getText().toString().isEmpty()) {
                    writeHomework.add(1, dataYMD);
                    writeHomework.add(2, setHomework.getText().toString().replace(" ","_").replace("\n","_"));
                }

                if (writeHomework.size()==1) {
                    writeHomework = new ArrayList();
                }
                if (sumStrHomeWork==-1) {
                    homeWorkListFull.add(0, arrayToStr(writeHomework," "));
                } else {
                    homeWorkListFull.set(sumStrHomeWork, arrayToStr(writeHomework," "));
                }
                //сохранение в файл
                try {
                    // отрываем поток для записи
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                            openFileOutput("timeBase", MODE_PRIVATE)));
                    // пишем данные
                    String strWrite="";
                    for(int i = 0;i<nameListFull.size();i++) {
                        String[] addReadStr = String.valueOf(nameListFull.get(i)).split(" ");
                        for(int j = 0;j<addReadStr.length;j++) {
                            strWrite+=addReadStr[j]+" ";
                        }
                        strWrite+='\n';
                    }
                    strWrite+="@"+'\n';
                    for(int i = 0;i<timeListFull.size();i++) {
                        String[] addReadStr = String.valueOf(timeListFull.get(i)).split(" ");
                        for(int j = 0;j<addReadStr.length;j++) {
                            strWrite+=addReadStr[j]+" ";
                        }
                        strWrite+='\n';
                    }
                    strWrite+="@"+'\n';
                    for(int i = 0;i<homeWorkListFull.size();i++) {
                        strWrite+=homeWorkListFull.get(i);
                        if (i!=homeWorkListFull.size()-1) {
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
                setArrayList();
                alertDialog.cancel();
            }
        });

        //и отображаем его:
        alertDialog.show();
    }

    //зацикленная проверка
    private void update() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void run() {
                                time();
                                techYrok();
                                postProgress();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void techYrok() {
        //текущий
        nowYrok = -1;
        nextYrok = -1;
        nowYrok2 = new ArrayList();

        for (int i = 0; i < today.size(); i++) {
            if (nowTimeFormatSec / 60 >= (int) today.get(i).get(1 + dopInt) && nowTimeFormatSec / 60 < (int) today.get(i).get(2 + dopInt)) {
                nowYrok = i;
                nowYrok2.add(i);
            }
        }
        //следующий
        for (int i = 0; i < today.size(); i++) {
            if (nowTimeFormatSec / 60 < (int) today.get(i).get(1 + dopInt)) {
                if (nowYrok == -1) {
                    nextYrok = i;
                    break;
                } else {
                    if ((int) today.get(nowYrok).get(2 + dopInt) > (int) today.get(i).get(1 + dopInt)) {
                        nextYrok = i;
                        break;
                    }
                }
            }
        }
        if (nextYrok!=-1) {
            notifys((int) today.get(nextYrok).get(1 + dopInt)/60,(int) today.get(nextYrok).get(1 + dopInt)%60);
        }

        nowTimeFormatSec += pogr.getInt(pogr_PREFERENCES_COUNTER, 0);

        //задержка
        int minPogr = 24 * 60 + 1;
        for (int i = 0; i < today.size(); i++) {
            if (Math.abs((int) today.get(i).get(2) * 60 - nowTimeFormatSec) < minPogr) {
                minPogr = (int) today.get(i).get(2) * 60 - nowTimeFormatSec;
            }
            if (Math.abs((int) today.get(i).get(3) * 60 - nowTimeFormatSec) < minPogr) {
                minPogr = (int) today.get(i).get(3) * 60 - nowTimeFormatSec;
            }
        }
        if (minPogr != 24 * 60 + 1) {
            pogrGlobal = -minPogr;
        }

        nowTimeFormatSec -= pogr.getInt(pogr_PREFERENCES_COUNTER, 0);

        if (nowYrok + nowDataNed * 100 != oldTime.getInt(oldTime_PREFERENCES_COUNTER, 0)) {
            Log.i("nowYrok",String.valueOf(nowYrok));
            SharedPreferences.Editor editor = oldTime.edit();
            editor.putInt(oldTime_PREFERENCES_COUNTER, nowYrok + nowDataNed * 100);
            editor.apply();

            techYrok();
            setArrayList();
            set();
        }
    }

    private void postProgress() {
        double progress = 0.0;
        int s = 24 * 3600 + 1, m = 24 * 60 + 1, ch = 24 + 1;
        int sNext = 24 * 3600 + 1, mNext = 24 * 60 + 1, chNext = 24 + 1;
        String strProgress = " ";
        //нынешнее событие
        if (nowYrok != -1) {
            progress = (nowTimeFormatSec - (int) today.get(nowYrok).get(1 + dopInt) * 60) / (((int) today.get(nowYrok).get(2 + dopInt) - (int) today.get(nowYrok).get(1 + dopInt)) * 60.0) * 100.0;

            ch = ((int) today.get(nowYrok).get(2 + dopInt) * 60 - nowTimeFormatSec) / 3600;
            m = ((int) today.get(nowYrok).get(2 + dopInt) * 60 - nowTimeFormatSec) % 3600 / 60;
            s = ((int) today.get(nowYrok).get(2 + dopInt) * 60 - nowTimeFormatSec) % 60;
        }
        //следующее событие
        if (nextYrok != -1) {
            if (nextYrok != 0) {
                //следующее событие
                progress = (nowTimeFormatSec - (int) today.get(nextYrok - 1).get(2 + dopInt) * 60.0) / (((int) today.get(nextYrok).get(1 + dopInt) - (int) today.get(nextYrok - 1).get(2 + dopInt)) * 60.0) * 100.0;
            } else {
                //первое следующее
                progress = (nowTimeFormatSec) / ((int) today.get(nextYrok).get(1 + dopInt) * 60.0) * 100.0;
            }

            chNext = ((int) today.get(nextYrok).get(1 + dopInt) * 60 - nowTimeFormatSec) / 3600;
            mNext = ((int) today.get(nextYrok).get(1 + dopInt) * 60 - nowTimeFormatSec) % 3600 / 60;
            sNext = ((int) today.get(nextYrok).get(1 + dopInt) * 60 - nowTimeFormatSec) % 60;
        }

        if (ch <= chNext && m <= mNext && s <= sNext) {
            if (nowYrok != -1) {
                strProgress = String.valueOf(today.get(nowYrok).get(0)) + " закончится через: " +
                        (ch != 0 ? String.valueOf(ch) + "час. " : "") + (m != 0 ? String.valueOf(m) + "мин. " : "") + (s != 0 ? String.valueOf(s) + "сек. " : "");

            }

        } else {
            if (nextYrok != -1) {
                strProgress = String.valueOf(today.get(nextYrok).get(0)) + " начнётся через: " + (chNext != 0 ? String.valueOf(chNext) + "час. " : "") +
                        (mNext != 0 ? String.valueOf(mNext) + "мин. " : "") + (sNext != 0 ? String.valueOf(sNext) + "сек. " : "");

            }
        }

        notifys(mNext, sNext);

        tvProgressHorizontal.setTextColor(Color.rgb(255, 255, 255));
        pbHorizontal.setProgress((int) progress);

        if (progress == 0) {
            pbHorizontal.setSecondaryProgress(0);
        } else {
            pbHorizontal.setSecondaryProgress((int) (progress + 5));
        }
        tvProgressHorizontal.setText(strProgress);
    }

    private void notifys(int y, int y2) {
        //уведомление о событии
        if (nextYrok != -1) {
            if (Integer.valueOf((int) today.get(nextYrok).get(1)) == 1) {
                int m2, s2;
                String asd = "";
                m2 = notify.getInt(notify_PREFERENCES_COUNTER, 0) / 60;
                s2 = notify.getInt(notify_PREFERENCES_COUNTER, 0) % 60;
                int k = 0;
                if (s2 == 0) {
                    k = 1;
                }
                if (m2 == y && s2 + k == y2) {
                    if (m2 == 0 && s2 == 0 + k) {
                        asd = "Сейчас идёт: " + String.valueOf(today.get(nextYrok).get(0));
                    } else {
                        asd = String.valueOf(today.get(nextYrok).get(0)) + " начнется чеерез: " + String.valueOf(m2) + " мин. " + String.valueOf(s2) + " сек.";
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setAutoCancel(false)
                                    .setSmallIcon(R.drawable.info)
                                    .setContentIntent(pendingIntent)
                                    .setContentTitle("Напоминание")
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setContentText(asd)
                                    .setPriority(PRIORITY_HIGH);

                    createChannelIfNeeded(notificationManager);
                    notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
                }
            }
        }
        //уведомлении о включённом звуке
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int value = manager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (sound.getInt(sound_PREFERENCES_COUNTER, 0) / 10 != value) {
            notifyF = 0;
        }
        if (nowYrok != -1 && notifyF == 0 && value != 0) {
            if (sound.getInt(sound_PREFERENCES_COUNTER, 0) % 10 == 1) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setAutoCancel(false)
                                .setSmallIcon(R.drawable.sd)
                                .setContentIntent(pendingIntent)
                                .setContentTitle("Выключи звук!")
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setContentText("уровень громкости: " + String.valueOf(Integer.valueOf((int) (value / 15.0 * 100))) + "%")
                                .setPriority(PRIORITY_HIGH);

                notificationBuilder.setOngoing(true);
                notificationBuilder.setAutoCancel(false);
                createChannelIfNeeded(notificationManager);
                notificationManager.notify(NOTIFY_ID + 1, notificationBuilder.build());

                notifyF = 1;
            }
        }
        if (value == 0 || sound.getInt(sound_PREFERENCES_COUNTER, 0) % 10 == 0 || nowYrok == -1) {
            notificationManager.cancel(NOTIFY_ID + 1);
        }
        SharedPreferences.Editor editor = sound.edit();
        editor.putInt(sound_PREFERENCES_COUNTER, sound.getInt(sound_PREFERENCES_COUNTER, 0) % 10 + value * 10);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setArrayList() {
        today = new ArrayList<List>();
        homeWorkListFull = new ArrayList();
        nameListFull = new ArrayList();
        timeListFull = new ArrayList();
        homeWorkList = new ArrayList();
        nameList = new ArrayList();
        timeList = new ArrayList();
        //создание расписания на день из событий
        try {
            // открываем поток для чтения
            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                    openFileInput("time")));
            String str = "";
            // читаем содержимое
            int kolStr = 0;
            while ((str = timeRead.readLine()) != null) {
                kolStr++;
                String[] strSplit = str.split(" ");
                if (strSplit.length > 3) {
                    //для одноразовых
                    //название-доп-год-месяц-день-(начало-конец)
                    if (Integer.valueOf(strSplit[0]) == -1) {
                        for (int j = 2 + dopInt; j < strSplit.length; j += 5) {
                            List list1 = new ArrayList();
                            Calendar myCalendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
                            int year = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));
                            simpleDateFormat = new SimpleDateFormat("M");
                            int month = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));
                            simpleDateFormat = new SimpleDateFormat("d");
                            int day = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));

                            if (Integer.valueOf(strSplit[j]) == year) {
                                if (Integer.valueOf(strSplit[j + 1]) == month - 1) {
                                    if (Integer.valueOf(strSplit[j + 2]) == day) {
                                        list1.add(String.valueOf(strSplit[1].replace("_", " ")));
                                        list1.add(Integer.valueOf(strSplit[2]));
                                        list1.add(Integer.valueOf(strSplit[j + 3]));
                                        list1.add(Integer.valueOf(strSplit[j + 4]));
                                        if (list1.size() != 0) {
                                            today.add(list1);
                                        }
                                    }
                                }
                            }
                            if (Integer.valueOf(strSplit[j]) < year) {
                                if (Integer.valueOf(strSplit[j + 1]) < month - 1) {
                                    if (Integer.valueOf(strSplit[j + 2]) < day) {

                                    }
                                }
                            }
                        }
                    }

                    //для ежедневных
                    //название-доп-(начало-конец)
                    if (Integer.valueOf(strSplit[0]) == 0) {
                        for (int j = 2 + dopInt; j < strSplit.length; j += 2) {
                            List list1 = new ArrayList();
                            list1.add(String.valueOf(strSplit[1].replace("_", " ")));
                            list1.add(Integer.valueOf(strSplit[2]));
                            list1.add(Integer.valueOf(strSplit[j]));
                            list1.add(Integer.valueOf(strSplit[j + 1]));
                            if (list1.size() != 0) {
                                today.add(list1);
                            }
                        }
                    }

                    //для еженедельных
                    //название-доп-день недели-(начало-конец)
                    if (Integer.valueOf(strSplit[0]) == 7) {
                        for (int j = 2 + dopInt; j < strSplit.length; j += 3) {
                            List list1 = new ArrayList();
                            if (Integer.valueOf(strSplit[j]) == nowDataNed) {
                                list1.add(String.valueOf(strSplit[1].replace("_", " ")));
                                list1.add(Integer.valueOf(strSplit[2]));
                                list1.add(Integer.valueOf(strSplit[j + 1]));
                                list1.add(Integer.valueOf(strSplit[j + 2]));
                                if (list1.size() != 0) {
                                    today.add(list1);
                                }
                            }
                        }
                    }

                    //для ежемесячных
                    //название-доп-день-(начало-конец)
                    if (Integer.valueOf(strSplit[0]) == 31) {
                        for (int j = 2 + dopInt; j < strSplit.length; j += 3) {
                            List list1 = new ArrayList();
                            Calendar myCalendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d");
                            int day = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));

                            if (Integer.valueOf(strSplit[j]) == day) {
                                list1.add(String.valueOf(strSplit[1].replace("_", " ")));
                                list1.add(Integer.valueOf(strSplit[2]));
                                list1.add(Integer.valueOf(strSplit[j + 1]));
                                list1.add(Integer.valueOf(strSplit[j + 2]));
                                if (list1.size() != 0) {
                                    today.add(list1);
                                }
                            }
                        }
                    }

                    //для ежегодных
                    //название-доп-месяц-день-(начало-конец)
                    if (Integer.valueOf(strSplit[0]) == 365) {
                        for (int j = 2 + dopInt; j < strSplit.length; j += 4) {
                            List list1 = new ArrayList();
                            Calendar myCalendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M");
                            int month = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));
                            simpleDateFormat = new SimpleDateFormat("d");
                            int day = Integer.valueOf(simpleDateFormat.format(myCalendar.getTime()));

                            if (Integer.valueOf(strSplit[j]) == month - 1) {
                                if (Integer.valueOf(strSplit[j + 1]) == day) {
                                    list1.add(String.valueOf(strSplit[1].replace("_", " ")));
                                    list1.add(Integer.valueOf(strSplit[2]));
                                    list1.add(Integer.valueOf(strSplit[j + 2]));
                                    list1.add(Integer.valueOf(strSplit[j + 3]));
                                    if (list1.size() != 0) {
                                        today.add(list1);
                                    }
                                }
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
                        if (kolStr==nowDataNed) {
                            for (int i = 0;i<strSplit.length;i++) {
                                nameList.add(strSplit[i]);
                            }
                        }
                        nameListFull.add(str);
                    }
                    if (k==1) {
                        if (kolStr==nowDataNed) {
                            for (int i = 0;i<strSplit.length;i++) {
                                timeList.add(strSplit[i]);
                            }
                        }
                        timeListFull.add(str);
                    }
                    if (k==2) {
                        homeWorkList.add(str);
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

        //формировка
        for (int i = 0; i < nameList.size(); i++) {
            if (!(nameList.get(i).equals("")) && !(nameList.get(i).equals(" "))) {
                if (timeList.size()!=0 && timeList.size()/2>=i) {
                    if (!(timeList.get(i*2).equals("0")) || !(timeList.get(i*2).equals("0"))) {
                        List addList = new ArrayList();
                        addList.add(String.valueOf(nameList.get(i)).replace("_", " "));
                        addList.add(-1);
                        addList.add(Integer.valueOf(min(Integer.valueOf(String.valueOf(timeList.get(i*2))),Integer.valueOf(String.valueOf(timeList.get(i*2+1))))));
                        addList.add(Integer.valueOf(max(Integer.valueOf(String.valueOf(timeList.get(i*2))),Integer.valueOf(String.valueOf(timeList.get(i*2+1))))));
                        today.add(addList);
                    }
                }
            }
        }
        //сортировка
        todaySort = new ArrayList<List>();
        if (today.size() != 0) {
            todaySort.add(today.get(0));
            for (int i1 = dopInt; i1 < today.size(); i1++) {
                int f = 0;
                for (int i2 = 0; i2 < todaySort.size(); i2++) {
                    if ((int) today.get(i1).get(1 + dopInt) < (int) todaySort.get(i2).get(1 + dopInt)) {
                        todaySort.add(i2, today.get(i1));
                        f = 1;
                        break;
                    } else {
                        if ((int) today.get(i1).get(1 + dopInt) == (int) todaySort.get(i2).get(1 + dopInt)) {
                            if ((int) today.get(i1).get(2 + dopInt) < (int) todaySort.get(i2).get(2 + dopInt)) {
                                todaySort.add(i2, today.get(i1));
                                f = 1;
                                break;
                            }
                        }
                    }
                }
                if (f == 0) {
                    todaySort.add(today.get(i1));
                }
            }
            today = todaySort;
        }
    }

    public static String arrayToStr(List list, String space) {
        String str="";
        for (int i = 0;i<list.size();i++) {
            str+=list.get(i) + space;
        }
        return str;
    }
}

/*
android:background="@android:color/transparent"
app:srcCompat="@drawable/wifi"

    //заполнение окна
    private void set2() {
        SharedPreferences.Editor editor = zapros.edit();
        editor.putInt(zapros_PREFERENCES_COUNTER, 0);
        editor.apply();

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for (int i = 0; i < names[nowDataNed].length; i++) {

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView text = new TextView(this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size.getInt(size_PREFERENCES_COUNTER, 40));
            text.setText(predmet[names[nowDataNed][i][0]]);
            //TextViewCompat.setAutoSizeTextTypeWithDefaults(text, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

            //подсветка текущего
            if (nowYrok==i) {
                text.setTextColor(Color.rgb(0, 200, 0));
            }
            if (nextYrok==i) {
                text.setTextColor(Color.rgb(255, 165, 0));
            }
            tableRow.addView(text, 0);

            if (time.getInt(time_PREFERENCES_COUNTER, 0)==1){
                TextView text2 = new TextView(this);
                text2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
                text2.setText(String.valueOf((names[nowDataNed][i][1]/60+"."+names[nowDataNed][i][1]%60)+"-"+names[nowDataNed][i][2]/60+"."+names[nowDataNed][i][2]%60));
                tableRow.addView(text2, 1);
            }
            tableLayout.addView(tableRow);
        }

    }

 */
/*
Поиск элементов строкового массива по начальным символам
Допустим, у нас есть строковый массив и нам нужно по первым символам найти все слова, которые входят в данный массив.


// Сам метод
public static ArrayList<String> searchFromStart(String[] inputArray, String searchText) {
    ArrayList<String> outputArray = new ArrayList<>();

    for (int i = 0; i < inputArray.length; i++) {
        if (searchText.compareToIgnoreCase(inputArray[i].substring(0,
                searchText.length())) == 0) {
            outputArray.add(inputArray[i]);
        }
    }
    return outputArray;
}

// Массив строк
final String[] catNamesArray = new String[] { "Рыжик", "Барсик", "Мурзик",
        "Мурка", "Васька", "Томасина", "Бобик", "Кристина", "Пушок",
        "Дымка", "Кузя", "Китти", "Барбос", "Масяня", "Симба" };

// Применим метод. Ищем по буквам "мур":

List<String> words = searchFromStart(catNamesArray, "мур");
Toast.makeText(getApplicationContext(), words.get(0).toString() + ":" + words.get(1).toString(), Toast.LENGTH_SHORT).show();
 */
/*
    //первый аргумент-день недели   второй аргумент-номер события   третий аргумент-название,время начала и конца
    final Integer[][][] names = new Integer[][][]{{{0,8*60+50,9*60+30},{1,9*60+50,10*60+30},{3,10*60+50,11*60+30},{2,11*60+50,12*60+30},{4,12*60+50,13*60+30},{5,13*60+50,14*60+30}},
                                                  {{6,8*60+50,9*60+30},{1,9*60+50,10*60+30},{7,10*60+50,11*60+30},{8,11*60+50,12*60+30},{1,12*60+50,13*60+30},{9,13*60+50,14*60+30},{9,14*60+40,15*60+20}},
                                                  {{3,8*60+50,9*60+30},{2,9*60+50,10*60+30},{10,10*60+50,11*60+30},{5,11*60+50,12*60+30},{0,12*60+50,13*60+30},{7,13*60+50,14*60+30},{11,14*60+40,15*60+20}},
                                                  {{1,8*60+50,9*60+30},{12,9*60+50,10*60+30},{5,10*60+50,11*60+30},{9,11*60+50,12*60+30},{9,12*60+50,13*60+30},{8,13*60+50,14*60+30},{6,14*60+40,15*60+20}},
                                                  {{7,8*60+50,9*60+30},{10,9*60+50,10*60+30},{8,10*60+50,11*60+30},{6,11*60+50,12*60+30},{6,12*60+50,13*60+30},{0,13*60+50,14*60+30},{1,14*60+40,15*60+20}},
                                                  {{12,9*60+0,9*60+40},{7,9*60+50,10*60+30},{13,10*60+45,11*60+25}},
                                                  {}};
    //первое значение промежуток, второе значение имя, (третье значение день недели, четвёртое значение время начала, пятое значение время конца) и повторение
    final Integer[][] names2 = new Integer[][]{{7,0,0,8*60+50,9*60+30,2,12*60+50,13*60+30,4,13*60+50,14*60+30}, {7,1,0,9*60+50,10*60+30,1,9*60+50,10*60+30,3,8*60+50,9*60+3,4,14*60+40,15*60+20},
            {7,2,0,11*60+50,12*60+30,2,9*60+50,10*60+30}, {7,3,0,10*60+50,11*60+30,2,8*60+50,9*60+30}, {7,4,0,12*60+50,13*60+30}, {7,5,0,13*60+50,14*60+30,2,11*60+50,12*60+30,3,10*60+50,11*60+30},
            {7,6,1,8*60+50,9*60+30,3,14*60+40,15*60+20,4,11*60+50,12*60+30,4,12*60+50,13*60+30}, {7,7,1,10*60+50,11*60+30,2,13*60+50,14*60+30,4,8*60+50,9*60+30,5,9*60+50,10*60+30},
            {7,8,1,11*60+50,12*60+30,3,13*60+50,14*60+30,4,10*60+50,11*60+30}, {7,9,1,13*60+50,14*60+3,1,14*60+40,15*60+20,3,11*60+50,12*60+30,3,12*60+50,13*60+30},
            {7,10,2,10*60+50,11*60+30,4,9*60+50,10*60+30}, {7,11,2,14*60+40,15*60+20}, {7,12,3,9*60+50,10*60+30,5,9*60+0,9*60+40},{7,13,5,10*60+45,11*60+25}};
    final String predmet[] = new String[]{"История", "Физика", "Русский язык",
            "Обществознание", "ОБЖ", "Физ-ра" ,"Литература", "Информатика", "Английский язык", "Алгебра", "Геометрия", "3д моделирование", "Химия", "Астрономия"};
 */
/*
//создание расписания на день
        for (int i=0;i<names2.length;i++) {
            //для еженедельных
            if (Integer.valueOf(strSplit[0])==7) {
                    for (int j = 2+dopInt; j<strSplit.length; j+=3) {
                        List list1 = new ArrayList();
                        if (Integer.valueOf(strSplit[j])==nowDataNed) {
                            list1.add(String.valueOf(strSplit[1]));
                            list1.add(Integer.valueOf(strSplit[2]));
                            list1.add(Integer.valueOf(strSplit[j+1]));
                            list1.add(Integer.valueOf(strSplit[j+2]));
                            today.add(list1);
                        }
                    }

                }
            //для ежегодных
            //для ежемесячных
        }
 */
/*
            ch=(nowTimeFormatSec/3600-(int)today.get(nowYrok).get(1+dopInt)/60-(((int)today.get(nowYrok).get(2+dopInt)/60-(int)today.get(nowYrok).get(1+dopInt)/60)))+1;
            ch*=-1;
            m=(nowTimeFormatSec/60-(int)today.get(nowYrok).get(1+dopInt)-(((int)today.get(nowYrok).get(2+dopInt)-(int)today.get(nowYrok).get(1+dopInt))))+1+ch*60;
            m*=-1;
            s=(int) ((nowTimeFormatSec-(int)today.get(nowYrok).get(1+dopInt)*60-(((int)today.get(nowYrok).get(2+dopInt)-(int)today.get(nowYrok).get(1+dopInt))*60.0))+m*60+ch*3600)+1;
            s*=-1;
             */
/*
//задержка
        if (f==0){
            nowYrok=-1;
        } else {
            if (nowTimeFormatSec/60-(int)today.get(nowYrok).get(1+dopInt)>((int)today.get(nowYrok).get(2+dopInt)-(int)today.get(nowYrok).get(1))/2) {
                pogrGlobal=(int)today.get(nowYrok).get(2+dopInt)*60-nowTimeFormatSec;
            } else {
                pogrGlobal=nowTimeFormatSec-(int)today.get(nowYrok).get(1+dopInt)*60;
            }
        }
        if (f2==0){
            nextYrok=-1;
        } else {
            if (nextYrok!=0) {
                if (nowTimeFormatSec / 60 - (int) today.get(nextYrok - 1).get(2 + dopInt) > ((int) today.get(nextYrok).get(1 + dopInt) - (int) today.get(nextYrok - 1).get(2 + dopInt)) / 2) {
                    pogrGlobal = (int) today.get(nextYrok).get(1 + dopInt) * 60 - nowTimeFormatSec;
                } else {
                    pogrGlobal = nowTimeFormatSec - (int) today.get(nextYrok - 1).get(2 + dopInt) * 60;
                }
            } else {
                pogrGlobal = (int) today.get(nextYrok).get(1 + dopInt) * 60-nowTimeFormatSec;
            }
        }
 */
/*
public class NewThread extends AsyncTask<String, Void, String> {

        // Метод выполняющий запрос в фоне, в версиях выше 4 андроида, запросы в главном потоке выполнять
        // нельзя, поэтому все что вам нужно выполнять - выносите в отдельный тред
        @Override
        protected String doInBackground(String... arg) {

            // класс который захватывает страницу
            Document doc;
            try {
                // определяем откуда будем воровать данные
                doc = (Document) Jsoup.connect("http://freehabr.ru/").get();
                // задаем с какого места, я выбрал заголовке статей
                title = doc.select(".title");
                // чистим наш аррей лист для того что бы заполнить
                titleList.clear();
                // и в цикле захватываем все данные какие есть на странице
                for (Element titles : title) {
                    // записываем в аррей лист
                    titleList.add(titles.);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // ничего не возвращаем потому что я так захотел)
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            // после запроса обновляем листвью
            lv.setAdapter(adapter);
        }
}
 */