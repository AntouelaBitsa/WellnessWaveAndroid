package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.graphics.drawable.Drawable;

public class SpecializationSet {

    private String specializationType;
    private Drawable specializationIcon;

    public SpecializationSet(String specializationType, Drawable specializationIcon) {
        this.specializationType = specializationType;
        this.specializationIcon = specializationIcon;
    }

    public String getSpecializationType() {
        return specializationType;
    }

    public void setSpecializationType(String specializationType) {
        this.specializationType = specializationType;
    }

    public Drawable getSpecializationIcon() {
        return specializationIcon;
    }

    public void setSpecializationIcon(Drawable specializationIcon) {
        this.specializationIcon = specializationIcon;
    }
}
