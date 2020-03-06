package com.horovod.android.merchandiserdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.horovod.android.merchandiserdemo.showable.Store;
import com.horovod.android.merchandiserdemo.view.ListShowableAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Showable> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data.listStores.add(new Store("Store-1"));
        Data.listStores.add(new Store("Store-2"));
        Data.listStores.add(new Store("Store-3"));

        listView = findViewById(R.id.main_listview);
        listView.setDivider(null);

        if (adapter == null) {
            adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, Data.listStores);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
