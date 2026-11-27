
package Uji;

import dao.KategoriDao;
import exception.AppException;
import Model.Kategori;

import java.util.List;

public class UjiKategori {

    public static void main(String[] args) {

        KategoriDao dao = new KategoriDao();

        try {
            // SIMPAN DATA
            Kategori baru = new Kategori();
            baru.setKategoriId("K005");
            baru.setNama("Peralatan Komputer");
            baru.setKeterangan("Kategori perangkat komputer");
            baru.setBarang("Keyboard");

            dao.simpan(baru);

            System.out.println("[simpan    ] id dari AUTO_INCREMENT = "
                    + baru.getId());

            // CARI BERDASARKAN ID
            Kategori dibaca = dao.cariId(baru.getId());

            System.out.println("[cariId    ] " + dibaca.label());

            // CARI BERDASARKAN KODE KATEGORI
            Kategori lewatId = dao.cariId(baru.getId());

            System.out.println("[cariId    ] " + lewatId.label());

            // UBAH DATA
            dibaca.setBarang("Keyboard dan Mouse");

            System.out.println("[ubah      ] berhasil? "
                    + dao.ubah(dibaca));

            // AMBIL SEMUA DATA
            List<Kategori> semua = dao.ambilSemua();

            System.out.println("[ambilSemua] jumlah baris = "
                    + semua.size());

            for (Kategori k : semua) {
                System.out.println("             "
                        + k.getId() + ". " + k.label());
            }

            // HAPUS DATA
            System.out.println("[hapus     ] berhasil? "
                    + dao.hapus(baru.getId()));

            System.out.println("[hapus     ] id tak ada, berhasil? "
                    + dao.hapus(999999));

        } catch (AppException e) {
            System.out.println(e.pesanPengguna());
        }

        // DATA YANG DITOLAK SEBELUM MENYENTUH BASIS DATA
        try {
            Kategori salah = new Kategori();
            salah.setKategoriId("");
            salah.setNama("");
            salah.setKeterangan("Data tidak valid");
            salah.setBarang("Barang");

            dao.simpan(salah);

            System.out.println(
                    "[validasi  ] TIDAK TERJADI - seharusnya ditolak");

        } catch (AppException e) {
            System.out.println("[validasi  ] "
                    + e.pesanPengguna());
        }
    }
}
