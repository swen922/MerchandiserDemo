package com.horovod.android.merchandiserdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierBrand;
import com.horovod.android.merchandiserdemo.classifier.ClassifierFormat;
import com.horovod.android.merchandiserdemo.classifier.ClassifierRegion;
import com.horovod.android.merchandiserdemo.classifier.ClassifierStep;
import com.horovod.android.merchandiserdemo.data.Data;
import com.horovod.android.merchandiserdemo.data.Util;
import com.horovod.android.merchandiserdemo.showable.Shot;
import com.horovod.android.merchandiserdemo.showable.Showable;
import com.horovod.android.merchandiserdemo.showable.ShowableType;
import com.horovod.android.merchandiserdemo.showable.Store;
import com.horovod.android.merchandiserdemo.showable.StoreKeeper;
import com.horovod.android.merchandiserdemo.view.KeepersSpinnerAdapter;
import com.horovod.android.merchandiserdemo.view.ListShowableAdapter;
import com.horovod.android.merchandiserdemo.view.ShotFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Showable> adapter;
    private ArrayAdapter<Showable> keeperSpinnerAdapter;
    private Showable parent;

    private TextView header;
    private ImageView backArrow;
    private Spinner spinnerKeepers;
    private TextView comment;
    private TextView classifiers;

    private FragmentManager myFragmentManager;
    private WindowManager myWindowManager;

    private BroadcastReceiver changeShowableReceiver;
    //private BroadcastReceiver photoFragmentReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = StoreKeeper.getInstance(getResources().getString(R.string.show_desc_example_01));
        Data.addKeeper(parent);

        myFragmentManager = getSupportFragmentManager();
        myWindowManager = getWindowManager();

        emulateData();

        listView = findViewById(R.id.main_listview);
        listView.setDivider(null);

        header = findViewById(R.id.main_header);
        backArrow = findViewById(R.id.main_icon_back);
        spinnerKeepers = findViewById(R.id.main_spinner_keepers);
        comment = findViewById(R.id.main_comment);
        classifiers = findViewById(R.id.main_classifiers);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backArrow.setClickable(false);
                Showable newParent = parent.getParent();
                changeParent(newParent);
                handleBackArrow();
            }
        });

        fillHeaders();
        handleBackArrow();

        if (adapter == null) {
            adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, parent.getShowables(), myWindowManager);
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (keeperSpinnerAdapter == null) {
            keeperSpinnerAdapter = new KeepersSpinnerAdapter(getApplicationContext(), R.layout.keeper_spinner_row, R.layout.keeper_spinner_row_dropdown, Data.getKeepersList());
        }
        spinnerKeepers.setAdapter(keeperSpinnerAdapter);

        if (changeShowableReceiver == null) {
            changeShowableReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int id = intent.getIntExtra(Data.KEY_IDNUMBER, -1);
                    if (id > 0) {
                        Showable newParent = parent.getShowableById(id);
                        if (newParent != null) {
                            changeParent(newParent);
                            handleBackArrow();
                        }
                    }
                }
            };
        }

        /*if (photoFragmentReceiver == null) {
            photoFragmentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    int id = intent.getIntExtra(Data.KEY_IDNUMBER, -1);

                    if (id > 0) {
                        FragmentTransaction ft = myFragmentManager.beginTransaction();
                        Data.shotFragment = new ShotFragment();
                        Data.shotFragment.setMyFragmentManager(myFragmentManager, getApplicationContext());

                        Bundle args = new Bundle();
                        args.putInt(Data.KEY_IDNUMBER, id);
                        Data.shotFragment.setArguments(args);

                        ft.add(R.id.container_main, Data.shotFragment, null);
                        ft.commit();
                    }
                }
            };
        }*/

        IntentFilter changeShowableFilter = new IntentFilter(Data.INTENT_REPLACE_SHOWABLE);
        registerReceiver(changeShowableReceiver, changeShowableFilter);
        /*IntentFilter photoFragmentFilter = new IntentFilter(Data.INTENT_SHOW_PHOTO);
        registerReceiver(photoFragmentReceiver, photoFragmentFilter);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeShowableReceiver);
        //unregisterReceiver(photoFragmentReceiver);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*if (Data.shotFragment != null) {
            myFragmentManager.beginTransaction().remove(Data.shotFragment).commit();
            Data.shotFragment = null;
        }*/
        if (ShowableType.STORE_KEEPER != parent.getShowableType()) {
            Showable newParent = parent.getParent();
            changeParent(newParent);
            handleBackArrow();
        }
        else {
            super.onBackPressed();
        }
    }

    private void fillHeaders() {
        if (parent.getName() != null && !parent.getName().isEmpty()) {
            String fullName = "";
            if (ShowableType.STORE_KEEPER == parent.getShowableType()) {
                fullName = getResources().getString(R.string.show_desc_storekeeper) + ": ";
                fullName = fullName.toUpperCase();
            }
            else if (ShowableType.STORE == parent.getShowableType()) {
                fullName = getResources().getString(R.string.show_desc_store) + ": ";
                fullName = fullName.toUpperCase();
            }
            fullName = fullName + parent.getName();
            header.setText(fullName.trim());
        }


        if (parent.getComment() != null && !parent.getComment().isEmpty()) {
            comment.setText(parent.getComment());
            swapHeight(comment, false);
        }
        else {
            swapHeight(comment, true);
        }

        /** Высчитываем количество символов, которое помещается в одну строку classifiers,
         * причем считаем только один раз и в других случаях применяем сохраненный результат */
        if (Data.maxLengthClassifiersMain < 0) {
            calculateLength();
        }

        String fullClassifiers = Util.formatClassifiers(parent, getApplicationContext(), (Data.maxLengthClassifiersMain - 1));

        if (!fullClassifiers.isEmpty()) {
            classifiers.setText(fullClassifiers.trim());
            swapHeight(classifiers, false);
            classifiers.setBackgroundColor(getResources().getColor(R.color.gray_2));
        }
        else {
            swapHeight(classifiers, true);
            classifiers.setBackgroundColor(getResources().getColor(R.color.white_zero));
        }
    }

    private void handleBackArrow() {
        if (ShowableType.STORE_KEEPER == parent.getShowableType()) {
            backArrow.setVisibility(View.INVISIBLE);
            backArrow.setClickable(false);
            spinnerKeepers.setVisibility(View.VISIBLE);
            spinnerKeepers.setClickable(true);
        }
        else {
            backArrow.setVisibility(View.VISIBLE);
            backArrow.setClickable(true);
            spinnerKeepers.setVisibility(View.INVISIBLE);
            spinnerKeepers.setClickable(false);
        }
    }

    private void changeParent(Showable newParent) {
        parent = newParent;
        fillHeaders();
        adapter = new ListShowableAdapter(getApplication(), R.layout.list_showable_item, parent.getShowables(), getWindowManager());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void swapHeight(View view, boolean zero) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (zero) {
            params.height = 0;
        }
        else {
            params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        }
        view.setLayoutParams(params);
    }

    private void calculateLength() {
        String checkLength = getResources().getString(R.string.maxLength);
        classifiers.setText(checkLength);
        Data.maxLengthClassifiersMain = classifiers.getPaint().breakText(checkLength, 0, checkLength.length(), true, classifiers.getWidth(), null);
        classifiers.setText("");
        Data.maxLengthClassifiersMain = Data.maxLengthClassifiersMain > Data.minLength ? Data.maxLengthClassifiersMain : Data.minLength;
    }

    private void emulateData() {

        Field[] fields = R.raw.class.getFields();
        for (Field fl : fields) {
            //Log.i("RAW FILES : : : : : ", "fl = " + fl.getName());
            copyFileToFolder(Data.photoFolder, fl.getName());
        }

        Showable store1 = Store.getInstance(parent, "Традиция номер один");

        store1.setComment("Базовый вариант Традиции для всех регионов");
        store1.setPreview("shop_trad_1_step1_b_preview.jpg");
        store1.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_1)));
        store1.addClassifier(new ClassifierRegion("Приволжский"));
        store1.addClassifier(new ClassifierRegion("Центральный"));
        store1.addClassifier(new ClassifierRegion("Южный"));

        Classifier clStep0 = new ClassifierStep("0");
        Classifier clStep1 = new ClassifierStep("1");
        Classifier clStep2 = new ClassifierStep("2");
        Classifier clStep3 = new ClassifierStep("3");

        Classifier clBrandRossia = new ClassifierBrand("Rossia");
        clBrandRossia.setComment("Россия - щедрая душа");
        Classifier clBrandKitKat = new ClassifierBrand("KitKat");
        clBrandKitKat.setComment("КитКат");
        Classifier clBrandNUTS = new ClassifierBrand("NUTS");
        clBrandNUTS.setComment("НАТС");
        Classifier clBrandNesquik = new ClassifierBrand("Nesquik");
        clBrandNesquik.setComment("Несквик");
        Classifier clBrandNescafe = new ClassifierBrand("Nescafe");
        clBrandNescafe.setComment("Нескафе");
        Classifier clBranMaggi = new ClassifierBrand("Maggi");
        clBranMaggi.setComment("Магги");
        Classifier clBranPurina = new ClassifierBrand("Purina");
        clBranPurina.setComment("Пурина");

        Showable st1_shot1 = Shot.getInstance(store1, "Общий вид магазина в Шаге-0");
        st1_shot1.setPreview("shop_trad_1_step0_a_preview.jpg");
        st1_shot1.setImage("shop_trad_1_step0_a.jpg");
        st1_shot1.addClassifier(clStep0);

        Showable st1_shot2 = Shot.getInstance(store1, "Общий вид всех выкладок в Шаге-0");
        st1_shot2.setPreview("shop_trad_1_step0_b_preview.jpg");
        st1_shot2.setImage("shop_trad_1_step0_b.jpg");
        st1_shot2.setComment("Размещение шоколадных батончиков – в витрине или на полке в UMD максимально близко (не более 1 м) к месту расчета. Сахаристые БОН ПАРИ размещаются: на полке, на хуках или в витрине, в пределах 1 м от кассового узла. Если блистеров НЕСКВИК " +
                "и МАГГИ нет в наличии – необходимо размещать на кассовом кубе «живой» продукт.");
        st1_shot2.addClassifier(clStep0);

        Showable st1_shot3 = Shot.getInstance(store1, "POPM для всех Шагов");
        st1_shot3.setPreview("shop_trad_1_step0_popm_1_preview.jpg");
        st1_shot3.setImage("shop_trad_1_step0_popm_1.jpg");
        st1_shot3.addClassifier(clStep0);

        Showable st1_shot4 = Shot.getInstance(store1, "Планограмма батончиков в Шаге-0");
        st1_shot4.setComment("Бледно окрашенные SKU не обязательны для размещения на данном Шаге. Показан принцип рекомендованной выкладки в случае расширения ассортимента (витрина, UMD).");
        st1_shot4.setPreview("shop_trad_1_step0_plangr_sticks.jpg");
        st1_shot4.setImage("shop_trad_1_step0_plangr_sticks.jpg");
        st1_shot4.addClassifier(clStep0);
        st1_shot4.addClassifier(clBrandRossia);
        st1_shot4.addClassifier(clBrandKitKat);
        st1_shot4.addClassifier(clBrandNUTS);
        st1_shot4.addClassifier(clBrandNesquik);

        Showable st1_shot5 = Shot.getInstance(store1, "РОРМ для кофе NESCAFÉ");
        st1_shot5.setPreview("shop_trad_1_popm_coffee.jpg");
        st1_shot5.setImage("shop_trad_1_popm_coffee.jpg");
        st1_shot5.addClassifier(clStep0);
        st1_shot5.addClassifier(clBrandNescafe);

        Showable st1_shot6 = Shot.getInstance(store1, "Общий вид магазина в Шаге-1");
        st1_shot6.setPreview("shop_trad_1_step1_a_preview.jpg");
        st1_shot6.setImage("shop_trad_1_step1_a.jpg");
        st1_shot6.addClassifier(clStep1);

        Showable st1_shot7 = Shot.getInstance(store1, "Общий вид всех выкладок в Шаге-1");
        st1_shot7.setPreview("shop_trad_1_step1_b_preview.jpg");
        st1_shot7.setImage("shop_trad_1_step1_b.jpg");
        st1_shot7.addClassifier(clStep1);

        Showable st1_shot8 = Shot.getInstance(store1, "Планограмма Магги в дисплеях в Шаге-1");
        st1_shot8.setPreview("shop_trad_1_step1_g_preview.jpg");
        st1_shot8.setImage("shop_trad_1_step1_g.jpg");
        st1_shot8.setComment(" Размещение МАГГИ на стене осуществляется в настенном оборудовании.\nРазмещение на полке – в наполочном дисплее или в шелф-реди боксах.");
        st1_shot8.addClassifier(clStep1);
        st1_shot8.addClassifier(clBranMaggi);

        Showable st1_shot9 = Shot.getInstance(store1, "Планограмма Пурина в Шаге-1");
        st1_shot9.setPreview("shop_trad_1_step1_i_preview.jpg");
        st1_shot9.setImage("shop_trad_1_step1_i.jpg");
        st1_shot9.addClassifier(clStep1);
        st1_shot9.addClassifier(clBranPurina);

        Showable st1_shot10 = Shot.getInstance(store1, "Планограмма шоколадных плиток в Шаге-1");
        st1_shot10.setPreview("shop_trad_1_step1_plangr_tablet_preview.jpg");
        st1_shot10.setImage("shop_trad_1_step1_plangr_tablet.jpg");
        st1_shot10.setComment("Бледно окрашенные SKU не обязательны для размещения на данном Шаге. Показан принцип рекомендованной выкладки в случае расширения ассортимента (витрина, UMD).\n" +
                "Шоколад NESQUIK размещается вне корпоративного блока, рядом с шоколадом KINDER.");
        st1_shot10.addClassifier(clStep1);
        st1_shot10.addClassifier(clBrandRossia);
        st1_shot10.addClassifier(clBrandKitKat);
        st1_shot10.addClassifier(clBrandNesquik);
        st1_shot10.addClassifier(clBrandNUTS);

        Showable st1_shot11 = Shot.getInstance(store1, "Дополнительные POPM в Шаге-1");
        st1_shot11.setPreview("shop_trad_1_step1_popm_1.jpg");
        st1_shot11.setImage("shop_trad_1_step1_popm_1.jpg");
        st1_shot11.addClassifier(clStep1);


        Showable st1_shot12 = Shot.getInstance(store1, "Общий вид магазина в Шаге-2");
        st1_shot12.setPreview("shop_trad_1_step2_a_preview.jpg");
        st1_shot12.setImage("shop_trad_1_step2_a.jpg");
        st1_shot12.addClassifier(clStep2);

        Showable st1_shot13 = Shot.getInstance(store1, "Общий вид всех выкладок в Шаге-2");
        st1_shot13.setPreview("shop_trad_1_step2_b_preview.jpg");
        st1_shot13.setImage("shop_trad_1_step2_b.jpg");
        st1_shot13.addClassifier(clStep2);

        Showable st1_shot14 = Shot.getInstance(store1, "Вид отдельных выкладок в Шаге-2");
        st1_shot14.setPreview("shop_trad_1_step2_c_preview.jpg");
        st1_shot14.setImage("shop_trad_1_step2_c.jpg");
        st1_shot14.setComment("Если в магазине нет витрины для весовых конфет, то продукция размещается в UMD для весовых конфет.");
        st1_shot14.addClassifier(clStep2);



        Showable store2 = store1.cloneMe(parent);
        store2.setName("Традиция номер два упрощенная");
        store2.setComment("");
        store2.setPreview("shop_trad_1_step0_b_preview.jpg");
        store2.clearClassifiers();
        store2.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_1)));
        store2.addClassifier(new ClassifierRegion("Сибирский"));
        store2.addClassifier(new ClassifierRegion("Дальневосточный"));


        Showable store3 = Store.getInstance(parent, "Мини-маркет первый");
        store3.setComment("Comment store-3");
        store3.setPreview("shop_minimarket_1_step3_b_preview.jpg");
        store3.addClassifier(new ClassifierFormat(getResources().getString(R.string.class_format_2)));

        Showable store4 = Store.getInstance(parent, "Store-4");

        Showable store5 = Store.getInstance(parent, "Store-5");
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
