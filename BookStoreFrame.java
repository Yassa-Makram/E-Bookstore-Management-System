import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class BookStoreFrame extends JFrame implements ActionListener {

    JButton insert = new JButton("Insert");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");
    JButton search = new JButton("Search");

    JLabel isbnLabel = new JLabel("ISBN:");
    JTextField isbnField = new JTextField();

    JLabel titleLabel = new JLabel("Title:");
    JTextField titleField = new JTextField();

    JLabel priceLabel = new JLabel("Price:");
    JTextField priceField = new JTextField();

    JLabel typeLabel = new JLabel("Type:");
    JTextField typeField = new JTextField();

    JLabel pagesLabel = new JLabel("Pages:");
    JTextField pagesField = new JTextField();

    JLabel publisherCodeLabel = new JLabel("Pub_Code:");
    JTextField pubCodeField = new JTextField();


    JTextArea area = new JTextArea();

    BookStoreFrame(String userName) {

        setLayout(null);
        setTitle("Books Management, welcome ( " + userName + " )");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
//______________________________________________________________________________________________________________________
        isbnLabel.setBounds(30, 50, 80, 25);
        isbnLabel.setForeground(new Color(0, 102, 204));
        isbnField.setBounds(120, 50, 150, 25);

        titleLabel.setBounds(30, 90, 80, 25);
        titleLabel.setForeground(new Color(0, 102, 204));
        titleField.setBounds(120, 90, 150, 25);

        typeLabel.setBounds(30, 130, 80, 25);
        typeLabel.setForeground(new Color(0, 102, 204));
        typeField.setBounds(120, 130, 150, 25);

        publisherCodeLabel.setBounds(30, 170, 80, 25);
        publisherCodeLabel.setForeground(new Color(0, 102, 204));
        pubCodeField.setBounds(120, 170, 150, 25);

        priceLabel.setBounds(30, 250, 80, 25);
        priceLabel.setForeground(new Color(0, 102, 204));
        priceField.setBounds(120, 250, 150, 25);

        pagesLabel.setBounds(30, 210, 80, 25);
        pagesLabel.setForeground(new Color(0, 102, 204));
        pagesField.setBounds(120, 210, 150, 25);

        area.setBounds(30, 350, 530, 142);
        area.setEditable(false);
        area.setBackground(Color.LIGHT_GRAY);
        area.setFont(new Font("Bold", Font.BOLD, 13));
//______________________________________________________________________________________________________________________
        insert.setBounds(30, 300, 90, 30);
        update.setBounds(130, 300, 90, 30);
        search.setBounds(230, 300, 90, 30);
        delete.setBounds(330, 300, 90, 30);

        insert.setForeground(new Color(0, 102, 204));
        update.setForeground(new Color(0, 102, 204));
        search.setForeground(new Color(0, 102, 204));
        delete.setForeground(new Color(0, 102, 204));

        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        search.addActionListener(this);
//______________________________________________________________________________________________________________________
        add(isbnLabel); add(isbnField);
        add(titleLabel); add(titleField);
        add(priceLabel); add(priceField);
        add(pagesLabel); add(pagesField);
        add(typeLabel); add(typeField);
        add(insert); add(update); add(delete); add(search); add(area);
        add(pubCodeField); add(publisherCodeLabel);
//______________________________________________________________________________________________________________________
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == insert) {

            String isbn = isbnField.getText();
            String title = titleField.getText();
            String type = typeField.getText();
            String pubCode = pubCodeField.getText();
            double price = Double.parseDouble(priceField.getText());
            int pages = Integer.parseInt(pagesField.getText());


            try (Connection Yassa = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "root")) {

                String sql = "INSERT INTO books VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = Yassa.prepareStatement(sql);

                pst.setString(1, isbn);
                pst.setString(2, title);
                pst.setString(3, type);
                pst.setInt(4, pages);
                pst.setDouble(5, price);
                pst.setString(6, pubCode);

                pst.executeUpdate();

                area.setText("Book Saved to Database!\nLinked to Publisher: " + pubCode);

            } catch (SQLException ex) {
                area.setText("DATABASE ERROR: " + ex.getMessage());
            }

        } else if (e.getSource() == delete) {
            String deleteBook = isbnField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "root")) {
                String sql = "DELETE FROM books WHERE isbn = ?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, isbnField.getText());

                if (pst.executeUpdate() > 0) area.setText("Deleted Successfully!");
                else area.setText("No Book Found.");

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        } else if (e.getSource() == search) {

            String unique = isbnField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "root")) {
                String sql = "SELECT * FROM books WHERE isbn = ?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, isbnField.getText());
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    area.setText("Found: " + rs.getString("title") + " | Price: " + rs.getDouble("price"));
                } else area.setText("No Book Found.");

            } catch (SQLException ex) {
                area.setText("No book found with this ISBN!");
            }

        } else if (e.getSource() == update) {

            String uniqueKey = isbnField.getText();
            String newType = typeField.getText();
            String newTitle = titleField.getText();
            String newPublisherCode = pubCodeField.getText();
            double newPrice = Double.parseDouble(priceField.getText());
            int newPages = Integer.parseInt(pagesField.getText());

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "root")) {

                String sql = "UPDATE books SET Type = ?,Title = ?, Price = ?, Pages = ?, Pub_code = ? WHERE ISBN = ?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,newType);
                pst.setString(2,newTitle);
                pst.setDouble(3,newPrice);
                pst.setInt(4,newPages);
                pst.setString(5,newPublisherCode);
                pst.setString(6,uniqueKey);

                if (pst.executeUpdate() > 0) {

                    area.setText("Your data: \n" +
                            "Publisher Code: " + newPublisherCode + "\n" +
                            "ISBN: " + uniqueKey + "\n" +
                            "Type: " + newType + "\n" +
                            "Title: " + newTitle + "\n" +
                            "Price: " + newPrice + "\n" +
                            "Pages: " + newPages + "\n" +
                            "Successfully Updated to the new ones!");

                    pubCodeField.setText("");
                    isbnField.setText("");
                    typeField.setText("");
                    titleField.setText("");
                    priceField.setText("");
                    pagesField.setText("");

                } else {
                    area.setText("No Book Found with this ISBN!");
                }

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        }
    }

}