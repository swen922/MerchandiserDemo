package com.horovod.android.merchandiserdemo.data;

import com.horovod.android.merchandiserdemo.classifier.*;
import com.horovod.android.merchandiserdemo.showable.*;
import com.horovod.android.merchandiserdemo.showable.StoreKeeper;

import java.util.ArrayList;
import java.util.List;

public class Data {

    /** REAL APP - В реальном приложении не нужно (?) */
    private static int showableIdNumber = 1;
    private static List<Integer> existingIdShowables = new ArrayList<>();

    //public static Showable storeKeeper;

    public static final String photoFolder = "photofolder";

    public static final String INTENT_REPLACE_SHOWABLE = "com.horovod.android.merchandiserdemo.INTENT_REPLACE_SHOWABLE";

    public static final String KEY_IDNUMBER = "com.horovod.android.merchandiserdemo.KEY_IDNUMBER";

    public static int maxLengthClassifiersMain = -1;
    public static int maxLengthCommentList = -1;
    public static int minLength = 28;


    /** REAL APP - В реальном приложении не нужно */
    public static int incrementAndGetNewId() {
        showableIdNumber++;
        return showableIdNumber;
    }

    public static int getShowableIdNumber() {
        return showableIdNumber;
    }
}
