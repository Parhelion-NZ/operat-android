package nz.co.parhelion.operat.form;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import nz.co.parhelion.operat.R;
import nz.co.parhelion.operat.dto.Meshblock;

public class IntroductionFragment extends Fragment {

    private long meshblock;
    private int numProperties;

    public static IntroductionFragment newInstance(long meshblockId, int numProperties) {
        Bundle bundle = new Bundle();
        bundle.putLong("meshblock", meshblockId);
        bundle.putInt("numProperties", numProperties);

        IntroductionFragment fragment = new IntroductionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /** (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
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

        if (getArguments() != null) {
            meshblock = getArguments().getLong("meshblock");
            numProperties = getArguments().getInt("numProperties");
        }

        System.out.println("Saved instance state of intro: "+savedInstanceState);
/*
        long meshblock = getArguments().getLong("meshblock");
        int numProperties = getArguments().getInt("numProperties");
*/
        LinearLayout view = (LinearLayout)inflater.inflate(R.layout.form_introduction_layout, container, false);
        view.setPadding(20,20,20,20);
        TextView meshblockView = view.findViewById(R.id.TextViewMeshblock);
        meshblockView.setText("Meshblock: " + meshblock);
        TextView numPropertiesView = view.findViewById(R.id.TextViewNumProperties);
        numPropertiesView.setText("Number of properties: "+numProperties);

/*        if (savedInstanceState != null) {
            System.out.println("Saved instance state: " + savedInstanceState);
            System.out.println(savedInstanceState.getLong("meshblock"));
            long block = savedInstanceState.getLong("meshblock");

            TextView meshblockView = getView().findViewById(R.id.TextViewMeshblock);
            meshblockView.setText("Meshblock: " + block);
        }*/
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("meshblock", meshblock);
        outState.putInt("numProperties", numProperties);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        meshblock = savedInstanceState.getLong("meshblock");
        numProperties = savedInstanceState.getInt("numProperties");

    }
}
