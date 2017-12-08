package com.example.muhammadaliqadri.whatsapp.Activities;

/**
 * Created by Sana Fatima on 12/3/2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.muhammadaliqadri.whatsapp.Adapter.MyAdapter;
import com.example.muhammadaliqadri.whatsapp.Model.ItemData;
import com.example.muhammadaliqadri.whatsapp.R;

public class Account_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);
        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // this is data fro recycler view
        ItemData itemsData[] = {
                new ItemData("Account",R.drawable.ic_account_circle),
                new ItemData("Chats",R.drawable.ic_chat_black_24dp),
                new ItemData("Contacts",R.drawable.ic_supervisor_account),
                new ItemData("Help",R.drawable.help),
                // new ItemData("Rating",R.drawable.rating_important)
        };

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        MyAdapter mAdapter = new MyAdapter(itemsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
