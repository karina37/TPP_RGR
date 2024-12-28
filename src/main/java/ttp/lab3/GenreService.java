package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    @Autowired
    private DataSource dataSource;

    // Method to save/update genre
    public String saveGenre(Genres genre) {
        String message;
        // Convert id to 0 if null or empty
        int genreId = (genre.id() == null) ? 0 : genre.id();

        String query = genreId == 0
                ? "INSERT INTO genres (name, description) VALUES (?, ?)"
                : "UPDATE genres SET name = ?, description = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Data validation
            if (genre.name() == null || genre.name().trim().isEmpty()) {
                return "Error: genre name must not be empty";
            }

            statement.setString(1, genre.name().trim());
            statement.setString(2, genre.description() != null ? genre.description().trim() : "");

            if (genreId != 0) {
                statement.setInt(3, genreId);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                if (genreId == 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return "Genre successfully saved with ID: " + generatedKeys.getInt(1);
                        }
                    }
                }
                message = "Genre successfully updated!";
            } else {
                message = "Failed to save genre.";
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                message = "Error: genre with this ID already exists";
            } else {
                message = "Error while saving genre: " + e.getMessage();
            }
            e.printStackTrace();
        }
        return message;
    }

    // Method to get all genres
    public List<Genres> getAllGenres() {
        List<Genres> genres = new ArrayList<>();
        String query = "SELECT * FROM genres ORDER BY name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                genres.add(mapRowToGenre(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    // Method to find genre by ID
    public Genres findGenreById(int id) {
        if (id <= 0) {
            return null;
        }

        String query = "SELECT * FROM genres WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToGenre(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to delete genre by ID
    public String deleteGenre(int id) {
        if (id <= 0) {
            return "Error: invalid genre ID";
        }


        String query = "DELETE FROM genres WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return "Genre successfully deleted!";
            } else {
                return "Genre with ID " + id + " not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error while deleting genre: " + e.getMessage();
        }
    }

    // Find genres by name (search functionality)
    public List<Genres> findGenresByName(String name) {
        List<Genres> genres = new ArrayList<>();
        String query = "SELECT * FROM genres WHERE name LIKE ? ORDER BY name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + name + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    genres.add(mapRowToGenre(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    // Helper method to map genre from ResultSet
    private Genres mapRowToGenre(ResultSet resultSet) throws SQLException {
        return new Genres(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description")
        );
    }
}
