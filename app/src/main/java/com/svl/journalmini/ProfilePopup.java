package com.svl.journalmini;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.style.IconMarginSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ProfilePopup {

    private Context context;
    private PopupWindow popupWindow;
    private View popupView;
    private MainActivity mainActivity;
    public boolean AccountLockedByPhone = false;


    public ProfilePopup(Context context) throws JSONException {
        this.context = context;
        initPopup();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initPopup() throws JSONException {
        mainActivity = (MainActivity) context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.profile_view, null);


        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.getContentView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        assert popupView != null;
        Button LockProfileButton = popupView.findViewById(R.id.LockProfile);
        Button AccountExitButton = popupView.findViewById(R.id.ExitButton);
        Button closePoupButton = popupView.findViewById(R.id.closeBtn);
        Button RemoteUnlock = popupView.findViewById(R.id.RemoteProfileUnlock);

        LockProfileButton.setOnClickListener(v -> {
            if (mainActivity.passphrase.length() >= 8) {
                if (mainActivity.canOperateWithLockState) {
                    try {
                        JSONObject sendingLock = new JSONObject();
                        sendingLock.put("passPhrase", mainActivity.passphrase);
                        sendingLock.put("lockstate", true);
                        closePopup();
                        mainActivity.canOperateWithLockState = false;
                        mainActivity.sendUIData("https://journalui.ru/phoneLock", "POST", sendingLock, mainActivity.UIServerAuth);
                    } catch (Exception e) {
                        Log.e("/phoneLock", String.valueOf(e));
                    }
                } else {
                    mainActivity.showtoast("Узнаём некоторые данные... Попробуйте чуть позже");
                }
            } else {
                mainActivity.showtoast("Функции безопасности не готовы. Попробуйте позже");
            }
        });
        AccountExitButton.setOnClickListener(v -> { closePopup(); mainActivity.AccountExit(null);});

        Button EnterPinActionButton = popupView.findViewById(R.id.EnterPinAction);
        EnterPinActionButton.setOnClickListener(v -> {closePopup(); mainActivity.ProfileLocker();});



        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupView.setOnTouchListener((v, event) -> true);
        closePoupButton.setOnClickListener(v -> mainActivity.CloseProfilePopup(null));

        RemoteUnlock.setOnClickListener(v -> {
            if (mainActivity.passphrase.length() >= 8) {
                if (mainActivity.canOperateWithLockState) {
                    try {
                        JSONObject sendingLock = new JSONObject();
                        sendingLock.put("passPhrase", mainActivity.passphrase);
                        sendingLock.put("lockstate", false);
                        closePopup();
                        mainActivity.canOperateWithLockState = false;
                        mainActivity.sendUIData("https://journalui.ru/phoneLock", "POST", sendingLock, mainActivity.UIServerAuth);
                    } catch (Exception e) {
                        Log.e("/phoneLock", String.valueOf(e));
                    }
                } else {
                    mainActivity.showtoast("Узнаём некоторые данные... Попробуйте чуть позже");
                }
            } else {
                mainActivity.showtoast("Функции безопасности не готовы. Попробуйте позже");
            }
        });


        popupWindow.setOnDismissListener(() -> {
            mainActivity.CloseProfilePopup(null);
        });

        popupView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void showPopup(JSONObject userDataToAuth, JSONObject fullPopupInfo) throws JSONException {
        View rootView = ((ViewGroup) ((MainActivity) context).findViewById(android.R.id.content)).getChildAt(0);
        TextView UserNameOnDisplay = popupView.findViewById(R.id.UserName);
        TextView UserPinActive = popupView.findViewById(R.id.lockState);
        ConstraintLayout ServerLockedByPin = popupView.findViewById(R.id.PinNotIserted);
        Button LockProfileButton = popupView.findViewById(R.id.LockProfile);
        Button EnterPinActionButton = popupView.findViewById(R.id.EnterPinAction);
        Button RemoteUnlock = popupView.findViewById(R.id.RemoteProfileUnlock);
        String passPhrase = mainActivity.passphrase;
        TextView AccInfoView = popupView.findViewById(R.id.AccInfo);
        JSONObject AccInfo = mainActivity.JournalInfo;

        String full_name_info = null;
        try{
            full_name_info = userDataToAuth.getString("full_name");
        } catch (Exception ignored){}
        if (userDataToAuth != null & full_name_info!=null) {
            String UserName = userDataToAuth.getString("full_name");
            UserNameOnDisplay.setText(UserName);
        } else {
            UserNameOnDisplay.setText("Секунду...");
        }

        int points = 0;
        Log.w("AccInfo is", String.valueOf(AccInfo));
        if (AccInfo != null && AccInfo.length() != 0) {
            points = AccInfo.getJSONArray("gaming_points").getJSONObject(0).getInt("points") + AccInfo.getJSONArray("gaming_points").getJSONObject(1).getInt("points");
        }
        AccInfoView.setText("Кол-во баллов: "+ points);


        UserPinActive.setVisibility(View.GONE);
        if (fullPopupInfo != null && !fullPopupInfo.equals("null") && fullPopupInfo.length() != 0){
            Log.w("fullPopupInfo", String.valueOf(fullPopupInfo));
            Object PinUse = fullPopupInfo.getString("usePin");
            UserPinActive.setVisibility(View.VISIBLE);
            if (PinUse.equals("true")){
                UserPinActive.setText("PIN Установлен");
                if (mainActivity.ProfilePINUnlocked && passPhrase.length() > 8) {
                    ServerLockedByPin.setVisibility(View.GONE);
                    EnterPinActionButton.setVisibility(View.GONE);
                    LockProfileButton.setVisibility(View.VISIBLE);
                    RemoteUnlock.setVisibility(View.VISIBLE);
                    LockProfileButton.setVisibility(View.VISIBLE);


                } else {
                    ServerLockedByPin.setVisibility(View.VISIBLE);
                    EnterPinActionButton.setVisibility(View.VISIBLE);
                    LockProfileButton.setVisibility(View.GONE);
                    RemoteUnlock.setVisibility(View.GONE);

                }
            } else {
                UserPinActive.setText("PIN Не активен");
                ServerLockedByPin.setVisibility(View.GONE);
                LockProfileButton.setVisibility(View.GONE);
                EnterPinActionButton.setVisibility(View.GONE);
                RemoteUnlock.setVisibility(View.GONE);
            }
        }

        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    public void closePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
