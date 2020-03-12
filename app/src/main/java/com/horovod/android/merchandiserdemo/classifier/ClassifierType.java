package com.horovod.android.merchandiserdemo.classifier;

import android.content.Context;
import android.content.res.Resources;

import com.horovod.android.merchandiserdemo.R;

public enum ClassifierType {
    FORMAT(R.string.class_desc_format),
    REGION(R.string.class_desc_region),
    CITY_NAME(R.string.class_desc_city_name),
    CITY_TYPE(R.string.class_desc_city_type),
    CHAIN_NAME(R.string.class_desc_chain_name),
    CHAIN_TYPE(R.string.class_desc_chain_type),
    BRAND(R.string.class_desc_brand),
    OTHER(R.string.class_desc_other);

    private int headerRes;

    ClassifierType(int headerRes) {
        this.headerRes = headerRes;
    }

    public int getHeaderRes() {
        return headerRes;
    }

    public String getHeader(Resources resources) {
        String result = resources.getString(this.headerRes);
        return result;
    }



}
