package com.horovod.android.merchandiserdemo.data;

import com.horovod.android.merchandiserdemo.classifier.*;
import com.horovod.android.merchandiserdemo.showable.*;
import com.horovod.android.merchandiserdemo.view.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

    /** REAL APP - В реальном приложении не нужно (?) */
    private static int showableIdNumber = 1;
    private static List<Integer> existingIdShowables = new ArrayList<>();

    //public static Showable storeKeeper;
    private static Map<Integer, Showable> keepersMap = new ConcurrentHashMap<>();

    public static final String photoFolder = "photofolder";

    public static final String INTENT_REPLACE_SHOWABLE = "com.horovod.android.merchandiserdemo.INTENT_REPLACE_SHOWABLE";
    public static final String INTENT_SHOW_PHOTO = "com.horovod.android.merchandiserdemo.INTENT_SHOW_PHOTO";

    public static final String KEY_IDNUMBER = "com.horovod.android.merchandiserdemo.KEY_IDNUMBER";
    public static final String KEY_FILE_NAME = "com.horovod.android.merchandiserdemo.KEY_FILE_NAME";


    public static int maxLengthClassifiersMain = -1;
    public static int maxLengthCommentList = -1;
    public static int minLength = 28;

    public static ShotFragment shotFragment = null;


    /** REAL APP - В реальном приложении не нужно */
    public static int incrementAndGetNewId() {
        showableIdNumber++;
        return showableIdNumber;
    }

    public static int getShowableIdNumber() {
        return showableIdNumber;
    }

    public static List<Showable> getKeepersList() {
        List<Showable> result = new ArrayList<>();
        result.addAll(Data.keepersMap.values());
        return result;
    }

    public static void addKeeper(Showable newKeeper) {
        if (newKeeper != null && ShowableType.STORE_KEEPER == newKeeper.getShowableType()) {
            if (!Data.keepersMap.containsKey(newKeeper.getIdNumber())) {
                Data.keepersMap.put(newKeeper.getIdNumber(), newKeeper);
            }
        }
    }

    public static Showable getShowableByid(int id) {
       Showable result = null;
       for (Showable sh : keepersMap.values()) {
           result = sh.getShowableById(id);
           if (result != null) {
               return result;
           }
       }
       return null;
    }

}
