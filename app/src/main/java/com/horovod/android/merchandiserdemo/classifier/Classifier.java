package com.horovod.android.merchandiserdemo.classifier;

public interface Classifier {

    public ClassifierType getClassifierType();
    public String getName();
    public String getComment();

}
