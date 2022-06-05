package com.example.tiendaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class BuscarActivity extends AppCompatActivity {

    FirebaseDatabase base;
    DatabaseReference referencia;

    Button btnBusqueda;
    EditText edtBuscar;

    Cliente mc;
    String idcliente;

    ArrayList<Producto> listaProducto = new ArrayList<Producto>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);

        inicializarFirebase();

        idcliente= getIntent().getExtras().getString("cliente");

        edtBuscar = findViewById(R.id.edtBuscar);
        referencia.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    listaProducto.clear();
                    for (DataSnapshot dato: snapshot.getChildren()){
                        Producto prod = dato.getValue(Producto.class);
                        listaProducto.add(prod);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBusqueda = findViewById(R.id.btnBusqueda);
        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = edtBuscar.getText().toString();
                boolean correcto = false;

                for (Producto mp: listaProducto){
                    if (nombre.equalsIgnoreCase(mp.nombre)){
                        correcto = true;
                        Intent intencion = new Intent(BuscarActivity.this, DetalleActivity.class);
                        intencion.putExtra("producto", mp.id);
                        intencion.putExtra("cliente", idcliente);
                        startActivity(intencion);
                    }
                }

                if (!correcto){
                    Toast.makeText(BuscarActivity.this, "No existe el producto", Toast.LENGTH_SHORT).show();
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