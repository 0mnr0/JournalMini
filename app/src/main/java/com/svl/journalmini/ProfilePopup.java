package com.svl.journalmini;


import android.annotation.SuppressLint;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

public class ProfilePopup {

    private Context context;
    private PopupWindow popupWindow;
    private View popupView;
    private MainActivity mainActivity;


    public ProfilePopup(Context context) throws JSONException {
        this.context = context;
        initPopup();
    }

    public void ProfileLocker(){
        closePopup();
        mainActivity.ProfileLocker();
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

        LockProfileButton.setOnClickListener(v -> { ProfileLocker();});
        AccountExitButton.setOnClickListener(v -> { closePopup(); mainActivity.AccountExit(null);});




        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupView.setOnTouchListener((v, event) -> true);
        closePoupButton.setOnClickListener(v -> mainActivity.CloseProfilePopup(null));


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


        Log.wtf("uui val:", String.valueOf(userDataToAuth));
        TextView UserNameOnDisplay = popupView.findViewById(R.id.UserName);
        TextView UserPinActive = popupView.findViewById(R.id.lockState);
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

        UserPinActive.setVisibility(View.GONE);
        if (fullPopupInfo != null){
            Object PinUse = fullPopupInfo.getString("usePin");
            UserPinActive.setVisibility(View.VISIBLE);
            if (PinUse.equals("true")){
                UserPinActive.setText("PIN Установлен");
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
