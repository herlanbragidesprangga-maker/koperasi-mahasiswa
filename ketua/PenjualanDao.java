package dao;

import db.Koneksi;
import exception.DataAccessException;
import model.Penjualan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** DAO modul Penjualan (transaksi). Pemilik: Herlan Bragi DesPrangga (Ketua Tim). */
public class PenjualanDao extends BaseDao<Penjualan> {

    @Override
    protected String namaTabel() {
        return "penjualan";
    }

    @Override
    protected String[] kolomData() {
        return new String[]{"barang_id", "anggota_id", "tanggal", "jumlah", "total"};
    }

    @Override
    protected void isiParameter(PreparedStatement ps, Penjualan p)
            throws SQLException {
        // Urutan di sini WAJIB sama dengan urutan di kolomData().
        ps.setInt(1, p.getBarangId());
        ps.setInt(2, p.getAnggotaId());
        ps.setString(3, p.getTanggal());
        ps.setInt(4, p.getJumlah());
        ps.setDouble(5, p.getTotal());
    }

    @Override
    protected Penjualan dariBaris(ResultSet r) throws SQLException {
        Penjualan p = new Penjualan();
        p.setId(r.getInt("id"));
        p.setBarangId(r.getInt("barang_id"));
        p.setAnggotaId(r.getInt("anggota_id"));
        p.setTanggal(r.getString("tanggal"));
        p.setJumlah(r.getInt("jumlah"));
        p.setTotal(r.getDouble("total"));
        return p;
    }

    /** Khas modul penjualan: semua penjualan pada satu tanggal (yyyy-MM-dd). */
    public List<Penjualan> cariTanggal(String tanggal) throws DataAccessException {
        String sql = "SELECT * FROM penjualan WHERE tanggal = ? ORDER BY id";
        List<Penjualan> hasil = new ArrayList<Penjualan>();
        try (Connection c = Koneksi.buka();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tanggal);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    hasil.add(dariBaris(r));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("gagal mencari penjualan", e);
        }
        return hasil;
    }
}