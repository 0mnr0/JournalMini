package com.svl.journalmini;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ProfilePopup {

    private Context context;
    private PopupWindow popupWindow;


    public ProfilePopup(Context context) {
        this.context = context;
        initPopup();
    }

    private void initPopup() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.profile_view, null);
        popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.getContentView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        assert popupView != null;
        View bgTouch = popupView.findViewById(R.id.BackgroundTouch);
        bgTouch.setOnClickListener(v -> closePopup() );
        Button lockProfileButton = popupView.findViewById(R.id.ExitButton);
        MainActivity mainActivity = (MainActivity) context;
        lockProfileButton.setOnClickListener(v -> { closePopup(); mainActivity.AccountExit(null);});
        popupWindow.setFocusable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void showPopup() {
        View rootView = ((ViewGroup) ((MainActivity) context).findViewById(android.R.id.content)).getChildAt(0);
        popupWindow.showAtLocation(rootView, android.view.Gravity.CENTER, 0, 0);
    }

    public void closePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
