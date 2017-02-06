package ebk.batusrs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        LinearLayout root = (LinearLayout)view.findViewById(R.id.progressLayout);

        View element = inflater.inflate(R.layout.circular_progress_bar, container, false);

        root.addView(element);

        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

    }
}
