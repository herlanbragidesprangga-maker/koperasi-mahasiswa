package uji;

import dao.PenjualanDao;
import exception.AppException;
import model.Penjualan;

import java.util.List;

/**
 * Prasyarat: tabel barang dan anggota sudah punya baris dengan id 1
 * (lihat sql/penjualan.sql), supaya kunci asing terpenuhi.
 */
public class UjiDaoPenjualan {

    public static void main(String[] args) {
        PenjualanDao dao = new PenjualanDao();

        try {
            Penjualan baru = new Penjualan(1, 1, "2026-11-10", 2, 10000);
            dao.simpan(baru);
            System.out.println("[simpan      ] id dari AUTO_INCREMENT = " + baru.getId());

            Penjualan dibaca = dao.cariId(baru.getId());
            System.out.println("[cariId      ] " + dibaca.label()
                    + " | jumlah " + dibaca.getJumlah() + " | total " + dibaca.getTotal());

            List<Penjualan> sehari = dao.cariTanggal("2026-11-10");
            System.out.println("[cariTanggal ] jumlah baris pada 2026-11-10 = " + sehari.size());

            dibaca.setJumlah(3);
            dibaca.setTotal(15000);
            System.out.println("[ubah        ] berhasil? " + dao.ubah(dibaca));

            List<Penjualan> semua = dao.ambilSemua();
            System.out.println("[ambilSemua  ] jumlah baris = " + semua.size());
            for (Penjualan p : semua) {
                System.out.println("               " + p.getId() + ". " + p.label()
                        + " | jumlah " + p.getJumlah() + " | total " + p.getTotal());
            }

            System.out.println("[hapus       ] berhasil? " + dao.hapus(baru.getId()));
            System.out.println("[hapus       ] id tak ada, berhasil? " + dao.hapus(999999));

        } catch (AppException e) {
            System.out.println(e.pesanPengguna());
        }

        // data yang ditolak sebelum menyentuh basis data
        try {
            dao.simpan(new Penjualan(1, 1, "2026-11-10", 0, 0));
            System.out.println("[validasi    ] TIDAK TERJADI - seharusnya ditolak");
        } catch (AppException e) {
            System.out.println("[validasi    ] " + e.pesanPengguna());
        }
    }
}