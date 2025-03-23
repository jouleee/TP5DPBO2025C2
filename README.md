

# TP5DPBO2025C2

## Janji
Saya Julian Dwi Satrio dengan NIM 2300484 mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain Pemrograman Berorientasi Objek untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

## Desain Program  
Aplikasi ini dibuat menggunakan Java Swing dan Java Database Connectivity (JDBC) untuk mengelola data mahasiswa dengan penyimpanan ke database. Fitur utama meliputi:

- **Create**: Menambahkan data mahasiswa baru ke database.  
- **Read**: Menampilkan daftar mahasiswa yang tersimpan dalam database.  
- **Update**: Mengedit data mahasiswa yang sudah ada dalam database.  
- **Delete**: Menghapus data mahasiswa dari database.  

## Desain GUI
Aplikasi memiliki tampilan antarmuka berbasis tabel yang menampilkan daftar mahasiswa yang tersimpan dalam database. Terdapat tombol aksi untuk menambah, mengedit, dan menghapus data mahasiswa.

## Alur Program

1. **Memulai Aplikasi**  
   - Pengguna menjalankan aplikasi.  
   - Jendela utama ditampilkan dengan tabel berisi daftar mahasiswa yang dimuat dari database.  

2. **Menambahkan Mahasiswa**  
   - Form input muncul, pengguna mengisi **NIM, Nama, Jenis Kelamin, Jalur Masuk**.  
   - Pengguna menekan tombol "Add".  
   - Data dikirim ke database dan ditampilkan di tabel.  

3. **Mengedit Data Mahasiswa**  
   - Pengguna memilih mahasiswa dari tabel   
   - Form edit muncul dengan data yang sudah ada.  
   - Pengguna mengubah informasi yang diperlukan dan menekan tombol "Update".  
   - Data diperbarui dalam database dan tabel direfresh.  

4. **Menghapus Mahasiswa**  
   - Pengguna memilih mahasiswa dari tabel dan menekan tombol "Hapus".  
   - Konfirmasi muncul, jika pengguna menyetujui, data dihapus dari database.  
   - Tabel diperbarui untuk mencerminkan perubahan.  

5. **Menampilkan Data Mahasiswa**  
   - Setiap kali aplikasi dijalankan atau setelah CRUD dilakukan, tabel selalu diperbarui dengan data terbaru dari database.  

## Konfigurasi Database
Aplikasi ini menggunakan database MySQL dengan tabel `mahasiswa` yang memiliki struktur berikut:

```sql
CREATE TABLE mahasiswa (
    id int(11) PRIMARY KEY
    nim VARCHAR(225) NOT NULL,
    nama VARCHAR(225) NOT NULL,
    jenis_kelamin VARCHAR(225) NOT NULL,
    jalur_masuk VARCHAR(225) NOT NULL
);
```

Agar aplikasi dapat terhubung dengan database, pastikan Anda mengubah konfigurasi koneksi dalam `Database.java`:

```java
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_mahasiswa", "root", "");
```

## Dokumentasi Program 

https://github.com/user-attachments/assets/a241cf05-5935-4f43-b290-40dbd8289b07
