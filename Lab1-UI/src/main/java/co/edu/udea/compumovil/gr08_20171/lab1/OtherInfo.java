package co.edu.udea.compumovil.gr08_20171.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
        import android.widget.LinearLayout;
import android.widget.TextView;

public class OtherInfo extends AppCompatActivity {
    LinearLayout rating;
    LinearLayout rating1;
    LinearLayout rating2;
    LinearLayout rating3;
    LinearLayout rating4;
    CheckBox leer;
    CheckBox ver_Tv;
    CheckBox bailar;
    CheckBox cantar;
    CheckBox nadar;
    Button btnMos;
    CheckBox star;
    TextView tvDatos;
    String cadena = "";
    String[] other = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);
        other[0] = "0";
        other[1] = "0";
        other[2] = "0";
        other[3] = "0";
        other[4] = "0";
        tvDatos = (TextView)findViewById(R.id.tvInfo_contact);
        btnMos = (Button)findViewById(R.id.btnMostrar);

        btnMos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                cadena = bundle.getString("Info2").toString();
                cadena = cadena+"\n"+getString(R.string.leer)+":"+ other[0] +getString(R.string.stars)
                        +"\n" + getString(R.string.ver_tv) +":"+other[1]+ getString(R.string.stars)
                        +"\n"+ getString(R.string.bailar) +":"+other[2]+getString(R.string.stars)
                        +"\n"+getString(R.string.cantar) +":"+other[3]+getString(R.string.stars)
                        +"\n"+getString(R.string.nadar)+":"+other[4]+getString(R.string.stars);
                tvDatos.setText(cadena);
            }
        });

        leer=(CheckBox)findViewById(R.id.leer);
        ver_Tv=(CheckBox)findViewById(R.id.ver_tv);
        bailar=(CheckBox)findViewById(R.id.bailar);
        cantar=(CheckBox)findViewById(R.id.cantar);
        nadar=(CheckBox)findViewById(R.id.nadar);


            rating=(LinearLayout)findViewById(R.id.ratings);
            for(int i= 1; i<=6;i++){
                star=(CheckBox)rating.findViewWithTag(String.valueOf(i));
                star.setOnClickListener(starsListener);
            }

            rating1=(LinearLayout)findViewById(R.id.ratings1);
            for(int i= 1; i<=6;i++){
                star=(CheckBox)rating1.findViewWithTag(String.valueOf(i));
                star.setOnClickListener(starsListener1);
            }

            rating2=(LinearLayout)findViewById(R.id.ratings2);
            for(int i= 1; i<=6;i++){
                star=(CheckBox)rating2.findViewWithTag(String.valueOf(i));
                star.setOnClickListener(starsListener2);
            }

            rating3=(LinearLayout)findViewById(R.id.ratings3);
            for(int i= 1; i<=6;i++){
                star=(CheckBox)rating3.findViewWithTag(String.valueOf(i));
                star.setOnClickListener(starsListener3);
            }

            rating4=(LinearLayout)findViewById(R.id.ratings4);
            for(int i= 1; i<=6;i++){//se localizan los 5 checksboxing
                star=(CheckBox)rating4.findViewWithTag(String.valueOf(i));
                star.setOnClickListener(starsListener4);

            }
    }

    private View.OnClickListener starsListener= new View.OnClickListener(){
        public void onClick(View view){
            int tag= Integer.valueOf((String)view.getTag());
            other[0] = (tag-1) + "";
            //Se busca todos los check boxing que fueron tocados
            for (int i=1; i<= tag; i++){
                star =(CheckBox) rating.findViewWithTag(String.valueOf(i));
                star.setChecked(true);
            }
            //desactiva los chackboxing despues de ese
            for(int i=tag+1 ; i<=6; i++){
                star=(CheckBox)rating.findViewWithTag(String.valueOf(i));
                star.setChecked(false);
            }
        }
    };

    private View.OnClickListener starsListener1= new View.OnClickListener(){
        public void onClick(View view){
            int tag= Integer.valueOf((String)view.getTag());
            other[1] = (tag-1) + "";

            for (int i=1; i<= tag; i++){
                star =(CheckBox) rating1.findViewWithTag(String.valueOf(i));
                star.setChecked(true);
            }

            for(int i=tag+1 ; i<=6; i++){
                star=(CheckBox)rating1.findViewWithTag(String.valueOf(i));
                star.setChecked(false);
            }
        }
    };

    private View.OnClickListener starsListener2= new View.OnClickListener(){
        public void onClick(View view){
            int tag= Integer.valueOf((String)view.getTag());
            other[2] = (tag-1) + "";

            for (int i=1; i<= tag; i++){
                star =(CheckBox) rating2.findViewWithTag(String.valueOf(i));
                star.setChecked(true);
            }

            for(int i=tag+1 ; i<=6; i++){
                star=(CheckBox)rating2.findViewWithTag(String.valueOf(i));
                star.setChecked(false);
            }
        }
    };

    private View.OnClickListener starsListener3= new View.OnClickListener(){
        public void onClick(View view){
            int tag= Integer.valueOf((String)view.getTag());
            other[3] = (tag-1) + "";

            for (int i=1; i<= tag; i++){
                star =(CheckBox) rating3.findViewWithTag(String.valueOf(i));
                star.setChecked(true);
            }

            for(int i=tag+1 ; i<=6; i++){
                star=(CheckBox)rating3.findViewWithTag(String.valueOf(i));
                star.setChecked(false);
            }
        }
    };

    private View.OnClickListener starsListener4= new View.OnClickListener(){
        public void onClick(View view){
            int tag= Integer.valueOf((String)view.getTag());
            other[4] = (tag-1) + "";

            for (int i=1; i<= tag; i++){
                star =(CheckBox) rating4.findViewWithTag(String.valueOf(i));
                star.setChecked(true);
            }

            for(int i=tag+1 ; i<=6; i++){
                star=(CheckBox)rating4.findViewWithTag(String.valueOf(i));
                star.setChecked(false);
            }
        }
    };
}