import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final int TILE_SIZE = 20;
    private static final int GRID_SIZE = 20;
    private int[] x, y;
    private int length;
    private char direction;
    private boolean running;
    private int foodX, foodY;
    private Timer timer;
    private int score;

    public GamePanel() {
        setPreferredSize(new Dimension(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initializeGame();
    }

    private void initializeGame() {
        x = new int[GRID_SIZE * GRID_SIZE];
        y = new int[GRID_SIZE * GRID_SIZE];
        length = 3;
        direction = 'R';
        running = true;

        for (int i = 0; i < length; i++) {
            x[i] = TILE_SIZE * GRID_SIZE / 2 - i * TILE_SIZE;
            y[i] = TILE_SIZE * GRID_SIZE / 2;
        }

        placeFood();

        timer = new Timer(100, this);
        timer.start();
    }

    private void placeFood() {
        Random random = new Random();
        foodX = random.nextInt(GRID_SIZE) * TILE_SIZE;
        foodY = random.nextInt(GRID_SIZE) * TILE_SIZE;

        // Make sure the food is not placed on the snake
        for (int i = 0; i < length; i++) {
            if (foodX == x[i] && foodY == y[i]) {
                placeFood();
                return;
            }
        }
    }

    private void move() {
        for (int i = length - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= TILE_SIZE;
                break;
            case 'D':
                y[0] += TILE_SIZE;
                break;
            case 'L':
                x[0] -= TILE_SIZE;
                break;
            case 'R':
                x[0] += TILE_SIZE;
                break;
        }
    }

    private void checkCollision() {
        if (x[0] < 0 || x[0] >= TILE_SIZE * GRID_SIZE || y[0] < 0 || y[0] >= TILE_SIZE * GRID_SIZE) {
            running = false;
            timer.stop();
        }

        for (int i = 1; i < length; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                timer.stop();
            }
        }
    }

    private void paintSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (int i = 0; i < length; i++) {
            g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
        }
    }

    private void paintFood(Graphics g) {
        // Implement food drawing logic here
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Game Over", TILE_SIZE * GRID_SIZE / 2 - 70, TILE_SIZE * GRID_SIZE / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (running) {
            paintSnake(g);
            paintFood(g);
        } else {
            gameOver(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
