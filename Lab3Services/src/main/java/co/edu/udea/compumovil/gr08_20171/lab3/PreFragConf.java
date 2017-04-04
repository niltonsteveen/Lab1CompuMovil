package co.edu.udea.compumovil.gr08_20171.lab3;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreFragConf extends PreferenceFragment {
    public static PreFragConf newInstance() {

        Bundle args = new Bundle();

        PreFragConf fragment = new PreFragConf();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.f_configuracion);
    }
}