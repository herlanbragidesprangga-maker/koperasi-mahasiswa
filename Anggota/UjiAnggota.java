package uji;

import dao.AnggotaDao;
import exception.AppException;
import model.Anggota;

public class UjiAnggota {

    public static void main(String[] args) {

        AnggotaDao dao = new AnggotaDao();

        try {

            // Simpan
            Anggota baru = new Anggota(
                    "Riezer Andrian Desta",
                    "6304250081",
                    "Bengkalis",
                    "081234567890"
            );

            dao.simpan(baru);

            System.out.println(
                    "[simpan    ] id dari AUTO_INCREMENT = "
                            + baru.getId()
            );

            // Cari berdasarkan id
            Anggota hasil = dao.cariId(baru.getId());

            if (hasil != null) {

                System.out.println(
                        "[cariId    ] "
                                + hasil.label()
                );
            }

            // Ambil semua
            System.out.println(
                    "[ambilSemua] jumlah baris = "
                            + dao.ambilSemua().size()
            );

            int nomor = 1;

            for (Anggota a : dao.ambilSemua()) {

                System.out.println(
                        "             "
                                + nomor++
                                + ". "
                                + a.label()
                );
            }

            // Ubah
            baru.setAlamat("Dumai");

            boolean berhasil = dao.ubah(baru);

            System.out.println(
                    "[ubah      ] berhasil? "
                            + berhasil
            );

            // Hapus
            boolean hapus =
                    dao.hapus(baru.getId());

            System.out.println(
                    "[hapus     ] berhasil? "
                            + hapus
            );

        } catch (AppException e) {

            System.out.println(
                    "[kesalahan ] "
                            + e.pesanPengguna()
            );
        }
    }
}