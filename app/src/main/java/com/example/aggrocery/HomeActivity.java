package com.example.aggrocery;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.Models.Usermodel;
import com.example.aggrocery.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    public ActivityHomeBinding activityHomeBinding;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activityHomeBinding =ActivityHomeBinding.inflate(getLayoutInflater());
        View view = activityHomeBinding.getRoot();
        setContentView(view);
        init();


    }

    private void init(){
        try{
            sharedPref = getSharedPreferences(StringKeyboard.SP_userSession, Context.MODE_PRIVATE);
            String savedUsername = sharedPref.getString(StringKeyboard.SP_userName, "");
            Gson gson = new Gson();
            // convert string UserModel in actual Model
            Usermodel user = gson.fromJson(savedUsername, Usermodel.class);
            Toast.makeText(this,savedUsername,Toast.LENGTH_SHORT);
      activityHomeBinding.txtWelcome.setText("Welcome " + user.getUserName() + " !");
            actionBarDrawerToggle = new ActionBarDrawerToggle(this,activityHomeBinding.drawerLayout,activityHomeBinding.materialTool,R.string.nav_open,R.string.nav_close);
            activityHomeBinding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            setSupportActionBar(activityHomeBinding.materialTool);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      setNavigationDrawer();
        }
        catch (Exception e){
            Log.d("ExceptionInitHome",e.toString());
        }

    }
    private void setNavigationDrawer() {


        activityHomeBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment frag = null;

                int itemId = item.getItemId();

                if (itemId == R.id.nav_add_stock) {
                    frag = new AddStockFragment();
                }
                else if(itemId == R.id.nav_sales) {
                    frag = new SalesFragment();
                }
                else if(itemId == R.id.nav_purchase) {
                    frag = new PurchaseFragment();
                }
                else if(itemId == R.id.nav_search_stock){
                    frag = new SearchStockFragment();
                }
                else if(itemId == R.id.nav_list_stock) {
                    frag = new ListStockFragment();
                }
                else if(itemId == R.id.nav_log_out) {
                    SharedPreferences sharedPreferences = getSharedPreferences(StringKeyboard.SP_userSession, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(StringKeyboard.SP_userName);
                    editor.apply();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
                if (frag != null) {
                    FragmentTransaction frgTrans = getSupportFragmentManager().beginTransaction();
                    frgTrans.replace(R.id.frame, frag);
                    frgTrans.commit();
                    activityHomeBinding.drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
