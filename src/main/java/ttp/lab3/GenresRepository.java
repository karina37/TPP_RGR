package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
public class GenresRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Genres> rowMapper = (ResultSet rs, int rowNum) -> new Genres(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description")
    );

    public List<Genres> findAll() {
        return jdbcTemplate.query("SELECT * FROM genres", rowMapper);
    }

    public Optional<Genres> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM genres WHERE id = ?", rowMapper, id).stream().findFirst();
    }

    public int save(Genres genre) {
        return jdbcTemplate.update("INSERT INTO genres (name, description) VALUES (?, ?)",
                genre.name(), genre.description());
    }

    public int update(Genres genre) {
        return jdbcTemplate.update("UPDATE genres SET name = ?, description = ? WHERE id = ?",
                genre.name(), genre.description(), genre.id());
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM genres WHERE id = ?", id);
    }
}
