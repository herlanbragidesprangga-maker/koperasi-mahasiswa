package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Anggota;

public class AnggotaDao extends BaseDao<Anggota> {

    @Override
    protected String namaTabel() {
        return "anggota";
    }

    @Override
    protected String[] kolomData() {
        return new String[]{
                "nama",
                "nim",
                "alamat",
                "no_hp"
        };
    }

    @Override
    protected void isiParameter(
            PreparedStatement ps,
            Anggota a) throws SQLException {

        ps.setString(1, a.getNama());
        ps.setString(2, a.getNim());
        ps.setString(3, a.getAlamat());
        ps.setString(4, a.getNoHp());
    }

    @Override
    protected Anggota dariBaris(
            ResultSet r) throws SQLException {

        Anggota a = new Anggota();

        a.setId(r.getInt("id"));
        a.setNama(r.getString("nama"));
        a.setNim(r.getString("nim"));
        a.setAlamat(r.getString("alamat"));
        a.setNoHp(r.getString("no_hp"));

        return a;
    }
}