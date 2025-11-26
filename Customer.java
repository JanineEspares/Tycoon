import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {
    public enum OrderType { BURGER, HOTDOG, FRIES, SODA }

    private double patience = 100.0;
    private boolean isAngry = false;
    private List<OrderType> orderItems = new ArrayList<>();
    private int imageIndex;

    public Customer() {
        this(1, 1);
    }

    public Customer(int minItems, int maxItems) {
        Random rand = new Random();
        this.imageIndex = rand.nextInt(5);
        int count = minItems;
        if (maxItems > minItems) {
            count = minItems + rand.nextInt(maxItems - minItems + 1);
        }
        for (int i = 0; i < count; i++) {
            int pick = rand.nextInt(4);
            switch (pick) {
                case 0: orderItems.add(OrderType.BURGER); break;
                case 1: orderItems.add(OrderType.HOTDOG); break;
                case 2: orderItems.add(OrderType.FRIES); break;
                default: orderItems.add(OrderType.SODA); break;
            }
        }
    }

    public void decreasePatience() {
        patience -= 2;
        if (patience <= 50 && !isAngry) isAngry = true;
        if (patience < 0) patience = 0;
    }

    public double getPatience() { return patience; }
    public boolean isAngry() { return isAngry; }

    public String getOrder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < orderItems.size(); i++) {
            sb.append(orderItems.get(i).name());
            if (i < orderItems.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public double getOrderPrice() {
        double sum = 0.0;
        for (OrderType t : orderItems) sum += getPriceFor(t);
        return sum;
    }

    public int getImageIndex() { return imageIndex; }

    public boolean serveItem(OrderType t) {
        for (int i = 0; i < orderItems.size(); i++) {
            if (orderItems.get(i) == t) {
                orderItems.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isOrderComplete() { return orderItems.isEmpty(); }

    public boolean needsItem(OrderType t) {
        return orderItems.contains(t);
    }

    public static double getPriceFor(OrderType t) {
        switch (t) {
            case BURGER: return 100.00;
            case HOTDOG: return 80.00;
            case FRIES: return 60.00;
            default: return 30.00;
        }
    }
}


