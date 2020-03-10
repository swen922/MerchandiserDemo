package com.horovod.android.merchandiserdemo;

import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierFormat;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.horovod.android.merchandiserdemo.showable.Store;
import com.horovod.android.merchandiserdemo.view.ListShowableAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Showable> adapter;

    private TextView comment;
    private TextView commentBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, Data.listStores);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void emulateData() {

        //String address = "http://horovod.com/assets/data/_codexp/merchdemo/";
        String fileName1 = "shop1_step0_b_preview.jpg";
        String fileName2 = "shop1_step0_b.jpg";

        copyFileToFolder(Data.photoFolder, fileName1);
        copyFileToFolder(Data.photoFolder, fileName2);

        /*File file1 = new File(getFilesDir() + File.separator + Data.photoFolder + File.separator + fileName1);
        File file2 = new File(getFilesDir() + File.separator + Data.photoFolder + File.separator + fileName2);*/

        Showable store1 = new Store("Store-1");
        store1.setComment("Comment store-1");
        store1.setPreview("shop1_step0_b_preview.jpg");
        Classifier classifier = new ClassifierFormat("Традиционный магазин");
        classifier.setComment("Коммент к классификатору Традиционный магазин");
        store1.addClassifier(classifier);
        Data.listStores.add(store1);
        Showable store2 = new Store("Store-2");
        //store2.setComment("Comment store-2");
        Data.listStores.add(store2);
        Showable store3 = new Store("Store-3");
        store3.setComment("Comment store-3");
        Data.listStores.add(store3);
        Showable store4 = new Store("Store-4");

        Data.listStores.add(store4);
        Showable store5 = new Store("Store-5");
        store5.setComment("Comment store-5");
        Data.listStores.add(store5);

    }

    private void copyFileToFolder(String folderName, String fileName) {
        /*URL url = new URL(fileAddress);
        HttpURLConnection connection = url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();*/

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

}
