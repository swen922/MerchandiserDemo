package com.horovod.android.merchandiserdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierFormat;
import com.horovod.android.merchandiserdemo.classifier.ClassifierRegion;
import com.horovod.android.merchandiserdemo.classifier.ClassifierStep;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.showable.Shot;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.horovod.android.merchandiserdemo.showable.Store;
import com.horovod.android.merchandiserdemo.showable.StoreKeeper;
import com.horovod.android.merchandiserdemo.view.ListShowableAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Showable> adapter;
    private Showable parent;

    private TextView comment;
    private TextView commentBack;

    private BroadcastReceiver changeShowableReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Data.storeKeeper = new StoreKeeper();
        parent = Data.storeKeeper;

        emulateData();

        listView = findViewById(R.id.main_listview);
        listView.setDivider(null);

        comment = findViewById(R.id.main_comment);
        commentBack = findViewById(R.id.main_comment_back);
        //comment.setText(R.string.sample_comment);
        if (!comment.getText().toString().isEmpty()) {
            ViewGroup.LayoutParams params = comment.getLayoutParams();
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
            comment.setLayoutParams(params);
            commentBack.setBackgroundColor(getResources().getColor(R.color.gray_2));
        }

        if (adapter == null) {
            adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, parent.getShowables(), getWindowManager());
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (changeShowableReceiver == null) {
            changeShowableReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int id = intent.getIntExtra(Data.KEY_IDNUMBER, -1);

                    Log.i("ON RECEIVE ||||| " , "id = " + id);

                    if (id > 0) {
                        Showable newParent = parent.getShowableById(id);

                        Log.i("ON RECEIVE ||||| " , "newParent = " + newParent);

                        if (newParent != null) {
                            parent = newParent;
                            adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, parent.getShowables(), getWindowManager());
                            listView.setAdapter(null);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            };
        }

        IntentFilter changeShowableFilter = new IntentFilter(Data.INTENT_REPLACE_SHOWABLE);
        registerReceiver(changeShowableReceiver, changeShowableFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeShowableReceiver);
    }

    private void emulateData() {

        Field[] fields = R.raw.class.getFields();
        for (Field fl : fields) {
            //Log.i("RAW FILES : : : : : ", "fl = " + fl.getName());
            copyFileToFolder(Data.photoFolder, fl.getName());
        }

        Showable store1 = Store.getInstance(Data.storeKeeper, "Традиция номер один");

        store1.setComment("Comment store-1");
        store1.setPreview("shop_trad_1_step1_b_preview.jpg");
        store1.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_1)));
        store1.addClassifier(new ClassifierRegion("Приволжский регион"));
        store1.addClassifier(new ClassifierRegion("Центральный регион"));
        store1.addClassifier(new ClassifierRegion("Южный регион"));

        Classifier clStep0 = new ClassifierStep("0");

        Showable st1_shot1 = Shot.getInstance(store1, "Общий вид магазина");
        st1_shot1.setPreview("shop_trad_1_step0_a_preview.jpg");
        st1_shot1.setImage("shop_trad_1_step0_a.jpg");
        st1_shot1.addClassifier(clStep0);
        store1.addShowable(st1_shot1);

        Showable st1_shot2 = Shot.getInstance(store1, "Общий вид всех выкладок");
        st1_shot2.setPreview("shop_trad_1_step0_b_preview.jpg");
        st1_shot2.setImage("shop_trad_1_step0_b.jpg");
        st1_shot2.addClassifier(clStep0);
        store1.addShowable(st1_shot2);

        Showable st1_shot3 = Shot.getInstance(store1, "Общий вид POPM в Шаге-1");
        st1_shot3.setPreview("shop_trad_1_step0_popm_1_preview.jpg");
        st1_shot3.setImage("shop_trad_1_step0_popm_1.jpg");
        st1_shot3.addClassifier(clStep0);
        store1.addShowable(st1_shot3);


        Showable store2 = store1.cloneMe(Data.storeKeeper);
        store2.setName("Традиция номер два упрощенная");
        store2.setPreview("shop_trad_1_step1_b_preview.jpg");
        store2.clearClassifiers();
        store2.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_1)));
        store2.addClassifier(new ClassifierRegion("Сибирский регион"));
        store2.addClassifier(new ClassifierRegion("Дальневосточный регион"));


        Showable store3 = Store.getInstance(Data.storeKeeper, "Мини-маркет первый");
        store3.setComment("Comment store-3");
        store3.setPreview("shop_minimarket_1_step3_b_preview.jpg");
        store3.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_2)));

        Showable store4 = Store.getInstance(Data.storeKeeper, "Store-4");

        Showable store5 = Store.getInstance(Data.storeKeeper, "Store-5");
        store5.setComment("Comment store-5");


    }

    private void copyFileToFolder(String folderName, String fileName) {
        /*URL url = new URL(fileAddress);
        HttpURLConnection connection = url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();*/

        fileName = checkJPGextension(fileName);

        File folder = new File(getFilesDir() + File.separator + folderName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (success) {
            File image = new File(folder.toString(), fileName);
            if (!image.exists()) {

                try {
                    image.createNewFile();

                    String fileNameWithoutExt = fileName.substring(0, (fileName.length() - 4));

                    InputStream inn = getResources().openRawResource(getResources().getIdentifier(fileNameWithoutExt, "raw", getPackageName()));
                    FileOutputStream out = new FileOutputStream(image);
                    if (inn.available() > 0) {
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = inn.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    }
                    inn.close();
                    out.flush();
                    out.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String checkJPGextension(String fileName) {
        if (fileName.toLowerCase().endsWith(".jpg")) {
            return fileName;
        }
        else {
            String result = fileName + ".jpg";
            return result;
        }
    }

}
