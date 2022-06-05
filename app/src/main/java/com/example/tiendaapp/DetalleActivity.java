package com.example.tiendaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetalleActivity extends AppCompatActivity {

    TextView tvProducto;
    TextView tvDesc;
    TextView tvCal;
    TextView tvPrecio;
    Button btnAgregar;
    Producto mp;
    String idcliente;

    FirebaseDatabase base;
    DatabaseReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        tvProducto = findViewById(R.id.tvProducto);
        tvDesc = findViewById(R.id.tvDesc);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvCal = findViewById(R.id.tvCal);
        btnAgregar = findViewById(R.id.btnAgregar);

        inicializarFirebase();

        String idProducto = getIntent().getExtras().getString("producto");
        idcliente= getIntent().getExtras().getString("cliente");

        referencia.child("productos").child(idProducto).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mp = snapshot.getValue(Producto.class);
                tvProducto.setText(mp.nombre);
                tvDesc.setText(mp.descripcion);
                tvCal.setText("Calificacion: "+mp.calificacion);
                tvPrecio.setText("$"+mp.precio);
                setTitle("Detalle: "+mp.nombre);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                referencia.child("Clientes").child(idcliente).child("carrito").child(mp.id).setValue(true);
                Toast.makeText(DetalleActivity.this, "Agregado al carrito", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        base = FirebaseDatabase.getInstance();
        referencia = base.getReference();
    }
}