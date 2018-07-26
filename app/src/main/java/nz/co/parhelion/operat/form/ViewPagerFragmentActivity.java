package nz.co.parhelion.operat.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import nz.co.parhelion.operat.R;
import nz.co.parhelion.operat.model.OperatForm;



/**
 * The <code>ViewPagerFragmentActivity</code> class is the fragment activity hosting the ViewPager
 * @author mwho
 */
public class ViewPagerFragmentActivity extends FragmentActivity{
    /** maintains the pager adapter*/
    private PagerAdapter mPagerAdapter;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */

    private long meshblock;
    private int numProperties;


    private enum Questions {
        Q1(R.string.q1, R.string.q1_help, Boolean.class, "q1"),
        Q2(R.string.q2, R.string.q2_help, Boolean.class, "q2"),
        Q3(R.string.q3, R.string.q3_help, Boolean.class, "q3"),
        Q4(R.string.q4, R.string.q4_help, Boolean.class, "q4"),
        Q5(R.string.q5, R.string.q5_help, Boolean.class, "q5"),
        Q6(R.string.q6, R.string.q6_help, Boolean.class, "q6"),
        Q7(R.string.q7, R.string.q7_help, Boolean.class, "q7"),
        Q8(R.string.q8, R.string.q8_help, OperatForm.Q8.class, "q8"),
        Q9(R.string.q9, R.string.q9_help, OperatForm.Q9.class, "q9"),
        Q10(R.string.q10, R.string.q10_help, OperatForm.Q10.class, "q10"),
        Q11(R.string.q11, R.string.q11_help, OperatForm.Q11.class, "q11"),
        Q12(R.string.q12, R.string.q12_help, OperatForm.Q12.class, "q12");

        int question;
        int helpText;
        Class<?> clazz;
        String fieldName;

        Questions(int question, int helpText, Class<?> clazz, String fieldName) {
            this.question = question;
            this.helpText = helpText;
            this.clazz = clazz;
            this.fieldName = fieldName;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.form_single_layout);

        Intent intent = getIntent();
        meshblock = intent.getLongExtra("MESHBLOCK", 0);
        numProperties = intent.getIntExtra("NUM_PROPERTIES", 0);
        //initialsie the pager
        this.initialisePaging();

        final OperatForm viewModel = ViewModelProviders.of(this).get(OperatForm.class);

    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        Bundle bundle = new Bundle();
        bundle.putLong("meshblock", meshblock);
        System.out.println("Putting bundle "+meshblock);
        System.out.println(bundle);
        fragments.add(IntroductionFragment.newInstance(meshblock, numProperties));


/*        List<String> questions = Stream.of(
                "Is there public grass or verges?",
                "Are there sounds of nature (e.g. birdsong, water)?",
                "Are there clear and easy to read road name signs?",
                "Are there street lights?",
                "Are there any unlit streets or alleyways?",
                "Are there instances of littering, dog fouling or broken glass?",
                "Are there loud traffic, industrial or other noises?").collect(Collectors.toList());*/

        List<String> questions = new ArrayList<>();
        questions.add("Is there public grass or verges?");
        questions.add("Are there sounds of nature (e.g. birdsong, water)?");
        questions.add("Are there clear and easy to read road name signs?");
        questions.add("Are there street lights?");
        questions.add("Are there any unlit streets or alleyways?");
        questions.add("Are there instances of littering, dog fouling or broken glass?");
        questions.add("Are there loud traffic, industrial or other noises?");


        for (int i = 0; i < Questions.values().length; i++) {
            Questions question = Questions.values()[i];

            String questionText = getResources().getString(question.question);
            String helpText = getResources().getString(question.helpText);
            if (question.clazz == Boolean.class) {
                fragments.add(YesNoFragment.newInstance(questionText, i+1));
            } else if (Enum.class.isAssignableFrom(question.clazz)) {
                Class<? extends Enum> qClazz = (Class<? extends Enum>)question.clazz;
                try {
                    fragments.add(ThreeOptionFragment.newInstance(questionText, i+1, qClazz, OperatForm.class.getField(question.fieldName), question.helpText));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }

        }

/*        int questionNo = 1;
        for (String question : questions) {
            fragments.add(YesNoFragment.newInstance(question, questionNo));
            questionNo++;
        }

        try {
            fragments.add(ThreeOptionFragment.newInstance("Approximate number of vehicles that drove past during assessment?", questionNo++, OperatForm.Q8.class, OperatForm.class.getField("q8")));

            fragments.add(ThreeOptionFragment.newInstance("Is there a continuous pavement, that is wide enough for 2 people or a wheelchair and is well maintained?", questionNo++, OperatForm.Q10.class, OperatForm.class.getField("q10")));
            fragments.add(ThreeOptionFragment.newInstance("How steep is the pavement and/or road?", questionNo++, OperatForm.Q11.class, OperatForm.class.getField("q11")));
            fragments.add(ThreeOptionFragment.newInstance("How well is the road maintained?", questionNo++, OperatForm.Q12.class, OperatForm.class.getField("q12")));
            fragments.add(ThreeOptionFragment.newInstance("What is the main outlook?", questionNo++, OperatForm.Q13.class, OperatForm.class.getField("q13")));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }*/
/*
        fragments.add(Fragment.instantiate(this, YesNoFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, YesNoFragment.class.getName()));
*/
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        ViewPager pager = (ViewPager)super.findViewById(R.id.form_pager);
        pager.setAdapter(this.mPagerAdapter);
    }

    public void progress(int number) {
        System.out.println("Setting progress to "+number);
        ViewPager pager = (ViewPager)super.findViewById(R.id.form_pager);
        pager.setCurrentItem(/*pager.getCurrentItem()+*/ number + 1);
    }
}