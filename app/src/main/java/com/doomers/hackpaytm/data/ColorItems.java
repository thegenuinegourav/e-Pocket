package com.doomers.hackpaytm.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class ColorItems {

    private static final String KEY_SAVED_COLOR_ITEMS = "Colors.Keys.SAVED_COLOR_ITEMS";
    private static final String KEY_LAST_PICKED_COLOR = "Colors.Keys.LAST_PICKED_COLOR";
    private static final int DEFAULT_LAST_PICKED_COLOR = Color.WHITE;
    private static final Gson GSON = new Gson();

    private static final Type COLOR_ITEM_LIST_TYPE = new TypeToken<List<com.doomers.hackpaytm.data.ColorItem>>() {
    }.getType();

    public static final Comparator<com.doomers.hackpaytm.data.ColorItem> CHRONOLOGICAL_COMPARATOR = new Comparator<com.doomers.hackpaytm.data.ColorItem>() {
        @Override
        public int compare(com.doomers.hackpaytm.data.ColorItem lhs, com.doomers.hackpaytm.data.ColorItem rhs) {
            return (int) (rhs.getId() - lhs.getId());
        }
    };

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Get the last picked color.
     *
     * @param context a {@link android.content.Context}.
     * @return the last picked color.
     */
    public static int getLastPickedColor(Context context) {
        return getPreferences(context).getInt(KEY_LAST_PICKED_COLOR, DEFAULT_LAST_PICKED_COLOR);
    }

    /**
     * Save the last picked color.
     *
     * @param context         a {@link android.content.Context}.
     * @param lastPickedColor the last picked color.
     * @return Returns true if the new color was successfully written to persistent storage.
     */
    public static boolean saveLastPickedColor(Context context, int lastPickedColor) {
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putInt(KEY_LAST_PICKED_COLOR, lastPickedColor);
        return editor.commit();
    }

    public static List<com.doomers.hackpaytm.data.ColorItem> getSavedColorItems(Context context) {
        return getSavedColorItems(getPreferences(context));
    }

    @SuppressWarnings("unchecked")
    public static List<com.doomers.hackpaytm.data.ColorItem> getSavedColorItems(SharedPreferences sharedPreferences) {
        final String jsonColorItems = sharedPreferences.getString(KEY_SAVED_COLOR_ITEMS, "");

        // No saved colors were found.
        // Return an empty list.
        if ("".equals(jsonColorItems)) {
            return Collections.EMPTY_LIST;
        }

        // Parse the json into colorItems.
        final List<com.doomers.hackpaytm.data.ColorItem> colorItems = GSON.fromJson(jsonColorItems, COLOR_ITEM_LIST_TYPE);

        // Sort the color items chronologically.
        Collections.sort(colorItems, CHRONOLOGICAL_COMPARATOR);
        return colorItems;
    }

    public static boolean saveColorItem(Context context, com.doomers.hackpaytm.data.ColorItem colorToSave) {
        if (colorToSave == null) {
            throw new IllegalArgumentException("Can't save a null color.");
        }

        final List<com.doomers.hackpaytm.data.ColorItem> savedColorsItems = getSavedColorItems(context);
        final SharedPreferences.Editor editor = getPreferences(context).edit();
        final List<com.doomers.hackpaytm.data.ColorItem> colorItems = new ArrayList<>(savedColorsItems.size() + 1);

        // Add the saved color items except the one with the same ID. It will be overridden.
        final int size = savedColorsItems.size();
        for (int i = 0; i < size; i++) {
            final com.doomers.hackpaytm.data.ColorItem candidate = savedColorsItems.get(i);
            if (candidate.getId() != colorToSave.getId()) {
                colorItems.add(candidate);
            }
        }

        // Add the new color to save
        colorItems.add(colorToSave);

        editor.putString(KEY_SAVED_COLOR_ITEMS, GSON.toJson(colorItems));

        return editor.commit();
    }

    public static boolean deleteColorItem(Context context, com.doomers.hackpaytm.data.ColorItem colorItemToDelete) {
        if (colorItemToDelete == null) {
            throw new IllegalArgumentException("Can't delete a null color item");
        }

        final SharedPreferences sharedPreferences = getPreferences(context);
        final List<com.doomers.hackpaytm.data.ColorItem> savedColorsItems = getSavedColorItems(sharedPreferences);

        for (Iterator<com.doomers.hackpaytm.data.ColorItem> it = savedColorsItems.iterator(); it.hasNext(); ) {
            final com.doomers.hackpaytm.data.ColorItem candidate = it.next();
            if (candidate.getId() == colorItemToDelete.getId()) {
                it.remove();
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_SAVED_COLOR_ITEMS, GSON.toJson(savedColorsItems));
                return editor.commit();
            }
        }

        return false;
    }

    public static void registerListener(Context context, OnColorItemChangeListener onColorItemChangeListener) {
        getPreferences(context).registerOnSharedPreferenceChangeListener(onColorItemChangeListener);
    }

    public static void unregisterListener(Context context, OnColorItemChangeListener onColorItemChangeListener) {
        getPreferences(context).unregisterOnSharedPreferenceChangeListener(onColorItemChangeListener);
    }

    public abstract static class OnColorItemChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (KEY_SAVED_COLOR_ITEMS.equals(key)) {
                onColorItemChanged(getSavedColorItems(sharedPreferences));
            }
        }

        public abstract void onColorItemChanged(List<com.doomers.hackpaytm.data.ColorItem> colorItems);
    }

    // Non-instantiability
    private ColorItems() {
    }
}
