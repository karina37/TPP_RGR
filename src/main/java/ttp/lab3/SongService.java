package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SongService {

    @Autowired
    private DataSource dataSource;

    // Method to save/update song
    public String saveSong(Song song) {
        String message;
        Integer songId = song.id();
        if (songId == null || songId <= 0) {
            songId = 0;
        }

        String query = songId == 0
                ? "INSERT INTO songs (name, duration) VALUES (?, ?)"
                : "UPDATE songs SET name = ?, duration = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Перевірка вхідних даних
            if (song.name() == null || song.name().trim().isEmpty()) {
                return "Error: song name must not be empty";
            }

            if (song.duration() < 1 || song.duration() > 900) {
                return "Error: duration must be between 1 and 900 seconds";
            }

            statement.setString(1, song.name().trim());
            statement.setInt(2, song.duration());

            if (songId != 0) {
                statement.setInt(3, songId);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                if (songId == 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return "Song successfully saved with ID: " + generatedKeys.getInt(1);
                        }
                    }
                }
                message = "Song successfully updated!";
            } else {
                message = "Failed to save song.";
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                message = "Error: song with this ID already exists";
            } else {
                message = "Error while saving song: " + e.getMessage();
            }
            e.printStackTrace();
        }
        return message;
    }

    // Method to get all songs
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT * FROM songs ORDER BY name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                songs.add(mapRowToSong(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songs;
    }

    // Method to find song by ID
    public Song findSongById(int id) {
        if (id <= 0) {
            return null;
        }

        String query = "SELECT * FROM songs WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToSong(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to delete song by ID
    public String deleteSong(int id) {
        if (id <= 0) {
            return "Error: invalid song ID";
        }

        String query = "DELETE FROM songs WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return "Song successfully deleted!";
            } else {
                return "Song with ID " + id + " not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error while deleting song: " + e.getMessage();
        }
    }


    // Helper method to map song from ResultSet
    private Song mapRowToSong(ResultSet resultSet) throws SQLException {
        return new Song(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("duration")
        );
    }
}
