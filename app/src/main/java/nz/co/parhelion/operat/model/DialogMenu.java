package nz.co.parhelion.operat.model;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import nz.co.parhelion.operat.R;


public class DialogMenu implements View.OnClickListener {
    private Dialog dialog;
    private OnDialogMenuListener listener;

    public DialogMenu(Context context) {
        dialog = new Dialog(context, R.style.AppTheme_PopupOverlay);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.score_popup);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


        TextView operatScore = (TextView) dialog.findViewById(R.id.popup_operat_score);
        TextView naturalElementsScore = (TextView) dialog.findViewById(R.id.popup_natural_elements_score);
        TextView incivilitiesScore = (TextView) dialog.findViewById(R.id.popup_incivilities_score);
        TextView territorialScore = (TextView) dialog.findViewById(R.id.popup_territorial_score);
        TextView navigationScore = (TextView) dialog.findViewById(R.id.popup_navigation_score);

        operatScore.setOnClickListener(this);
        naturalElementsScore.setOnClickListener(this);
        incivilitiesScore.setOnClickListener(this);
        territorialScore.setOnClickListener(this);
        navigationScore.setOnClickListener(this);

    }

    public void setListener(OnDialogMenuListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.popup_operat_score:
                listener.onOperatPress();
                dialog.dismiss();
                break;
            case R.id.popup_natural_elements_score:
                listener.onNaturalElementsPress();
                dialog.dismiss();
                break;
            case R.id.popup_incivilities_score:
                listener.onIncivilitiesPress();
                dialog.dismiss();
                break;
            case R.id.popup_territorial_score:
                listener.onTerritorialPress();
                dialog.dismiss();
                break;
            case R.id.popup_navigation_score:
                listener.onNavigationPress();
                dialog.dismiss();
                break;
        }
    }

    public interface OnDialogMenuListener{
        void onOperatPress();
        void onNaturalElementsPress();
        void onIncivilitiesPress();
        void onTerritorialPress();
        void onNavigationPress();
    }
}