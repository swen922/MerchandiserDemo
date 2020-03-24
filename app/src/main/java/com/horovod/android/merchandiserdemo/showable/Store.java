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
    private int previewWidth = -1;
    private int previewHeight = -1;
    private boolean isVisible = true;
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
    public int getPreviewWidth() {
        return this.previewWidth;
    }

    @Override
    public void setPreviewWidth(int width) {
        this.previewWidth = width;
    }

    @Override
    public int getPreviewHeight() {
        return this.previewHeight;
    }

    @Override
    public void setPreviewHeight(int height) {
        this.previewHeight = height;
    }

    @Override
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void setVisible(boolean show) {
        this.isVisible = show;
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
    public void setClassifiers(List<Classifier> list) {
        if (list != null) {
            this.classifiers = list;
        }
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
        if (!hasEqualClassifier(classifier)) {
            this.classifiers.add(classifier);
        }
    }

    @Override
    public void clearClassifiers() {
        this.classifiers.clear();
    }

    @Override
    public List<Showable> getShowables() {
        return this.showables;
    }

    @Override
    public Showable getShowableById(int id) {
        if (id == this.idNumber) {
            return this;
        }
        if (!this.showables.isEmpty()) {
            Showable result = null;
            for (Showable sh : showables) {
                result = sh.getShowableById(id);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    @Override
    public void setShowables(List<Showable> list) {
        if (list != null) {
            this.showables = list;
        }
    }

    @Override
    public void addShowable(Showable showable) {
        if (showable != null) {
            this.showables.add(showable);
        }
    }

    @Override
    public void clearShowables() {
        this.showables.clear();
    }

    @Override
    public Showable getParent() {
        return this.parent;
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

    @Override
    public Showable cloneMe(Showable newParent) {
        Showable result = Store.getInstance(newParent, this.name);
        result.setPreview(this.preview);
        result.setComment(this.comment);
        List<Classifier> newListClass = new ArrayList<>();
        for (Classifier cl : this.classifiers) {
            newListClass.add(cl.clonMe());
        }
        result.setClassifiers(newListClass);
        List<Showable> newListShow = new ArrayList<>();
        for (Showable sh : this.showables) {
            newListShow.add(sh.cloneMe(result));
        }
        result.setShowables(newListShow);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Showable ");
        sb.append(this.name).append(", id = ").append(this.idNumber);
        return sb.toString();
    }

    private boolean hasEqualClassifier(Classifier classifier) {
        for (Classifier cl : classifiers) {
            if (cl.equals(classifier)) {
                return true;
            }
        }
        return false;
    }
}
