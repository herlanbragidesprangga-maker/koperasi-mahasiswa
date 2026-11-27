
package dao;

import Model.Kategori;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KategoriDao extends BaseDao<Kategori> {

    // Menentukan nama tabel
    @Override
    protected String namaTabel() {
        return "kategori";
    }

    // Menentukan kolom data (tanpa kolom id)
    @Override
    protected String[] kolomData() {
        return new String[]{
                "kategori_id",
                "nama",
                "keterangan",
                "barang"
        };
    }

    // Mengisi parameter untuk INSERT dan UPDATE
    @Override
    protected void isiParameter(
            PreparedStatement ps, Kategori k) throws SQLException {

        ps.setString(1, k.getKategoriId());
        ps.setString(2, k.getNama());
        ps.setString(3, k.getKeterangan());
        ps.setString(4, k.getBarang());
    }

    // Mengubah hasil ResultSet menjadi objek Kategori
    @Override
    protected Kategori dariBaris(ResultSet r) throws SQLException {

        Kategori k = new Kategori();

        k.setId(r.getInt("id"));
        k.setKategoriId(r.getString("kategori_id"));
        k.setNama(r.getString("nama"));
        k.setKeterangan(r.getString("keterangan"));
        k.setBarang(r.getString("barang"));

        return k;
    }
}
