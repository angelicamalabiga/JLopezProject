package com.lopez.payroll.login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ADMIN_verify_attendance {

    private DashboardFrame parentFrame;
    private DefaultTableModel attendanceModel;
    private JTable attendanceTable;

    // Buttons
    private JButton loadButton, viewFileButton, approveButton, rejectButton, refreshButton, editButton;

    // Colors
    private static final Color BLACK = Color.decode("#1a1a1a");
    private static final Color WHITE = Color.decode("#ffffff");
    private static final Color MUSTARD = Color.decode("#ffe380");

    public ADMIN_verify_attendance(DashboardFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WHITE);

        // ---------- HEADER ----------
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(WHITE);
        northPanel.add(createHeaderPanel("Attendance Verification"), BorderLayout.NORTH);

        // ---------- TOP BUTTONS ----------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(WHITE);

        loadButton = createButton("Load Submissions");
        refreshButton = createButton("Refresh");
        topPanel.add(loadButton);
        topPanel.add(refreshButton);

        northPanel.add(topPanel, BorderLayout.SOUTH);
        panel.add(northPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        String[] columns = {"ID", "Employee Name", "Period Start", "Period End", "File Name", "Status", "Date Uploaded"};
        attendanceModel = new DefaultTableModel(columns, 0);
        attendanceTable = new JTable(attendanceModel);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 13));
        attendanceTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.getViewport().setBackground(WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ---------- BOTTOM BUTTONS ----------
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(WHITE);

        viewFileButton = createButton("View File");
        editButton = createButton("Edit Attendance");
        approveButton = createButton("Approve");
        rejectButton = createButton("Reject");

        bottomPanel.add(viewFileButton);
        bottomPanel.add(editButton);
        bottomPanel.add(approveButton);
        bottomPanel.add(rejectButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Add popup trigger (UI only)
        editButton.addActionListener(e -> openEditDialog());

        return panel;
    }

    // ---------- POPUP EDIT FORM ----------
    private void openEditDialog() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an attendance record first.");
            return;
        }

        JDialog editDialog = new JDialog(parentFrame, "Edit Attendance", true);
        editDialog.setLayout(new GridBagLayout());
        editDialog.setSize(400, 400);
        editDialog.setLocationRelativeTo(parentFrame);
        editDialog.getContentPane().setBackground(WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Labels & Fields
        JLabel lblEmpName = new JLabel("Employee Name:");
        JTextField txtEmpName = new JTextField(attendanceModel.getValueAt(selectedRow, 1).toString());
        txtEmpName.setEditable(false);

        JLabel lblHours = new JLabel("Hours Worked:");
        JTextField txtHours = new JTextField();

        JLabel lblLates = new JLabel("Lates (minutes):");
        JTextField txtLates = new JTextField();

        JLabel lblAbsences = new JLabel("Absences:");
        JTextField txtAbsences = new JTextField();

        JLabel lblRemarks = new JLabel("Remarks:");
        JTextArea txtRemarks = new JTextArea(3, 15);
        txtRemarks.setLineWrap(true);
        txtRemarks.setWrapStyleWord(true);
        JScrollPane remarksScroll = new JScrollPane(txtRemarks);

        // Add components
        editDialog.add(lblEmpName, gbc);
        gbc.gridx = 1; editDialog.add(txtEmpName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        editDialog.add(lblHours, gbc);
        gbc.gridx = 1; editDialog.add(txtHours, gbc);

        gbc.gridx = 0; gbc.gridy++;
        editDialog.add(lblLates, gbc);
        gbc.gridx = 1; editDialog.add(txtLates, gbc);

        gbc.gridx = 0; gbc.gridy++;
        editDialog.add(lblAbsences, gbc);
        gbc.gridx = 1; editDialog.add(txtAbsences, gbc);

        gbc.gridx = 0; gbc.gridy++;
        editDialog.add(lblRemarks, gbc);
        gbc.gridx = 1; editDialog.add(remarksScroll, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(WHITE);
        JButton btnSave = createButton("Save");
        JButton btnCancel = createButton("Cancel");

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        editDialog.add(buttonPanel, gbc);

        // Close dialog on Cancel
        btnCancel.addActionListener(e -> editDialog.dispose());

        editDialog.setVisible(true);
    }

    // ---------- HEADER PANEL ----------
    private JPanel createHeaderPanel(String titleText) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BLACK);
        header.setPreferredSize(new Dimension(100, 64));
        header.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel title = new JLabel(titleText);
        title.setForeground(MUSTARD);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);

        return header;
    }

    // ---------- STYLE BUTTON ----------
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(MUSTARD);
        button.setForeground(BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // ---------- ACCESSORS ----------
    public void setAttendanceData(Object[][] data) {
        attendanceModel.setRowCount(0);
        for (Object[] row : data) {
            attendanceModel.addRow(row);
        }
    }

    public int getSelectedRowId() {
        int row = attendanceTable.getSelectedRow();
        if (row == -1) return -1;
        return Integer.parseInt(attendanceModel.getValueAt(row, 0).toString());
    }

    public JButton getLoadButton() { return loadButton; }
    public JButton getViewFileButton() { return viewFileButton; }
    public JButton getApproveButton() { return approveButton; }
    public JButton getRejectButton() { return rejectButton; }
    public JButton getRefreshButton() { return refreshButton; }
    public JButton getEditButton() { return editButton; }
}
