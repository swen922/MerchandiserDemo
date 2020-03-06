package com.horovod.android.merchandiserdemo.showable;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.List;

public class Store implements Showable {

    private String preview;
    private String name;

    public Store(String name) {
        this.name = name;
    }

    @Override
    public String getPreview() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public List<Classifier> getClassifiers() {
        return null;
    }

    @Override
    public List<Classifier> getClassifiersByType(ClassifierType type) {
        return null;
    }

    @Override
    public List<Showable> getShowables() {
        return null;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setName(String name) {
        this.name = name;
    }
}
