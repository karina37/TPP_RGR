package ttp.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private DataSource dataSource;

    // Метод для збереження/оновлення групи
    public String saveGroup(Group group) {
        String message;
        // Конвертація id в 0 якщо він null або порожній
        int groupId = (group.id() == null || group.id().toString().trim().isEmpty()) ? 0 : group.id();

        String query = groupId == 0
                ? "INSERT INTO groups (name, members_count) VALUES (?, ?)"
                : "UPDATE groups SET name = ?, members_count = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Валідація даних
            if (group.name() == null || group.name().trim().isEmpty()) {
                return "Помилка: всі поля повинні бути заповнені";
            }

            if (group.memberCount() < 1) {
                return "Помилка: кількість учасників повинна бути більше нуля";
            }

            statement.setString(1, group.name().trim());
            statement.setInt(2, group.memberCount());

            if (groupId != 0) {
                statement.setInt(3, groupId);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                if (groupId == 0) {
                    try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            return "Групу успішно збережено з ID: " + generatedKeys.getInt(1);
                        }
                    }
                }
                message = "Групу успішно оновлено!";
            } else {
                message = "Не вдалося зберегти групу.";
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                message = "Помилка: група з таким ID вже існує";
            } else {
                message = "Помилка під час збереження групи: " + e.getMessage();
            }
            e.printStackTrace();
        }
        return message;
    }

    // Метод для отримання всіх груп
    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        String query = "SELECT * FROM groups ORDER BY name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                groups.add(mapRowToGroup(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groups;
    }

    // Метод для пошуку групи за ID
    public Group findGroupById(int id) {
        if (id <= 0) {
            return null;
        }

        String query = "SELECT * FROM groups WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToGroup(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для видалення групи за ID
    public String deleteGroup(int id) {
        if (id <= 0) {
            return "Помилка: некоректний ID групи";
        }

        String query = "DELETE FROM groups WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return "Групу успішно видалено!";
            } else {
                return "Групу з ID " + id + " не знайдено.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Помилка під час видалення групи: " + e.getMessage();
        }
    }

    // Допоміжний метод для відображення групи з ResultSet
    private Group mapRowToGroup(ResultSet resultSet) throws SQLException {
        return new Group(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getInt("members_count")
        );
    }
}
