
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    // 添加图书
    public boolean addBook(String title, String author, String publisher, String price, int stock) {
        try (Connection conn = DBHelper.getConnection()) {
            String query = "INSERT INTO Books (title, author, publisher, price, stock) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setString(4, price);
            stmt.setInt(5, stock);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 采购图书
    public boolean purchaseBook(String title, int quantity) {
        try (Connection conn = DBHelper.getConnection()) {
            String searchQuery = "SELECT book_id FROM Books WHERE title = ?";
            PreparedStatement searchStmt = conn.prepareStatement(searchQuery);
            searchStmt.setString(1, title);
            ResultSet rs = searchStmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                String updateQuery = "UPDATE Books SET stock = stock + ? WHERE book_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, bookId);
                updateStmt.executeUpdate();

                String purchaseQuery = "INSERT INTO Purchases (book_id, quantity, purchase_date) VALUES (?, ?, ?)";
                PreparedStatement purchaseStmt = conn.prepareStatement(purchaseQuery);
                purchaseStmt.setInt(1, bookId);
                purchaseStmt.setInt(2, quantity);
                purchaseStmt.setDate(3, Date.valueOf(java.time.LocalDate.now()));
                purchaseStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 淘汰图书
    public boolean retireBook(String title, int quantity) {
        try (Connection conn = DBHelper.getConnection()) {
            String searchQuery = "SELECT book_id FROM Books WHERE title = ?";
            PreparedStatement searchStmt = conn.prepareStatement(searchQuery);
            searchStmt.setString(1, title);
            ResultSet rs = searchStmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                String updateQuery = "UPDATE Books SET stock = stock - ? WHERE book_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, bookId);
                updateStmt.executeUpdate();

                String retireQuery = "INSERT INTO Retirements (book_id, quantity, retirement_date) VALUES (?, ?, ?)";
                PreparedStatement retireStmt = conn.prepareStatement(retireQuery);
                retireStmt.setInt(1, bookId);
                retireStmt.setInt(2, quantity);
                retireStmt.setDate(3, Date.valueOf(java.time.LocalDate.now()));
                retireStmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 租借图书
    public boolean rentBook(String title, String customerName) {
        try (Connection conn = DBHelper.getConnection()) {
            String searchQuery = "SELECT book_id, stock FROM Books WHERE title = ?";
            PreparedStatement searchStmt = conn.prepareStatement(searchQuery);
            searchStmt.setString(1, title);
            ResultSet rs = searchStmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                int stock = rs.getInt("stock");

                if (stock > 0) {
                    String updateQuery = "UPDATE Books SET stock = stock - 1 WHERE book_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, bookId);
                    updateStmt.executeUpdate();

                    String rentQuery = "INSERT INTO Rentals (book_id, customer_name, rental_date) VALUES (?, ?, ?)";
                    PreparedStatement rentStmt = conn.prepareStatement(rentQuery);
                    rentStmt.setInt(1, bookId);
                    rentStmt.setString(2, customerName);
                    rentStmt.setDate(3, Date.valueOf(java.time.LocalDate.now()));
                    rentStmt.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 根据图书名称查询图书详细信息
    public Book getBookByTitle(String title) {
        Book book = null;
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT * FROM Books WHERE title = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getString("price"),
                        rs.getInt("stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
    // 查询采购记录
    public List<String> getPurchaseHistory(String title) {
        List<String> history = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT quantity, purchase_date FROM Purchases WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add("数量: " + rs.getInt("quantity") + ", 日期: " + rs.getDate("purchase_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    // 查询淘汰记录
    public List<String> getRetirementHistory(String title) {
        List<String> history = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT quantity, retirement_date FROM Retirements WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add("数量: " + rs.getInt("quantity") + ", 日期: " + rs.getDate("retirement_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    // 查询租借记录
    public List<String> getRentalHistory(String title) {
        List<String> history = new ArrayList<>();
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT customer_name, rental_date,return_date FROM Rentals WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add("客户: " + rs.getString("customer_name") + ", 借阅日期: " + rs.getDate("rental_date")+ ", 归还日期: " +rs.getDate("return_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    // 获取所有采购记录的统计
    public int getTotalPurchases(String title) {
        int totalPurchases = 0;
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT SUM(quantity) FROM Purchases WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalPurchases = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalPurchases;
    }

    // 获取所有租借记录的统计
    public int getTotalRentals(String title) {
        int totalRentals = 0;
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT COUNT(*) FROM Rentals WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalRentals = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRentals;
    }

    // 获取所有淘汰记录的统计
    public int getTotalRetirements(String title) {
        int totalRetirements = 0;
        try (Connection conn = DBHelper.getConnection()) {
            String query = "SELECT SUM(quantity) FROM Retirements WHERE book_id = (SELECT book_id FROM Books WHERE title = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalRetirements = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRetirements;
    }

    // 归还图书
    public boolean returnBook(String title, String customerName) {
        try (Connection conn = DBHelper.getConnection()) {
            // 查询图书ID和当前库存
            String searchQuery = "SELECT book_id, stock FROM Books WHERE title = ?";
            PreparedStatement searchStmt = conn.prepareStatement(searchQuery);
            searchStmt.setString(1, title);
            ResultSet rs = searchStmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                int currentStock = rs.getInt("stock");

                // 确保该客户已经租借了这本书
                String rentalQuery = "SELECT rental_id FROM Rentals WHERE book_id = ? AND customer_name = ? AND return_date IS NULL";
                PreparedStatement rentalStmt = conn.prepareStatement(rentalQuery);
                rentalStmt.setInt(1, bookId);
                rentalStmt.setString(2, customerName);
                ResultSet rentalRs = rentalStmt.executeQuery();

                if (rentalRs.next()) {
                    // 更新图书库存
                    String updateStockQuery = "UPDATE Books SET stock = stock + 1 WHERE book_id = ?";
                    PreparedStatement updateStockStmt = conn.prepareStatement(updateStockQuery);
                    updateStockStmt.setInt(1, bookId);
                    updateStockStmt.executeUpdate();

                    // 更新租借记录，添加归还日期
                    String updateRentalQuery = "UPDATE Rentals SET return_date = ? WHERE rental_id = ?";
                    PreparedStatement updateRentalStmt = conn.prepareStatement(updateRentalQuery);
                    updateRentalStmt.setDate(1, Date.valueOf(java.time.LocalDate.now()));
                    updateRentalStmt.setInt(2, rentalRs.getInt("rental_id"));
                    updateRentalStmt.executeUpdate();

                    return true;
                } else {
                    System.out.println("该客户未租借此图书！");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
