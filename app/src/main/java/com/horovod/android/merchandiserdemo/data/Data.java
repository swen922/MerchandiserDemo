package com.horovod.android.merchandiserdemo.data;

import com.horovod.android.merchandiserdemo.classifier.*;
import com.horovod.android.merchandiserdemo.showable.*;
import com.horovod.android.merchandiserdemo.showable.StoreKeeper;

import java.util.ArrayList;
import java.util.List;

public class Data {

    //public static List<Showable> listStores = new ArrayList<>();

    public static Showable storeKeeper;

    public static final String photoFolder = "photofolder";

    public static void addStore(Showable showable) {
        storeKeeper.addShowable(showable);
    }

}
