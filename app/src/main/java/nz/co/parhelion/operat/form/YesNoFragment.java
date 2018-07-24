package nz.co.parhelion.operat.form;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import nz.co.parhelion.operat.R;
import nz.co.parhelion.operat.model.OperatForm;

public class YesNoFragment extends Fragment {

    private Boolean selected = null;
    private String question = null;
    private Integer number = null;

    private Handler mHandler = new Handler();

    private OperatForm form = null;

    public static YesNoFragment newInstance(String question, int number) {
        Bundle bundle = new Bundle();
        bundle.putString("question", question);
        bundle.putInt("number", number);

        YesNoFragment fragment = new YesNoFragment();
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
        View view = inflater.inflate(R.layout.yes_no_fragment_layout, container, false);
        TextView question = view.findViewById(R.id.yes_no_question);

        question.setText(getArguments().getString("question"));
        number = getArguments().getInt("number");

        final Button yes = view.findViewById(R.id.yes);
        final Button no = view.findViewById(R.id.no);



        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedYes(yes, no);
                form.setYesNo(number, true);
                advance();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedNo(yes, no);
                form.setYesNo(number, false);
                advance();

            }
        });

        if (selected != null) {
            if (selected) {
                setSelectedYes(yes, no);
            } else {
                setSelectedNo(yes, no);
            }
        }

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

    private void setSelectedYes(Button yes, Button no) {
        selected = true;
        yes.setBackgroundColor(Color.rgb(0,255,0));
        no.setBackground(null);
    }

    private void setSelectedNo(Button yes, Button no) {
        selected = false;
        no.setBackgroundColor(Color.rgb(255,0,0));
        yes.setBackground(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("question", question);
        if (selected != null) {
            outState.putBoolean("selected", selected);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        number = savedInstanceState.getInt("number");
        question = savedInstanceState.getString("question");
        selected = savedInstanceState.getBoolean("selected");
        updateButtonsFromState();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        number = savedInstanceState.getInt("number");
        question = savedInstanceState.getString("question");
        selected = savedInstanceState.getBoolean("selected");

        updateButtonsFromState();
    }

    private void updateButtonsFromState() {
        if (selected == null) {
            return;
        }
        View view = getView();
        final Button yes = view.findViewById(R.id.yes);
        final Button no = view.findViewById(R.id.no);
        if (selected) {
            setSelectedYes(yes, no);
        } else {
            setSelectedNo(yes, no);
        }

    }
}
