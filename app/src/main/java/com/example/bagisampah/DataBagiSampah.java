package com.example.bagisampah;

import android.graphics.Bitmap;
import android.net.Uri;

public class DataBagiSampah {
    private static String namaSampah;
    private static String deskripsiSampah;
    private static String kategoriSampah;
    private static String alamatSampah;
    private static String hargaSampah;
    private static String latLoc;


    public static String getLatLoc() {
        return latLoc;
    }

    public static void setLatLoc(String latLoc) {
        DataBagiSampah.latLoc = latLoc;
    }

    public static String getLongLoc() {
        return longLoc;
    }

    public static void setLongLoc(String longLoc) {
        DataBagiSampah.longLoc = longLoc;
    }

    private static String longLoc;
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

    public static void setNullAll (){
        setNamaSampah(null);
        setAlamatSampah(null);
        setHargaSampah(null);
        setDeskripsiSampah(null);
        setImgSampah(null);
        setAlamatSampah(null);
        setLatLoc(null);
        setLongLoc(null);
    }

}

