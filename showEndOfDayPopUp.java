import javax.swing.JOptionPane;

public class showEndOfDayPopUp {

    public static void show(DayManager dayManager, GameManager gm) {

       
        boolean isLastDay = dayManager.getCurrentDay() >= dayManager.getMaxCustomersForToday() &&
            dayManager.getCurrentDay() >= 3; 

        String[] options = isLastDay ? new String[]{"Return to Menu"} : new String[]{"Return to Menu", "Proceed to Next Day"};

        int option = JOptionPane.showOptionDialog(
            null,
            "Day " + dayManager.getCurrentDay() + " Complete!\n" +
            "Customers Served: " + dayManager.getCustomersServed() + "\n" +
            "Profit: ₱" + dayManager.getDailyProfit(),
            "End of Day Summary",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (option == 0) {
            gm.showMenu();
        } else if (!isLastDay && option == 1) {
          
            dayManager.nextDay();
            gm.showDayScreen();
        }
    }
}
