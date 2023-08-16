package com.example.currencyconverter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CurrencyArrayAdapter extends ArrayAdapter<CharSequence> {

    private CharSequence[] values; // Holds the currency_values array

    public CurrencyArrayAdapter(Context context, int resource, CharSequence[] objects, CharSequence[] values) {
        super(context, resource, objects);
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setText(getItem(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setText(getItem(position));
        return view;
    }

    public CharSequence getValue(int position) {
        return values[position];
    }
}
