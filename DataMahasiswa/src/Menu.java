import com.mysql.cj.protocol.Resultset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JFrame{
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(480, 560);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;

    private Database database;

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JComboBox jalurMasukcomboBox;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        database = new Database();

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] jeniskelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel((new DefaultComboBoxModel(jeniskelaminData)));

        String[] jalurmasukData = {"", "SNBP", "SNBT", "Seleksi Mandiri", "SMM PTN Barat"};
        jalurMasukcomboBox.setModel((new DefaultComboBoxModel(jalurmasukData)));

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex == -1){
                    insertData();
                }
                else{
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedIndex >= 0){
                    deleteData();
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedJalurMasuk = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);
                jalurMasukcomboBox.setSelectedItem(selectedJalurMasuk);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Jalur Masuk"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try{
            ResultSet resultset = database.selectQuery("SELECT * FROM mahasiswa");

            int i = 0;
            while(resultset.next()){
                Object[] row = new Object[5];

                row[0] = i+1;
                row[1] = resultset.getString("nim");
                row[2] = resultset.getString("nama");
                row[3] = resultset.getString("jenis_kelamin");
                row[4] = resultset.getString("jalur_masuk");

                temp.addRow(row);
                i++;
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return temp; // return juga harus diganti
    }

    public void insertData() {
        // Ambil value dari textfield dan combobox
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jalurMasuk = jalurMasukcomboBox.getSelectedItem().toString();

        // Pastikan semua field tidak kosong
        if (!nim.isEmpty() && !nama.isEmpty() && !jenisKelamin.isEmpty() && !jalurMasuk.isEmpty()) {
            try {
                // Periksa apakah NIM sudah ada di database
                String checkQuery = "SELECT nim FROM mahasiswa WHERE nim = '" + nim + "';";
                ResultSet resultSet = database.selectQuery(checkQuery);

                if (resultSet.next()) {
                    // Jika NIM sudah ada, tampilkan pesan error
                    JOptionPane.showMessageDialog(null, "NIM sudah terdaftar! Silakan gunakan NIM lain.");
                } else {
                    // Jika NIM belum ada, masukkan data baru
                    String sql = "INSERT INTO mahasiswa VALUES (null, '" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + jalurMasuk + "');";
                    database.insertUpdateDeleteQuery(sql);

                    // Update tabel
                    mahasiswaTable.setModel(setTable());

                    // Bersihkan form
                    clearForm();

                    // Feedback
                    JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menyimpan data: " + e.getMessage());
            }
        } else {
            // Tampilkan pesan jika ada field yang kosong
            JOptionPane.showMessageDialog(null, "Mohon isi semua field yang diperlukan sebelum melanjutkan!");
        }
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jalurMasuk = jalurMasukcomboBox.getSelectedItem().toString();

        // Mengecek apakah terdapat input field/box yang belum di isi
        if (!nim.isEmpty() && !nama.isEmpty() && !jenisKelamin.isEmpty() && !jalurMasuk.isEmpty()) {
            // ubah data mahasiswa di database
            String sql = "UPDATE mahasiswa SET nama = '" + nama + "', jenis_kelamin = '" + jenisKelamin +
                    "', jalur_masuk = '" + jalurMasuk + "' WHERE nim = '" + nim + "';";
            database.insertUpdateDeleteQuery(sql);

            // update tabel
            mahasiswaTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil!");
            JOptionPane.showMessageDialog(null, "Data berhasil dirubah!");
        }
        else { // Apabila terdapat input field/box yang kosong
            // feedback
            System.out.println("Update gagal! terdapat input field/box yang masih kosong");
            JOptionPane.showMessageDialog(null, "Mohon isi semua field yang diperlukan sebelum melanjutkan!");
        }
    }
//
    public void deleteData() {
        // Tampilkan konfirmasi sebelum menghapus
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Yakin menghapus data ini?",
                "Konfirmasi Penghapusan",
                JOptionPane.YES_NO_OPTION
        );

        // Jika pengguna memilih "Yes", hapus data
        if (confirm == JOptionPane.YES_OPTION) {
            String nim = nimField.getText();
            // hapus data dari list
            String sql = "DELETE FROM mahasiswa WHERE nim = '" + nim + "';";
            database.insertUpdateDeleteQuery(sql);
            // update tabel
            mahasiswaTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Delete Berhasil!");
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus!");
        }
    }


    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");
        jalurMasukcomboBox.setSelectedItem("");

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }
}
