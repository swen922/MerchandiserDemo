package com.horovod.android.merchandiserdemo.showable;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.List;

public interface Showable {

    public String getPreview();
    public String getName();
    public String getComment();
    public List<Classifier> getClassifiers();
    public List<Classifier> getClassifiersByType(ClassifierType type);
    public List<Showable> getShowables();

}
