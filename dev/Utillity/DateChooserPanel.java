package Utillity;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateChooserPanel extends JPanel {
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> dayComboBox;

    private JLabel selectedDateLabel;

    public DateChooserPanel() {
        setLayout(new FlowLayout());

        // Year ComboBox
        yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = currentYear; year >= 1900; year--) {
            yearComboBox.addItem(year);
        }


        // Month ComboBox
        monthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        // Day ComboBox
        dayComboBox = new JComboBox<>();
        updateDayComboBox();
        add(dayComboBox);
        add(monthComboBox);
        add(yearComboBox);
        setSelectedDate(LocalDate.now());


        // Action listeners to update dayComboBox when year or month changes
        yearComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDayComboBox();
            }
        });

        monthComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDayComboBox();
            }
        });
    }

    private void updateDayComboBox() {
        int year = (int) yearComboBox.getSelectedItem();
        int month = monthComboBox.getSelectedIndex();

        Calendar calendar = new GregorianCalendar(year, month, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        dayComboBox.removeAllItems();
        for (int day = 1; day <= daysInMonth; day++) {
            dayComboBox.addItem(day);
        }
    }

    public LocalDate getSelectedDate() {
        int year = (int) yearComboBox.getSelectedItem();
        int month = monthComboBox.getSelectedIndex();
        int day = (int) dayComboBox.getSelectedItem();

        return LocalDate.of(year, month, day);
    }

    public void setSelectedDate(LocalDate date) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        yearComboBox.setSelectedItem(year);
        monthComboBox.setSelectedIndex(month - 1); // Adjust for 0-based index
        dayComboBox.setSelectedItem(day);
    }
}
