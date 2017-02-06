package ebk.batusrs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState){
        CheckBox stopModeCheckBox = (CheckBox) view.findViewById(R.id.checkbox_stopMode);

        stopModeCheckBox.setChecked(MainActivity.stopMode);

        stopModeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MainActivity.stopMode = b;

                //Create a object SharedPreferences from getSharedPreferences("name_file",MODE_PRIVATE) of Context
                SharedPreferences pref = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
                //Using putXXX - with XXX is type data you want to write like: putString, putInt...   from      Editor object
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("stopMode", MainActivity.stopMode);
                Log.i("onDestroy", Boolean.toString(MainActivity.stopMode));
                //finally, when you are done saving the values, call the commit() method.
                editor.commit();
            }
        });
    }
}
