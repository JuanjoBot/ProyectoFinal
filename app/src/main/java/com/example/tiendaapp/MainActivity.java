package com.example.tiendaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Cliente> listaClientes = new ArrayList<Cliente>();

    FirebaseDatabase base;
    DatabaseReference referencia;

    EditText edtCodigo;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarFirebase();
        listarDatos();

        edtCodigo = findViewById(R.id.edtCodigo);


        //Cliente eli = new Cliente("756","b236","Edgar","Ed123","56","1523","69");

        //referencia.child("Clientes").child(eli.id).setValue(eli);
    }

    private void listarDatos() {
        referencia.child("Clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaClientes.clear();
                for (DataSnapshot Dato : dataSnapshot.getChildren()){
                    Cliente cl = Dato.getValue(Cliente.class);
                    listaClientes.add(cl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        presionarBoton();
    }

    private void presionarBoton() {
        btnIngresar = findViewById(R.id.btnIngresar);
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = edtCodigo.getText().toString();
                boolean correcto = false;

                for (Cliente mc: listaClientes){
                    if (codigo.equalsIgnoreCase(mc.codigo)){
                        correcto=true;
                        Intent intencion = new Intent(MainActivity.this, ProductosActivity.class);
                        intencion.putExtra("cliente", mc.id);
                        startActivity(intencion);
                    }
                }

                if (!correcto){
                    Toast.makeText(MainActivity.this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        base = FirebaseDatabase.getInstance();
        referencia = base.getReference();

    }
}