package BusinessLayer.InventoryBusinessLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PopUpAlert {
    public void showPopupWindow(List<Product> productToOrder) {
        JFrame frame = new JFrame("Missing Products Alert");
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JLabel label1 = new JLabel("We need to place an order from a supplier for the following products:");
        label1.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label1);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        for (Product p : productToOrder) {
            String productName = p.getProductName();
            int productID = p.getProductID();
            JLabel label = new JLabel(String.format("Product Name: %s | Product ID: %d", productName, productID));
            label.setFont(new Font("Arial", Font.PLAIN, 13));
            panel.add(label);
        }

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton button = new JButton("OK");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(80, 30));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel.add(button);
        frame.add(panel);
        if (productToOrder.size() == 1)
        {
            frame.setSize(550, 125);

        }
        else {
            frame.setSize(550, 50 * productToOrder.size()+2 + 50);
        }
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }
}



