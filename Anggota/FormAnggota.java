package gui;

import dao.AnggotaDao;
import exception.AppException;
import exception.ValidationException;
import model.Anggota;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormAnggota extends BaseForm {

    private final AnggotaDao dao = new AnggotaDao();

    private JTextField txtNama;
    private JTextField txtNim;
    private JTextField txtAlamat;
    private JTextField txtNoHp;

    private JTextField txtCari;

    private GenericTableModel<Anggota> model;
    private JTable tabel;

    private int idTerpilih;

    public FormAnggota() {
        super("Data Anggota", 720, 440);
        muatTabel();
    }

    @Override
    protected void susunKomponen() {

        txtNama = new JTextField();
        txtNim = new JTextField();
        txtAlamat = new JTextField();
        txtNoHp = new JTextField();

        String[] label = {
                "Nama",
                "NIM",
                "Alamat",
                "No HP"
        };

        JComponent[] isian = {
                txtNama,
                txtNim,
                txtAlamat,
                txtNoHp
        };

        String[] kolom = {
                "ID",
                "Nama",
                "NIM",
                "Alamat",
                "No HP"
        };

        model = new GenericTableModel<>(
                kolom,
                a -> new Object[]{
                        a.getId(),
                        a.getNama(),
                        a.getNim(),
                        a.getAlamat(),
                        a.getNoHp()
                }
        );

        tabel = new JTable(model);

        tabel.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabel.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabel.getSelectedRow() >= 0) {

                int baris =
                        tabel.convertRowIndexToModel(
                                tabel.getSelectedRow()
                        );

                pilihBaris(model.baris(baris));
            }
        });

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

        JPanel atas = new JPanel(new BorderLayout());

        atas.add(
                panelIsian(
                        "Data Anggota",
                        label,
                        isian
                ),
                BorderLayout.CENTER
        );

        atas.add(
                panelTombol,
                BorderLayout.SOUTH
        );

        txtCari = new JTextField(20);

        JButton btnCari =
                new JButton("Cari");

        JButton btnSemua =
                new JButton("Tampilkan Semua");

        JPanel panelCari = new JPanel();

        panelCari.add(
                new JLabel("Cari Anggota:")
        );

        panelCari.add(txtCari);
        panelCari.add(btnCari);
        panelCari.add(btnSemua);

        btnCari.addActionListener(
                e -> cariAnggota()
        );

        txtCari.addActionListener(
                e -> cariAnggota()
        );

        btnSemua.addActionListener(e -> {
            txtCari.setText("");
            muatTabel();
        });

        JPanel panelUtama = new JPanel(
                new BorderLayout(5, 5)
        );

        panelUtama.add(
                atas,
                BorderLayout.CENTER
        );

        panelUtama.add(
                panelCari,
                BorderLayout.SOUTH
        );

        setLayout(
                new BorderLayout(8, 8)
        );

        add(panelUtama, BorderLayout.NORTH);
        add(
                new JScrollPane(tabel),
                BorderLayout.CENTER
        );
    }

    private void muatTabel() {

        java.util.List<Anggota> daftar =
                new java.util.ArrayList<>();

        Anggota a1 = new Anggota(
                "Riezer Andrian Desta",
                "6304250081",
                "Bengkalis",
                "081234567890"
        );
        a1.setId(1);

        Anggota a2 = new Anggota(
                "Muhammad Firdaus",
                "6304250054",
                "Dumai",
                "082345678901"
        );
        a2.setId(2);

        Anggota a3 = new Anggota(
                "Ahmad Aidil",
                "6304250063",
                "Pekanbaru",
                "083456789012"
        );
        a3.setId(3);

        daftar.add(a1);
        daftar.add(a2);
        daftar.add(a3);

        model.isiUlang(daftar);
    }

    private void cariAnggota() {

        String kataKunci =
                txtCari.getText()
                        .trim()
                        .toLowerCase();

        try {

            List<Anggota> daftar =
                    dao.ambilSemua();

            daftar.removeIf(a ->
                    !a.getNama()
                            .toLowerCase()
                            .contains(kataKunci)
                            &&
                            !a.getNim()
                                    .toLowerCase()
                                    .contains(kataKunci)
            );

            model.isiUlang(daftar);

        } catch (AppException e) {
            tampilkanKesalahan(e);
        }
    }

    private void kosongkan() {

        idTerpilih = 0;

        txtNama.setText("");
        txtNim.setText("");
        txtAlamat.setText("");
        txtNoHp.setText("");

        tabel.clearSelection();
    }

    private void pilihBaris(
            Anggota a) {

        idTerpilih = a.getId();

        txtNama.setText(a.getNama());
        txtNim.setText(a.getNim());
        txtAlamat.setText(a.getAlamat());
        txtNoHp.setText(a.getNoHp());
    }

    private Anggota dariIsian() {

        Anggota a = new Anggota(
                txtNama.getText().trim(),
                txtNim.getText().trim(),
                txtAlamat.getText().trim(),
                txtNoHp.getText().trim()
        );

        a.setId(idTerpilih);

        return a;
    }

    private void simpan() {

        try {

            Anggota a = dariIsian();

            dao.simpan(a);

            pesan("Data anggota berhasil disimpan.");

            kosongkan();
            muatTabel();

        } catch (ValidationException e) {

            tampilkanKesalahan(e);

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    private void ubah() {

        if (idTerpilih == 0) {
            pesan("Pilih data terlebih dahulu.");
            return;
        }

        try {

            if (dao.ubah(dariIsian())) {

                pesan("Data berhasil diubah.");

                kosongkan();
                muatTabel();
            }

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    private void hapus() {

        if (idTerpilih == 0) {
            pesan("Pilih data terlebih dahulu.");
            return;
        }

        if (!konfirmasi(
                "Yakin ingin menghapus data ini?")) {
            return;
        }

        try {

            dao.hapus(idTerpilih);

            pesan("Data berhasil dihapus.");

            kosongkan();
            muatTabel();

        } catch (AppException e) {

            tampilkanKesalahan(e);
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
                new FormAnggota().setVisible(true)
        );
    }
}