package com.hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.hotel.models.Guest;
import com.hotel.services.GuestService;
import java.util.List;

public class Main extends JFrame {
    private JTable guestTable;
    private DefaultTableModel tableModel;
    private GuestService guestService;

    public Main() {
        guestService = new GuestService();
        setTitle("Hotel Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create menu and toolbar
        createMenu();
        createToolBar();

        // Create JTable to display guest list
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Room", "Check-in Date"}, 0);
        guestTable = new JTable(tableModel);

        // Populate the table with guest data
        populateGuestTable();

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(guestTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void populateGuestTable() {
        List<Guest> guests = guestService.getAllGuests();
        for (Guest guest : guests) {
            tableModel.addRow(new Object[]{guest.getId(), guest.getName(), guest.getRoomNumber(), guest.getCheckInDate()});
        }
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu guestMenu = new JMenu("Guests");
        JMenuItem addGuest = new JMenuItem("Add New Guest");
        JMenuItem checkOutGuest = new JMenuItem("Check-out Guest");
        JMenuItem viewRoomStatus = new JMenuItem("View Room Status");

        addGuest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGuestDialog();
            }
        });

        checkOutGuest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkOutGuest();
            }
        });

        viewRoomStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewRoomStatus();
            }
        });

        guestMenu.add(addGuest);
        guestMenu.add(checkOutGuest);
        guestMenu.add(viewRoomStatus);
        menuBar.add(guestMenu);

        setJMenuBar(menuBar);
    }

    private void createToolBar() {
        JToolBar toolBar = new JToolBar();
        JButton addButton = new JButton("Add Guest");
        JButton checkOutButton = new JButton("Check-out Guest");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGuestDialog();
            }
        });

        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkOutGuest();
            }
        });

        toolBar.add(addButton);
        toolBar.add(checkOutButton);
        add(toolBar, BorderLayout.NORTH);
    }

    // JDialog for adding new guests
    private void openGuestDialog() {
        JDialog dialog = new JDialog(this, "Add New Guest", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        
        JLabel roomLabel = new JLabel("Room:");
        JComboBox<String> roomComboBox = new JComboBox<>();
        for (String room : guestService.getEmptyRooms()) {
            roomComboBox.addItem(room);  // Add available rooms to JComboBox
        }
        
        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guestName = nameField.getText();
                String roomNumber = (String) roomComboBox.getSelectedItem();

                // Add the guest to database
                guestService.addGuest(new Guest(guestName, roomNumber));

                // Update the table
                tableModel.addRow(new Object[]{guestService.getLastGuestId(), guestName, roomNumber, "Today"});
                dialog.dispose();
            }
        });

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(roomLabel);
        dialog.add(roomComboBox);
        dialog.add(new JLabel());  // Empty label to align the button
        dialog.add(submitButton);

        dialog.setVisible(true);
    }

    // Check-out function
    private void checkOutGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guest to check-out.");
            return;
        }

        int guestId = (int) tableModel.getValueAt(selectedRow, 0);
        guestService.checkOutGuest(guestId);
        tableModel.removeRow(selectedRow);
        JOptionPane.showMessageDialog(this, "Guest checked out successfully.");
    }

    // View occupied and available rooms
    private void viewRoomStatus() {
        StringBuilder status = new StringBuilder();
        status.append("Occupied Rooms:\n");

        List<Guest> occupiedRooms = guestService.getAllGuests();
        for (Guest guest : occupiedRooms) {
            status.append("Room ").append(guest.getRoomNumber()).append(": ").append(guest.getName()).append("\n");
        }

        status.append("\nEmpty Rooms:\n");
        List<String> emptyRooms = guestService.getEmptyRooms();
        for (String room : emptyRooms) {
            status.append("Room ").append(room).append("\n");
        }

        JOptionPane.showMessageDialog(this, status.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
