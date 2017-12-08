package com.example.muhammadaliqadri.whatsapp.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadaliqadri.whatsapp.Adapter.ViewPagerAdapter;
import com.example.muhammadaliqadri.whatsapp.Fragment.ChatFragment;
import com.example.muhammadaliqadri.whatsapp.Fragment.ContactsFragment;
import com.example.muhammadaliqadri.whatsapp.Model.WhatsappUser;
import com.example.muhammadaliqadri.whatsapp.Observers.MyObserver;
import com.example.muhammadaliqadri.whatsapp.R;

import java.util.ArrayList;

public class CustomTabActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    ChatFragment chatFragment;
    ContactsFragment contactsFragment;

    String[] tabTitle={"CHAT","CONTACTS"};
    int[] unreadCount={5,0};

    WhatsappUser user;
    private int PERMISSIONS_REQUEST_READ_CONTACTS=1;
    private int INSERT_CONTACT_REQUEST=2;

    MyObserver myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);

        //getting user
        Intent intent = getIntent();
        user = (WhatsappUser) intent.getSerializableExtra("user");

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(0);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        try
        {
            setupTabIcons();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(contactsFragment!=null && contactsFragment.contactListAdapter!=null)
                contactsFragment.contactListAdapter.getFilter().filter(newText);
                return true;
            }
        });



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
           /* case R.id.action_status:
                Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        getIntent().putExtra("user", user);

        chatFragment=new ChatFragment();
        contactsFragment=new ContactsFragment();

        adapter.addFragment(chatFragment,"CHAT");
        adapter.addFragment(contactsFragment,"CONTACTS");
        viewPager.setAdapter(adapter);

    }

    private View prepareTabView(int pos) {

        View view = getLayoutInflater().inflate(R.layout.custom_tab,null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);

        if(unreadCount[pos]>0)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(""+unreadCount[pos]);
        }
        else
            tv_count.setVisibility(View.GONE);


        return view;
    }

    private void setupTabIcons()
    {

        for(int i=0;i<tabTitle.length;i++)
        {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/

            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                contactsFragment.getPermissionsToShowContacts();
            } else {

                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
// TODO Auto-generated method stub
        if(requestCode == INSERT_CONTACT_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                Toast.makeText(getBaseContext(), "Contacts Adding Successfully", Toast.LENGTH_SHORT).show();

            }else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(getBaseContext(), "Contacts Adding Error", Toast.LENGTH_SHORT).show();
            }

        }
    }*/

    @Override
    protected void onPause() {
      //  getContentResolver().unregisterContentObserver(myObserver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //myObserver = new MyObserver(new Handler());
        //getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_VCARD_URI,false,myObserver);

//        contactsFragment.getPhoneContactList();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
