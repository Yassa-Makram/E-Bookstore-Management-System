import com.mysql.cj.protocol.ProtocolEntityReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AuthorFrame extends JFrame implements ActionListener {

    JButton insert = new JButton("Insert");
    JButton update = new JButton("Update");
    JButton delete = new JButton("Delete");
    JButton search = new JButton("Search");
    JButton link = new JButton("Link");

    JLabel authorIdLabel = new JLabel("Author_ID:");
    JTextField authorIdField = new JTextField();

    JLabel fNameLabel = new JLabel("First Name:");
    JTextField fNameField = new JTextField();

    JLabel lNameLabel = new JLabel("Last Name:");
    JTextField lNameField = new JTextField();

    JTextArea area = new JTextArea();

    AuthorFrame(String userName) {

        setLayout(null);
        setTitle("Authors Management, welcome ( " + userName + " )");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(new Color(235, 245, 235));
//______________________________________________________________________________________________________________________
        authorIdLabel.setBounds(30, 50, 80, 25);
        authorIdLabel.setForeground(new Color(45, 90, 45));
        authorIdField.setBounds(120, 50, 150, 25);

        fNameLabel.setBounds(30, 90, 80, 25);
        fNameLabel.setForeground(new Color(45, 90, 45));
        fNameField.setBounds(120, 90, 150, 25);

        lNameLabel.setBounds(30, 130, 80, 25);
        lNameLabel.setForeground(new Color(45, 90, 45));
        lNameField.setBounds(120, 130, 150, 25);

        link.setBounds(430, 220, 120, 30);

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
        link.addActionListener(this);
//______________________________________________________________________________________________________________________
        add(authorIdLabel); add(authorIdField);
        add(fNameLabel); add(fNameField);
        add(lNameLabel); add(lNameField);
        add(insert); add(update); add(delete); add(search); add(area);
        add(link);
//______________________________________________________________________________________________________________________
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == insert) {

            String authorId = authorIdField.getText();
            String fName = fNameField.getText();
            String lName = lNameField.getText();


            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {
                String sql = "INSERT INTO authors VALUES (?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,authorId);
                pst.setString(2,fName);
                pst.setString(3,lName);
                pst.executeUpdate();

                area.setText("New Author Inserted Successfully: " + "\n" +
                        "Author ID: " + authorId + "\n" +
                        "First Name: " + fName + "\n" +
                        "Last Name: " + lName);

                authorIdField.setText("");
                fNameField.setText("");
                lNameField.setText("");

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        } else if (e.getSource() == delete) {

            String deleteID = authorIdField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {
                String sql = "DELETE FROM authors WHERE author_id = ?";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,deleteID);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    area.setText("Author with id: " + deleteID + ", deleted successfully!" );
                } else {
                    area.setText("There is no author with this id: " + deleteID);
                }

            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        } else if (e.getSource() == search) {

            String unique = authorIdField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {
                String authorSql = "SELECT * FROM authors WHERE author_id = ?";

                PreparedStatement pst = con.prepareStatement(authorSql);

                pst.setString(1,unique);
                ResultSet rsAuthor = pst.executeQuery();

                if (rsAuthor.next()) {

                    String aName = rsAuthor.getString("f_name") + " " + rsAuthor.getString("l_name");
                    String output = "Author: " + aName + "\nLinks Found:\n";

                    String bookSql = "SELECT isbn FROM book_authors WHERE author_id = ?";
                    PreparedStatement pst2 = con.prepareStatement(bookSql);

                    pst2.setString(1,unique);
                    ResultSet rsBook = pst2.executeQuery();
                    while (rsBook.next()) {
                        output += "- Book ISBN: " + rsBook.getString("isbn") + "\n";
                    }

                    area.setText(output);

                } else {
                    area.setText("There is no author with this id: " + unique);
                }
            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        } else if (e.getSource() == update) {

            String authorID = authorIdField.getText();
            String newF_Name = fNameField.getText();
            String newL_Name = lNameField.getText();

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {
                String sql = "UPDATE authors SET f_name = ?, l_name = ? WHERE author_id = ?" ;
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,newF_Name);
                pst.setString(2,newL_Name);
                pst.setString(3,authorID);
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    area.setText("Author ID " + authorID + " updated successfully!");
                } else {
                    area.setText("Updated Failed! Author ID " + authorID + " not found.");
                }
            } catch (SQLException ex) {
                area.setText("Error: " + ex.getMessage());
            }

        } else if (e.getSource() == link) {

            String authorID = authorIdField.getText();
            String isbn = JOptionPane.showInputDialog(this, "Enter ISBN to link to this Author:");

            if (!isbn.isEmpty()) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebookstore","root","root")) {

                String sql = "INSERT INTO book_authors (isbn, author_id) VALUES (?, ?)";
                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1,isbn);
                pst.setString(2,authorID);
                pst.executeUpdate();

                area.setText("Connection Created Successfully!\n" + authorID + " is now linked to Book " + isbn);
            } catch (SQLException ex) {
                area.setText("Link Error: Make sure the ISBN and Author ID both exist first!");
            }

            }
        }
    }
}
