package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeOwnerActivity extends AppCompatActivity {

    ViewPager viewPager;
    MyPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_owner);

        viewPager = findViewById(R.id.viewPagerHome);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(0);

    }

    /**
     * inflates the menu for a log out option
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * run upon the selection of an options menu
     * has a MenuItem as a parameter that is what item was selected
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOutItem:
                //if you select the log out option in the menu then you should go back to log in screen
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            default:
                //if it is not a logOutItem
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {
        /**
         * calls the parent class FragmentPagerAdapter
         *
         * @param fm
         */
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * returns the fragment associated to one of two positions
         * 1 - AdminsServiceFragment
         * 2 - AdminUserFragment
         * <p>
         * this method is easily scalable for more fragments
         *
         * @param position - integer position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeOwnerMainFragment.newInstance();
            }
            return null;
        }

        /**
         * returns the count of Fragments
         * <p>
         * this method is easily scalable for more fragments
         *
         * @return {int}
         */
        @Override
        public int getCount() {
            return 1;
        }
    }

}
