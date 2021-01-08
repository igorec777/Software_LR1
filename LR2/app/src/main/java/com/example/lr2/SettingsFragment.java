package com.example.lr2;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

public class SettingsFragment extends PreferenceFragmentCompat
{
    String TABLE_NAME = "trains";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.settings, rootKey);

        Preference theme = findPreference("theme");

        theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if ((boolean)newValue)
                {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
                }
                return true;
            }
        });

        Preference button = findPreference("clearData");

        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {

            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Удаление данных")
                        .setMessage("Удалить все пользовательские данные?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                SharedPreferences prefs = PreferenceManager
                                        .getDefaultSharedPreferences(getContext());
                                prefs.edit().clear().commit();
                                DatabaseHelper dbHelper = new DatabaseHelper(getContext(),TABLE_NAME, 1);
                                dbHelper.DeleteAllRecords(TABLE_NAME);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dialog.dismiss();
                    }
                })
                        .show();
                return true;
            }
        });

    }
}
