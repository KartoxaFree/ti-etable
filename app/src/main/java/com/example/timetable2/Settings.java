package com.example.timetable2;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.timetable2.MainActivity.buttonBackgroundColor;
import static com.example.timetable2.MainActivity.buttonTextColor;
import static com.example.timetable2.MainActivity.pogrGlobal;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

import static java.lang.Math.abs;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class Settings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    public int k;
    public int r1,g1,b1, colorText;
    public String strOut="";

    final Context context = this;

    private SeekBar seekBar;
    private ConstraintLayout mScreenLayout;
    TextView mTextView;

    public static final String size_PREFERENCES = "size";
    public static final String size_PREFERENCES_COUNTER = "size2";
    private SharedPreferences size;

    public static final String time_PREFERENCES = "time";
    public static final String time_PREFERENCES_COUNTER = "time2";
    private SharedPreferences time;

    public static final String pogr_PREFERENCES = "pogr";
    public static final String pogr_PREFERENCES_COUNTER = "pogr2";
    private SharedPreferences pogr;

    public static final String color_PREFERENCES = "color";
    public static final String color_PREFERENCES_COUNTER = "color2";
    private SharedPreferences color;

    public static final String notify_PREFERENCES = "notify";
    public static final String notify_PREFERENCES_COUNTER = "notify2";
    private SharedPreferences notify;

    public static final String sound_PREFERENCES = "sound";
    public static final String sound_PREFERENCES_COUNTER = "sound2";
    private SharedPreferences sound;

    public static final String uploadBase_PREFERENCES = "upload";
    public static final String uploadBase_PREFERENCES_COUNTER = "upload2";
    private SharedPreferences uploadBase;

    public static final String downloadBase_PREFERENCES = "download";
    public static final String downloadBase_PREFERENCES_COUNTER = "download2";
    private SharedPreferences downloadBase;

    public static final String simpleMode_PREFERENCES = "simple";
    public static final String simpleMode_PREFERENCES_COUNTER = "simple2";
    private SharedPreferences simpleMode;

    public static final String authorization_PREFERENCES = "authorizationPar";
    public static final String authorization_PREFERENCES_COUNTER = "authorizationPar2";
    private SharedPreferences authorization;

    public static final String autoSyncing_PREFERENCES = "AutoSyncingPar";
    public static final String autoSyncing_PREFERENCES_COUNTER = "AutoSyncingPar2";
    private SharedPreferences autoSyncing;

    //онлайн firebase
    private DatabaseReference ref;
    private FirebaseDatabase database;

    public boolean autoF = false;

    Button save, pogrButton, back;
    Button exportButton, importButton;
    Switch timeSw, soundSw, simpleSw, autoSyncingSw;
    EditText pogrEditTextMin,pogrEditTextSec;
    EditText red1, green1, blue1;
    EditText notifyMinEditText, notifySecEditText;
    ImageButton UIDDownloadButton, UIDUploadButton;
    EditText syncingEditText, uploadEditText, autoSyncingEditText;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
        /*View aView = getWindow().getDecorView();
        aView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

        final SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

        size = getSharedPreferences(size_PREFERENCES, Context.MODE_PRIVATE);
        time = getSharedPreferences(time_PREFERENCES, Context.MODE_PRIVATE);
        pogr = getSharedPreferences(pogr_PREFERENCES, Context.MODE_PRIVATE);
        color = getSharedPreferences(color_PREFERENCES, Context.MODE_PRIVATE);
        notify = getSharedPreferences(notify_PREFERENCES, Context.MODE_PRIVATE);
        sound = getSharedPreferences(sound_PREFERENCES, Context.MODE_PRIVATE);
        uploadBase = getSharedPreferences(uploadBase_PREFERENCES, Context.MODE_PRIVATE);
        downloadBase = getSharedPreferences(downloadBase_PREFERENCES, Context.MODE_PRIVATE);
        simpleMode = getSharedPreferences(simpleMode_PREFERENCES, Context.MODE_PRIVATE);
        authorization = getSharedPreferences(authorization_PREFERENCES, Context.MODE_PRIVATE);
        autoSyncing = getSharedPreferences(autoSyncing_PREFERENCES, Context.MODE_PRIVATE);

        mTextView = (TextView) findViewById(R.id.TextView);
        save = (Button) findViewById(R.id.save);
        timeSw = (Switch) findViewById(R.id.timeSw);
        soundSw = (Switch) findViewById(R.id.soundSw);
        pogrEditTextSec = (EditText) findViewById(R.id.pogrEditTextSec);
        pogrEditTextMin = (EditText) findViewById(R.id.pogrEditTextMin);
        red1 = (EditText) findViewById(R.id.red1);
        green1 = (EditText) findViewById(R.id.green1);
        blue1 = (EditText) findViewById(R.id.blue1);
        pogrButton = (Button) findViewById(R.id.pogrButton);
        back = (Button) findViewById(R.id.back);
        importButton = (Button) findViewById(R.id.importButton);
        exportButton = (Button) findViewById(R.id.exportButton);
        UIDDownloadButton = (ImageButton) findViewById(R.id.UIDDownloadButton);
        UIDUploadButton = (ImageButton) findViewById(R.id.UIDUploadButton);
        syncingEditText = (EditText) findViewById(R.id.syncingEditText);
        uploadEditText = (EditText) findViewById(R.id.uploadEditText);
        autoSyncingEditText = (EditText) findViewById(R.id.autoSyncingEditText);
        autoSyncingSw = (Switch) findViewById(R.id.autoSyncingSw);

        notifySecEditText = (EditText) findViewById(R.id.notifySecEditText);
        notifyMinEditText = (EditText) findViewById(R.id.notifyMinEditText);
        simpleSw = (Switch) findViewById(R.id.simpleSw);

        ScrollView scrollView2 = findViewById(R.id.scrollView2);

        //установка цвета
        save.setTextColor(buttonTextColor);
        save.setBackgroundColor(buttonBackgroundColor);
        pogrButton.setTextColor(buttonTextColor);
        pogrButton.setBackgroundColor(buttonBackgroundColor);
        back.setTextColor(buttonTextColor);
        back.setBackgroundColor(buttonBackgroundColor);
        exportButton.setTextColor(buttonTextColor);
        exportButton.setBackgroundColor(buttonBackgroundColor);
        importButton.setTextColor(buttonTextColor);
        importButton.setBackgroundColor(buttonBackgroundColor);

        // Узнаем размеры экрана из ресурсов
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        // установить размеры для listView
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        ViewGroup.LayoutParams par = scrollView2.getLayoutParams();
        par.height = displaymetrics.heightPixels-140;
        scrollView2.setLayoutParams(par);

        //размер
        k=size.getInt(size_PREFERENCES_COUNTER, 40);
        mTextView.setText("Размер интерфейса в главном окне: " + String.valueOf(k));
        seekBar.setProgress(size.getInt(size_PREFERENCES_COUNTER, k));

        //время начала и конца
        if (time.getInt(time_PREFERENCES_COUNTER, 0)== 0) {
            timeSw.setChecked(false);
        }else{
            timeSw.setChecked(true);
        }
        if (sound.getInt(sound_PREFERENCES_COUNTER, 0)%10==0) {
            soundSw.setChecked(false);
        }else{
            soundSw.setChecked(true);
        }

        //задержка
        pogrEditTextMin.setHint("мин: "+String.valueOf(pogr.getInt(pogr_PREFERENCES_COUNTER, 0)/60));
        pogrEditTextSec.setHint("сек: "+String.valueOf(pogr.getInt(pogr_PREFERENCES_COUNTER, 0)%60));
        //цвет
        red1.setHint("Red: "+String.valueOf(color.getInt(color_PREFERENCES_COUNTER, 0)/1000000));
        green1.setHint("Green: "+String.valueOf(color.getInt(color_PREFERENCES_COUNTER, 0)/1000%1000));
        blue1.setHint("Blue: "+String.valueOf(color.getInt(color_PREFERENCES_COUNTER, 0)%1000));
        //задержка
        notifyMinEditText.setHint("мин: "+String.valueOf(notify.getInt(notify_PREFERENCES_COUNTER, 0)/60));
        notifySecEditText.setHint("сек: "+String.valueOf(notify.getInt(notify_PREFERENCES_COUNTER, 0)%60));
        //простой режим
        if (simpleMode.getInt(simpleMode_PREFERENCES_COUNTER, 0)==0) {
            simpleSw.setChecked(false);
        }else{
            simpleSw.setChecked(true);
        }
        //онлайн база данных
        uploadEditText.setText(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, ""));
        syncingEditText.setText(downloadBase.getString(downloadBase_PREFERENCES_COUNTER, ""));

        //авторизация
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (authorization.getString(authorization_PREFERENCES_COUNTER, "").equals("")) {
                    for(int i = 0;i<1000;i++) {
                        String authStr = randomString(17);
                        try {
                            snapshot.child("Users").child(authStr).getValue().toString();
                            autoF=false;
                        } catch (NullPointerException e) {
                            SharedPreferences.Editor editor = authorization.edit();
                            editor.putString(authorization_PREFERENCES_COUNTER, authStr);
                            editor.apply();
                            ref.child("Users").child(authStr).setValue(authStr);
                            autoF=true;
                            break;
                        }
                    }
                } else {
                    autoF=true;
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        //автосинхронизация
        if (autoSyncing.getInt(autoSyncing_PREFERENCES_COUNTER, 0)<=0) {
            autoSyncingSw.setChecked(false);
        }else{
            autoSyncingSw.setChecked(true);
        }
        if (autoSyncing.getInt(autoSyncing_PREFERENCES_COUNTER, 0)!=0) {
            autoSyncingEditText.setHint(String.valueOf(abs(autoSyncing.getInt(autoSyncing_PREFERENCES_COUNTER, 0)))+" ч");
        }

        newActivity();

    }

    private void newActivity(){
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                //сохранение данных

                //размер
                SharedPreferences.Editor editor = size.edit();
                editor.putInt(size_PREFERENCES_COUNTER, k);
                editor.apply();

                //время начала и конца
                editor = time.edit();
                if (timeSw.isChecked()==false) {
                    editor.putInt(time_PREFERENCES_COUNTER, 0);
                }else{
                    editor.putInt(time_PREFERENCES_COUNTER, 1);
                }
                editor.apply();

                //уведомлять о включённом звуке во время события
                editor = autoSyncing.edit();
                if (soundSw.isChecked()==false) {
                    editor.putInt(autoSyncing_PREFERENCES_COUNTER, 0);
                }else{
                    editor.putInt(autoSyncing_PREFERENCES_COUNTER, 1);
                }
                editor.apply();

                //задержка
                zader();
                //цвет
                ColorVoid();
                //уведомление
                notifySet();

                //простой режим
                editor = simpleMode.edit();
                if (simpleSw.isChecked()==false) {
                    editor.putInt(simpleMode_PREFERENCES_COUNTER, 0);
                }else{
                    editor.putInt(simpleMode_PREFERENCES_COUNTER, 1);
                }
                editor.apply();

                //автосинхронизация
                if (!autoSyncingEditText.getText().toString().isEmpty()) {
                    editor = autoSyncing.edit();
                    if (autoSyncingSw.isChecked()==false) {
                        editor.putInt(autoSyncing_PREFERENCES_COUNTER, -abs(Integer.valueOf(autoSyncingEditText.getText().toString())));
                    }else{
                        editor.putInt(autoSyncing_PREFERENCES_COUNTER, abs(Integer.valueOf(autoSyncingEditText.getText().toString())));
                    }
                    editor.apply();
                } else {
                    editor = autoSyncing.edit();
                    if (autoSyncingSw.isChecked()==false) {
                        editor.putInt(autoSyncing_PREFERENCES_COUNTER, 0);
                    }else{
                        editor.putInt(autoSyncing_PREFERENCES_COUNTER, 1);
                    }
                    editor.apply();
                }
                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.this,    MainActivity.class);
                startActivity(intent);
            }
        });
        pogrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,    MainActivity.class);
                startActivity(intent);
            }
        });
        importButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                createDialogImport();
            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                createDialogExport();
            }
        });

        UIDDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference();

                if (syncingEditText.getText().toString().isEmpty()) {
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
                    tv.setText("Удалить привязку?");
                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder.setCancelable(false).setPositiveButton("да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    //действие после нажатия:
                                    SharedPreferences.Editor editor = downloadBase.edit();
                                    editor.putString(downloadBase_PREFERENCES_COUNTER, "");
                                    editor.apply();
                                    syncingEditText.setText("");
                                    Toast.makeText(getApplicationContext(), "Удалено", Toast.LENGTH_SHORT).show();
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
                } else {
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            try {
                                snapshot.child("lessons").child(syncingEditText.getText().toString()).getValue().toString();
                                try {
                                    // отрываем поток для записи
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                            openFileOutput("timeBase", MODE_PRIVATE)));
                                    // пишем данные
                                    bw.write(snapshot.child("lessons").child(syncingEditText.getText().toString()).getValue().toString());
                                    bw.newLine();

                                    // закрываем поток
                                    bw.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                SharedPreferences.Editor editor = downloadBase.edit();
                                editor.putString(downloadBase_PREFERENCES_COUNTER, syncingEditText.getText().toString());
                                editor.apply();
                                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                Toast.makeText(getApplicationContext(), "Введите другое имя", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        UIDUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                ref = database.getReference();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String sendID = randomString(10);
                        String readStr = "";
                        try {
                            // открываем поток для чтения
                            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                                    openFileInput("timeBase")));
                            String str = "";
                            int kStr = 0, k = 0;
                            // читаем содержимое
                            while ((str = timeRead.readLine()) != null) {
                                if (!(str.equals(""))) {
                                    readStr += str + "\n";
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (uploadEditText.length()==0) {
                            try {
                                snapshot.child("lessons").child(sendID).getValue().toString();
                                Toast.makeText(getApplicationContext(), "Произошла ошибка, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                            } catch (NullPointerException e) {
                                if (!(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "").equals(""))) {
                                    ref.child("lessons").child(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "")).removeValue();
                                    ref.child("privateLessons").child(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "")).removeValue();
                                }
                                ref.child("lessons").child(sendID).setValue(readStr);
                                ref.child("privateLessons").child(sendID).setValue(authorization.getString(authorization_PREFERENCES_COUNTER, ""));
                                SharedPreferences.Editor editor = uploadBase.edit();
                                editor.putString(uploadBase_PREFERENCES_COUNTER, sendID);
                                editor.apply();
                                uploadEditText.setText(sendID);

                                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            try {
                                snapshot.child("lessons").child(uploadEditText.getText().toString()).getValue().toString();
                                if (snapshot.child("privateLessons").child(uploadEditText.getText().toString()).getValue().toString()
                                        .equals(authorization.getString(authorization_PREFERENCES_COUNTER, ""))) {
                                    ref.child("lessons").child(uploadEditText.getText().toString()).setValue(readStr);
                                    Toast.makeText(getApplicationContext(), "Обновлено", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "У вас нет прав на это действие", Toast.LENGTH_SHORT).show();
                                    Log.i("Myapp",String.valueOf("У вас нет прав на это действие"));
                                }
                            } catch (NullPointerException e) {
                                Log.i("uploadBase",String.valueOf(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "")));
                                if (!(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "").equals(""))) {
                                    ref.child("lessons").child(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "")).removeValue();
                                    ref.child("privateLessons").child(uploadBase.getString(uploadBase_PREFERENCES_COUNTER, "")).removeValue();
                                }
                                ref.child("lessons").child(uploadEditText.getText().toString()).setValue(readStr);
                                ref.child("privateLessons").child(uploadEditText.getText().toString()).setValue(authorization.getString(authorization_PREFERENCES_COUNTER, ""));

                                SharedPreferences.Editor editor = uploadBase.edit();
                                editor.putString(uploadBase_PREFERENCES_COUNTER, uploadEditText.getText().toString());
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Сохранено", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public String randomString(int top){
        Random ran = new Random();
        char data = ' ';
        String dat = "";

        for (int i=0; i<=top; i++) {
            data = (char)(ran.nextInt(25)+97);
            dat = data + dat;
        }
        return dat;
    }

    private void createDialogImport() {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.imports, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText tv = (EditText) promptsView.findViewById(R.id.tv);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder.setCancelable(false).setPositiveButton("Сохранить",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //действие после нажатия:
                        boolean f=true;
                        strOut=tv.getText().toString();
                        if (strOut.length()!=0) {
                            String[] strSplit = strOut.split("\n");
                            for (int i = 0; i < strSplit.length; i++) {
                                String[] strDoubleSplit = strSplit[i].split(" ");
                                for (int j = 0; j < strDoubleSplit.length; j++) {
                                    if (j != 1) {
                                        if (strDoubleSplit[j].matches("(?i).*[a-zа-я].*") || strDoubleSplit[j].matches("[a-zA-Zа-яА-Я]+")) {
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    "Ошибка в " + String.valueOf(i + 1) + " строке " + String.valueOf(j + 1) + " значении ", Toast.LENGTH_SHORT);
                                            toast.show();
                                            f = false;
                                        }
                                        //единожды
                                        if (j == 0 && Integer.valueOf(strDoubleSplit[j]) == -1) {
                                            if ((strDoubleSplit.length - 3) % 5 != 0) {
                                                error(i);
                                                f = false;
                                            }
                                        }
                                        //ежедневно
                                        if (j == 0 && Integer.valueOf(strDoubleSplit[j]) == 0) {
                                            if ((strDoubleSplit.length - 3) % 2 != 0) {
                                                error(i);
                                                f = false;
                                            }
                                        }
                                        //еженедельно
                                        if (j == 0 && Integer.valueOf(strDoubleSplit[j]) == 7) {
                                            if ((strDoubleSplit.length - 3) % 3 != 0) {
                                                error(i);
                                                f = false;
                                            }
                                        }
                                        //ежемесячно
                                        if (j == 0 && Integer.valueOf(strDoubleSplit[j]) == 31) {
                                            if ((strDoubleSplit.length - 3) % 3 != 0) {
                                                error(i);
                                                f = false;
                                            }
                                        }
                                        //ежегодно
                                        if (j == 0 && Integer.valueOf(strDoubleSplit[j]) == 365) {
                                            if ((strDoubleSplit.length - 3) % 4 != 0) {
                                                error(i);
                                                f = false;
                                            }
                                        }
                                        if (strDoubleSplit.length <= 3) {
                                            error(i);
                                            f = false;
                                        }
                                    }
                                }
                            }
                        if (strSplit.length>3) {
                            if (f) {
                                try {
                                    // отрываем поток для записи
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                                            openFileOutput("time", MODE_APPEND)));
                                    // пишем данные
                                    for (int i=0;i<strSplit.length;i++) {
                                        bw.write(strSplit[i]);
                                        bw.newLine();
                                    }

                                    // закрываем поток
                                    bw.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Успешно добавлено", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Введите хоть что-нибудь", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }

                    private void error(int numStr) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Неправильный формат в "+String.valueOf(numStr+1)+" строке ", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton("Отмена",
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

    private void createDialogExport() {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.export, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final TextView tv = (TextView) promptsView.findViewById(R.id.tv);

        try {
            // открываем поток для чтения
            BufferedReader timeRead = new BufferedReader(new InputStreamReader(
                    openFileInput("time")));
            String str = "";
            strOut = "";
            // читаем содержимое
            while ((str = timeRead.readLine()) != null) {
                strOut+=str;
                strOut+='\n';
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.setText(strOut);
        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder.setCancelable(false).setPositiveButton("Копировать",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //действие после нажатия:
                        //копирование в буфер
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("", strOut);
                        clipboard.setPrimaryClip(clip);

                        Toast toast = Toast.makeText(getApplicationContext(),
                                String.valueOf("Успешно скопировано"), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                })
                .setNegativeButton("Отмена",
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

    private void notifySet() {
        SharedPreferences.Editor editor = notify.edit();
        int m=0,s=0;
        int f = 2;
        if (notifyMinEditText.getText().length()==0){
            f--;
        } else {
            m = Integer.valueOf(notifyMinEditText.getText().toString());
        }

        if (notifySecEditText.getText().length()==0){
            f--;
        } else {
            s = Integer.valueOf(notifySecEditText.getText().toString());
        }

        if (f!=0) {
            editor.putInt(notify_PREFERENCES_COUNTER, m * 60 + s);
            editor.apply();
        }
    }

    private void zader(){
        SharedPreferences.Editor editor = pogr.edit();
        int m=0,s=0;
        int f = 2;
        if (pogrEditTextMin.getText().length()==0){
            //m = pogr.getInt(pogr_PREFERENCES_COUNTER, 0)/60;
            f--;
        } else {
            m = Integer.valueOf(pogrEditTextMin.getText().toString());
        }

        if (pogrEditTextSec.getText().length()==0){
            //s = pogr.getInt(pogr_PREFERENCES_COUNTER, 0)%60;
            f--;
        } else {
            s = Integer.valueOf(pogrEditTextSec.getText().toString());
        }
        if (f!=0) {
            editor.putInt(pogr_PREFERENCES_COUNTER, m * 60 + s);
            editor.apply();
        }
    }

    private void ColorVoid() {
        SharedPreferences.Editor editor = color.edit();
        int r,g,b,cl = 0,f=0;
        if (red1.getText().length()==0){
            r=-1;
        } else {
            r = Integer.valueOf(red1.getText().toString());
        }
        if (green1.getText().length()==0){
            g=-1;
        } else {
            g = Integer.valueOf(green1.getText().toString());
        }
        if (blue1.getText().length()==0){
            b=-1;
        } else {
            b = Integer.valueOf(blue1.getText().toString());
        }

        if (r!=-1 && r<256) {
            cl+=r*1000000;
            f=1;
        } else {
            //cl+=color.getInt(color_PREFERENCES_COUNTER, 0)/1000000*1000000;
        }
        if (g!=-1 && g<256) {
            cl+=g*1000;
            f=1;
        } else {
            //cl+=color.getInt(color_PREFERENCES_COUNTER, 0)/1000%1000*1000;
        }
        if (b!=-1 && b<256) {
            cl+=b;
            f=1;
        } else {
            //cl+=color.getInt(color_PREFERENCES_COUNTER, 0)%1000;
        }
        if (f!=0){
            editor.putInt(color_PREFERENCES_COUNTER, cl);
            editor.apply();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mTextView.setText("Размер интерфейса в главном окне: "+String.valueOf(seekBar.getProgress()));
        k = seekBar.getProgress();
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void createDialog() {
        //Получаем вид с файла prompt.xml, который применим для диалогового окна:
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.pogrdroplist, null);

        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);

        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final TextView tv = (TextView) promptsView.findViewById(R.id.tv);
        int pogr2 = pogrGlobal;
        tv.setText("Вы уверены, что хотите установить задержку:\n"+String.valueOf(pogr2/60)+" мин. "+String.valueOf(pogr2%60)+" сек."+"?");

        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder.setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //действие после нажатия:
                        SharedPreferences.Editor editor = pogr.edit();
                        editor.putInt(pogr_PREFERENCES_COUNTER, pogr2);
                        editor.apply();
                        //обновление Activity
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);


                    }
                })
                .setNegativeButton("Отмена",
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
}
/*
//оптимизация под тёмную тему
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // ночная тема не активна, используется светлая тема

                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // ночная тема активна, и она используется
                TextView blank = findViewById(R.id.blank);
                TextView blank2 = findViewById(R.id.blank2);
                TextView blank3 = findViewById(R.id.blank3);
                TextView blank4 = findViewById(R.id.blank4);
                TextView blank5 = findViewById(R.id.blank5);
                TextView blank6 = findViewById(R.id.blank6);
                TextView blank7 = findViewById(R.id.blank7);
                TextView blank8 = findViewById(R.id.blank8);
                TextView blank9 = findViewById(R.id.blank9);
                TextView blank10 = findViewById(R.id.blank10);
                TextView blank11 = findViewById(R.id.blank11);
                TextView blank12 = findViewById(R.id.blank12);
                TextView blank13 = findViewById(R.id.blank13);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    blank.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank2.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank3.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank4.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank5.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank6.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank7.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank8.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank9.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank10.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank11.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank12.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                    blank13.setBackgroundColor(getResources().getColor(R.color.blankNight, getResources().newTheme()));
                }
                break;
        }
 */