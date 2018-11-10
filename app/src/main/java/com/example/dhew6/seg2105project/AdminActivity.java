package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;
    ViewPager viewPager;
    ConstraintLayout layout;

    /**
     *
     *
     *
     * @param {Bundle} savedInstanceState -
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        layout = findViewById(R.id.layout);
        viewPager = findViewById(R.id.viewPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

        Snackbar snackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Swipe left and right for other screens!", 5000);
        snackbar.show();

    }

    /**
     *
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     *
     * called when and item from OptionsItem is selected; a param MenuItem item is provided to define the
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //switch checks to see if it is
            case R.id.logOutItem:
                //if you select the log out option in the menu then you should go back to log in screen
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                //finishes the adminActivity then starts the MainActivity
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     */
    public class MyPagerAdapter extends FragmentPagerAdapter{
        /**
         * calls the parent class FragmentPagerAdapter
         *
         * @param fm
         */
        public MyPagerAdapter(FragmentManager fm){
            super(fm);
        }

        /**
         *  returns the fragment associated to one of two positions
         *  1 - AdminsServiceFragment
         *  2 - AdminUserFragment
         *
         *  this method is easily scalable for more fragments
         *
         * @param position - integer position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AdminServiceFragment.newInstance();
                case 1:
                    return AdminUserFragment.newInstance();
            }
            return null;
        }

        /**
         * returns the count of Fragments
         *
         * this method is easily scalable for more fragments
         *
         * @return {int}
         */
        @Override
        public int getCount() {
            return 2;
        }
    }


}
