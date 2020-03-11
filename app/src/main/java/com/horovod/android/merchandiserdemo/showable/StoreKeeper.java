package com.horovod.android.merchandiserdemo.showable;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;

import java.util.ArrayList;
import java.util.List;

public class StoreKeeper implements Showable {

    private List<Showable> showables = new ArrayList<>();

    @Override
    public String getPreview() {
        return null;
    }

    @Override
    public void setPreview(String preview) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public void setComment(String comment) {

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
    public void addClassifier(Classifier classifier) {

    }

    @Override
    public List<Showable> getShowables() {
        return this.showables;
    }

    @Override
    public void addShowable(Showable showable) {
        if (showable != null) {
            this.showables.add(showable);
        }
    }

    @Override
    public Showable getParent() {
        return null;
    }

    @Override
    public void setParent(Showable parent) {

    }
}
