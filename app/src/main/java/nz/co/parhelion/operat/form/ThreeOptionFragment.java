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
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Arrays;

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

    public static <E extends Enum<E>> ThreeOptionFragment newInstance(String question, int number, Class<E> options, Field formField, int helpText) {
        Bundle bundle = new Bundle();
        bundle.putString("question", question);
        bundle.putInt("number", number);
        System.out.println("CSJM Options for options "+options);
        System.out.println(Arrays.toString(options.getEnumConstants()));
        bundle.putString("option_1", options.getEnumConstants()[0].toString());
        bundle.putString("option_2", options.getEnumConstants()[1].toString());
        bundle.putString("option_3", options.getEnumConstants()[2].toString());
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



        final Button option1 = view.findViewById(R.id.option_1);
        final Button option2 = view.findViewById(R.id.option_2);
        final Button option3 = view.findViewById(R.id.option_3);

        option1.setText(getArguments().getString("option_1"));
        option2.setText(getArguments().getString("option_2"));
        option3.setText(getArguments().getString("option_3"));

        try {
            options = (Class<? extends Enum>) Class.forName(getArguments().getString("clazz"));
            formField = form.getClass().getField(getArguments().getString("field"));
        } catch (ClassNotFoundException | ClassCastException |NoSuchFieldException e) {
            e.printStackTrace();
        }
        final ViewPagerFragmentActivity activity = (ViewPagerFragmentActivity) getActivity();

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enum option = options.getEnumConstants()[0];
                form.setEnum(option);
                setButton(0);
                advance();
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enum option = options.getEnumConstants()[1];
                form.setEnum(option);
                setButton(1);
                advance();
            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enum option = options.getEnumConstants()[2];
                form.setEnum(option);
                setButton(2);
                advance();
            }
        });

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
        final Button option1 = view.findViewById(R.id.option_1);
        final Button option2 = view.findViewById(R.id.option_2);
        final Button option3 = view.findViewById(R.id.option_3);

        switch (button) {
            case 0:
                option1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option2.setBackground(null);
                option3.setBackground(null);
                break;
            case 1:
                option2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option1.setBackground(null);
                option3.setBackground(null);
                break;
            case 2:
                option3.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                option2.setBackground(null);
                option1.setBackground(null);
                break;

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
