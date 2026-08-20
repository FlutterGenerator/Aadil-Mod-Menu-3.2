package com.aadil;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import java.util.LinkedHashSet;
import java.util.Set;

public class Preferences {
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final double DEFAULT_DOUBLE_VALUE = 0.0d;
    private static final float DEFAULT_FLOAT_VALUE = 0.0f;
    private static final int DEFAULT_INT_VALUE = 0;
    private static final long DEFAULT_LONG_VALUE = 0;
    private static final String DEFAULT_STRING_VALUE = null;
    private static final String LENGTH = "_length";
    public static Context context;
    public static boolean isExpanded;
    public static boolean loadPref;
    private static Preferences prefsInstance;
    private static SharedPreferences sharedPreferences;

    public static native void Changes(Context context2, int i, String str, int i2, boolean z, String str2);

    public static void changeFeatureInt(String str, int i, int i2) {
        with(context).writeInt(i, i2);
        Changes(context, i, str, i2, false, null);
    }

    public static void changeFeatureString(String str, int i, String str2) {
        with(context).writeString(i, str2);
        Changes(context, i, str, 0, false, str2);
    }

    public static void changeFeatureBool(String str, int i, boolean z) {
        with(context).writeBoolean(i, z);
        Changes(context, i, str, 0, z, null);
    }

    public static int loadPrefInt(String str, int i) {
        if (loadPref) {
            int readInt = with(context).readInt(i);
            Changes(context, i, str, readInt, false, null);
            return readInt;
        }
        return 0;
    }

    public static boolean loadPrefBool(String str, int i, boolean z) {
        boolean readBoolean = with(context).readBoolean(i, z);
        if (i == -1) {
            loadPref = readBoolean;
        }
        if (i == -3) {
            isExpanded = readBoolean;
        }
        if (!loadPref && i >= 0) {
            readBoolean = z;
        }
        Changes(context, i, str, 0, readBoolean, null);
        return readBoolean;
    }

    public static String loadPrefString(String str, int i) {
        if (loadPref || i <= 0) {
            String readString = with(context).readString(i);
            Changes(context, i, str, 0, false, readString);
            return readString;
        }
        return "";
    }

    Preferences(Context context2) {
        sharedPreferences = context2.getApplicationContext().getSharedPreferences(new StringBuffer().append(context2.getPackageName()).append("_preferences").toString(), 0);
    }

    Preferences(Context context2, String str) {
        sharedPreferences = context2.getApplicationContext().getSharedPreferences(str, 0);
    }

    public static Preferences with(Context context2) {
        if (prefsInstance == null) {
            prefsInstance = new Preferences(context2);
        }
        return prefsInstance;
    }

    public static Preferences with(Context context2, boolean z) {
        if (z) {
            prefsInstance = new Preferences(context2);
        }
        return prefsInstance;
    }

    public static Preferences with(Context context2, String str) {
        if (prefsInstance == null) {
            prefsInstance = new Preferences(context2, str);
        }
        return prefsInstance;
    }

    public static Preferences with(Context context2, String str, boolean z) {
        if (z) {
            prefsInstance = new Preferences(context2, str);
        }
        return prefsInstance;
    }

    public String readString(String str) {
        return sharedPreferences.getString(str, "");
    }

    public String readString(int i) {
        try {
            return sharedPreferences.getString(String.valueOf(i), "");
        } catch (ClassCastException e) {
            return "";
        }
    }

    public String readString(String str, String str2) {
        return sharedPreferences.getString(str, str2);
    }

    public void writeString(String str, String str2) {
        sharedPreferences.edit().putString(str, str2).apply();
    }

    public void writeString(int i, String str) {
        sharedPreferences.edit().putString(String.valueOf(i), str).apply();
    }

    public int readInt(String str) {
        return sharedPreferences.getInt(str, 0);
    }

