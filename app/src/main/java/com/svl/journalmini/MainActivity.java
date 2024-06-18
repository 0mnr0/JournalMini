package com.svl.journalmini;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.ColorUtils;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mackhartley.roundedprogressbar.RoundedProgressBar;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView LoginButton;
    EditText LoginID;
    EditText PasswordID;
    TextView LoginText;
    TextView PasswordText;
    TextView ErrorText;
    String Access_Token="null";
    int Year;
    int Month;
    int Day;
    String LastSheduleTime = "{}";
    boolean TimerLaunched = false;
    CircularProgressBar HomeWorkProgress;
    final String CurrentDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DATE);
    private RecyclerView recyclerView;
    private List<DataModel> dataList;
    CustomAdapter adapter;
    private RecyclerView LeadRecyclerView;
    private StudentAdapter Studapter;
    private List<Student> studentList;
    boolean CanLogin = true;

    public void OpenRepository(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/0mnr0/JournalMini/releases/tag/Public"));
        startActivity(browserIntent);
    }

    public void showtoast(Object text){

        if (String.valueOf(text).equals("null")) {
            text = "None";
        }
        Toast.makeText(getApplicationContext(), text.toString(),
                Toast.LENGTH_LONG).show();
    }

    public void updateMonetColors(){
        Button SheduleAndExams = findViewById(R.id.SheduleAndExams);
        SheduleAndExams.setBackgroundColor(GetMonetViewBackground());
        SheduleAndExams.setTextColor(GetMonetLite());
        TextView JournalText = findViewById(R.id.JournalPreview);
        JournalText.setTextColor(GetMonetLite());
        TextView MadedPercents = findViewById(R.id.MadedPercents);
        MadedPercents.setTextColor(GetMonetLite());
        TextView CurrentDayInSchedule = findViewById(R.id.SheduleDayDisplaying);
        CurrentDayInSchedule.setTextColor(GetMonetLite());
        Button PreviousDayShedule = findViewById(R.id.PreviousDayShedule);
        PreviousDayShedule.setBackgroundColor(GetMonetViewBackground());
        PreviousDayShedule.setTextColor(GetMonetLite());

        Button NextDaySchedule = findViewById(R.id.NextDayShedule);
        NextDaySchedule.setBackgroundColor(GetMonetViewBackground());
        NextDaySchedule.setTextColor(GetMonetLite());
    }

    public int ColorUtils(int Color1, int Color2){
        return (ColorUtils.blendARGB(Color1, Color2, 0.5F));
    }

    public int GetMonetText(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getResources().getColor(android.R.color.system_accent1_600);
        } else {
            return Color.parseColor("#ffffff");
        }

    }

    public int GetMonetLite(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getResources().getColor(android.R.color.system_accent1_100);
        } else {
            return Color.parseColor("#555555");
        }

    }
    public int GetMonetBackground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getResources().getColor(android.R.color.system_accent1_300);
        } else {
            return Color.parseColor("#222222");
        }

    }
    public int GetMonetForeground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getResources().getColor(android.R.color.system_accent1_900);
        } else {
            return Color.parseColor("#555555");
        }

    }
    public int GetMonetViewBackground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return getResources().getColor(android.R.color.system_accent1_700);
        } else {
            return Color.parseColor("#101010");
        }

    }
    public void ClearEnters(){
        PasswordID.setText("");
        LoginID.setText("");
    }

    public void AccountExit(View view){
        LoadEnters();
        Group LoginGroup = findViewById(R.id.EnterGroup);
        LoginGroup.setVisibility(View.VISIBLE);
    }

    public void CloseStudentImage(View view){
        ConstraintLayout Const = findViewById(R.id.ViewStudentAvatar);
        Const.setVisibility(View.GONE);
    }
    public void ShowStudentImage(View view){
        ConstraintLayout Const = findViewById(R.id.ViewStudentAvatar);
        Const.setVisibility(View.VISIBLE);
        ImageView imagePreview = findViewById(R.id.ShowStudentImage);
        ImageView imageView = (ImageView) view;
        CharSequence tooltipText = imageView.getTooltipText();
        Glide.with(getApplicationContext())
                .load(tooltipText)
                .transform(new RoundedCorners(40))
                .into(imagePreview);
    }

    public void LoadEnters(){
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        if (prefs.getString("LoginData", null) != null) {
            LoginID.setText(prefs.getString("LoginData", null));
            LoginID.setEnabled(true);
            PasswordID.setText(prefs.getString("PassData", null));
            PasswordID.setEnabled(true);
            CanLogin = true;
        }
        Group LoginGroup = findViewById(R.id.EnterGroup);
        LoginGroup.setVisibility(View.VISIBLE);
        Group InAccountGroup = findViewById(R.id.InAccountGroup);
        InAccountGroup.setVisibility(View.GONE);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void ProcessShedule(String SheduleVar){
        Log.d("ProcessShedule", SheduleVar);
        dataList.clear();
        adapter.notifyDataSetChanged();
        Group ScheduleDaySelection = findViewById(R.id.ScheduleDaySelection);
        ScheduleDaySelection.setVisibility(View.VISIBLE);
        Button ShowExamsBtn = findViewById(R.id.SheduleAndExams);
        ShowExamsBtn.setText("Показать экзамены");

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Lesson>>() {}.getType();
        List<Lesson> lessons = gson.fromJson(SheduleVar, listType);
        TextView SheduleDayDisplaying = findViewById(R.id.SheduleDayDisplaying);
        SheduleDayDisplaying.setText(Year+"-"+Month+"-"+Day);

        for (Lesson lesson : lessons) {
            dataList.add(new DataModel(lesson.getSubject_name(), lesson.getTeacher_name(), lesson.getFinished_at(), lesson.getStarted_at()+" - ", lesson.getRoom_name()));
            adapter.notifyDataSetChanged();
        }
        LoadingBar(false);
    }

    public void LoadingBar(boolean visible){
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        if (visible){
            LoginBar.setVisibility(View.VISIBLE);
        } else {
            LoginBar.setVisibility(View.GONE);
        }
    }

    public void ProcessSheduleWidget(String SheduleVar) throws JSONException {
        int MonetColorText = getApplicationContext().getResources().getColor(android.R.color.system_accent1_600);
        int MonetColorTextLite = getApplicationContext().getResources().getColor(android.R.color.system_accent1_500);

        RemoteViews updatedViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.new_app_widget);
        JSONArray jsonArray = new JSONArray(SheduleVar);


        {updatedViews.setTextViewText(R.id.LessonFirst, "Пусто");updatedViews.setTextViewText(R.id.FirstLessonTimeID, "-");updatedViews.setTextViewText(R.id.LessonSecond, "Пусто");updatedViews.setTextViewText(R.id.SecondLessonTimeID, "-");updatedViews.setTextViewText(R.id.LessonThird, "Пусто");updatedViews.setTextViewText(R.id.ThirdLessonTimeID, "-");updatedViews.setTextViewText(R.id.LessonFourth, "Пусто");updatedViews.setTextViewText(R.id.FourthLessonTimeID, "-");}


        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject;
            jsonObject = jsonArray.getJSONObject(i);
            switch (i){
                case 0:
                    updatedViews.setTextViewText(R.id.LessonFirst, jsonObject.getString("subject_name"));
                    updatedViews.setTextViewText(R.id.FirstLessonTimeID, jsonObject.getString("started_at"));
                    updatedViews.setTextColor(R.id.FirstLessonTimeID, MonetColorTextLite);
                    updatedViews.setTextColor(R.id.LessonFirst, MonetColorText);
                    break;
                case 1:
                    updatedViews.setTextViewText(R.id.LessonSecond, jsonObject.getString("subject_name"));
                    updatedViews.setTextViewText(R.id.SecondLessonTimeID, jsonObject.getString("started_at"));
                    updatedViews.setTextColor(R.id.LessonSecond, MonetColorText);
                    updatedViews.setTextColor(R.id.SecondLessonTimeID, MonetColorTextLite);
                    break;
                case 2:
                    updatedViews.setTextViewText(R.id.LessonThird, jsonObject.getString("subject_name"));
                    updatedViews.setTextViewText(R.id.ThirdLessonTimeID, jsonObject.getString("started_at"));
                    updatedViews.setTextColor(R.id.LessonThird, MonetColorText);
                    updatedViews.setTextColor(R.id.ThirdLessonTimeID, MonetColorTextLite);
                    break;
                case 3:
                    updatedViews.setTextViewText(R.id.LessonFourth, jsonObject.getString("subject_name"));
                    updatedViews.setTextViewText(R.id.FourthLessonTimeID, jsonObject.getString("started_at"));
                    updatedViews.setTextColor(R.id.LessonFourth, MonetColorText);
                    updatedViews.setTextColor(R.id.FourthLessonTimeID, MonetColorTextLite);
                    break;
            }

        }
        ComponentName thisWidget = new ComponentName(getApplicationContext(), NewAppWidget.class);
        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(thisWidget, updatedViews);
    }


    public void ProcessMainPageAverageAttendace(int AverageAttendace){
        RoundedProgressBar AverageBar = findViewById(R.id.RoundedAttendanceProgreess);
        AverageBar.setProgressPercentage(AverageAttendace, true);
        TextView AverageText = findViewById(R.id.AvgProcessDecription);
        AverageText.setText("Посещаемость: "+AverageAttendace+"%");
        AverageText.setTextColor(GetMonetLite());
    }

    public void ProcessMainPageAvgScore(int AverageScore){
        RoundedProgressBar AverageBar = findViewById(R.id.RoundedAveragreProgreess);
        AverageBar.setProgressPercentage(((double) AverageScore /12)*100, true);
        TextView AverageText = findViewById(R.id.AvgMarkDecription);
        AverageText.setText("Средний балл: "+AverageScore+"/12");
        AverageText.setTextColor(GetMonetLite());
    }

    @SuppressLint("SetTextI18n")
    public void HomeWorkInfoLoader(JSONArray jsonArray) throws JSONException {
        HomeWorkProgress.setBackgroundColor(GetMonetLite());
        HomeWorkProgress.setProgressBarColor(GetMonetBackground());
        HomeWorkProgress.setVisibility(View.VISIBLE);
        HomeWorkProgress.setRoundBorder(true);
        HomeWorkProgress.setProgressBarWidth(7f);
        HomeWorkProgress.setBackgroundProgressBarWidth(15F);
        int CookedHomeworks = 0;
        int NotCooked = 0;
        int totalHomeworks = 0;
        TextView MissedHW  = findViewById(R.id.MissedHWs);
        TextView CheckedHW  = findViewById(R.id.CheckedHWs);
        TextView CheckingHW = findViewById(R.id.OnCheckingHWs);
        TextView MissingHW  = findViewById(R.id.NotCookedHWs);
        TextView CookedWrong = findViewById(R.id.CookedWrong);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            switch (jsonObject.getInt("counter_type")) {
                case 0://Missed
                    MissedHW.setText("Просрочено: "+jsonObject.getInt("counter"));
                    NotCooked += jsonObject.getInt("counter");
                    totalHomeworks+= jsonObject.getInt("counter");
                    break;
                case 1://Maded and Checked
                    CheckedHW.setText("Проверено: "+jsonObject.getInt("counter"));
                    CookedHomeworks += jsonObject.getInt("counter");
                    totalHomeworks+= jsonObject.getInt("counter");
                    break;
                case 2://Checking
                    CheckingHW.setText("На проверке: "+jsonObject.getInt("counter"));
                    CookedHomeworks += jsonObject.getInt("counter");
                    totalHomeworks+= jsonObject.getInt("counter");
                    break;
                case 3://Need To COok
                    MissingHW.setText("Текущие: "+jsonObject.getInt("counter"));
                    NotCooked += jsonObject.getInt("counter");
                    totalHomeworks+= jsonObject.getInt("counter");
                    break;
                case 5://Cooked wrong
                    CookedWrong.setText("Принято: "+jsonObject.getInt("counter"));
                    CookedHomeworks += jsonObject.getInt("counter");
                    totalHomeworks+= jsonObject.getInt("counter");
                    break;
            }
        }
        HomeWorkProgress.setProgressMax(CookedHomeworks + NotCooked);
        HomeWorkProgress.setProgressWithAnimation(CookedHomeworks, 1800L);
        double CookedInPercents = Math.round(((double) CookedHomeworks /totalHomeworks * 100)*10.0)/10.0;
        TextView MadedPercents = findViewById(R.id.MadedPercents);
        MadedPercents.setText(CookedInPercents+"%");
        MissedHW.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> CheckingHW.setVisibility(View.VISIBLE), 200);
        new Handler().postDelayed(() -> CheckedHW.setVisibility(View.VISIBLE), 400);
        new Handler().postDelayed(() -> MissingHW.setVisibility(View.VISIBLE), 600);
        new Handler().postDelayed(() -> CookedWrong.setVisibility(View.VISIBLE), 800);
    }

    public void ShowSheduleMinus(View view){
        if (Day > 1) {
            Day--;
        }
        else {
            Day = 31;
            Month--;
        }
        if (Month < 1) {
            Month = 12;
            Year--;
        }
        LastSheduleTime="https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter="+Year+"-"+Month+"-"+Day;
        getData(LastSheduleTime, "GET", new JSONObject(), Access_Token);
    }

    public void ShowShedulePlus(View view){
        if (Day < 31) {
            Day++;
        }
        else {
            Day = 1;
            Month++;
        }
        if (Month > 12) {
            Month = 1;
            Year++;
        }
        LastSheduleTime="https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter="+Year+"-"+Month+"-"+Day;
        getData(LastSheduleTime, "GET", new JSONObject(), Access_Token);
    }


    public void SetProfile(String AccPhotoURL, String AccName, String GroupNameInCode){
        ImageView ProfileImage = findViewById(R.id.AccPhoto);
        Glide.with(this)
                .load(AccPhotoURL)
                .transform(new RoundedCorners(20))
                .into(ProfileImage);
        TextView AccFullName = findViewById(R.id.FullNameText);
        AccFullName.setText(AccName);
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        LoginBar.setVisibility(View.GONE);
        TextView GroupName = findViewById(R.id.GroupName);
        GroupName.setText(GroupNameInCode);
        AppSettings.set(getApplicationContext(), "ProfileImageLink", AccPhotoURL);

        LastSheduleTime="https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter="+Year+"-"+Month+"-"+Day;
        getData(LastSheduleTime, "GET", new JSONObject(), Access_Token);
    }

    public void ShowExams(View view){
        LoadingBar(true);
        Group ScheduleDaySelection = findViewById(R.id.ScheduleDaySelection);
        ScheduleDaySelection.setVisibility(View.GONE);
        Button ShowExamsBtn = findViewById(R.id.SheduleAndExams);
        if (ShowExamsBtn.getText().equals("Показать экзамены")) {
            ShowExamsBtn.setText("Показать расписание");
            getData("https://msapi.top-academy.ru/api/v2/dashboard/info/future-exams", "GET", null, Access_Token);
        } else {
            getData(LastSheduleTime, "GET", new JSONObject(), Access_Token);

        }
    }
    @SuppressLint("NotifyDataSetChanged")
    public void ProcessExams(String SheduleVar){
        dataList.clear();
        adapter.notifyDataSetChanged();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Lesson>>() {}.getType();
        List<Lesson> lessons = gson.fromJson(SheduleVar, listType);

        for (Lesson lesson : lessons) {
            dataList.add(new DataModel(lesson.getExamName(),"", "", "",  lesson.getExamDate()));
            adapter.notifyDataSetChanged();
        }
        LoadingBar(false);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void LoadStreamLeader(JSONArray jsonArray) throws JSONException {
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        LoginBar.setVisibility(View.GONE);
        Button OpenLeaderStream = findViewById(R.id.LeaderStream);
        OpenLeaderStream.setBackgroundColor(GetMonetLite());
        OpenLeaderStream.setTextColor(GetMonetText());
        studentList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            String fullName = obj.getString("full_name");
            String photoPath = obj.getString("photo_path");
            int amount = obj.getInt("amount");
            studentList.add(new Student(fullName, photoPath, amount));
        }
        Studapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void LoadGroupLeader(JSONArray jsonArray) throws JSONException {
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        LoginBar.setVisibility(View.GONE);
        Button OpenGroupStream = findViewById(R.id.GroupLeader);
        OpenGroupStream.setBackgroundColor(GetMonetLite());
        OpenGroupStream.setTextColor(GetMonetText());
        studentList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            String fullName = obj.getString("full_name");
            String photoPath = obj.getString("photo_path");
            int amount = obj.getInt("amount");
            studentList.add(new Student(fullName, photoPath, amount));
        }
        Studapter.notifyDataSetChanged();
    }

    public void SucsessfulLogin() {
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (!LoginID.getText().toString().isEmpty() && !PasswordID.getText().toString().isEmpty()) {
            editor.putString("LoginData", LoginID.getText().toString());
            editor.putString("PassData", PasswordID.getText().toString());
            editor.putString("LastSucsessfulAccessToken",Access_Token);
            editor.apply();
        }
        TextView AccNameView = findViewById(R.id.FullNameText);
        AccNameView.setTextColor(GetMonetLite());
        ClearEnters();
        Group LoginGroup = findViewById(R.id.EnterGroup);
        LoginGroup.setVisibility(View.GONE);
        Group InAccountGroup = findViewById(R.id.InAccountGroup);
        InAccountGroup.setVisibility(View.VISIBLE);
        ErrorText.setText("");
        getData("https://msapi.top-academy.ru/api/v2/settings/user-info", "GET", new JSONObject(), Access_Token);
        getData("https://msapi.top-academy.ru/api/v2/dashboard/chart/average-progress", "GET", new JSONObject(), Access_Token);
        getData("https://msapi.top-academy.ru/api/v2/dashboard/chart/attendance", "GET", new JSONObject(), Access_Token);
        getData("https://msapi.top-academy.ru/api/v2/count/homework", "GET", new JSONObject(), Access_Token);
        getData("https://msapi.top-academy.ru/api/v2/dashboard/progress/leader-group", "GET", new JSONObject(), Access_Token);


        if (!TimerLaunched) {
            TimerLaunched=true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getData("https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter=" + Year + "-" + Month + "-" + Day, "GET", new JSONObject(), Access_Token);
                }
            }, 0, 20 * 60 * 1000);
        }
    }




    public void getData(String URL, String FETCH_TYPE, JSONObject JSON, String Access_Token) {
        if (JSON == null){JSON = new JSONObject() ;}
        SendDataTask_GET getData = new SendDataTask_GET(URL, JSON, Access_Token, this::onTaskCompleted);
        getData.execute();
    }
    public void sendData(String URL, String FETCH_TYPE, JSONObject JSON, String Access_Token) {
        SendDataTask sendDataTask = new SendDataTask(URL, FETCH_TYPE, JSON, Access_Token, this::onTaskCompleted);
        sendDataTask.execute();
    }

    public void OpenLeaderStream(View view){
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        LoginBar.setVisibility(View.VISIBLE);
        Button OpenLeaderGroup = findViewById(R.id.GroupLeader);
        getData("https://msapi.top-academy.ru/api/v2/dashboard/progress/leader-stream", "GET", new JSONObject(), Access_Token);
        OpenLeaderGroup.setTextColor(GetMonetLite());
        OpenLeaderGroup.setBackgroundColor(GetMonetText());
    }
    public void OpenLeaderGroup(View view){
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        LoginBar.setVisibility(View.VISIBLE);
        Button OpenLeaderStream = findViewById(R.id.LeaderStream);
        getData("https://msapi.top-academy.ru/api/v2/dashboard/progress/leader-group", "GET", new JSONObject(), Access_Token);
        OpenLeaderStream.setTextColor(GetMonetLite());
        OpenLeaderStream.setBackgroundColor(GetMonetText());
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Trace AppInitStart = FirebasePerformance.getInstance().newTrace("AppInit");
        AppInitStart.start();



        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        Year = Calendar.getInstance().get(Calendar.YEAR);
        Month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Day = Calendar.getInstance().get(Calendar.DATE);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        updateMonetColors();

        LeadRecyclerView = findViewById(R.id.LeaderRecycleView);
        LeadRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        Studapter = new StudentAdapter(this, studentList);
        LeadRecyclerView.setAdapter(Studapter);
        Button OpenLeaderGroup = findViewById(R.id.GroupLeader);
        Button OpenLeaderStream = findViewById(R.id.LeaderStream);
        OpenLeaderStream.setBackgroundColor(GetMonetText());
        OpenLeaderStream.setTextColor(GetMonetLite());
        OpenLeaderGroup.setBackgroundColor(GetMonetLite());
        OpenLeaderGroup.setTextColor(GetMonetText());

        HomeWorkProgress = findViewById(R.id.hw_progress);
        HomeWorkProgress.setProgress(0F);

        View Layout = findViewById(R.id.main);
        Layout.setBackgroundColor(ColorUtils(GetMonetViewBackground(), Color.parseColor("#000000")));
        LoginButton = findViewById(R.id.ButtonLogin);
        LoginID = findViewById(R.id.LoginID);
        PasswordID = findViewById(R.id.PasswordID);
        LoginText = findViewById(R.id.LoginText);
        PasswordText = findViewById(R.id.PasswordText);
        ErrorText = findViewById(R.id.ErrorText);
        FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        if (prefs.getString("LoginData", null) != null) {
            Trace myTrace = FirebasePerformance.getInstance().newTrace("AutoLogin");
            myTrace.start();
            LoadEnters();
            AccEnterInit();
            myTrace.stop();
        }

        LoginText.setTextColor(GetMonetLite());
        PasswordText.setTextColor(GetMonetLite());
        LoginButton.setOnClickListener(v -> AccEnterInit());


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataList = new ArrayList<>();
        adapter = new CustomAdapter(this, dataList);
        recyclerView.setAdapter(adapter);


        AppInitStart.stop();




    }

    private void AccEnterInit() {
        ProgressBar LoginBar = findViewById(R.id.progressBar);
        LoginBar.setIndeterminateTintList(ColorStateList.valueOf(GetMonetLite()));
        LoginBar.setVisibility(View.VISIBLE);
        PasswordID.setEnabled(false);
        LoginID.setEnabled(false);
        CanLogin = false;
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("application_key", "6a56a5df2667e65aab73ce76d1dd737f7d1faef9c52e8b8c55ac75f565d8e8a6");
            JSON.put("id_city", JSONObject.NULL);
            JSON.put("password", PasswordID.getText());
            JSON.put("username", LoginID.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendData("https://msapi.top-academy.ru/api/v2/auth/login", "POST", JSON, Access_Token);
    }

    public void onTaskCompleted(String result) {
        Log.d("SendDataTask", "onTaskCompleted: " + result);
        int ReturnCode = Integer.parseInt(result.split("\\|")[0]);
        String ReturnValue = result.split("\\|")[1];
        String UrlQuestion = result.split("\\|")[2];


        if (ReturnCode == 200) {
            switch (UrlQuestion) {
                case "https://msapi.top-academy.ru/api/v2/auth/login":
                    try {
                        JSONObject jsonObject = new JSONObject(ReturnValue);
                        Access_Token = jsonObject.getString("access_token");
                        SucsessfulLogin();
                    } catch (JSONException e) {
                        showtoast("Не удалось получить ключ входа");
                    }
                    break;
                case "https://msapi.top-academy.ru/api/v2/settings/user-info":
                    try {
                        JSONObject jsonObject = new JSONObject(ReturnValue);
                        SetProfile(jsonObject.getString("photo"), jsonObject.getString("full_name"), jsonObject.getString("group_name"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "https://msapi.top-academy.ru/api/v2/dashboard/chart/average-progress":
                    try {
                        JSONArray jsonArray = new JSONArray(ReturnValue);
                        int lastPoints = (jsonArray.getJSONObject(jsonArray.length() - 1)).getInt("points");
                        ProcessMainPageAvgScore(lastPoints);
                    } catch (JSONException ignored) {}
                    break;
                case "https://msapi.top-academy.ru/api/v2/dashboard/chart/attendance":
                    try {
                        JSONArray jsonArray = new JSONArray(ReturnValue);
                        int lastPoints = (jsonArray.getJSONObject(jsonArray.length() - 1)).getInt("points");
                        ProcessMainPageAverageAttendace(lastPoints);
                    } catch (JSONException ignored) {}
                    break;
                case "https://msapi.top-academy.ru/api/v2/count/homework":
                    try {
                        JSONArray jsonArray = new JSONArray(ReturnValue);
                        HomeWorkInfoLoader(jsonArray);
                    }catch (JSONException ignored) {}
                    break;
                case "https://msapi.top-academy.ru/api/v2/dashboard/progress/leader-group":
                    try {
                        JSONArray jsonArray = new JSONArray(ReturnValue);
                        LoadGroupLeader(jsonArray);
                    }catch (JSONException ignored) {}
                    break;
                case "https://msapi.top-academy.ru/api/v2/dashboard/progress/leader-stream":
                    try {
                        JSONArray jsonArray = new JSONArray(ReturnValue);
                        LoadStreamLeader(jsonArray);
                    }catch (JSONException ignored) {}
                    break;
                case "https://msapi.top-academy.ru/api/v2/dashboard/info/future-exams":
                    try {
                        JSONArray jsonArray = new JSONArray(ReturnValue);
                        ProcessExams(String.valueOf(jsonArray));
                    }catch (JSONException ignored) {}
                    break;
            }
            if (UrlQuestion.contains("https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter")){
                ProcessShedule(ReturnValue);
                if (UrlQuestion.contains("https://msapi.top-academy.ru/api/v2/schedule/operations/get-by-date?date_filter="+CurrentDate)){
                    try {
                        ProcessSheduleWidget(ReturnValue);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }


        } else if (ReturnCode == 422) {
            showtoast("Неверное имя пользователя или пароль");
        } else if (ReturnCode == -123) {
            showtoast("Не удалось запустить процедуру проверки");
        }

        if (ReturnCode == 422 || ReturnCode == -123 || ReturnCode == 405) {
            Button OpenLeaderGroup = findViewById(R.id.GroupLeader);
            Button OpenLeaderStram = findViewById(R.id.LeaderStream);
            ProgressBar LoginBar = findViewById(R.id.progressBar);

            ErrorText.setText(result);
            {
                PasswordID.setEnabled(true);
                CanLogin = true;
                LoginID.setEnabled(true);
                LoginBar.setVisibility(View.GONE);
            }
            if (!OpenLeaderGroup.isEnabled()){
                OpenLeaderGroup.setEnabled(true);
            }
            if (!OpenLeaderStram.isEnabled()){
                OpenLeaderStram.setEnabled(true);
            }

        }
    }




}