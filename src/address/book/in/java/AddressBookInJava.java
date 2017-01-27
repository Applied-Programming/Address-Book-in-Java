package address.book.in.java;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

class AddressBookInJava implements ActionListener {

    JPanel top_panel, bottom_panel;
    JScrollPane scroll_pane;
    JFrame frame;

    JMenuBar menu_bar = new JMenuBar();
    JMenu menu = new JMenu();
    JMenuItem menu_item;

    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screen_size = kit.getScreenSize();
    int screen_height = screen_size.height;
    int screen_width = screen_size.width;

    Image img = kit.getImage("images/icon.JPG");

    AddressBookInJava() {

        frame = new JFrame("Address Book");
        frame.setSize(680, 200);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(screen_width / 4, screen_height / 4);
        frame.setIconImage(img);
        addWidgets();
        frame.show();

    }

    public void addWidgets() {
        menu_bar.add(menu);

        menu = new JMenu("Options");
        menu_item = new JMenuItem("Add New Contact");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_item = new JMenuItem("Delete Contact");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_item = new JMenuItem("Search Contacts");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_item = new JMenuItem("Sort Contacts");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_item = new JMenuItem("View All Contacts");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_item = new JMenuItem("Backup Contacts");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_bar.add(menu);

        menu = new JMenu("Help");

        menu_item = new JMenuItem("Help Contents");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_item = new JMenuItem("About");
        menu.add(menu_item);
        menu_item.addActionListener(this);

        menu_bar.add(menu);

        frame.setJMenuBar(menu_bar);

        JPanel top_panel = new JPanel();
        JPanel bottom_panel = new JPanel();

        //Add Buttons To Bottom Panel
        JButton add_new = new JButton("Add New Contact");
        JButton delete_contact = new JButton("Delete Contact");
        JButton search_contacts = new JButton("Search Contacts");
        JButton sort_contacts = new JButton("Sort Contacts");
        JButton view_contact_list = new JButton("View All Contacts ");

        JLabel label = new JLabel("<HTML><FONT FACE = ARIAL SIZE = 2 > "
                + "<B> Use the given options below and in the menu bar To manage Contacts");

        //Add Action Listeners
        add_new.addActionListener(this);
        delete_contact.addActionListener(this);
        search_contacts.addActionListener(this);
        sort_contacts.addActionListener(this);
        view_contact_list.addActionListener(this);

        top_panel.add(label);

        bottom_panel.add(add_new);
        bottom_panel.add(delete_contact);
        bottom_panel.add(search_contacts);
        bottom_panel.add(sort_contacts);
        bottom_panel.add(view_contact_list);

        frame.getContentPane().add(top_panel,
                BorderLayout.NORTH);
        frame.getContentPane().add(bottom_panel,
                BorderLayout.SOUTH);
        frame.setResizable(false);

    }

    public static void main(String args[]) {
        AddressBookInJava add = new AddressBookInJava();

   }

    OperationHandler oh = new OperationHandler();

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand() == "Add New Contact") {
            oh.add_new();

        } else if (ae.getActionCommand() == "Search Contacts") {
            oh.search_contacts();

        } else if (ae.getActionCommand() == "Sort Contacts") {
            oh.sort_contacts();

        } else if (ae.getActionCommand() == "Delete Contact") {
            oh.delete_contact();

        } else if (ae.getActionCommand() == "View All Contacts") {

            oh.ViewAllContacts();

        } else if (ae.getActionCommand() == "About") {
            JOptionPane.showMessageDialog(frame, "Manage Contacts:", "About Address Book", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getActionCommand() == "Help Contents") {
            try {
                oh.showHelp();
            } catch (IOException e) {
            }

        } else if (ae.getActionCommand() == "Backup Contacts") {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setMultiSelectionEnabled(false);

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.showSaveDialog(frame);
            FileOutputStream bfout = null;
            FileInputStream bfin = null;
            String filename = null;

            int p;

            try {
                filename = chooser.getSelectedFile().getPath();
            } catch (Exception e) {
            }

            try {
                bfout = new FileOutputStream(filename + "\ndata.dat");
            } catch (Exception e) {

            }
            try {
                bfin = new FileInputStream("data/data.dat");
            } catch (Exception e) {

            }

            try {
                do {
                    p = bfin.read();
                    if (p != -1) {
                        bfout.write(p);
                    }
                } while (p != -1);
            } catch (Exception e) {

            }

        }

    }

}

