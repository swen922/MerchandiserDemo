package com.horovod.android.merchandiserdemo.showable;

import android.content.res.Resources;

import com.horovod.android.merchandiserdemo.R;

public enum ShowableType {

    STORE_KEEPER(R.string.show_desc_storekeeper),
    STORE(R.string.show_desc_store),
    SHOT(R.string.show_desc_shot);

    private int headerRes;

    ShowableType(int headerRes) {
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
