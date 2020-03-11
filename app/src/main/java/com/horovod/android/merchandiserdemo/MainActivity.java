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
import java.lang.reflect.Field;
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
            adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, Data.storeKeeper.getShowables());
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void emulateData() {

        Field[] fields = R.raw.class.getFields();
        for (Field fl : fields) {
            //Log.i("RAW FILES : : : : : ", "fl = " + fl.getName());
            copyFileToFolder(Data.photoFolder, fl.getName());
        }

        Showable store1 = Store.getInstance(Data.storeKeeper, "Традиция номер один");

        store1.setComment("Comment store-1");
        store1.setPreview("shop_trad_1_step0_b_preview.jpg");
        store1.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_1)));

        Showable store2 = Store.getInstance(Data.storeKeeper,"Традиция номер два");
        store2.setPreview("shop_trad_1_step0_b_preview.jpg");
        store2.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_1)));

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
