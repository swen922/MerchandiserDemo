package com.horovod.android.merchandiserdemo.classifier;

public class ClassifierCityType implements Classifier {

    private String name = "";
    private String comment = "";

    public ClassifierCityType(String name) {
        this.name = name;
    }

    @Override
    public ClassifierType getClassifierType() {
        return ClassifierType.CITY_TYPE;
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
    public Classifier clonMe() {
        Classifier clone = new ClassifierCityType(this.name);
        clone.setComment(this.comment);
        return clone;
    }
}
