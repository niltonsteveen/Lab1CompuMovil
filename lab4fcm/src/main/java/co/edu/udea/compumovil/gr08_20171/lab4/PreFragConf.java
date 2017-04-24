package co.edu.udea.compumovil.gr08_20171.lab4;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreFragConf extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferent_fragment_configuracion);

    }
}