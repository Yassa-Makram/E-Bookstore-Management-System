import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {

    JButton bookDatabase = new JButton("Manage Books");
    JButton publisherDatabase = new JButton("Manage Publishers");
    JButton authorDatabase = new JButton("Manage Authors");
    JLabel name = new JLabel("Enter Your name   : ");
    JTextField nameField = new JTextField();

    LoginFrame() {
        
        setTitle("E-Bookstore Management System");
        setSize(400, 400);
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(new Color(123, 50, 250)); // What is getContentPane()
//  ____________________________________________________________________________________________________________________

        setLayout(null);

        name.setBounds(50, 30, 300, 30);
        nameField.setBounds(50, 70, 300, 35);
        name.setForeground(Color.WHITE);

        bookDatabase.setBounds(75, 140, 250, 45);
        publisherDatabase.setBounds(75, 205, 250, 45);
        authorDatabase.setBounds(75, 270, 250, 45);

        add(bookDatabase); add(publisherDatabase); add(authorDatabase);
        add(name); add(nameField);

        bookDatabase.addActionListener(this);
        publisherDatabase.addActionListener(this);
        authorDatabase.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String userName = nameField.getText();
        if (userName.isEmpty()) {
            userName = "Guest";
        }

        if (e.getSource() == bookDatabase) {
            new BookStoreFrame(userName);
            this.dispose();
        }
        if (e.getSource() == publisherDatabase) {
            new PublisherFrame(userName);
            this.dispose();
        }
        if (e.getSource() == authorDatabase) {
            new AuthorFrame(userName);
            this.dispose();
        }

    }
}
