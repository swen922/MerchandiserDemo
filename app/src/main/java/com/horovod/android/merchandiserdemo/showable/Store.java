package com.horovod.android.merchandiserdemo.showable;

import android.util.Log;

import com.horovod.android.merchandiserdemo.classifier.Classifier;
import com.horovod.android.merchandiserdemo.classifier.ClassifierType;
import com.horovod.android.merchandiserdemo.data.Data;

import java.util.ArrayList;
import java.util.List;

public class Store implements Showable {

    private int idNumber = -1;
    private String preview = "";
    private String name = "";
    private String comment = "";
    private List<Classifier> classifiers = new ArrayList<>();
    private List<Showable> showables = new ArrayList<>();
    private Showable parent;

    private Store() {
    }

    public static Store getInstance(Showable parent, String name) {
        Store store = new Store();
        store.setIdNumber(Data.incrementAndGetNewId());
        store.setName(name);
        store.setParent(parent); // Этим действием сразу и добавляем магазин в список (см. метод setParent() ---> parent.addShowable())
        return store;
    }

    @Override
    public int getIdNumber() {
        return this.idNumber;
    }

    @Override
    public ShowableType getShowableType() {
        return ShowableType.STORE;
    }

    @Override
    public void setIdNumber(int newIdNumber) {
        if (newIdNumber > 0) {
            this.idNumber = newIdNumber;
        }
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
    public String getImage() {
        return null;
    }

    @Override
    public void setImage(String image) {

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
        List<Classifier> result = new ArrayList<>();
        for (Classifier cl : this.classifiers) {
            if (cl.getClassifierType() == type) {
                result.add(cl);
            }
        }
        return result;
    }

    @Override
    public void addClassifier(Classifier classifier) {
        if (classifier != null) {
            this.classifiers.add(classifier);
        }
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
        if (parent != null) {
            this.parent = parent;
            if (!this.parent.getShowables().contains(this)) {
                this.parent.addShowable(this);
            }
        }
    }

}
