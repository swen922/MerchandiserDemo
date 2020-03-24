package com.horovod.android.merchandiserdemo.classifier;

import java.util.Objects;

public class ClassifierChainName implements Classifier {

    private String name = "";
    private String comment = "";

    public ClassifierChainName(String name) {
        this.name = name;
    }

    @Override
    public ClassifierType getClassifierType() {
        return ClassifierType.CHAIN_NAME;
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
        Classifier clone = new ClassifierChainName(this.name);
        clone.setComment(this.comment);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassifierChainName that = (ClassifierChainName) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getComment());
    }
}
