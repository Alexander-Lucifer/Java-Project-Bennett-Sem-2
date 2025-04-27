import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/games_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void createTable() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS high_scores (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "game_name VARCHAR(50)," +
                        "player1_name VARCHAR(50)," +
                        "player2_name VARCHAR(50)," +
                        "player1_score INT," +
                        "player2_score INT," +
                        "date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveScore(String gameName, String player1Name, String player2Name, 
                               int player1Score, int player2Score) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO high_scores (game_name, player1_name, player2_name, player1_score, player2_score) " +
                "VALUES (?, ?, ?, ?, ?)")) {
            
            pstmt.setString(1, gameName);
            pstmt.setString(2, player1Name);
            pstmt.setString(3, player2Name);
            pstmt.setInt(4, player1Score);
            pstmt.setInt(5, player2Score);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void showHighScores() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                "SELECT * FROM high_scores WHERE game_name = 'Pong' ORDER BY date_played DESC LIMIT 5")) {
            
            System.out.println("\nTop 5 Recent Games:");
            System.out.println("-------------------");
            while (rs.next()) {
                System.out.printf("%s vs %s: %d - %d (Played on: %s)%n",
                    rs.getString("player1_name"),
                    rs.getString("player2_name"),
                    rs.getInt("player1_score"),
                    rs.getInt("player2_score"),
                    rs.getTimestamp("date_played"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 