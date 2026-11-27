
package Gui;

import dao.KategoriDao;
import exception.AppException;
import exception.ValidationException;
import Model.Kategori;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.util.List;

public class FormKategori extends BaseForm {

    private final KategoriDao dao = new KategoriDao();

    private JTextField txtKategoriId;
    private JTextField txtNama;
    private JTextField txtKeterangan;
    private JTextField txtBarang;

    // Komponen pencarian
    private JTextField txtCari;

    private GenericTableModel<Kategori> model;
    private JTable tabel;

    // ID database dan ID kategori
    private int idTerpilih;
    private String kategoriIdTerpilih;

    public FormKategori() {
        super("SiTiket - Data Kategori", 720, 440);
        muatTabel();
    }

    @Override
    protected void susunKomponen() {

        txtKategoriId = new JTextField();
        txtNama = new JTextField();
        txtKeterangan = new JTextField();
        txtBarang = new JTextField();

        String[] label = {
                "ID Kategori",
                "Nama",
                "Keterangan",
                "Barang"
        };

        JComponent[] isian = {
                txtKategoriId,
                txtNama,
                txtKeterangan,
                txtBarang
        };

        String[] kolom = {
                "ID Kategori",
                "Nama",
                "Keterangan",
                "Barang"
        };

        model = new GenericTableModel<>(
                kolom,
                k -> new Object[]{
                        k.getKategoriId(),
                        k.getNama(),
                        k.getKeterangan(),
                        k.getBarang()
                }
        );

        tabel = new JTable(model);

        tabel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabel.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabel.getSelectedRow() >= 0) {

                int baris = tabel.convertRowIndexToModel(
                        tabel.getSelectedRow()
                );

                pilihBaris(model.baris(baris));
            }
        });

        // TOMBOL CRUD
        JButton btnBaru = new JButton("Baru");
        JButton btnSimpan = new JButton("Simpan");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");

        btnBaru.addActionListener(e -> kosongkan());
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());

        JPanel panelTombol = new JPanel();

        panelTombol.add(btnBaru);
        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);

        // FORM ISIAN
        JPanel atas = new JPanel(new BorderLayout());

        atas.add(
                panelIsian("Isian Kategori", label, isian),
                BorderLayout.CENTER
        );

        atas.add(panelTombol, BorderLayout.SOUTH);

        // PANEL PENCARIAN
        txtCari = new JTextField(20);

        JButton btnCari = new JButton("Cari");
        JButton btnTampilkanSemua =
                new JButton("Tampilkan Semua");

        JPanel panelCari = new JPanel();

        panelCari.add(new JLabel("Cari Kategori:"));
        panelCari.add(txtCari);
        panelCari.add(btnCari);
        panelCari.add(btnTampilkanSemua);

        btnCari.addActionListener(e -> cariKategori());

        txtCari.addActionListener(e -> cariKategori());

        btnTampilkanSemua.addActionListener(e -> {
            txtCari.setText("");
            muatTabel();
        });

        // PANEL UTAMA
        JPanel panelUtama = new JPanel(new BorderLayout(5, 5));

        panelUtama.add(atas, BorderLayout.CENTER);
        panelUtama.add(panelCari, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 8));

        add(panelUtama, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
    }

    // MEMUAT DATA KATEGORI
    private void muatTabel() {

        try {

            List<Kategori> daftar = dao.ambilSemua();

            model.isiUlang(daftar);

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    // MENCARI KATEGORI
    private void cariKategori() {

        String kataKunci = txtCari.getText()
                .trim()
                .toLowerCase();

        try {

            List<Kategori> daftar = dao.ambilSemua();

            daftar.removeIf(k ->
                    !k.getKategoriId().toLowerCase()
                            .contains(kataKunci)
                            &&
                            !k.getNama().toLowerCase()
                                    .contains(kataKunci)
            );

            model.isiUlang(daftar);

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    // MENGOSONGKAN FORM
    private void kosongkan() {

        idTerpilih = 0;
        kategoriIdTerpilih = null;

        txtKategoriId.setText("");
        txtNama.setText("");
        txtKeterangan.setText("");
        txtBarang.setText("");

        tabel.clearSelection();

        txtKategoriId.requestFocus();
    }

    // MEMILIH DATA DARI TABEL
    private void pilihBaris(Kategori k) {

        idTerpilih = k.getId();
        kategoriIdTerpilih = k.getKategoriId();

        txtKategoriId.setText(k.getKategoriId());
        txtNama.setText(k.getNama());
        txtKeterangan.setText(k.getKeterangan());
        txtBarang.setText(k.getBarang());
    }

    // MENGAMBIL ISIAN FORM MENJADI OBJEK
    private Kategori dariIsian() {

        Kategori k = new Kategori(
                txtKategoriId.getText().trim(),
                txtNama.getText().trim(),
                txtKeterangan.getText().trim(),
                txtBarang.getText().trim()
        );

        k.setId(idTerpilih);

        return k;
    }

    // MENYIMPAN DATA
    private void simpan() {

        try {

            Kategori k = dariIsian();

            k.validasi();

            k.setId(0);

            dao.simpan(k);

            pesan("Data kategori berhasil disimpan.");

            kosongkan();
            muatTabel();

        } catch (ValidationException e) {

            tampilkanKesalahan(e);

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    // MENGUBAH DATA
    private void ubah() {

        if (idTerpilih == 0) {

            pesan("Pilih dulu satu baris di tabel.");
            return;
        }

        try {

            Kategori k = dariIsian();

            k.validasi();

            k.setId(idTerpilih);

            if (dao.ubah(k)) {

                pesan("Perubahan kategori berhasil disimpan.");

                kosongkan();
                muatTabel();

            } else {

                pesan("Tidak ada baris yang berubah.");
            }

        } catch (ValidationException e) {

            tampilkanKesalahan(e);

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    // MENGHAPUS DATA
    private void hapus() {

        if (idTerpilih == 0) {

            pesan("Pilih dulu satu baris di tabel.");
            return;
        }

        if (!konfirmasi(
                "Hapus kategori dengan ID "
                        + kategoriIdTerpilih + "?")) {

            return;
        }

        try {

            if (dao.hapus(idTerpilih)) {

                pesan("Satu kategori berhasil dihapus.");

                kosongkan();
                muatTabel();

            } else {

                pesan("Tidak ada baris yang terhapus.");
            }

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    // MENJALANKAN FORM
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new FormKategori().setVisible(true);

        });
    }
}
