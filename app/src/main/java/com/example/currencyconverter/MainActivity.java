package com.example.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.currencyconverter.databinding.ActivityMainBinding;
import com.example.currencyconverter.lib.ConvertUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MaterialCardView currencyTopLayout;
    private MaterialCardView currencyBottomLayout;
    private Spinner spinner_top;
    private Spinner spinner_bottom;
    TextView textInputLabel;

    TextInputEditText textFieldFrom;
    TextInputEditText textFieldTo;
    CurrencyArrayAdapter cAdapter;

    CharSequence[] entries;
    CharSequence[] values;
    ConvertUtils convertUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.includeToolbar.toolbar);

        convertUtils = new ConvertUtils();

        setupViews();
        setupSpinners();
        setDefaultCurrencies();
        setupFAB();
        setupActionListeners();
    }

    private void setupViews() {
        // Get a reference to the parent layout
        currencyTopLayout = findViewById(R.id.top_currency_selector);
        // Find the Spinner within the parent layout
        spinner_top = currencyTopLayout.findViewById(R.id.spinner);

        currencyBottomLayout = findViewById(R.id.bottom_currency_selector);
        spinner_bottom = currencyBottomLayout.findViewById(R.id.spinner);

        // get reference to currencyBottomLayout -> text_input_label
        textInputLabel = currencyBottomLayout.findViewById(R.id.text_input_label);

        // get reference to currencyTopLayout -> text input
        textFieldFrom = currencyTopLayout.findViewById(R.id.text_input);
        textFieldTo = currencyBottomLayout.findViewById(R.id.text_input);
    }

    private void setupSpinners() {
        // Obtain the arrays from resources
        entries = getResources().getTextArray(R.array.currency_entries);
        values = getResources().getTextArray(R.array.currency_values);

        // create the adapter
        cAdapter = new CurrencyArrayAdapter(this, android.R.layout.simple_spinner_item, entries, values);
        cAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_top.setAdapter(cAdapter);
        spinner_bottom.setAdapter(cAdapter);
    }

    private void setDefaultCurrencies() {
        textInputLabel.setText(R.string.converted_currency);

        // todo: set default
        // Find the position of "usd" and "ils" in the values array
        int USPosition = Arrays.asList(values).indexOf("usd");
        int ILPosition = Arrays.asList(values).indexOf("ils");

        // Set the default selections
        spinner_top.setSelection(USPosition);
        spinner_bottom.setSelection(ILPosition);
    }

    private void setupFAB() {
        binding.fab.fab.setOnClickListener(
            view -> {
                // start task on separate thread for network request
                // call convert utils function
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            double converted = convertUtils.convertAndGetAmount();

                            // set your converted currency input
                            runOnUiThread(() -> {
                                //Your code
                                textFieldTo.setText(String.valueOf(converted));
                            });

                            Snackbar.make(view, "Converted currency amount: " + converted, Snackbar.LENGTH_LONG)
                                    .setAnchorView(R.id.fab)
                                    .setAction("Action", null).show();

                            // todo: turn off loader
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            });
    }

    private void setupActionListeners() {
        spinner_top.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected value using the custom adapter
                CharSequence selectedValue = cAdapter.getValue(position);
                convertUtils.setCurrencyFrom((String) selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("Error: Nothing selected!");
            }
        });

        // todo: fix duplicate code
        spinner_bottom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected value using the custom adapter
                CharSequence selectedValue = cAdapter.getValue(position);
                convertUtils.setCurrencyTo((String) selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.println("Error: Nothing selected!");
            }
        });

        textFieldFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(charSequence);
                if (charSequence != null && !charSequence.toString().isEmpty())
                    convertUtils.setCurrencyFromAmount(Double.parseDouble(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity2.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_about);
        builder.setMessage(R.string.about_message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }
}