class Contact implements Serializable {

    private String FName;
    private String LName;
    private String Nname;
    private String EMail;
    private String Address;
    private String PhoneNo;
    private String Webpage;
    private String Bday;

    public void setDetails(String fname, String lname, String nname,
            String email, String address, String phone, String web, String bday) {
        FName = fname;
        LName = lname;
        Nname = nname;
        EMail = email;
        Address = address;
        PhoneNo = phone;
        Webpage = web;
        Bday = bday;
    }

    public String getFName() {
        return FName;
    }

    public String getLName() {
        return LName;
    }

    public String getNname() {
        return Nname;
    }

    public String getEMail() {
        return EMail;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public String getWebpage() {
        return Webpage;
    }

    public String getBday() {
        return Bday;
    }

}

class OperationHandler implements ListSelectionListener,
        ActionListener,
        Runnable {

    JFrame new_frame;
    JTextField tFirstName;
    JTextField tLastName;
    JTextField tNickname;
    JTextField tEMail;
    JTextField tAddress;
    JTextField tPhoneNo;
    JTextField tWebpage;
    JTextField tBDay;

    JButton btnSaveAdded;

    Vector v = new Vector(10, 3);
    int i = 0, k = 0;

    Toolkit kit = Toolkit.getDefaultToolkit();
    Dimension screen_size = kit.getScreenSize();
    int screen_height = screen_size.height;
    int screen_width = screen_size.width;

    Image img = kit.getImage("images/icon.JPG");

    FileInputStream fis;
    ObjectInputStream ois;

    JList list;
    DefaultListModel listModel;
    ListSelectionModel listSelectionModel;

    JRadioButton byfname, bylname;

    Thread t;

    JTable searchTable;

    JTextField txtSearch;

    String columnNames[] = {"First Name",
        "Last Name",
        "Nickname",
        "E Mail Address",
        "Address",
        "Phone No.",
        "Webpage",
        "B'day"
    };

    Object data[][] = new Object[5][8];

    OperationHandler() {

        try {
            fis = new FileInputStream("data/data.dat");
            ois = new ObjectInputStream(fis);
            v = (Vector) ois.readObject();
            ois.close();
        } catch (Exception e) {

        }

    }

    public void run() {

        try {
            FileOutputStream fos = new FileOutputStream("data/data.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(v);
            oos.flush();
            oos.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(new_frame, "Error Opening Data File: "
                    + "Could Not Save Contents.", "Error Opening Data File", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void add_new() {
        new_frame = new JFrame("Add New");
        new_frame.setSize(220, 250);
        new_frame.setResizable(false);
        new_frame.setIconImage(img);

        JLabel lFirstName = new JLabel("First Name: ");
        JLabel lLastName = new JLabel("Last Name: ");
        JLabel lNickname = new JLabel("Nickname: ");
        JLabel lEMail = new JLabel("EMail: ");
        JLabel lAddress = new JLabel("Address: ");
        JLabel lPhoneNo = new JLabel("Phone No: ");
        JLabel lWebpage = new JLabel("Webpage: ");
        JLabel lBDay = new JLabel("BDay: ");
        JLabel lEmpty1 = new JLabel("");
        JLabel lEmpty2 = new JLabel("");

        tFirstName = new JTextField(10);
        tLastName = new JTextField(10);
        tNickname = new JTextField(10);
        tEMail = new JTextField(10);
        tAddress = new JTextField(10);
        tPhoneNo = new JTextField(10);
        tWebpage = new JTextField(10);
        tBDay = new JTextField(10);

        JButton BttnAdd = new JButton("Add New!");
        btnSaveAdded = new JButton("Save Added!");

        BttnAdd.addActionListener(this);
        btnSaveAdded.addActionListener(this);
        btnSaveAdded.setEnabled(false);

        JPanel center_pane = new JPanel();
        JPanel bottom_pane = new JPanel();

        center_pane.add(lFirstName);
        center_pane.add(tFirstName);
        center_pane.add(lLastName);
        center_pane.add(tLastName);
        center_pane.add(lNickname);
        center_pane.add(tNickname);
        center_pane.add(lEMail);
        center_pane.add(tEMail);
        center_pane.add(lAddress);
        center_pane.add(tAddress);
        center_pane.add(lPhoneNo);
        center_pane.add(tPhoneNo);
        center_pane.add(lWebpage);
        center_pane.add(tWebpage);
        center_pane.add(lBDay);
        center_pane.add(tBDay);
        bottom_pane.add(BttnAdd);
        bottom_pane.add(btnSaveAdded);

        center_pane.setLayout(new GridLayout(0, 2));

        new_frame.getContentPane().add(center_pane, BorderLayout.CENTER);

        new_frame.getContentPane().add(bottom_pane, BorderLayout.SOUTH);
        new_frame.setLocation(screen_width / 4, screen_height / 4);
        new_frame.show();

    }

    public void search_contacts() {
        new_frame = new JFrame("Search Contacts");
        new_frame.setSize(700, 220);
        new_frame.setLocation(screen_width / 4, screen_height / 4);
        new_frame.setIconImage(img);
        new_frame.setResizable(false);

        JPanel top_pane = new JPanel();
        JLabel label1 = new JLabel("Search Contacts by First Name or Last Name"
                + " or Both Spaced Via a Single Space:");
        top_pane.add(label1);

        JPanel center_pane = new JPanel();
        JLabel label2 = new JLabel("Search String:");
        txtSearch = new JTextField(20);
        JButton bttnSearch = new JButton("Search!");
        bttnSearch.addActionListener(this);
        JButton bttnCancel = new JButton("Cancel");
        bttnCancel.addActionListener(this);
        center_pane.add(label2);
        center_pane.add(txtSearch);
        center_pane.add(bttnSearch);
        center_pane.add(bttnCancel);

        searchTable = new JTable(data, columnNames);
        JScrollPane scroll_pane = new JScrollPane(searchTable);
        searchTable.setPreferredScrollableViewportSize(new Dimension(500, 90));

        new_frame.getContentPane().add(scroll_pane, BorderLayout.SOUTH);

        new_frame.getContentPane().add(top_pane,
                BorderLayout.NORTH);
        new_frame.getContentPane().add(center_pane,
                BorderLayout.CENTER);
        new_frame.show();
    }

    public void sort_contacts() {
        new_frame = new JFrame("Sort Contacts");
        new_frame.setSize(250, 160);
        new_frame.setLocation(screen_width / 4, screen_height / 4);
        new_frame.setIconImage(img);
        new_frame.setResizable(false);

        byfname = new JRadioButton("By First Name");
        byfname.setActionCommand("By First Name");
        byfname.setSelected(true);

        bylname = new JRadioButton("By Last Name");
        bylname.setActionCommand("By Last Name");

        ButtonGroup group = new ButtonGroup();
        group.add(byfname);
        group.add(bylname);

        JPanel top_pane = new JPanel();
        JLabel label = new JLabel("Sort Contacts By:");
        top_pane.add(label);

        JPanel pane = new JPanel(new GridLayout(0, 1));
        pane.add(byfname);
        pane.add(bylname);

        JPanel bottom_pane = new JPanel();
        JButton sortBttn = new JButton("Sort Contacts");
        JButton cancelBttn = new JButton("Cancel");
        bottom_pane.add(sortBttn);
        bottom_pane.add(cancelBttn);
        sortBttn.addActionListener(this);
        cancelBttn.addActionListener(this);

        new_frame.getContentPane().add(top_pane,
                BorderLayout.NORTH);
        new_frame.getContentPane().add(pane,
                BorderLayout.CENTER);
        new_frame.getContentPane().add(bottom_pane,
                BorderLayout.SOUTH);

        new_frame.show();

    }

    public void delete_contact() {
        String fname, lname;
        new_frame = new JFrame("Delete Contact");
        new_frame.setSize(300, 300);
        new_frame.setLocation(screen_width / 4, screen_height / 4);
        new_frame.setIconImage(img);

        JPanel center_pane = new JPanel();
        listModel = new DefaultListModel();

        Contact contact = new Contact();

        for (int l = 0; l < v.size(); l++) {
            contact = (Contact) v.elementAt(l);

            fname = contact.getFName();
            lname = contact.getLName();
            listModel.addElement(fname + " " + lname);

        }

        list = new JList(listModel);

        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel = list.getSelectionModel();
        listSelectionModel.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(list);

        JPanel top_pane = new JPanel();
        JLabel label = new JLabel("Current Contacts:");
        top_pane.add(label);

        JPanel bottom_pane = new JPanel();

        JButton bttnDelete = new JButton("Delete Selected");
        bottom_pane.add(bttnDelete);
        bttnDelete.addActionListener(this);

        JButton bttnCancel = new JButton("Cancel");
        bottom_pane.add(bttnCancel);
        bttnCancel.addActionListener(this);

        new_frame.getContentPane().add(top_pane,
                BorderLayout.NORTH);
        new_frame.getContentPane().add(listScrollPane,
                BorderLayout.CENTER);
        new_frame.getContentPane().add(bottom_pane,
                BorderLayout.SOUTH);

        new_frame.show();

    }

    public void ViewAllContacts() {

        new_frame = new JFrame("All Contacts In The AddressBook");
        new_frame.setSize(600, 300);
        new_frame.setIconImage(img);

        Contact con = new Contact();

        String columnNames[] = {"First Name",
            "Last Name",
            "Nickname",
            "E Mail Address",
            "Address",
            "Phone No.",
            "Webpage",
            "B'day"
        };

        Object data[][] = new Object[v.size()][8];

        for (int j = 0; j < v.size(); j++) {

            con = (Contact) v.elementAt(k);

            data[j][0] = con.getFName();
            data[j][1] = con.getLName();
            data[j][2] = con.getNname();
            data[j][3] = con.getEMail();
            data[j][4] = con.getAddress();
            data[j][5] = con.getPhoneNo();
            data[j][6] = con.getWebpage();
            data[j][7] = con.getBday();

            k++;

        }
        k = 0;

        JTable abtable = new JTable(data, columnNames);
        JScrollPane scroll_pane = new JScrollPane(abtable);
        abtable.setPreferredScrollableViewportSize(new Dimension(500, 370));

        JPanel pane = new JPanel();
        JLabel label = new JLabel("Contacts Currently In The Address Book");
        pane.add(label);

        new_frame.getContentPane().add(pane, BorderLayout.SOUTH);
        new_frame.getContentPane().add(scroll_pane,
                BorderLayout.CENTER);
        new_frame.setLocation(screen_width / 4,
                screen_height / 4);
        new_frame.show();

    }

    public void showHelp() throws IOException {

        String title = "Help Contents";
        String data = "";
        FileInputStream fishelp = null;
        int i;

        new_frame = new JFrame(title);
        new_frame.setSize(401, 400);
        new_frame.setResizable(false);
        new_frame.setLocation(screen_width / 4, screen_height / 4);
        new_frame.setIconImage(img);

        JTextArea textArea = new JTextArea(5, 20);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        try {
            fishelp = new FileInputStream("help/help.txt");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new_frame, "Help FileNot Found.",
                    "Help File Not Found", JOptionPane.INFORMATION_MESSAGE);
        }

        do {
            i = fishelp.read();
            if (i != 1) {
                data = data + (char) i;
            }
        } while (i != -1);

        fishelp.close();

        textArea.setText(data);

        JPanel bottom_pane = new JPanel();
        JButton button = new JButton("Ok");
        bottom_pane.add(button);
        button.addActionListener(this);

        JPanel top_pane = new JPanel();
        JLabel label = new JLabel(title);
        top_pane.add(label);

        JScrollPane scroll_pane = new JScrollPane(textArea);

        new_frame.getContentPane().add(top_pane, BorderLayout.NORTH);
        new_frame.getContentPane().add(scroll_pane);

        new_frame.getContentPane().add(bottom_pane, BorderLayout.SOUTH);

        new_frame.show();

    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand() == "Add New!") {

            if (tFirstName.getText().equals("")
                    && tLastName.getText().equals("") && tNickname.getText().equals("")
                    && tEMail.getText().equals("") && tAddress.getText().equals("")
                    && tPhoneNo.getText().equals("") && tWebpage.getText().equals("")
                    && tBDay.getText().equals("")) {

                JOptionPane.showMessageDialog(new_frame,
                        "Entries Empty! Fill in the required entries to save Contact.", 
                        "EntriesEmpty", JOptionPane.INFORMATION_MESSAGE);

            } else {
                Contact contact = new Contact();
                contact.setDetails(tFirstName.getText(), tLastName.getText(),
                        tNickname.getText(), tEMail.getText(),
                        tAddress.getText(), tPhoneNo.getText(),
                        tWebpage.getText(), tBDay.getText());
                v.addElement(contact);
                tFirstName.setText("");
                tLastName.setText("");
                tNickname.setText("");
                tEMail.setText("");
                tAddress.setText("");
                tPhoneNo.setText("");
                tWebpage.setText("");
                tBDay.setText("");

                if (btnSaveAdded.isEnabled() == false) {
                    btnSaveAdded.setEnabled(true);
                }
            }

        } else if (ae.getActionCommand() == "Save Added!") {

            saveVector();
            new_frame.setVisible(false);

        } else if (ae.getActionCommand() == "Ok") {
            new_frame.setVisible(false);

        } else if (ae.getActionCommand() == "Delete Selected") {

            int index;
            try {

                index = list.getSelectedIndex();

                if (index == -1) {

                    JOptionPane.showMessageDialog(new_frame, "Select a Contact first to delete it.",
                            "Select a Contact First", JOptionPane.INFORMATION_MESSAGE);
                } else {

                    int n
                            = JOptionPane.showConfirmDialog(new_frame,
                                    "Are you sure you want to delete the selected Contact?",
                                    "Are you sure ?", JOptionPane.YES_NO_OPTION);

                    if (n == JOptionPane.YES_OPTION) {
                        listModel.remove(index);
                        v.removeElementAt(index);
                        saveVector();
                        new_frame.show();

                    } else if (n
                            == JOptionPane.NO_OPTION) {

                    }

                }

            } catch (Exception e) {

            }

        } else if (ae.getActionCommand() == "Cancel") {

            new_frame.setVisible(false);
        } else if (ae.getActionCommand() == "Search!") {
            String SearchStr;
            SearchStr = txtSearch.getText();
            boolean flag = false;
            Contact con = new Contact();
            int c = 0;

            for (int t = 0; t < 5; t++) {
                data[t][0] = "";
                data[t][1] = "";
                data[t][2] = "";
                data[t][3] = "";
                data[t][4] = "";
                data[t][5] = "";
                data[t][6] = "";
                data[t][7] = "";
            }

            for (int t = 0; t < v.size(); t++) {

                con = (Contact) v.elementAt(t);

                if (SearchStr.equalsIgnoreCase(con.getFName())
                        || SearchStr.equalsIgnoreCase(con.getLName())
                        || SearchStr.equalsIgnoreCase(con.getFName() + " " + con.getLName())) {
                    flag = true;

                    data[c][0] = con.getFName();
                    data[c][1] = con.getLName();
                    data[c][2] = con.getNname();
                    data[c][3] = con.getEMail();
                    data[c][4] = con.getAddress();
                    data[c][5] = con.getPhoneNo();
                    data[c][6] = con.getWebpage();
                    data[c][7] = con.getBday();
                    searchTable = new JTable(data, columnNames);
                    new_frame.setSize(700, 221);
                    new_frame.setSize(700, 220);

                    if (c < 5) {
                        c++;
                    }
                }

            }

            if (flag) {
                JOptionPane.showMessageDialog(new_frame,
                        "Contact Found!", "Search Result!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(new_frame, "No Such Contact Found!", 
                        "Search Result!", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (ae.getActionCommand() == "Sort Contacts") {

            if (byfname.isSelected()) {
                Contact contact1 = new Contact();
                Contact contact2 = new Contact();
                Contact temp = new Contact();
                int l, m;

                for (l = 0; l < v.size() - 1; l++) {
                    for (m = l + 1; m < v.size(); m++) {
                        contact1 = (Contact) v.elementAt(l);
                        contact2 = (Contact) v.elementAt(m);

                        if (contact1.getFName().compareTo(contact2.getFName()) > 0) {
                            temp = (Contact) v.elementAt(m);

                            v.setElementAt(v.elementAt(l), m);
                            v.setElementAt(temp, l);
                        }

                    }
                }

                saveVector();
            } else {

                Contact contact1 = new Contact();
                Contact contact2 = new Contact();
                Contact temp = new Contact();
                int l, m;

                for (l = 0; l < v.size() - 1; l++) {
                    for (m = l + 1; m < v.size(); m++) {
                        contact1 = (Contact) v.elementAt(l);
                        contact2 = (Contact) v.elementAt(m);

                        if (contact1.getLName().compareTo(contact2.getLName()) > 0) {
                            temp = (Contact) v.elementAt(m);

                            v.setElementAt(v.elementAt(l), m);
                            v.setElementAt(temp, l);
                        }

                    }
                }

                saveVector();
            }

            new_frame.setVisible(false);
        }

    }

    public void saveVector() {
        t = new Thread(this, "Save Vector Thread");
        t.start();

    }

    public void valueChanged(ListSelectionEvent lse) {

    }

}

