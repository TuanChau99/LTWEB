package vn.iotstar.daos;

import java.sql.*;
import java.util.*;

import vn.iotstar.configs.DBConnect;
import vn.iotstar.models.ReviewModel;
import vn.iotstar.models.UserModel;

public class ReviewDAO {

    // ✅ Lấy danh sách review theo id sách
    public List<ReviewModel> getByBookId(int bookId) {
        List<ReviewModel> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM reviews r JOIN users u ON r.user_id = u.id WHERE r.book_id = ? ORDER BY r.id DESC";

        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReviewModel r = new ReviewModel();
                UserModel u = new UserModel();

                r.setId(rs.getInt("id"));
                r.setBookId(rs.getInt("book_id"));
                r.setUserId(rs.getInt("user_id"));
                r.setReviewText(rs.getString("review_text"));
                r.setCreatedAt(rs.getTimestamp("created_at"));

                u.setId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                r.setUser(u);

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Thêm review mới
    public void addReview(int bookId, int userId, String reviewText) {
        String sql = "INSERT INTO reviews (book_id, user_id, review_text, created_at) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = new DBConnect().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            ps.setString(3, reviewText);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
