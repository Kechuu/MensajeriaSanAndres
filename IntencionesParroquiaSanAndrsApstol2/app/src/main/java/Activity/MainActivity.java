package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cabrerajesusk.intencionesparroquiasanandrsapstol.AdapterMensajes;
import com.cabrerajesusk.intencionesparroquiasanandrsapstol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import Entidades.MensajeEnviar;
import Entidades.MensajeRecibir;
import Entidades.Usuario;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView fotoPerfil;
    private TextView nombre;
    private RecyclerView rvMensajes;
    private EditText txtMensaje;
    private Button btnEnviar;
    private Button cerrarSesion;
    private AdapterMensajes adapter;
    private String NOMBRE_USUARIO = "Intenciones";
    private FrameLayout frameLayout;
    private ImageView difuntos;
    private TextView textDifuntos;
    private ImageView accionDeGracias;
    private TextView textAccionDeGracias;
    private ImageView porLaSalud;
    private TextView textPorLaSalud;
    private ImageView options;
    private boolean verificar =false;
    private String opcionElegida="1";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        nombre = (TextView) findViewById(R.id.nombre);
        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnEnviar = (Button) findViewById(R.id.btnMensajeEnviar);
        cerrarSesion = (Button) findViewById(R.id.cerrarSesion);
        frameLayout = (FrameLayout) findViewById(R.id.idFrameLayout);
        difuntos = (ImageView) findViewById(R.id.idDifuntos);
        textDifuntos = (TextView) findViewById(R.id.idTextDifuntos);
        accionDeGracias = (ImageView) findViewById(R.id.idAccionDeGracias);
        textAccionDeGracias = (TextView) findViewById(R.id.idTextAccionDeGracias);
        porLaSalud = (ImageView) findViewById(R.id.idPorLaSalud);
        textPorLaSalud = (TextView) findViewById(R.id.idTextPorLaSalud);
        options = (ImageView) findViewById(R.id.idOptions);
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Intenciones1"); //Sala de chat

        adapter = new AdapterMensajes(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adapter);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Intenciones1").push().setValue(new MensajeEnviar(txtMensaje.getText().toString(), NOMBRE_USUARIO,"",opcionElegida, ServerValue.TIMESTAMP));
                txtMensaje.setText("");
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                retunrLogin();
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificar == false){
                    frameLayout.setVisibility(View.VISIBLE);
                    verificar=true;
                }else{
                    frameLayout.setVisibility(View.GONE);
                    verificar=false;
                }
            }
        });

        difuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setImageResource(R.drawable.image_difuntos);
                opcionElegida="1";
                frameLayout.setVisibility(View.GONE);
            }
        });

        textDifuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setImageResource(R.drawable.image_difuntos);
                opcionElegida="1";
                frameLayout.setVisibility(View.GONE);
            }
        });

        accionDeGracias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setImageResource(R.drawable.image_accion_de_gracias);
                opcionElegida="2";
                frameLayout.setVisibility(View.GONE);
            }
        });

        textAccionDeGracias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setImageResource(R.drawable.image_accion_de_gracias);
                opcionElegida="2";
                frameLayout.setVisibility(View.GONE);
            }
        });

        porLaSalud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setImageResource(R.drawable.image_por_la_salud);
                opcionElegida="3";
                frameLayout.setVisibility(View.GONE);
            }
        });

        textPorLaSalud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                options.setImageResource(R.drawable.image_por_la_salud);
                opcionElegida="3";
                frameLayout.setVisibility(View.GONE);
            }
        });


        databaseReference.child("Intenciones1").orderByChild("option").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s){
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
                adapter.addMensaje(m);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            btnEnviar.setEnabled(false);
            DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    NOMBRE_USUARIO = usuario.getNombre();
                    nombre.setText(NOMBRE_USUARIO);
                    btnEnviar.setEnabled(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            retunrLogin();
        }
    }

    private void retunrLogin(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void setScrollbar(){
        rvMensajes.scrollToPosition(adapter.getItemCount()-1);
    }
}
