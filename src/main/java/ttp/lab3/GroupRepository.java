package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Group> rowMapper = new RowMapper<Group>() {
        @Override
        public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Group(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("member_count")
            );
        }
    };

    public List<Group> findAll() {
        return jdbcTemplate.query("SELECT id, name, members_count FROM groups", rowMapper);
    }

    public Optional<Group> findById(int id) {
        return jdbcTemplate.query("SELECT id, name, members_count FROM groups WHERE id = ?", rowMapper, id)
                .stream().findFirst();
    }

    public int save(Group group) {
        return jdbcTemplate.update(
                "INSERT INTO groups (name, members_count) VALUES (?, ?)",
                group.name(), group.memberCount()
        );
    }

    public int update(Group group) {
        return jdbcTemplate.update(
                "UPDATE groups SET name = ?, members_count = ? WHERE id = ?",
                group.name(), group.memberCount(), group.id()
        );
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM groups WHERE id = ?", id);
    }
}
