import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookManagementUI extends JFrame {
    private JTextField titleField, authorField, publisherField, priceField, quantityField, customerField;
    private JTextArea textArea;
    private BookService bookService;

    public BookManagementUI() {
        bookService = new BookService();

        setTitle("图书管理系统");
        setSize(1000, 800);  // 窗口尺寸适当调整
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置字体
        Font font = new Font("微软雅黑", Font.PLAIN, 16);  // 设置字体类型为微软雅黑，大小为16

        // 主面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);  // 设置组件之间的间距

        // 设置标题栏
        JLabel titleLabel = new JLabel("书名:");
        titleLabel.setFont(font);  // 设置字体
        titleField = new JTextField();
        titleField.setFont(font);  // 设置字体
        addComponent(panel, titleLabel, 0, 0, gbc);
        addComponent(panel, titleField, 1, 0, gbc);

        // 设置作者栏
        JLabel authorLabel = new JLabel("作者:");
        authorLabel.setFont(font);  // 设置字体
        authorField = new JTextField();
        authorField.setFont(font);  // 设置字体
        addComponent(panel, authorLabel, 0, 1, gbc);
        addComponent(panel, authorField, 1, 1, gbc);

        // 设置出版社栏
        JLabel publisherLabel = new JLabel("出版社:");
        publisherLabel.setFont(font);  // 设置字体
        publisherField = new JTextField();
        publisherField.setFont(font);  // 设置字体
        addComponent(panel, publisherLabel, 0, 2, gbc);
        addComponent(panel, publisherField, 1, 2, gbc);

        // 设置价格栏
        JLabel priceLabel = new JLabel("价格:");
        priceLabel.setFont(font);  // 设置字体
        priceField = new JTextField();
        priceField.setFont(font);  // 设置字体
        addComponent(panel, priceLabel, 0, 3, gbc);
        addComponent(panel, priceField, 1, 3, gbc);

        // 设置数量栏
        JLabel quantityLabel = new JLabel("数量:");
        quantityLabel.setFont(font);  // 设置字体
        quantityField = new JTextField();
        quantityField.setFont(font);  // 设置字体
        addComponent(panel, quantityLabel, 0, 4, gbc);
        addComponent(panel, quantityField, 1, 4, gbc);

        // 设置客户姓名栏
        JLabel customerLabel = new JLabel("客户姓名:");
        customerLabel.setFont(font);  // 设置字体
        customerField = new JTextField();
        customerField.setFont(font);  // 设置字体
        addComponent(panel, customerLabel, 0, 5, gbc);
        addComponent(panel, customerField, 1, 5, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 0));

        JButton addButton = new JButton("添加图书");
        addButton.setFont(font);  // 设置字体
        JButton purchaseButton = new JButton("采购图书");
        purchaseButton.setFont(font);  // 设置字体
        JButton retireButton = new JButton("淘汰图书");
        retireButton.setFont(font);  // 设置字体
        JButton rentButton = new JButton("租借图书");
        rentButton.setFont(font);  // 设置字体
        JButton statsButton = new JButton("图书统计信息");
        statsButton.setFont(font);  // 设置字体
        JButton selectButton = new JButton("查询图书信息");
        selectButton.setFont(font);  // 设置字体
        // UI中增加“归还图书”按钮
        JButton returnButton = new JButton("归还图书");
        returnButton.setFont(font);  // 设置字体

        buttonPanel.add(addButton);
        buttonPanel.add(purchaseButton);
        buttonPanel.add(retireButton);
        buttonPanel.add(rentButton);
        buttonPanel.add(statsButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(returnButton);

        // 输出区域
        textArea = new JTextArea(15, 50); // 15行，50列
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));  // 设置字体
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // 添加按钮面板和输出区域到主面板
        gbc.gridwidth = 2;
        addComponent(panel, buttonPanel, 0, 6, gbc);
        addComponent(panel, scrollPane, 0, 7, gbc);

        // 设置按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                String publisher = publisherField.getText();
                String price = priceField.getText();
                int stock = Integer.parseInt(quantityField.getText());
                if(title.isEmpty()||author.isEmpty()||publisher.isEmpty()||price.isEmpty()||priceField.getText().isEmpty()){
                    textArea.append("请输入完整信息！\n");
                }

                if (bookService.addBook(title, author, publisher, price, stock)) {
                    textArea.append("图书添加成功！\n");
                } else {
                    textArea.append("图书添加失败！\n");
                }
                textArea.append("-------------------------------------\n");
            }
        });

        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                if (title.isEmpty()) {
                    textArea.append("请输入图书名称！\n");
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());

                if (bookService.purchaseBook(title, quantity)) {
                    textArea.append("图书采购成功！\n");
                } else {
                    textArea.append("图书采购失败！\n");
                }
                textArea.append("-------------------------------------\n");
            }
        });

        retireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                if (title.isEmpty()) {
                    textArea.append("请输入图书名称！\n");
                    return;
                }
                int quantity = Integer.parseInt(quantityField.getText());

                if (bookService.retireBook(title, quantity)) {
                    textArea.append("图书淘汰成功！\n");
                } else {
                    textArea.append("图书淘汰失败！\n");
                }
                textArea.append("-------------------------------------\n");
            }
        });

        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String customer = customerField.getText();
                if (title.isEmpty()) {
                    textArea.append("请输入图书名称！\n");
                    return;
                }
                if (customer.isEmpty()){
                    textArea.append("请输入客户姓名！\n");
                }
                if (bookService.rentBook(title, customer)) {
                    textArea.append("图书租借成功！\n");
                } else {
                    textArea.append("图书租借失败！\n");
                }
                textArea.append("-------------------------------------\n");
            }
        });

        // 查询按钮事件
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                if (title.isEmpty()) {
                    textArea.append("请输入图书名称进行统计！\n");
                    return;
                }
                Book bookDetails = bookService.getBookDetails(title);
                int stock = bookDetails.getStock();
                int totalPurchases = bookService.getTotalPurchases(title);
                int totalRentals = bookService.getTotalRentals(title);
                int totalRetirements = bookService.getTotalRetirements(title);

                textArea.append("图书统计信息：\n");
                textArea.append("总库存量：" + stock + "\n");
                textArea.append("总采购量: " + totalPurchases + "\n");
                textArea.append("总租借量: " + totalRentals + "\n");
                textArea.append("总淘汰量: " + totalRetirements + "\n");
                textArea.append("-------------------------------------\n");
            }
        });

        // 查看图书信息事件
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                if (title.isEmpty()) {
                    textArea.append("请输入图书名称查看信息！\n");
                    return;
                }

                Book book = bookService.getBookDetails(title);
                if (book != null) {
                    textArea.append("图书信息：\n");
                    textArea.append("书名: " + book.getTitle() + "\n");
                    textArea.append("作者: " + book.getAuthor() + "\n");
                    textArea.append("出版社: " + book.getPublisher() + "\n");
                    textArea.append("价格: " + book.getPrice() + "\n");
                    textArea.append("库存: " + book.getStock() + "\n");

                    // 查询并显示图书的采购记录
                    textArea.append("采购记录：\n");
                    for (String record : bookService.getPurchaseHistory(title)) {
                        textArea.append(record + "\n");
                    }

                    // 查询并显示图书的淘汰记录
                    textArea.append("淘汰记录：\n");
                    for (String record : bookService.getRetirementHistory(title)) {
                        textArea.append(record + "\n");
                    }

                    // 查询并显示图书的租借记录
                    textArea.append("租借记录：\n");
                    for (String record : bookService.getRentalHistory(title)) {
                        textArea.append(record + "\n");
                    }
                } else {
                    textArea.append("没有找到图书信息！\n");
                }
                textArea.append("-------------------------------------\n");
            }
        });
        // 归还图书按钮事件
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String customer = customerField.getText();
                if (title.isEmpty()) {
                    textArea.append("请输入图书名称！\n");
                    return;
                }
                if (customer.isEmpty()) {
                    textArea.append("请输入客户姓名！\n");
                    return;
                }

                if (bookService.returnBook(title, customer)) {
                    textArea.append("图书归还成功！\n");
                } else {
                    textArea.append("图书归还失败！\n");
                }
                textArea.append("-------------------------------------\n");
            }
        });

        add(panel, BorderLayout.CENTER);
    }


    // 工具方法：用于添加组件
    private void addComponent(JPanel panel, Component component, int gridx, int gridy, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        panel.add(component, gbc);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookManagementUI().setVisible(true);
            }
        });
    }
}
