package com.example.bagisampah.Model;

public class DataFilter {
    private static String maxJarak;
    private static String maxHarga;
    private static Boolean filtered=false;

    public static String getMaxJarak() {
        return maxJarak;
    }

    public static void setMaxJarak(String maxJarak) {
        DataFilter.maxJarak = maxJarak;
    }

    public static String getMaxHarga() {
        return maxHarga;
    }

    public static Boolean getFiltered() {
        return filtered;
    }

    public static void setFiltered(Boolean filtered) {
        DataFilter.filtered = filtered;
    }

    public static void setMaxHarga(String maxHarga) {
        DataFilter.maxHarga = maxHarga;
    }

    public static void setNullAll(){
        setMaxHarga(null);
        setMaxJarak(null);
        setFiltered(false);
    }
}
