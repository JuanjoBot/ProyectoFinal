package com.example.tiendaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    TextView tvNombre;
    TextView tvUsuario;
    TextView tvPuntos;
    TextView tvNivel;

    Button btnCarrito;
    Button btnBuscar;
    ListView lvProductos;
    Cliente mc;

    FirebaseDatabase base;
    DatabaseReference referencia;

    ArrayList<Producto> listaProducto;
    ArrayAdapter<Producto> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        String id = getIntent().getExtras().getString("cliente");

        inicializarFirebase();

        tvNombre = findViewById(R.id.tvNombre);
        tvUsuario = findViewById(R.id.tvUsuario);
        tvNivel = findViewById(R.id.tvNivel);
        tvPuntos = findViewById(R.id.tvPuntos);

        lvProductos = findViewById(R.id.lvProductos);

        listaProducto = new ArrayList<>();
        adaptador = new ArrayAdapter<Producto>(ProductosActivity.this, android.R.layout.simple_list_item_1, listaProducto);

        lvProductos.setAdapter(adaptador);

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intencion = new Intent(ProductosActivity.this, DetalleActivity.class);
                intencion.putExtra("producto", listaProducto.get(i).id);
                intencion.putExtra("cliente", mc.id);
                startActivity(intencion);
            }
        });


        referencia.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    listaProducto.clear();
                    for (DataSnapshot dato: snapshot.getChildren()){
                        Producto prod = dato.getValue(Producto.class);
                        listaProducto.add(prod);
                        adaptador.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referencia.child("Clientes").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mc = dataSnapshot.getValue(Cliente.class);
                tvNombre.setText("Nombre: "+mc.nombre);
                tvUsuario.setText("Usuario: "+mc.usuario);
                tvNivel.setText("Nivel: "+mc.nivel);
                tvPuntos.setText(("Puntos: "+mc.puntos));


                if (mc.carrito!=null){
                    btnCarrito.setText(getString(R.string.ver_carrito)+" ("+mc.carrito.size()+")");
                }else{
                    btnCarrito.setText(getString(R.string.ver_carrito));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnCarrito = findViewById(R.id.btnCarrito);
        btnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(ProductosActivity.this, CarritoActivity.class);
                intencion.putExtra("cliente", mc.id);
                startActivity(intencion);
            }
        });

        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(ProductosActivity.this, BuscarActivity.class);
                intencion.putExtra("cliente", mc.id);
                startActivity(intencion);
            }
        });

    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        base = FirebaseDatabase.getInstance();
        referencia = base.getReference();
    }
}