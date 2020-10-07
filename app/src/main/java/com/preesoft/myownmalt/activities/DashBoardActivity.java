package com.preesoft.myownmalt.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.preesoft.myownmalt.R;

import java.util.List;

public class DashBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupActionBarWithNavController(this,navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

}