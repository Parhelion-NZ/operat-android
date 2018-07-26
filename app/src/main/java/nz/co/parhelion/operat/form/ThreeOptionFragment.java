package nz.co.parhelion.operat.form;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nz.co.parhelion.operat.R;
import nz.co.parhelion.operat.model.AlertDialogFragment;
import nz.co.parhelion.operat.model.OperatForm;

public class ThreeOptionFragment extends Fragment {

    private String question = null;
    private Integer number = null;

    private Handler mHandler = new Handler();

    private OperatForm form = null;

    Class<? extends Enum> options;
    Field formField;

    private List<Button> buttons;

    public static <E extends Enum<E>> ThreeOptionFragment newInstance(String question, int number, Class<E> options, Field formField, int helpText) {
        Bundle bundle = new Bundle();
        bundle.putString("question", question);
        bundle.putInt("number", number);
        System.out.println(Arrays.toString(options.getEnumConstants()));

        bundle.putString("clazz", options.getName());
        bundle.putString("field", formField.getName());
        bundle.putInt("helpText", helpText);

        ThreeOptionFragment fragment = new ThreeOptionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        form = ViewModelProviders.of(getActivity()).get(OperatForm.class);

        System.out.println("On create called with bundle "+savedInstanceState);
        buttons = new ArrayList<>();

    }

    /** (non-Javadoc)
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        System.out.println("Got savedInstanceState "+savedInstanceState);
        View view = inflater.inflate(R.layout.three_option_fragment_layout, container, false);
        TextView question = view.findViewById(R.id.three_option_question);



        question.setText(getArguments().getString("question"));
        number = getArguments().getInt("number");



        try {
            options = (Class<? extends Enum>) Class.forName(getArguments().getString("clazz"));
            formField = form.getClass().getField(getArguments().getString("field"));

            Enum[] optionsArray = options.getEnumConstants();
            buttons = new ArrayList<>();
            for (int i = 0; i < optionsArray.length; i++) {
                final int index = i;
                final Enum option = optionsArray[i];
                Button button = new Button(getContext());
                ((RelativeLayout)view).addView(button);
                buttons.add(button);
                button.setText(option.toString());

                button.setId(View.generateViewId());

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (i == 0) {
                    params.addRule(RelativeLayout.BELOW, R.id.three_option_question);
                } else {
                    params.addRule(RelativeLayout.BELOW, buttons.get(i-1).getId());
                }
                params.setMargins(25,25,25,25);
                button.setLayoutParams(params);
                button.setTransformationMethod(null);

                /*button.setLayou
                android:layout_width="match_parent" android:layout_height="wrap_content"
                android:text="Option 1"
                android:layout_weight="1.0"
                android:layout_margin="25dip"
                android:layout_below="@+id/three_option_question"*/

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        form.setEnum(option);
                        setButton(index);
                        advance();
                    }
                });
            }


        } catch (ClassNotFoundException | ClassCastException |NoSuchFieldException e) {
            e.printStackTrace();
        }
        final ViewPagerFragmentActivity activity = (ViewPagerFragmentActivity) getActivity();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment newFragment = AlertDialogFragment.newInstance(
                        R.string.app_name, getArguments().getInt("helpText"));
                newFragment.show(getFragmentManager(), "dialog");

            }
        });

        return view;
    }

    private void advance() {
        final ViewPagerFragmentActivity activity = (ViewPagerFragmentActivity) getActivity();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                activity.progress(number);
            }
        }, 500);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("question", question);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateButtonsFromState();
        if (savedInstanceState == null) {
            return;
        }
        number = savedInstanceState.getInt("number");
        question = savedInstanceState.getString("question");
//        selected = savedInstanceState.getBoolean("selected");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        updateButtonsFromState();
        if (savedInstanceState == null) {
            return;
        }
        number = savedInstanceState.getInt("number");
        question = savedInstanceState.getString("question");
        //selected = savedInstanceState.getBoolean("selected");

    }

    private void setButton(int button) {
        View view = getView();
        if (buttons == null || buttons.isEmpty() || button > buttons.size()) {
            return;
        }

        for (Button buttonView : buttons) {
            if (buttons.get(button).equals(buttonView)) {
                buttonView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                buttonView.setBackground(null);
            }
        }

        System.out.println("CSJM buttons updated");
    }

    private void updateButtonsFromState() {

        try {
            Enum option = (Enum) formField.get(form);
            if (option != null) {
                for (int i = 0; i < options.getEnumConstants().length; i++) {
                    if (option.name().equals(options.getEnumConstants()[i].name())) {
                        setButton(i);
                        return;
                    }
                }
                System.out.println("Option " + option.name() + " didn't match any of the options!");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

       /* if (selected == null) {
            return;
        }
        View view = getView();
        final Button yes = view.findViewById(R.id.yes);
        final Button no = view.findViewById(R.id.no);
        if (selected) {
            setSelectedYes(yes, no);
        } else {
            setSelectedNo(yes, no);
        }*/

    }
}
