package com.example.bagisampah;

import android.graphics.Bitmap;
import android.net.Uri;

public class DataBagiSampah {
    private static String namaSampah, deskripsiSampah, kategoriSampah, alamatSampah, hargaSampah;
    private static Uri imgSampahUri;
    private static Bitmap imgSampah;


    public static String getNamaSampah() {
        return namaSampah;
    }

    public static void setNamaSampah(String namaSampah) {
        DataBagiSampah.namaSampah = namaSampah;
    }

    public static String getDeskripsiSampah() {
        return deskripsiSampah;
    }

    public static void setDeskripsiSampah(String deskripsiSampah) {
        DataBagiSampah.deskripsiSampah = deskripsiSampah;
    }

    public static String getKategoriSampah() {
        return kategoriSampah;
    }

    public static void setKategoriSampah(String kategoriSampah) {
        DataBagiSampah.kategoriSampah = kategoriSampah;
    }

    public static String getAlamatSampah() {
        return alamatSampah;
    }

    public static void setAlamatSampah(String alamatSampah) {
        DataBagiSampah.alamatSampah = alamatSampah;
    }

    public static String getHargaSampah() {
        return hargaSampah;
    }

    public static void setHargaSampah(String hargaSampah) {
        DataBagiSampah.hargaSampah = hargaSampah;
    }

    public static Uri getImgSampahUri() {
        return imgSampahUri;
    }

    public static void setImgSampahUri(Uri imgSampahUri) {
        DataBagiSampah.imgSampahUri = imgSampahUri;
    }

    public static Bitmap getImgSampah() {
        return imgSampah;
    }

    public static void setImgSampah(Bitmap imgSampah) {
        DataBagiSampah.imgSampah = imgSampah;
    }
}
