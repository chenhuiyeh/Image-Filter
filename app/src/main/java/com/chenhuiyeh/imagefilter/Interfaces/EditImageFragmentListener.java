package com.chenhuiyeh.imagefilter.Interfaces;

public interface EditImageFragmentListener {
    void onBrightnessChanged(int val);
    void onSaturationChanged(float val);
    void onContrastChanged(float val);
    void onEditStarted();
    void onEditCompleted();
}