    public int readInt(int i) {
        try {
            return sharedPreferences.getInt(String.valueOf(i), 0);
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public int readInt(String str, int i) {
        return sharedPreferences.getInt(str, i);
    }

    public void writeInt(String str, int i) {
        sharedPreferences.edit().putInt(str, i).apply();
    }

    public void writeInt(int i, int i2) {
        sharedPreferences.edit().putInt(String.valueOf(i), i2).apply();
    }

    public double readDouble(String str) {
        if (contains(str)) {
            return Double.longBitsToDouble(readLong(str));
        }
        return 0.0d;
    }

    public double readDouble(String str, double d) {
        return !contains(str) ? d : Double.longBitsToDouble(readLong(str));
    }

    public void writeDouble(String str, double d) {
        writeLong(str, Double.doubleToRawLongBits(d));
    }

    public float readFloat(String str) {
        return sharedPreferences.getFloat(str, 0.0f);
    }

    public float readFloat(String str, float f) {
        return sharedPreferences.getFloat(str, f);
    }

    public void writeFloat(String str, float f) {
        sharedPreferences.edit().putFloat(str, f).apply();
    }

    public long readLong(String str) {
        return sharedPreferences.getLong(str, 0L);
    }

    public long readLong(String str, long j) {
        return sharedPreferences.getLong(str, j);
    }

    public void writeLong(String str, long j) {
        sharedPreferences.edit().putLong(str, j).apply();
    }

    public boolean readBoolean(String str) {
        return sharedPreferences.getBoolean(str, false);
    }

    public boolean readBoolean(int i) {
        return sharedPreferences.getBoolean(String.valueOf(i), false);
    }

    public boolean readBoolean(String str, boolean z) {
        return sharedPreferences.getBoolean(str, z);
    }

    public boolean readBoolean(int i, boolean z) {
        try {
            return sharedPreferences.getBoolean(String.valueOf(i), z);
        } catch (ClassCastException e) {
            return z;
        }
    }

    public void writeBoolean(String str, boolean z) {
        sharedPreferences.edit().putBoolean(str, z).apply();
    }

    public void writeBoolean(int i, boolean z) {
        sharedPreferences.edit().putBoolean(String.valueOf(i), z).apply();
    }

    @TargetApi(11)
    public void putStringSet(String str, Set<String> set) {
        if (Build.VERSION.SDK_INT >= 11) {
            sharedPreferences.edit().putStringSet(str, set).apply();
        } else {
            putOrderedStringSet(str, set);
        }
    }

    public void putOrderedStringSet(String str, Set<String> set) {
        int i = 0;
        int readInt = sharedPreferences.contains(new StringBuffer().append(str).append(LENGTH).toString()) ? readInt(new StringBuffer().append(str).append(LENGTH).toString()) : 0;
        writeInt(new StringBuffer().append(str).append(LENGTH).toString(), set.size());
        for (String str2 : set) {
            writeString(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(str).append("[").toString()).append(i).toString()).append("]").toString(), str2);
            i++;
        }
        while (i < readInt) {
            remove(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(str).append("[").toString()).append(i).toString()).append("]").toString());
            i++;
        }
    }

    @TargetApi(11)
    public Set<String> getStringSet(String str, Set<String> set) {
        return Build.VERSION.SDK_INT >= 11 ? sharedPreferences.getStringSet(str, set) : getOrderedStringSet(str, set);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.util.Set<java.lang.String>] */
    /* JADX WARN: Type inference failed for: r8v1, types: [java.util.Set<java.lang.String>] */
    /* JADX WARN: Type inference failed for: r8v2, types: [java.util.LinkedHashSet] */
    public Set<String> getOrderedStringSet(String str, Set<String> set) {
        if (contains(new StringBuffer().append(str).append(LENGTH).toString())) {
            set = new LinkedHashSet<>();
            int readInt = readInt(new StringBuffer().append(str).append(LENGTH).toString());
            if (readInt >= 0) {
                for (int i = 0; i < readInt; i++) {
                    set.add(readString(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(str).append("[").toString()).append(i).toString()).append("]").toString()));
                }
            }
        }
        return set;
    }

    public void remove(String str) {
        int readInt;
        if (contains(new StringBuffer().append(str).append(LENGTH).toString()) && (readInt = readInt(new StringBuffer().append(str).append(LENGTH).toString())) >= 0) {
            sharedPreferences.edit().remove(new StringBuffer().append(str).append(LENGTH).toString()).apply();
            for (int i = 0; i < readInt; i++) {
                sharedPreferences.edit().remove(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(str).append("[").toString()).append(i).toString()).append("]").toString()).apply();
            }
        }
        sharedPreferences.edit().remove(str).apply();
    }

    public boolean contains(String str) {
        return sharedPreferences.contains(str);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}