import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PublisherFrame extends JFrame implements ActionListener {

    JButton insert = new JButton("Insert");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");
    JButton search = new JButton("Search");

    JLabel pubCodeLabel = new JLabel("Pub_Code:");
    JTextField pubCodeField = new JTextField();

    JLabel nameLabel = new JLabel("Name:");
    JTextField nameField = new JTextField();

    JLabel cityLabel = new JLabel("City:");
    JTextField cityField = new JTextField();

    JLabel phoneLabel = new JLabel("Phone:");
    JTextField phoneField = new JTextField();

    JTextArea area = new JTextArea();

    PublisherFrame(String userName) {

        setLayout(null);
        setTitle("Publishers Management, welcome ( " + userName + " )");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(235, 245, 235));
//______________________________________________________________________________________________________________________
        pubCodeLabel.setBounds(30, 50, 80, 25);
        pubCodeLabel.setForeground(new Color(45, 90, 45));
        pubCodeField.setBounds(120, 50, 150, 25);

        nameLabel.setBounds(30, 90, 80, 25);
        nameLabel.setForeground(new Color(45, 90, 45));
        nameField.setBounds(120, 90, 150, 25);

        cityLabel.setBounds(30, 130, 80, 25);
        cityLabel.setForeground(new Color(45, 90, 45));
        cityField.setBounds(120, 130, 150, 25);

        phoneLabel.setBounds(30, 170, 80, 25);
        phoneLabel.setForeground(new Color(45, 90, 45));
        phoneField.setBounds(120, 170, 150, 25);

        area.setBounds(30, 270, 530, 130);
        area.setEditable(false);
        area.setBackground(Color.LIGHT_GRAY);
        area.setFont(new Font("Bold", Font.BOLD, 13));
//______________________________________________________________________________________________________________________
        insert.setBounds(30, 220, 90, 30);
        update.setBounds(130, 220, 90, 30);
        search.setBounds(230, 220, 90, 30);
        delete.setBounds(330, 220, 90, 30);

        insert.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        search.addActionListener(this);
//______________________________________________________________________________________________________________________
        add(pubCodeLabel); add(pubCodeField);
        add(nameLabel); add(nameField);
        add(cityLabel); add(cityField);
        add(phoneLabel); add(phoneField);
        add(insert); add(update); add(delete); add(search); add(area);

//______________________________________________________________________________________________________________________
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == insert) {

            String pub_code = pubCodeField.getText();
            String name = nameField.getText();
            String city = cityField.getText();
            String phone = phoneField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "root")) {
                String sql = "INSERT INTO publishers VALUES (?, ?, ?, ?)";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,pub_code);
                pst.setString(2,name);
                pst.setString(3,city);
                pst.setString(4,phone);

                pst.executeUpdate();

                area.setText("Your data: \n" +
                        "Publisher Code: " + pub_code + "\n" +
                        "Name: " + name + "\n" +
                        "City: " + city + "\n" +
                        "Phone: " + phone + "\n" +
                        "Successfully inserted in our database!");

                pubCodeField.setText("");
                nameField.setText("");
                cityField.setText("");
                phoneField.setText("");

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }


        } else if (e.getSource() == delete) {
            String id = pubCodeField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore", "root", "root")) {
                String sql = "DELETE FROM publishers WHERE pub_code = ?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,id);
                int rowsDeleted = pst.executeUpdate();

                if (rowsDeleted > 0 ) {
                    area.setText("Publisher " + id + " deleted successfully!");
                } else {
                    area.setText("No publisher found with that code.");
                }

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        } else if (e.getSource() == search) {

            String id = pubCodeField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {

                String sql = "SELECT * FROM publishers WHERE pub_code = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1,id);

                ResultSet resultSet = pst.executeQuery();

                if (resultSet.next()) {

                    String n = resultSet.getString("name");
                    String c = resultSet.getString("city");
                    String p = resultSet.getString("phone");

                    String result = "Publisher Found:\n" +
                            "Name: " + n + "\nCity: " + c + "\nPhone: " + p + "\n";

                    result += "\nBooks Published:\n";

                    String bookSql = "SELECT title FROM books WHERE pub_code = ?";
                    PreparedStatement bookSt = con.prepareStatement(bookSql);

                    bookSt.setString(1,id);
                    ResultSet rsBooks = bookSt.executeQuery();

                    boolean hasBooks = false;
                    while (rsBooks.next()) {
                        String book = rsBooks.getString("title");

                        result += "- " + book + "\n";
                        hasBooks = true;
                    }
                    if (!hasBooks) result += "No books found.";

                    area.setText(result);
                } else {
                    area.setText("No publisher found with code: " + id);
                }

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }


        } else if (e.getSource() == update) {

            String pub_code = pubCodeField.getText();
            String name = nameField.getText();
            String city = cityField.getText();
            String phone = phoneField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {
                String sql = "UPDATE publishers SET name = ?, city = ?, phone = ? Where pub_code = ?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,name);
                pst.setString(2,city);
                pst.setString(3,phone);
                pst.setString(4,pub_code);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    area.setText("Your data: \n" +
                            "Publisher Code: " + pub_code + "\n" +
                            "Name: " + name + "\n" +
                            "City: " + city + "\n" +
                            "Phone: " + phone + "\n" +
                            "Successfully Updated to the new ones!");

                    pubCodeField.setText("");
                    nameField.setText("");
                    cityField.setText("");
                    phoneField.setText("");

                } else {
                    area.setText("No publisher found with that code.");
                }
            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }
        }
    }
}