package com.example.tiendaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarritoActivity extends AppCompatActivity {

    ListView lvCarrito;
    TextView tvTotal;

    ArrayList<Producto> listaCarrito;
    ArrayAdapter<Producto> adaptador;
    String idcliente;

    FirebaseDatabase base;
    DatabaseReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        lvCarrito = findViewById(R.id.lvCarrito);
        tvTotal = findViewById(R.id.tvTotal);
        setTitle("Carrito de compras");

        idcliente = getIntent().getExtras().getString("cliente");
        listaCarrito = new ArrayList<>();
        adaptador = new ArrayAdapter<Producto>(CarritoActivity.this, android.R.layout.simple_list_item_1,
                listaCarrito);
        lvCarrito.setAdapter(adaptador);

        lvCarrito.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                referencia.child("Clientes").child(idcliente).child("carrito").child(listaCarrito.get(i).id).removeValue();
                return true;
            }
        });

        inicializarFirebase();

        referencia.child("Clientes").child(idcliente).child("carrito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaCarrito.clear();
                if (snapshot.hasChildren()){
                    for (DataSnapshot item: snapshot.getChildren()){
                        String idProd = item.getKey();
                        referencia.child("productos").child(idProd).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Producto mp = snapshot.getValue(Producto.class);
                                listaCarrito.add(mp);
                                adaptador.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    tvTotal.setText("Total: Pendiente");
                }else{
                    tvTotal.setText("Total: $0");
                    adaptador.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        base = FirebaseDatabase.getInstance();
        referencia = base.getReference();
    }
}