package com.example.bagisampah;

public class List_Sampah {
    private String img, nama, deskripsi, kategori, latloc, longloc, harga, status, jarak, alamat, user, namaUser, nomorTelepon, key;

    public List_Sampah(String img, String nama, String deskripsi, String kategori, String latloc, String longloc, String harga, String status, String jarak, String alamat, String user, String namaUser, String nomorTelepon) {
        this.img = img;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.latloc = latloc;
        this.longloc = longloc;
        this.harga = harga;
        this.status = status;
        this.jarak = jarak;
        this.alamat = alamat;
        this.user = user;
        this.namaUser = namaUser;
        this.nomorTelepon = nomorTelepon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List_Sampah(String img, String nama, String deskripsi, String kategori, String latloc, String longloc, String harga, String status, String jarak, String alamat, String user, String namaUser, String nomorTelepon, String key) {
        this.img = img;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.latloc = latloc;
        this.longloc = longloc;
        this.harga = harga;
        this.status = status;
        this.jarak = jarak;
        this.alamat = alamat;
        this.user = user;
        this.namaUser = namaUser;
        this.nomorTelepon = nomorTelepon;
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getLatloc() {
        return latloc;
    }

    public void setLatloc(String latloc) {
        this.latloc = latloc;
    }

    public String getLongloc() {
        return longloc;
    }

    public void setLongloc(String longloc) {
        this.longloc = longloc;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }
}
