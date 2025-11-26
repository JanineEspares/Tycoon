import java.util.*;

public class GameState {
    // Time management
    private int currentHour = 12;
    private int currentMinute = 0;
    private boolean isPM = true;
    private Timer timeTimer;
    private final int TIME_INTERVAL = 20000; // 10 seconds per time increment

    // Money management
    private double totalMoney = 40.00;
    private double dailyEarnings = 0.0;
    private int currentDay = 1;
    private boolean dayOver = false;

    // Customer management
    private int maxCustomers = 5;
    private Queue<Customer> customerQueue = new LinkedList<>();
    private Customer currentCustomer = null;
    private Timer patienceTimer;
    private final int PATIENCE_INTERVAL = 2000; // 1 second for patience decrease
    private DayManager dayManager;

    public GameState(DayManager dayManager) {
        this.dayManager = dayManager;
        initializeTime();
        initializeCustomers();
    }

    private void initializeTime() {
        timeTimer = new Timer();
        timeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                incrementTime();
            }
        }, TIME_INTERVAL, TIME_INTERVAL);
    }

    private void incrementTime() {
        currentMinute += 30;
        if (currentMinute >= 60) {
            currentMinute = 0;
            currentHour++;
            if (currentHour > 12) {
                currentHour = 1;
            }
            if (currentHour == 12) {
                isPM = !isPM;
            }
        }
    }

    private void initializeCustomers() {
        // Initialize customers based on DayManager rules
        customerQueue.clear(); // Clear any existing customers
        int num = 5;
        int minItems = 1;
        int maxItems = 1;
        if (dayManager != null) {
            num = dayManager.getMaxCustomersForToday();
            minItems = dayManager.getMinItemsForOrder();
            maxItems = dayManager.getMaxItemsForOrder();
        }
        for (int i = 0; i < num; i++) {
            customerQueue.offer(new Customer(minItems, maxItems));
        }

        // Make sure we have a current customer
        if (currentCustomer == null) {
            currentCustomer = customerQueue.poll(); // Timer will start when "Okay" is clicked
        }
    }

    public String getTimeString() {
        return String.format("%d:%02d%s", currentHour, currentMinute, isPM ? "PM" : "AM");
    }

    public void addMoney(double amount) {
        totalMoney += amount;
        dailyEarnings += amount;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public Customer getCurrentCustomer() {
        if (currentCustomer == null && !customerQueue.isEmpty()) {
            currentCustomer = customerQueue.poll(); // Don't start timer until "Okay" is clicked
        }
        return currentCustomer;
    }

    // Call this method when "Okay" is clicked
    public void startCurrentCustomerTimer() {
        if (currentCustomer != null) {
            if (patienceTimer != null) {
                patienceTimer.cancel(); // Cancel any existing timer
            }
            patienceTimer = new Timer();
            patienceTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (currentCustomer != null) {
                        currentCustomer.decreasePatience();
                        if (currentCustomer.getPatience() <= 0) {
                            customerLeft();
                        }
                    }
                }
            }, PATIENCE_INTERVAL, PATIENCE_INTERVAL);
        }
    }

    private void customerLeft() {
        if (patienceTimer != null) {
            patienceTimer.cancel();
        }
        currentCustomer = null;
        if (customerQueue.isEmpty()) {
            dayOver = true;
        }
    }

    public void completeCurrentCustomer() {
        if (patienceTimer != null) {
            patienceTimer.cancel();
        }
        currentCustomer = null;
        if (customerQueue.isEmpty()) {
            dayOver = true;
        }
    }

    public void cleanup() {
        if (timeTimer != null) {
            timeTimer.cancel();
        }
        if (patienceTimer != null) {
            patienceTimer.cancel();
        }
    }

    // Simple day helpers used by GamePanel
    public int getCurrentDay() { return currentDay; }
    public double getDailyEarnings() { return dailyEarnings; }
    public boolean isDayOver() { return dayOver; }
    public void startNextDay() {
        if (currentDay >= 3) return;
        currentDay++;
        dailyEarnings = 0.0;
        dayOver = false;
        // reinitialize customers
        currentCustomer = null;
        initializeCustomers();
    }
}
