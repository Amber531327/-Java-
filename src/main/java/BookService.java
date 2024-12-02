import java.util.List;

public class BookService {
    private BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }
//    添加图书
    public boolean addBook(String title, String author, String publisher, String price, int stock) {
        return bookDAO.addBook(title, author, publisher, price, stock);
    }
    // 采购图书
    public boolean purchaseBook(String title, int quantity) {
        return bookDAO.purchaseBook(title, quantity);
    }
    // 淘汰图书
    public boolean retireBook(String title, int quantity) {
        return bookDAO.retireBook(title, quantity);
    }
    // 租借图书
    public boolean rentBook(String title, String customerName) {
        return bookDAO.rentBook(title, customerName);
    }
    // 查询图书的详细信息
    public Book getBookDetails(String title) {
        return bookDAO.getBookByTitle(title);
    }
    // 查询图书的采购记录
    public List<String> getPurchaseHistory(String title) {
        return bookDAO.getPurchaseHistory(title);
    }

    // 查询图书的淘汰记录
    public List<String> getRetirementHistory(String title) {
        return bookDAO.getRetirementHistory(title);
    }

    // 查询图书的租借记录
    public List<String> getRentalHistory(String title) {
        return bookDAO.getRentalHistory(title);
    }

    // 获取图书的总采购量
    public int getTotalPurchases(String title) {
        return bookDAO.getTotalPurchases(title);
    }

    // 获取图书的总租借量
    public int getTotalRentals(String title) {
        return bookDAO.getTotalRentals(title);
    }

    // 获取图书的总淘汰量
    public int getTotalRetirements(String title) {
        return bookDAO.getTotalRetirements(title);
    }
    // 归还图书
    public boolean returnBook(String title, String customerName) {
        return bookDAO.returnBook(title, customerName);
    }


}
