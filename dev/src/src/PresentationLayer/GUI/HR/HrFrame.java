package PresentationLayer.GUI.HR;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HrFrame extends JFrame {
    private JPanel main;
    private JPanel buttonPanel;

    public HrFrame() {
        hrOptions();
    }


    public void hrOptions() {
        // Set up the JFrame
        setTitle("HR Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        setSize(1000,1000);
        // Create the main panel with a layered pane
        main = new JPanel();
        main.setLayout(new BorderLayout());

        // Create the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(getWidth(), getHeight()));

        // Create the background image label
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Replace "background.jpg" with your actual image file path
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());

        // Create the "Hello HR" label
        JLabel helloLabel = new JLabel("<html><h1>Hello HR</h1></html>");
        helloLabel.setBounds(0, 100, getWidth(), getHeight());
        helloLabel.setHorizontalAlignment(JLabel.CENTER);
        helloLabel.setVerticalAlignment(JLabel.TOP);

        // Create the button panel
        buttonPanel = new JPanel(new GridBagLayout());
        addButtons();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setBounds(0, 0, getWidth(), getHeight());

        // Add the components to the layered pane
        layeredPane.add(backgroundLabel, new Integer(0));
        layeredPane.add(helloLabel, new Integer(1));
        layeredPane.add(buttonPanel, new Integer(2));

        // Add the layered pane to the main panel
        main.add(layeredPane, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(main);

        setVisible(true);
    }

    private void addButtons() {
        String[] buttonLabels = {"Register employee",
                "Register driver",
                "Edit employee details",
                "Choose branch", "Drivers salary report", "Exit"
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between buttons

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(new ButtonClickListener(i + 1));
            button.setPreferredSize(new Dimension(300, 50)); // Set preferred size for the button
            buttonPanel.add(button, gbc);
            gbc.gridy++;
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int buttonNumber;

        public ButtonClickListener(int buttonNumber) {
            this.buttonNumber = buttonNumber;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Open a new frame or panel based on the button clicked
            switch (buttonNumber) {
                case 1:
                    new AddEmployee(HrFrame.this);
                    dispose(); // Close the current frame
                    break;
                case 2:
                    new AddDriver(HrFrame.this);
                    dispose();
                    break;
                case 3:
                    new EditDetails(HrFrame.this);
                    dispose();
                    break;
                case 4:
                    new ChooseBranch(HrFrame.this);
                    dispose();
                    break;
                case 5:
                    new DriverSalary(HrFrame.this);
                    break;
                case 6:
                    // Handle button 7 action
                    System.exit(0);
                    break;
            }
        }
    }
}
