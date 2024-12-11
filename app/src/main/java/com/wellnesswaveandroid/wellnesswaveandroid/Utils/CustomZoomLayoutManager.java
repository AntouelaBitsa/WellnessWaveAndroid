package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomZoomLayoutManager extends LinearLayoutManager {

    private final float shrinkAmount = 0.15f;
    private final float shrinkDistance = 0.9f;

    public CustomZoomLayoutManager(Context context) {
        super(context, HORIZONTAL, false);
    }


    @Override
    public void onLayoutChildren(@NonNull RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleChildren();
//        float midpoint = getWidth() / 2f;
//        float d1 = shrinkDistance * midpoint;
//
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            if (child != null) {
//                float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f;
//                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
//                float scale = 1f - shrinkAmount * (d / d1);
//                child.setScaleX(scale);
//                child.setScaleY(scale);
//
//                // Optional: Adjust translation to keep items closer together
//                float translationFactor = (1 - scale) * 100; // Adjust for spacing
//                child.setTranslationY(translationFactor);
//            }
//        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
        scaleChildren();
        return scrolled;
    }

    private void scaleChildren() {
        float midpoint = getWidth() / 2f; // Find the horizontal midpoint of the RecyclerView
        float d1 = shrinkDistance * midpoint; // Define the shrinkable distance

        if (getChildCount() == 1){
            View child = getChildAt(0);
            if (child != null){
                // Reset scale and translation for the placeholder
                child.setScaleX(1f);
                child.setScaleY(1f);
                child.setTranslationY(0);
            }
            return;
        }


        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child != null) {
                // Calculate the scale factor based on the distance of the child from the midpoint
                float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = 1f - shrinkAmount * (d / d1);
//                float scale = Math.max(0.85f, 1f - shrinkAmount * (d / d1)); // Limit how small the card can get


                // Apply scaling
                child.setScaleX(scale);
                child.setScaleY(scale);

                // Adjust the translation to keep items visually aligned
                float translationFactor = (1 - scale) * 95; // Adjust spacing dynamically
                child.setTranslationY(translationFactor);
            }
        }
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        if (lp != null) {
            lp.width = (int) (getWidth() * 0.50); // Items occupy 50% of the width
        }
        return true;
    }
}