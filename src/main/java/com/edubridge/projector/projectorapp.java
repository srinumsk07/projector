package com.edubridge.projector;
import java.sql.*;
import java.util.Scanner;

public class projectorapp {
    private static final String URL = "jdbc:mysql://localhost:3306/edb12492";
    private static final String USER = "root";
    private static final String PASSWORD = "4113";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Add Projector");
                System.out.println("2. View All Projectors");
                System.out.println("3. Update Projector");
                System.out.println("4. Delete One Projector");
                System.out.println("5. Delete All Projectors");
                System.out.println("6. Search Projector");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        // Add Projector
                        System.out.print("Enter projector name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter brand: ");
                        String brand = scanner.nextLine();
                        System.out.print("Enter resolution: ");
                        String resolution = scanner.nextLine();
                        System.out.print("Enter brightness (in lumens): ");
                        int brightness = scanner.nextInt();
                        addProjector(connection, name, brand, resolution, brightness);
                        break;
                    case 2:
                        // View All Projectors
                        viewAllProjectors(connection);
                        break;
                    case 3:
                        // Update Projector
                        System.out.print("Enter projector ID to update: ");
                        int projectorId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter new name: ");
                        String newName = scanner.nextLine();
                        updateProjector(connection, projectorId, newName);
                        break;
                    case 4:
                        // Delete One Projector
                        System.out.print("Enter projector ID to delete: ");
                        int deleteId = scanner.nextInt();
                        deleteProjector(connection, deleteId);
                        break;
                    case 5:
                        // Delete All Projectors
                        deleteAllProjectors(connection);
                        break;
                    case 6:
                        // Search Projector
                        System.out.print("Enter projector name to search: ");
                        String searchName = scanner.nextLine();
                        searchProjector(connection, searchName);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addProjector(Connection connection, String name, String brand, String resolution, int brightness) {
        String query = "INSERT INTO projectors (name, brand, resolution, brightness) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, brand);
            pstmt.setString(3, resolution);
            pstmt.setInt(4, brightness);
            pstmt.executeUpdate();
            System.out.println("Projector added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllProjectors(Connection connection) {
        String query = "SELECT * FROM projectors";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Brand: " + rs.getString("brand") +
                                   ", Resolution: " + rs.getString("resolution") +
                                   ", Brightness: " + rs.getInt("brightness") + " lumens");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateProjector(Connection connection, int id, String newName) {
        String query = "UPDATE projectors SET name = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Projector updated successfully.");
            } else {
                System.out.println("Projector not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteProjector(Connection connection, int id) {
        String query = "DELETE FROM projectors WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Projector deleted successfully.");
            } else {
                System.out.println("Projector not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteAllProjectors(Connection connection) {
        String query = "DELETE FROM projectors";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
            System.out.println("All projectors deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchProjector(Connection connection, String name) {
        String query = "SELECT * FROM projectors WHERE name LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") +
                                       ", Name: " + rs.getString("name") +
                                       ", Brand: " + rs.getString("brand") +
                                       ", Resolution: " + rs.getString("resolution") +
                                       ", Brightness: " + rs.getInt("brightness") + " lumens");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



