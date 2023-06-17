package PresentationLayer.GUI.HR;

import ServiceLayer.HR.CompanyService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverSalary extends JFrame {

    private CardLayout cardLayout;
    private JPanel cards;

    private CompanyService service;

    public DriverSalary(HrFrame HRFrame) {
        service = new CompanyService();
        // Set up the JFrame
        setTitle("Drivers salary report");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);

        // Create the card layout and panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);
        getContentPane().add(cards, BorderLayout.CENTER);

        JPanel salaryReportPanel = salary(HRFrame);


        cards.add(salaryReportPanel, "Drivers salary report");

        cardLayout.show(cards, "Main Menu");

        setVisible(true);
    }

    private JPanel salary(HrFrame HRFrame) {

        JPanel salaryPanel = new JPanel(new BorderLayout());
        JPanel salaryPanel1= new JPanel(new FlowLayout());

        try {
            String reportText = service.driversSalaryReport();

            JTextArea salaryReportArea = new JTextArea(10, 30);
            salaryReportArea.setText(reportText);
            salaryReportArea.setEditable(false);
            Font newFont = salaryReportArea.getFont().deriveFont(24f); // Create a new Font object with size 16
            salaryReportArea.setFont(newFont); // Set the new font to the salaryReportArea

            JScrollPane reportPane = new JScrollPane(salaryReportArea);

            // Create a panel to hold the cancellation scroll pane and the cancel button
            salaryPanel.add(reportPane, BorderLayout.CENTER);
            // Button to return to the main menu

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0; // Assigns weight to the button


            JButton returnButton = new JButton("Return to Main Menu");
            salaryPanel.add(returnButton, BorderLayout.SOUTH);
            returnButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    HRFrame.setVisible(true);
                    dispose();
                }
            });

            // Create a panel to hold both the cancellation panel and the cancellation buttons
            JPanel combinedPanel = new JPanel(new BorderLayout());
            combinedPanel.add(salaryPanel, BorderLayout.CENTER);
//            combinedPanel.add(salaryPanel1, BorderLayout.SOUTH);

            return salaryPanel;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(salaryPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    return salaryPanel;
    }

}


