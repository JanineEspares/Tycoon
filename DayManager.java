public class DayManager {
    private int currentDay;
    private int customersServed;
    private double dailyProfit;
    private boolean endOfDay;

    // array of max customers per day (index 0 = day 1, index 1 = day 2, ...)
    private final int[] maxCustomersPerDay = {5, 7, 10};

    public DayManager() {
        currentDay = 1;        // start at day 1
        customersServed = 0;
        dailyProfit = 0;
        endOfDay = false;
    }

    // === Customer + Profit Tracking ===
    public void customerServed(double profit) {
        customersServed++;
        dailyProfit += profit;

        // Automatically check for end of day
        if (customersServed >= getMaxCustomersForToday()) {
            endOfDay = true;
        }
    }

    // === Day End State ===
    public boolean isEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(boolean end) {
        this.endOfDay = end;
    }

    // === Getters ===
    public int getCustomersServed() {
        return customersServed;
    }

    public double getDailyProfit() {
        return dailyProfit;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    // === Order size rules per day ===
    // Day 1: 1 item each
    // Day 2: 1-2 items
    // Day 3: 2-3 items
    public int getMinItemsForOrder() {
        switch (currentDay) {
            case 1: return 1;
            case 2: return 1;
            case 3: return 2;
            default: return 1;
        }
    }

    public int getMaxItemsForOrder() {
        switch (currentDay) {
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            default: return 1;
        }
    }

    // === Get max customers dynamically based on current day ===
    public int getMaxCustomersForToday() {
        if (currentDay - 1 < maxCustomersPerDay.length) {
            return maxCustomersPerDay[currentDay - 1];
        } else {
            // after last defined day, keep max at last value
            return maxCustomersPerDay[maxCustomersPerDay.length - 1];
        }
    }

    // === Reset + Next Day ===
    public void nextDay() {
        currentDay++;
        customersServed = 0;
        dailyProfit = 0;
        endOfDay = false;
    }
}
