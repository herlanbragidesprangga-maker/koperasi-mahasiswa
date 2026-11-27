package gui;

import dao.PenjualanDao;
import exception.AppException;
import exception.ValidationException;
import model.Penjualan;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.util.List;

/** Form modul Penjualan. Pemilik: Herlan Bragi DesPrangga (Ketua Tim). */
public class FormPenjualan extends BaseForm {

    private final PenjualanDao dao = new PenjualanDao();

    // JANGAN diberi nilai awal di sini: BaseForm memanggil susunKomponen()
    // dari konstruktornya, sebelum baris deklarasi kelas ini dijalankan.
    private JTextField txtBarang;
    private JTextField txtAnggota;
    private JTextField txtTanggal;
    private JTextField txtJumlah;
    private JTextField txtTotal;

    private GenericTableModel<Penjualan> model;
    private JTable tabel;
    private int idTerpilih;

    public FormPenjualan() {
        super("Koperasi Mahasiswa - Data Penjualan", 760, 460);
        muatTabel();
    }

    @Override
    protected void susunKomponen() {
        txtBarang = new JTextField();
        txtAnggota = new JTextField();
        txtTanggal = new JTextField();
        txtJumlah = new JTextField();
        txtTotal = new JTextField();

        String[] label = {"Id barang", "Id anggota", "Tanggal (yyyy-MM-dd)", "Jumlah", "Total"};
        JComponent[] isian = {txtBarang, txtAnggota, txtTanggal, txtJumlah, txtTotal};

        String[] kolom = {"Barang", "Anggota", "Tanggal", "Jumlah", "Total"};

        model = new GenericTableModel<Penjualan>(kolom,
                p -> new Object[]{p.getBarangId(), p.getAnggotaId(),
                        p.getTanggal(), p.getJumlah(), p.getTotal()});
        tabel = new JTable(model);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabel.getSelectedRow() >= 0) {
                pilihBaris(model.baris(tabel.getSelectedRow()));
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
        atas.add(panelIsian("Isian Penjualan", label, isian), BorderLayout.CENTER);
        atas.add(panelTombol, BorderLayout.SOUTH);

        setLayout(new BorderLayout(8, 8));
        add(atas, BorderLayout.NORTH);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
    }

    private void muatTabel() {
        try {
            List<Penjualan> daftar = dao.ambilSemua();
            model.isiUlang(daftar);
        } catch (AppException e) {
            tampilkanKesalahan(e);
        }
    }

    private void kosongkan() {
        idTerpilih = 0;
        txtBarang.setText("");
        txtAnggota.setText("");
        txtTanggal.setText("");
        txtJumlah.setText("");
        txtTotal.setText("");
        tabel.clearSelection();
        txtBarang.requestFocus();
    }

    private void pilihBaris(Penjualan p) {
        idTerpilih = p.getId();
        txtBarang.setText(String.valueOf(p.getBarangId()));
        txtAnggota.setText(String.valueOf(p.getAnggotaId()));
        txtTanggal.setText(p.getTanggal());
        txtJumlah.setText(String.valueOf(p.getJumlah()));
        txtTotal.setText(String.valueOf(p.getTotal()));
    }

    /** Membaca bilangan bulat dari isian. Salah ketik menjadi ValidationException. */
    private int bacaInt(JTextField isian, String namaField) throws ValidationException {
        String teks = isian.getText().trim();
        if (teks.isEmpty()) {
            throw new ValidationException(namaField, "tidak boleh kosong");
        }
        try {
            return Integer.parseInt(teks);
        } catch (NumberFormatException e) {
            throw new ValidationException(namaField, "harus berupa bilangan bulat");
        }
    }

    private double bacaDesimal(JTextField isian, String namaField) throws ValidationException {
        String teks = isian.getText().trim();
        if (teks.isEmpty()) {
            throw new ValidationException(namaField, "tidak boleh kosong");
        }
        try {
            return Double.parseDouble(teks);
        } catch (NumberFormatException e) {
            throw new ValidationException(namaField, "harus berupa angka");
        }
    }

    /** Mengambil isian layar menjadi objek. Aturan entity diperiksa oleh DAO. */
    private Penjualan dariIsian() throws ValidationException {
        Penjualan p = new Penjualan(
                bacaInt(txtBarang, "barang"),
                bacaInt(txtAnggota, "anggota"),
                txtTanggal.getText().trim(),
                bacaInt(txtJumlah, "jumlah"),
                bacaDesimal(txtTotal, "total"));
        p.setId(idTerpilih);
        return p;
    }

    private void simpan() {
        try {
            Penjualan p = dariIsian();
            p.setId(0);
            dao.simpan(p);
            pesan("Tersimpan dengan id " + p.getId() + ".");
            kosongkan();
            muatTabel();
        } catch (AppException e) {
            tampilkanKesalahan(e);
        }
    }

    private void ubah() {
        if (idTerpilih == 0) {
            pesan("Pilih dulu satu baris di tabel.");
            return;
        }
        try {
            if (dao.ubah(dariIsian())) {
                pesan("Perubahan disimpan.");
                muatTabel();
            } else {
                pesan("Tidak ada baris yang berubah.");
            }
        } catch (AppException e) {
            tampilkanKesalahan(e);
        }
    }

    private void hapus() {
        if (idTerpilih == 0) {
            pesan("Pilih dulu satu baris di tabel.");
            return;
        }
        if (!konfirmasi("Hapus penjualan dengan id " + idTerpilih + "?")) {
            return;
        }
        try {
            if (dao.hapus(idTerpilih)) {
                pesan("Satu baris dihapus.");
                kosongkan();
                muatTabel();
            } else {
                pesan("Tidak ada baris yang terhapus.");
            }
        } catch (AppException e) {
            tampilkanKesalahan(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FormPenjualan().setVisible(true);
            }
        });
    }
}