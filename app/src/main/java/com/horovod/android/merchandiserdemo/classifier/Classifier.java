package com.horovod.android.merchandiserdemo.classifier;

public interface Classifier {

    public ClassifierType getClassifierType();
    public String getName();
    public void setName(String name);
    public String getComment();
    public void setComment(String comment);
    public Classifier clonMe();

}
