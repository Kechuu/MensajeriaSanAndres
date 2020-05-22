package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cabrerajesusk.intencionesparroquiasanandrsapstol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText txtCorreo, txtContraseña;
    private Button btnLogin, btnRegsitrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = (EditText) findViewById(R.id.idCorreoLogin);
        txtContraseña = (EditText) findViewById(R.id.idContraseñaLogin);
        btnLogin = (Button) findViewById(R.id.idLoginLogin);
        btnRegsitrar = (Button) findViewById(R.id.idRegistrarseLogin);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = txtCorreo.getText().toString();
                if (isValidEmail(correo) && validarContraseña()){
                    String contraseña = txtContraseña.getText().toString();
                    mAuth.signInWithEmailAndPassword(correo, contraseña)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(LoginActivity.this, "Se logeo correctamente", Toast.LENGTH_SHORT).show();
                                        nextActivity();
                                    }else{
                                        Toast.makeText(LoginActivity.this, "No se encuentra el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(LoginActivity.this, "Error contraseña o correo incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegsitrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistroActivity.class));
            }
        });
    }

    private boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public boolean validarContraseña(){
        String contraseña,contraseñaRepetida;
        contraseña = txtContraseña.getText().toString();
            if (contraseña.length()>=6 && contraseña.length()<=16){
                return true;
            }else return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            Toast.makeText(this, "Dios te bendiga!", Toast.LENGTH_SHORT).show();
            nextActivity();
        }
    }
    private void nextActivity(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
