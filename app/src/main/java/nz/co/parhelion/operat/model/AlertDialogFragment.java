package nz.co.parhelion.operat.model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import org.w3c.dom.Text;

import nz.co.parhelion.operat.R;

public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance(int title, int helpText) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("text", helpText);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        TextView view = new TextView(getContext());
        view.setText(getArguments().getInt("text"));

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.baseline_help_24)
                .setTitle(title)
                .setView(view)
                .setPositiveButton("OK"/*R.string.alert_dialog_ok*/,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //
                            }
                        }
                )
                .create();
    }
}