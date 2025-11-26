public class DayManager {
    private int currentDay;
    private int customersServed;
    private double dailyProfit;
    private boolean endOfDay;

    private final int[] maxCustomersPerDay = {5, 7, 10};

    public DayManager() {
        currentDay = 1;      
        customersServed = 0;
        dailyProfit = 0;
        endOfDay = false;
    }

    public void customerServed(double profit) {
        customersServed++;
        dailyProfit += profit;

        if (customersServed >= getMaxCustomersForToday()) {
            endOfDay = true;
        }
    }

    public boolean isEndOfDay() {
        return endOfDay;
    }

    public void setEndOfDay(boolean end) {
        this.endOfDay = end;
    }

    public int getCustomersServed() {
        return customersServed;
    }

    public double getDailyProfit() {
        return dailyProfit;
    }

    public int getCurrentDay() {
        return currentDay;
    }

 
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

    public int getMaxCustomersForToday() {
        if (currentDay - 1 < maxCustomersPerDay.length) {
            return maxCustomersPerDay[currentDay - 1];
        } else {
            return maxCustomersPerDay[maxCustomersPerDay.length - 1];
        }
    }

    public void nextDay() {
        currentDay++;
        customersServed = 0;
        dailyProfit = 0;
        endOfDay = false;
    }
}
