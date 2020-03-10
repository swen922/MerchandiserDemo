package com.horovod.android.merchandiserdemo.showable;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.List;

public interface Showable {

    public String getPreview();
    public void setPreview(String preview);
    public String getName();
    public void setName(String name);
    public String getComment();
    public void setComment(String comment);
    public List<Classifier> getClassifiers();
    public List<Classifier> getClassifiersByType(ClassifierType type);
    public void addClassifier(Classifier classifier);
    public List<Showable> getShowables();

}
