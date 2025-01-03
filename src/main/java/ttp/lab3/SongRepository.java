package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class SongRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Song> rowMapper = (ResultSet rs, int rowNum) -> new Song(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("duration")
    );

    public List<Song> findAll() {
        return jdbcTemplate.query("SELECT * FROM songs", rowMapper);
    }

    public Optional<Song> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM songs WHERE id = ?", rowMapper, id).stream().findFirst();
    }

    public int save(Song song) {
        return jdbcTemplate.update("INSERT INTO songs (name, duration) VALUES (?, ?)",
                song.name(), song.duration());
    }

    public int update(Song song) {
        return jdbcTemplate.update("UPDATE songs SET name = ?, duration = ? WHERE id = ?",
                song.name(), song.duration(), song.id());
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM songs WHERE id = ?", id);
    }
}

