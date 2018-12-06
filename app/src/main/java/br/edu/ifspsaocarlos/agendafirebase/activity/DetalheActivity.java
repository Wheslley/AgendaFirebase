package br.edu.ifspsaocarlos.agendafirebase.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifspsaocarlos.agendafirebase.model.Contato;
import br.edu.ifspsaocarlos.agendafirebase.R;
import br.edu.ifspsaocarlos.agendafirebase.model.IProperties;


public class DetalheActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Contato contato;
    private DatabaseReference databaseReference;
    private String FirebaseID;
    private Spinner spinner;
    private String tipoContato;
    private List<String> categorias;

    public DetalheActivity() {
        this.categorias = new ArrayList<>();
        this.categorias.add(IProperties.FRIEND);
        this.categorias.add(IProperties.FAMILY);
        this.categorias.add(IProperties.JOB);
        this.categorias.add(IProperties.OTHERS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.spinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> arrayDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, this.categorias);
        arrayDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(arrayDataAdapter);

        this.databaseReference = FirebaseDatabase.getInstance().getReference();

        if (getIntent().hasExtra(IProperties.STRING_FIREBASE_ID)) {

            FirebaseID = getIntent().getStringExtra(IProperties.STRING_FIREBASE_ID);

            this.databaseReference.child(FirebaseID).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    contato = snapshot.getValue(Contato.class);

                    if (contato != null) {

                        EditText nameText = (EditText) findViewById(R.id.editTextNome);
                        nameText.setText(contato.getNome());


                        EditText foneText = (EditText) findViewById(R.id.editTextFone);
                        foneText.setText(contato.getFone());


                        EditText emailText = (EditText) findViewById(R.id.editTextEmail);
                        emailText.setText(contato.getEmail());

                        Spinner tipoContato = (Spinner) findViewById(R.id.spinner);
                        tipoContato.setSelection(0);

                        if ((contato.getTipoContato() != null)) {
                            switch (contato.getTipoContato()) {
                                case IProperties.FRIEND:
                                    tipoContato.setSelection(0);
                                    break;

                                case IProperties.FAMILY:
                                    tipoContato.setSelection(1);
                                    break;

                                case IProperties.JOB:
                                    tipoContato.setSelection(2);
                                    break;

                                case IProperties.OTHERS:
                                    tipoContato.setSelection(3);
                                    break;

                                default:
                                    tipoContato.setSelection(0);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (this.FirebaseID == null) {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void apagar() {

        this.databaseReference.child(this.FirebaseID).removeValue();
        Intent resultIntent = new Intent();
        setResult(3, resultIntent);
        finish();
    }

    private void salvar() {

        String name = ((EditText) findViewById(R.id.editTextNome)).getText().toString();
        String fone = ((EditText) findViewById(R.id.editTextFone)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();

        if (this.contato == null) {
            this.contato = new Contato(name, fone, email, this.tipoContato);
            this.databaseReference.push().setValue(contato);

        } else {
            this.contato.setNome(name);
            this.contato.setFone(fone);
            this.contato.setEmail(email);
            this.contato.setTipoContato(this.tipoContato);
            this.databaseReference.child(this.FirebaseID).setValue(contato);
        }

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.tipoContato = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}

