import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    final int SCREEN_WIDTH = 1280;
    final int SCREEN_HEIGHT = 700;
    
    private final int panX = 580;
    private final int panY = 360;
    private final int panW = 200;
    private final int panH = 120;

  
    private int mouseX = -1;
    private int mouseY = -1;

    Thread gameThread;
    boolean running = false;
    final int FPS = 60;

    private GameState gameState;
    private DayManager dayManager;

    private BufferedImage frontBg;
    private BufferedImage kitchenBg;
    private BufferedImage insideKitchenBg;

    private enum Station {
        FRONT,
        KITCHEN,
        INSIDE_KITCHEN
    }
    private Station currentStation = Station.FRONT;

    private boolean paused = false;
    private GameManager gm;

    private BufferedImage burgerBunImg;
    private BufferedImage cookingPattyImg;
    private BufferedImage cookedPattyImg;
    private BufferedImage rawPattyImg;
    private BufferedImage pattiesImg;
    private BufferedImage hotdogsssImg;
    private BufferedImage rawFriesImg;
    private BufferedImage cookingFriesImg;
    private BufferedImage cookedFriesImg;
    private BufferedImage rawHotdogImg;
    private BufferedImage hotdogBunImg;
    private BufferedImage cookedHotdogImg;
    private BufferedImage cupPileImg;
    private BufferedImage sodaMachineImg;
    private BufferedImage sodaMachineFillImg;
    private BufferedImage burgerFinishedImg;
    private BufferedImage friesFinishedImg;
    private BufferedImage friesContainerImg;
    private BufferedImage hotdogWithBunImg;
    private BufferedImage hotdogImg;
    private BufferedImage sodaImg;
    private BufferedImage trayImg;
    private BufferedImage ketchupImg;
    private BufferedImage mustardImg;


    private boolean pattyOnGrill = false;
    private boolean friesInFryer = false;
    private boolean hotdogOnGrill = false;
    private boolean hotdogHasBun = false;
    private boolean hotdogHasKetchup = false;
    private boolean hotdogHasMustard = false;
    private boolean cupSelected = false;

 
    private boolean burgerOnKitchenTray = false;
    private boolean friesOnKitchenTray = false;
    private boolean hotdogOnKitchenTray = false;
    private boolean sodaOnKitchenTray = false;
    
 
    private boolean burgerOnFrontTray = false;
    private boolean friesOnFrontTray = false;
    private boolean hotdogOnFrontTray = false;
    private boolean sodaOnFrontTray = false;

  
    private Set<String> trayItems = new HashSet<>();
    private boolean sodaMachineFilling = false;
    private boolean trayOnTable = false; 
    private Timer cookTimer = new Timer();
  
    private Rectangle burgerBunHotspot = new Rectangle(775, 73, 208, 252);
    private Rectangle rawPattyHotspot = new Rectangle(412, 73, 208, 254);
    private Rectangle rawFriesHotspot = new Rectangle(592, 70, 210, 264);
    private Rectangle rawHotdogHotspot = new Rectangle(240, 70, 200, 267);
    private Rectangle hotdogBunHotspot = new Rectangle(955, 73, 210, 260);
    private Rectangle cupsHotspot = new Rectangle(1150, 180, 125, 180);
    private Rectangle sodaMachineHotspot = new Rectangle(-100, 10, 425, 518);
    private Rectangle friesContainerHotspot = new Rectangle(230, 420, 120, 120);

    private boolean pattyCooking = false;
    private boolean pattyCooked = false;
    private boolean friesCooking = false;
    private boolean friesCooked = false;
    private boolean hotdogCooking = false;
    private boolean hotdogCooked = false;
    private Set<String> currentAssembly = new HashSet<>();

   
    private boolean showServeInstructionInFront = false;
    private String serveInstructionText = "";
    
    public GamePanel(GameManager gm, DayManager dayManager) {
        this.gm = gm;
        this.dayManager = dayManager;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.gameState = new GameState(this.dayManager);

       
        try {
            frontBg = ImageIO.read(getClass().getResourceAsStream("images/restaurant_front.jpg"));
            kitchenBg = ImageIO.read(getClass().getResourceAsStream("images/restaurant_back.jpg"));
            insideKitchenBg = ImageIO.read(getClass().getResourceAsStream("images/inside_kitchen (1).png"));
          
            burgerBunImg = ImageIO.read(getClass().getResourceAsStream("images/burgerBuns.png"));
            cookingPattyImg = ImageIO.read(getClass().getResourceAsStream("images/cookingPatty.png"));
            cookedPattyImg = ImageIO.read(getClass().getResourceAsStream("images/cookedPatty.png"));
            rawPattyImg = ImageIO.read(getClass().getResourceAsStream("images/rawPatty.png"));
            pattiesImg = ImageIO.read(getClass().getResourceAsStream("/images/patties.png"));
            rawFriesImg = ImageIO.read(getClass().getResourceAsStream("images/friess.png"));
            cookingFriesImg = ImageIO.read(getClass().getResourceAsStream("images/rawFries.png"));
            cookedFriesImg = ImageIO.read(getClass().getResourceAsStream("images/cookedFries.png"));
            rawHotdogImg = ImageIO.read(getClass().getResourceAsStream("images/rawHotdog.png"));
            hotdogsssImg = ImageIO.read(getClass().getResourceAsStream("images/hotdogs.png"));
            hotdogBunImg = ImageIO.read(getClass().getResourceAsStream("images/hotdogBuns.png"));
            cookedHotdogImg = ImageIO.read(getClass().getResourceAsStream("images/cookedHotdog.png"));
            cupPileImg = ImageIO.read(getClass().getResourceAsStream("images/cupPile.png"));
            sodaMachineImg = ImageIO.read(getClass().getResourceAsStream("images/sodaMachine.png"));
            sodaMachineFillImg = ImageIO.read(getClass().getResourceAsStream("images/sodaMachineFill.png"));
            burgerFinishedImg = ImageIO.read(getClass().getResourceAsStream("images/burger (1).png"));
            friesContainerImg = ImageIO.read(getClass().getResourceAsStream("images/friesContainer.png"));
            friesFinishedImg = ImageIO.read(getClass().getResourceAsStream("images/fries.png"));
            hotdogWithBunImg = ImageIO.read(getClass().getResourceAsStream("images/hotdogwBun.png"));
            hotdogImg = ImageIO.read(getClass().getResourceAsStream("images/hotdog.png"));
            sodaImg = ImageIO.read(getClass().getResourceAsStream("images/soda.png"));
            trayImg = ImageIO.read(getClass().getResourceAsStream("images/tray.png"));
            ketchupImg = ImageIO.read(getClass().getResourceAsStream("images/ketchup.png"));
            mustardImg = ImageIO.read(getClass().getResourceAsStream("images/mustard.png"));

            System.out.println("✅ Backgrounds loaded successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error loading backgrounds:");
            e.printStackTrace();
        }
        System.out.println("GamePanel: constructed");
    }

    public void startGameThread() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int frameCount = 0;

        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                if (!paused) {
                    update();
                }
                repaint();
                delta--;
                frameCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + frameCount + " | Station: " + currentStation + (paused ? " (Paused)" : ""));
                frameCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
    }

    private Rectangle settingsButton;
    private Rectangle homeButton;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch (currentStation) {
            case FRONT:
                if (frontBg != null) g2.drawImage(frontBg, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                try {
                    drawCustomerAndDialog(g2);  
                } catch (Exception e) {
                    System.out.println("Error drawing customer: " + e.getMessage());
                }
                drawTrayOnFront(g2);
                break;
            case KITCHEN:
                if (kitchenBg != null) g2.drawImage(kitchenBg, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                break;
            case INSIDE_KITCHEN:
                if (insideKitchenBg != null) {
                    g2.drawImage(insideKitchenBg, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
                  
                    if (burgerBunImg != null) g2.drawImage(burgerBunImg, burgerBunHotspot.x, burgerBunHotspot.y, burgerBunHotspot.width, burgerBunHotspot.height, null);
                   
                    if (hotdogBunImg != null) g2.drawImage(hotdogBunImg,  hotdogBunHotspot.x,  hotdogBunHotspot.y,  hotdogBunHotspot.width,  hotdogBunHotspot.height,  null);
                   
                    if (pattiesImg != null) g2.drawImage(pattiesImg, rawPattyHotspot.x, rawPattyHotspot.y, rawPattyHotspot.width, rawPattyHotspot.height, null);
                   
                    if (rawFriesImg != null) g2.drawImage(rawFriesImg, rawFriesHotspot.x, rawFriesHotspot.y, rawFriesHotspot.width, rawFriesHotspot.height, null);
                  
                    if (friesContainerImg != null) g2.drawImage(friesContainerImg, friesContainerHotspot.x, friesContainerHotspot.y, friesContainerHotspot.width, friesContainerHotspot.height, null);
                   
                    if (hotdogsssImg != null) g2.drawImage(hotdogsssImg, rawHotdogHotspot.x, rawHotdogHotspot.y, rawHotdogHotspot.width, rawHotdogHotspot.height, null);
                   
                    if (cupPileImg != null) g2.drawImage(cupPileImg, cupsHotspot.x, cupsHotspot.y, cupsHotspot.width, cupsHotspot.height, null);
                    if (sodaMachineFilling) {
                        if (sodaMachineFillImg != null) g2.drawImage(sodaMachineFillImg, sodaMachineHotspot.x, sodaMachineHotspot.y, sodaMachineHotspot.width, sodaMachineHotspot.height, null);
                    } else {
                        if (sodaMachineImg != null) g2.drawImage(sodaMachineImg, sodaMachineHotspot.x, sodaMachineHotspot.y, sodaMachineHotspot.width, sodaMachineHotspot.height, null);
                    }

                   
                    int panX = 580, panY = 360, panW = 200, panH = 120;
                    
                   
                    if (pattyCooking) {
                        g2.drawImage(rawPattyImg, panX, panY, panW, panH, null);
                    } else if (pattyCooked) {
                        g2.drawImage(cookedPattyImg, panX, panY, panW, panH, null);
                    }
                    
                  
                    if (friesCooking) {
                        g2.drawImage(cookingFriesImg, panX + 220, panY, panW, panH, null);
                    } else if (friesCooked) {
                        g2.drawImage(cookedFriesImg, panX + 220, panY, panW, panH, null);
                    }
                    
                 
                    if (hotdogCooking) {
                        g2.drawImage(rawHotdogImg, panX + -220, panY, panW, panH, null);
                    } else if (hotdogCooked) {
                        g2.drawImage(cookedHotdogImg, panX + -220, panY, panW, panH, null);
                    }
                    
                   
                    int trayX = 939, trayY = 315;
                    g2.drawImage(trayImg, trayX, trayY, 315, 248, null);
                    
                    if (burgerOnKitchenTray) {
                        g2.drawImage(burgerFinishedImg, trayX + 20, trayY + 20, 80, 80, null);
                    }
                    if (friesOnKitchenTray) {
                        g2.drawImage(friesFinishedImg, trayX + 110, trayY + 20, 80, 80, null);
                    }
                    if (hotdogOnKitchenTray) {
                        if (hotdogHasBun) {
                            g2.drawImage(hotdogWithBunImg, trayX + 200, trayY + 20, 80, 80, null);
                        } else {
                            g2.drawImage(hotdogImg, trayX + 200, trayY + 20, 80, 80, null);
                        }
                    }
                    if (sodaOnKitchenTray) {
                        g2.drawImage(sodaImg, trayX + 20, trayY + 60, 80, 80, null);
                    }
                } else {
                    g2.setColor(Color.GRAY);
                    g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Trebuchet MS", Font.BOLD, 48));
                    g2.drawString("Inside Kitchen View", 400, 350);
                }
                break;
        }

       
        drawTopBar(g2);
        drawUI(g2);

        drawMouseCoordinates(g2);

        if (paused) {
            g2.setColor(new Color(0, 0, 0, 160));
            g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Trebuchet MS", Font.BOLD, 40));
            g2.drawString("⏸ GAME PAUSED", SCREEN_WIDTH / 2 - 180, SCREEN_HEIGHT / 2 - 20);

            g2.setFont(new Font("Trebuchet MS", Font.PLAIN, 24));
            g2.drawString("Press P to Resume", SCREEN_WIDTH / 2 - 110, SCREEN_HEIGHT / 2 + 30);
            g2.drawString("Press H to Return to Menu", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 70);
        }

       
        if (currentStation == Station.KITCHEN) {
            String fontName = "Trebuchet MS"; 
            Font restaurantFont = new Font(fontName, Font.BOLD, 28);
            g2.setFont(restaurantFont);
            int yBase = SCREEN_HEIGHT - 110;
            int bgX = 20;
            int bgY = yBase - 30;
            int bgWidth = 700;
            int bgHeight = 110;
            g2.setColor(new Color(0, 0, 0, 160)); 
            g2.fillRoundRect(bgX, bgY, bgWidth, bgHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.drawString("click enter to go to kitchen", 40, yBase);
            g2.drawString("click spacebar to go back to counter", 40, yBase + 32);
            g2.drawString("click backspace to go exit the kitchen", 40, yBase + 64);
        }
       
        if (currentStation == Station.INSIDE_KITCHEN) {
            String fontName = "Trebuchet MS";
            Font restaurantFont = new Font(fontName, Font.BOLD, 36);
            g2.setFont(restaurantFont);
            int bgX = 0;
            int bgY = 600; 
            int bgWidth = SCREEN_WIDTH;
            int bgHeight = 90;
            g2.setColor(new Color(0, 0, 0, 220));
            g2.fillRect(bgX, bgY, bgWidth, bgHeight);
            g2.setColor(Color.YELLOW);
            String instructionText;
            if (currentFoodType != FoodType.NONE && kitchenInstruction != null && !kitchenInstruction.isEmpty()) {
                instructionText = kitchenInstruction;
            } else {
                instructionText = "Select an order to start cooking. (Return to counter and take an order)";
            }
            g2.drawString(instructionText, bgX + 40, bgY + 60);
        }
       
        if (currentStation == Station.FRONT && showServeInstructionInFront && serveInstructionText != null && !serveInstructionText.isEmpty()) {
          
            if (trayOnTable && serveInstructionText != null && !serveInstructionText.isEmpty()) {
                String fontName = "Trebuchet MS";
                Font restaurantFont = new Font(fontName, Font.BOLD, 36);
                g2.setFont(restaurantFont);
                int bgX = 0;
                int bgY = 60;
                int bgWidth = SCREEN_WIDTH;
                int bgHeight = 90;
                g2.setColor(new Color(0, 0, 0, 220));
                g2.fillRect(bgX, bgY, bgWidth, bgHeight);
                g2.setColor(Color.YELLOW);
                g2.drawString("click the food", bgX + 40, bgY + 60);
            }
        }
        g2.dispose();
    }

    private void drawTopBar(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, SCREEN_WIDTH, 60);

        settingsButton = new Rectangle(SCREEN_WIDTH - 120, 10, 40, 40);
        homeButton = new Rectangle(SCREEN_WIDTH - 60, 10, 40, 40);

        g2.setColor(Color.WHITE);
        g2.fill3DRect(settingsButton.x, settingsButton.y, settingsButton.width, settingsButton.height, true);
        g2.fill3DRect(homeButton.x, homeButton.y, homeButton.width, homeButton.height, true);

        g2.setColor(Color.BLACK);
        drawGearIcon(g2, settingsButton.x + 5, settingsButton.y + 5, 30);
        drawHomeIcon(g2, homeButton.x + 5, homeButton.y + 5, 30);
    }

    private void drawGearIcon(Graphics2D g2, int x, int y, int size) {
        g2.drawOval(x + size/4, y + size/4, size/2, size/2);
        for (int i = 0; i < 8; i++) {
            double angle = Math.PI * i / 4;
            int x1 = x + size/2 + (int)(Math.cos(angle) * size/3);
            int y1 = y + size/2 + (int)(Math.sin(angle) * size/3);
            int x2 = x + size/2 + (int)(Math.cos(angle) * size/2);
            int y2 = y + size/2 + (int)(Math.sin(angle) * size/2);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawHomeIcon(Graphics2D g2, int x, int y, int size) {
        int[] xPoints = {x + size/2, x, x + size};
        int[] yPoints = {y, y + size/2, y + size/2};
        g2.fillPolygon(xPoints, yPoints, 3);
        g2.fillRect(x + size/4, y + size/2, size/2, size/2);
    }

    private void drawUI(Graphics2D g2) {
     
        Font uiFont = new Font("Arial", Font.BOLD, 24);
        g2.setFont(uiFont);
        g2.setColor(Color.WHITE);

       
        g2.drawString("DAY: " + dayManager.getCurrentDay(), 20, 40);
        g2.drawString(gameState.getTimeString(), 150, 40);


       
        Customer currentCustomer = gameState.getCurrentCustomer();
        if (currentCustomer != null) {
            int patienceWidth = 200;
            int patienceHeight = 30;
            int x = 300;
            int y = 15;

           
            g2.setColor(Color.GRAY);
            g2.fillRect(x, y, patienceWidth, patienceHeight);

         
            if (currentCustomer.isAngry()) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.GREEN);
            }
            int width = (int)((currentCustomer.getPatience() / 100.0) * patienceWidth);
            g2.fillRect(x, y, width, patienceHeight);

            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, patienceWidth, patienceHeight);
            g2.drawString(String.format("%.0f%%", currentCustomer.getPatience()), x + patienceWidth + 10, y + 25);
        }

  
    g2.drawString(String.format("₱%.2f", gameState.getTotalMoney()), SCREEN_WIDTH - 260, 40);
    }

    private void drawTrayOnFront(Graphics2D g2) {
        if (!trayOnTable) return;
        serveInstructionText = "click the food";
        showServeInstructionInFront = true;
        int trayX = SCREEN_WIDTH/2 - 150;
        int trayY = SCREEN_HEIGHT - 180;
        
        g2.drawImage(trayImg, trayX, trayY, 300, 150, null);
        
        if (burgerOnFrontTray) {
            g2.drawImage(burgerFinishedImg, trayX + 20, trayY + 20, 80, 80, null);
        }
        if (friesOnFrontTray) {
            g2.drawImage(friesFinishedImg, trayX + 110, trayY + 20, 80, 80, null);
        }
        if (hotdogOnFrontTray) {
            g2.drawImage(hotdogImg, trayX + 200, trayY + 20, 80, 80, null);
        }
        if (sodaOnFrontTray) {
            g2.drawImage(sodaImg, trayX + 20, trayY + 60, 80, 80, null);
        }
    }

    private void checkAssemblyForOrder() {
        Customer c = gameState.getCurrentCustomer();
        if (c == null) return;
       
        if (c.needsItem(Customer.OrderType.BURGER) && currentAssembly.contains("bun") && currentAssembly.contains("cookedPatty")) {
            trayItems.add("BURGER");
            currentAssembly.remove("bun");
            currentAssembly.remove("cookedPatty");
            trayOnTable = true;
            showServeInstructionInFront = true;
            serveInstructionText = "click the food";
            currentAssembly.clear();
            currentStation = Station.FRONT;
            System.out.println("Burger assembled and moved to tray/table");
            return;
        }

        if (c.needsItem(Customer.OrderType.HOTDOG) && currentAssembly.contains("HOTDOG_COOKED")) {
            trayItems.add("HOTDOG");
            currentAssembly.remove("HOTDOG_COOKED");
            trayOnTable = true;
            showServeInstructionInFront = true;
            serveInstructionText = "click the food";
            currentAssembly.clear();
            currentStation = Station.FRONT;
            System.out.println("Hotdog assembled and moved to tray/table");
            return;
        }

        if (c.needsItem(Customer.OrderType.FRIES) && currentAssembly.contains("FRIES")) {
            trayItems.add("FRIES");
            currentAssembly.remove("FRIES");
            trayOnTable = true;
            showServeInstructionInFront = true;
            serveInstructionText = "click the food";
            currentAssembly.clear();
            currentStation = Station.FRONT;
            System.out.println("Fries moved to tray/table");
            return;
        }

        if (c.needsItem(Customer.OrderType.SODA) && trayItems.contains("SODA")) {
            trayOnTable = true;
            showServeInstructionInFront = true;
            serveInstructionText = "click the food";
            currentStation = Station.FRONT;
            System.out.println("Soda moved to tray/table");
            return;
        }
    }

    private void drawCustomerAndDialog(Graphics2D g2) throws IOException {
        Customer currentCustomer = gameState.getCurrentCustomer();
        if (currentCustomer != null) {
            try {
                int customerImageIndex = currentCustomer.getImageIndex();
                BufferedImage customerImage;
                
                if (currentCustomer.isAngry()) {
                    customerImage = ImageIO.read(getClass().getResourceAsStream("images/customer" + (customerImageIndex + 1) + "angry.png"));
                } else {
                    customerImage = ImageIO.read(getClass().getResourceAsStream("images/customer" + (customerImageIndex + 1) + ".png"));
                }
                
                if (customerImage != null) {
                    g2.drawImage(customerImage, 50, SCREEN_HEIGHT/2, 200, 300, null);
                }
            } catch (IOException e) {
                System.out.println("Error loading customer image: " + e.getMessage());
            }

            g2.setColor(Color.WHITE);
            int bubbleX = 280;
            int bubbleY = SCREEN_HEIGHT/2 - 50;
            g2.fillRoundRect(bubbleX, bubbleY, 400, 100, 20, 20);
            g2.setColor(Color.BLACK);
            g2.drawString(currentCustomer.getOrder(), bubbleX + 20, bubbleY + 50);

            g2.setColor(Color.WHITE);
            g2.fillRect(bubbleX, bubbleY + 120, 100, 40);
            g2.setColor(Color.BLACK);
            g2.drawString("Okay", bubbleX + 20, bubbleY + 145);
        }
    }

    private enum FoodType { NONE, FRIES, HOTDOG, BURGER, SODA }
    private FoodType currentFoodType = FoodType.NONE;
    private int kitchenStep = 0;
    private String kitchenInstruction = "";

    private void updateKitchenInstruction() {
        switch (currentFoodType) {
            case FRIES:
                if (kitchenStep == 0) kitchenInstruction = "🍟 Click the fries to start cooking. Please wait for it to be cooked.";
                else if (kitchenStep == 1) kitchenInstruction = "🍟 Click the fries container.";
                else if (kitchenStep == 2) kitchenInstruction = "🍟 Click the tray.";
                else if (kitchenStep == 3) kitchenInstruction = "🍟 Click the fries to serve it.";
                else kitchenInstruction = "";
                break;
            case HOTDOG:
                if (kitchenStep == 0) kitchenInstruction = "🌭 Click the hotdog to start cooking. Please wait for it to be cooked.";
                else if (kitchenStep == 1) kitchenInstruction = "🌭 Click the bun.";
                else if (kitchenStep == 2) kitchenInstruction = "🌭 Click the tray.";
                else if (kitchenStep == 3) kitchenInstruction = "🌭 Click the hotdog to serve it.";
                else kitchenInstruction = "";
                break;
            case BURGER:
                if (kitchenStep == 0) kitchenInstruction = "🍔 Click the patty to start cooking. Please wait for it to be cooked.";
                else if (kitchenStep == 1) kitchenInstruction = "🍔 Click the burger bun.";
                else if (kitchenStep == 2) kitchenInstruction = "🍔 Click the tray.";
                else if (kitchenStep == 3) kitchenInstruction = "🍔 Click the burger to serve it.";
                else kitchenInstruction = "";
                break;
            case SODA:
                if (kitchenStep == 0) kitchenInstruction = "🥤 Click the pile of cup. Please wait until the cup is filled.";
                else if (kitchenStep == 1) kitchenInstruction = "🥤 Click the soda machine.";
                else if (kitchenStep == 2) kitchenInstruction = "🥤 Click the tray.";
                else if (kitchenStep == 3) kitchenInstruction = "🥤 Click the soda to serve it.";
                else kitchenInstruction = "";
                break;
            default:
                kitchenInstruction = "";
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_P) {
            paused = !paused;
            System.out.println(paused ? "Game Paused" : "Game Resumed");
            repaint();
            return;
        }

        if (paused && code == KeyEvent.VK_H) {
            System.out.println("Returning to Main Menu...");
            paused = false;
            running = false; 
            gm.showMenu();  
            return;
        }

        if (paused) return;

        if (code == KeyEvent.VK_SPACE) {
            if (currentStation == Station.FRONT)
                currentStation = Station.KITCHEN;
            else if (currentStation == Station.KITCHEN)
                currentStation = Station.FRONT;
            System.out.println("Switched to " + currentStation + " view.");
        } else if (code == KeyEvent.VK_ENTER) {
            if (currentStation == Station.KITCHEN) {
               
                Customer c = gameState.getCurrentCustomer();
                if (c != null) {
                    if (c.needsItem(Customer.OrderType.FRIES)) {
                        currentFoodType = FoodType.FRIES;
                        kitchenStep = 0;
                    } else if (c.needsItem(Customer.OrderType.HOTDOG)) {
                        currentFoodType = FoodType.HOTDOG;
                        kitchenStep = 0;
                    } else if (c.needsItem(Customer.OrderType.BURGER)) {
                        currentFoodType = FoodType.BURGER;
                        kitchenStep = 0;
                    } else if (c.needsItem(Customer.OrderType.SODA)) {
                        currentFoodType = FoodType.SODA;
                        kitchenStep = 0;
                    } else {
                        currentFoodType = FoodType.NONE;
                        kitchenStep = 0;
                    }
                }
                updateKitchenInstruction();
                currentStation = Station.INSIDE_KITCHEN;
                System.out.println("Entered INSIDE_KITCHEN view.");
            }
        } else if (code == KeyEvent.VK_BACK_SPACE) {
            if (currentStation == Station.INSIDE_KITCHEN) {
                currentStation = Station.KITCHEN;
                currentFoodType = FoodType.NONE;
                kitchenStep = 0;
                kitchenInstruction = "";
                System.out.println("Returned to KITCHEN view.");
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
public void mouseClicked(MouseEvent e) {
    if (paused) return; 
    int x = e.getX();
    int y = e.getY();
    
    
    if (settingsButton != null && settingsButton.contains(x, y)) {
        gm.showSettings();
        return;
    }
    
    if (homeButton != null && homeButton.contains(x, y)) {
        gm.showMenu();
        return;
    }
    
    if (currentStation == Station.FRONT) {
        if (trayOnTable) {
            int trayX = SCREEN_WIDTH/2 - 150;
            int trayY = SCREEN_HEIGHT - 180;
            Rectangle trayRect = new Rectangle(trayX, trayY, 300, 150);
            
            Customer c = gameState.getCurrentCustomer();
            if (c != null) {
                boolean handled = false;

                Rectangle burgerRect = new Rectangle(trayX + 20, trayY + 20, 80, 80);
                if (burgerOnFrontTray && burgerRect.contains(x, y)) {
                    boolean served = c.serveItem(Customer.OrderType.BURGER);
                    if (served) {
                        burgerOnFrontTray = false;
                        double price = Customer.getPriceFor(Customer.OrderType.BURGER);
                        gameState.addMoney(price);
                        System.out.println("Served BURGER for: " + price);
                        dayManager.customerServed(price);
                        if (dayManager.isEndOfDay()) { showEndOfDayPopUp.show(dayManager, gm); return; }
                        if (c.isOrderComplete()) gameState.completeCurrentCustomer();
                    } else {
                        System.out.println("Wrong item: burger not in order");
                        c.decreasePatience();
                    }
                    handled = true;
                }

                Rectangle friesRect = new Rectangle(trayX + 110, trayY + 20, 80, 80);
                if (!handled && friesOnFrontTray && friesRect.contains(x, y)) {
                    boolean served = c.serveItem(Customer.OrderType.FRIES);
                    if (served) {
                        friesOnFrontTray = false;
                        double price = Customer.getPriceFor(Customer.OrderType.FRIES);
                        gameState.addMoney(price);
                        System.out.println("Served FRIES for: " + price);
                        dayManager.customerServed(price);
                        if (dayManager.isEndOfDay()) { showEndOfDayPopUp.show(dayManager, gm); return; }
                        if (c.isOrderComplete()) gameState.completeCurrentCustomer();
                    } else {
                        System.out.println("Wrong item: fries not in order");
                        c.decreasePatience();
                    }
                    handled = true;
                }

                Rectangle hotdogRect = new Rectangle(trayX + 200, trayY + 20, 80, 80);
                if (!handled && hotdogOnFrontTray && hotdogRect.contains(x, y)) {
                    boolean served = c.serveItem(Customer.OrderType.HOTDOG);
                    if (served) {
                        hotdogOnFrontTray = false;
                        double price = Customer.getPriceFor(Customer.OrderType.HOTDOG);
                        gameState.addMoney(price);
                        System.out.println("Served HOTDOG for: " + price);
                        dayManager.customerServed(price);
                        if (dayManager.isEndOfDay()) { showEndOfDayPopUp.show(dayManager, gm); return; }
                        if (c.isOrderComplete()) gameState.completeCurrentCustomer();
                    } else {
                        System.out.println("Wrong item: hotdog not in order");
                        c.decreasePatience();
                    }
                    handled = true;
                }

                Rectangle sodaRect = new Rectangle(trayX + 20, trayY + 60, 80, 80);
                if (!handled && sodaOnFrontTray && sodaRect.contains(x, y)) {
                    boolean served = c.serveItem(Customer.OrderType.SODA);
                    if (served) {
                        sodaOnFrontTray = false;
                        double price = Customer.getPriceFor(Customer.OrderType.SODA);
                        gameState.addMoney(price);
                        System.out.println("Served SODA for: " + price);
                        dayManager.customerServed(price);
                        if (dayManager.isEndOfDay()) { showEndOfDayPopUp.show(dayManager, gm); return; }
                        if (c.isOrderComplete()) gameState.completeCurrentCustomer();
                    } else {
                        System.out.println("Wrong item: soda not in order");
                        c.decreasePatience();
                    }
                    handled = true;
                }

                if (!handled && trayRect.contains(x, y)) {
                   
                    System.out.println("Clicked tray but no matching item to serve");
                    c.decreasePatience();
                }

                if (!burgerOnFrontTray && !friesOnFrontTray && !hotdogOnFrontTray && !sodaOnFrontTray) {
                    trayOnTable = false;
                    showServeInstructionInFront = false;
                }

                repaint();
                return;
            }
        }


            int bubbleX = 280;
            int bubbleY = SCREEN_HEIGHT/2 - 50;
            Rectangle okayButton = new Rectangle(bubbleX, bubbleY + 120, 100, 40);
            
            if (okayButton.contains(x, y)) {
                gameState.startCurrentCustomerTimer();
                currentStation = Station.KITCHEN;
            }
        } else if (currentStation == Station.INSIDE_KITCHEN) {
       
            if (rawPattyHotspot.contains(x, y) && !pattyCooking && !pattyCooked && currentFoodType == FoodType.BURGER && kitchenStep == 0) {
                pattyCooking = true;
                cookTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pattyCooking = false;
                        pattyCooked = true;
                        if (currentFoodType == FoodType.BURGER && kitchenStep == 0) {
                            kitchenStep = 1;
                            updateKitchenInstruction();
                        }
                        repaint();
                    }
                }, 5000);
            } else if (burgerBunHotspot.contains(x, y) && pattyCooked) {
                    pattyCooked = false;
                    burgerOnKitchenTray = true;
                    System.out.println("Burger assembled");
                   
                    if (currentFoodType == FoodType.BURGER && kitchenStep == 1) {
                        kitchenStep = 2;
                        updateKitchenInstruction();
                    }
                    repaint();
            }

            if (rawFriesHotspot.contains(x, y) && !friesCooking && !friesCooked && currentFoodType == FoodType.FRIES && kitchenStep == 0) {
                friesCooking = true;
                cookTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        friesCooking = false;
                        friesCooked = true;
                        if (currentFoodType == FoodType.FRIES && kitchenStep == 0) {
                            kitchenStep = 1;
                            updateKitchenInstruction();
                        }
                        repaint();
                    }
                }, 5000);
            } else if (friesCooked) {
                    if (friesContainerHotspot.contains(x, y)) {
                        friesCooked = false;
                        friesOnKitchenTray = true;
                        System.out.println("Fries moved to tray (via container)");
                       
                        if (currentFoodType == FoodType.FRIES && kitchenStep == 1) {
                            kitchenStep = 2;
                            updateKitchenInstruction();
                        }
                        repaint();
                    }
            }

            if (rawHotdogHotspot.contains(x, y) && !hotdogCooking && !hotdogCooked && currentFoodType == FoodType.HOTDOG && kitchenStep == 0) {
                hotdogCooking = true;
                cookTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        hotdogCooking = false;
                        hotdogCooked = true;
                        if (currentFoodType == FoodType.HOTDOG && kitchenStep == 0) {
                            kitchenStep = 1;
                            updateKitchenInstruction();
                        }
                        repaint();
                    }
                }, 5000);
            } else if (hotdogBunHotspot.contains(x, y) && hotdogCooked) {
                hotdogCooked = false;
                hotdogHasBun = true;
                hotdogOnKitchenTray = true;
                System.out.println("Hotdog assembled with bun");
                
                if (currentFoodType == FoodType.HOTDOG && kitchenStep == 1) {
                    kitchenStep = 2;
                    updateKitchenInstruction();
                }
                repaint();
            }

            // Soda preparation
            if (cupsHotspot.contains(x, y) && !cupSelected && currentFoodType == FoodType.SODA && kitchenStep == 0) {
                cupSelected = true;
                kitchenStep = 1;
                updateKitchenInstruction();
                repaint();
            } else if (sodaMachineHotspot.contains(x, y) && cupSelected && !sodaMachineFilling && currentFoodType == FoodType.SODA && kitchenStep == 1) {
                sodaMachineFilling = true;
                cookTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        sodaMachineFilling = false;
                        cupSelected = false;
                        sodaOnKitchenTray = true;
                        if (currentFoodType == FoodType.SODA && kitchenStep == 1) {
                            kitchenStep = 2;
                            updateKitchenInstruction();
                        }
                        repaint();
                    }
                }, 5000);
            }

            Rectangle kitchenTrayRect = new Rectangle(939, 315, 315, 248);
            if (kitchenTrayRect.contains(x, y)) {
                if (burgerOnKitchenTray || friesOnKitchenTray || hotdogOnKitchenTray || sodaOnKitchenTray) {
             
                    burgerOnFrontTray = burgerOnKitchenTray;
                    friesOnFrontTray = friesOnKitchenTray;
                    hotdogOnFrontTray = hotdogOnKitchenTray;
                    sodaOnFrontTray = sodaOnKitchenTray;
                    
                  
                    burgerOnKitchenTray = false;
                    friesOnKitchenTray = false;
                    hotdogOnKitchenTray = false;
                    sodaOnKitchenTray = false;
                    
                  
                    trayOnTable = true;
                    currentStation = Station.FRONT;
                    System.out.println("Items moved to front tray");
                    repaint();
                }
            }
        }
        System.out.println("Mouse clicked at: " + x + ", " + y);
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
        mouseX = -1;
        mouseY = -1;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    private void drawMouseCoordinates(Graphics2D g2) {
        if (mouseX >= 0 && mouseY >= 0) {
          
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Consolas", Font.BOLD, 16));
            String coords = String.format("Mouse Position - X: %d, Y: %d", mouseX, mouseY);
            g2.drawString(coords, 10, SCREEN_HEIGHT - 20);

            g2.setColor(Color.RED);
            g2.drawLine(mouseX - 5, mouseY, mouseX + 5, mouseY);
            g2.drawLine(mouseX, mouseY - 5, mouseX, mouseY + 5);
        }
    }
}