package com.horovod.android.merchandiserdemo.showable;

import android.util.Log;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.ArrayList;
import java.util.List;

public class Store implements Showable {

    private String preview = "";
    private String name = "";
    private String comment = "";
    private List<Classifier> classifiers = new ArrayList<>();

    public Store(String name) {
        this.name = name;
    }

    @Override
    public String getPreview() {
        return this.preview;
    }

    @Override
    public void setPreview(String preview) {
        if (preview != null) {
            this.preview = preview;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(String comment) {
        if (comment != null) {
            this.comment = comment;
        }
    }

    @Override
    public List<Classifier> getClassifiers() {
        return this.classifiers;
    }

    @Override
    public List<Classifier> getClassifiersByType(ClassifierType type) {
        return null;
    }

    @Override
    public List<Showable> getShowables() {
        return null;
    }

    @Override
    public void addClassifier(Classifier classifier) {
        this.classifiers.add(classifier);
    }



}
