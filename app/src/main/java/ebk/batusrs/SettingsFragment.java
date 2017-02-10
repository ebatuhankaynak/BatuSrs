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
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.function.ToDoubleBiFunction;

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

    // TODO: 8.2.2017 MOVE ALARM SETTING TO DIALOG, MOVE STOPMODE SETTING CEHCKBOC TO THE RIGHT(?) 
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

        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        final TextView alarmSettingNumberTextView = (TextView) view.findViewById(R.id.alarmSettingNumberTextView);
        // TODO: 8.2.2017 ADD DEFAULT HOUR TO alarmSettingNumberTextView (E.G 1 A.M)(GET FROM USER) 

        numberPicker.setMaxValue(23);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                String str;
                if (newVal <= 12){
                    str = " A.M";
                }else{
                    str = " P.M";
                }
                alarmSettingNumberTextView.setText(newVal + str);
                // TODO: 8.2.2017 CANCEL ALARM, SET IT AGAIN WITH NEW SPECS(THIS SHOULD ONLY HAPPEN WHEN ALARM HITS,) 
                // TODO: 8.2.2017 FIGURE WAYS TO SMARTLY CHNAGE ALARM(IF NEW IS 8 P.M, DAY SHOULD BE 0;IF NEW IS 3AM, DAY IS 1) 
            }
        });
    }
}
