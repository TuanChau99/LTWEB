package vn.iotstar.models;

import java.sql.Timestamp;

public class ReviewModel {
    private int id;
    private int bookId;
    private int userId;
    private String reviewText;
    private Timestamp createdAt;
    private UserModel user;

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public UserModel getUser() { return user; }
    public void setUser(UserModel user) { this.user = user; }
}
