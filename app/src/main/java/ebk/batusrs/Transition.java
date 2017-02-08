package ebk.batusrs;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by E.Batuhan Kaynak on 8.2.2017.
 */

public class Transition {

    private static Transition transition;

    private  Transition(){

    }

    public static Transition getInstance(){
        if(transition == null){
            transition = new Transition();
        }
        return transition;
    }

    public void switchFragment(FragmentManager fragmentManager, Fragment fragment){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
