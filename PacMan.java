import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener {

    class Block {
        int x, y, width, height;
        Image image;

        int startX, startY;
        char direction = 'U'; // U D L R
        int velocityX = 0, velocityY = 0;

        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void updateDirection(char direction) {
            char prev = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += velocityX;
            this.y += velocityY;

            for (Block wall : walls) {
                if (collision(this, wall)) {
                    this.x -= velocityX;
                    this.y -= velocityY;
                    this.direction = prev;
                    updateVelocity();
                }
            }
        }

        void updateVelocity() {
            int speed = tileSize / 4;
            switch (direction) {
                case 'U': velocityX = 0; velocityY = -speed; break;
                case 'D': velocityX = 0; velocityY = speed; break;
                case 'L': velocityX = -speed; velocityY = 0; break;
                case 'R': velocityX = speed; velocityY = 0; break;
            }
        }

        void reset() {
            x = startX;
            y = startY;
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage, blueGhostImage, orangeGhostImage, pinkGhostImage, redGhostImage;
    private Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;

    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;

    public PacMan() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        // load images
        wallImage = new ImageIcon(getClass().getResource("wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("redGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("pacmanRight.png")).getImage();

        loadMap();

        for (Block ghost : ghosts)
            ghost.updateDirection(directions[random.nextInt(4)]);

        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public void loadMap() {
        walls = new HashSet<>();
        foods = new HashSet<>();
        ghosts = new HashSet<>();

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                char ch = tileMap[r].charAt(c);

                int x = c * tileSize;
                int y = r * tileSize;

                switch (ch) {
                    case 'X':
                        walls.add(new Block(wallImage, x, y, tileSize, tileSize));
                        break;
                    case 'b':
                        ghosts.add(new Block(blueGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'o':
                        ghosts.add(new Block(orangeGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'p':
                        ghosts.add(new Block(pinkGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'r':
                        ghosts.add(new Block(redGhostImage, x, y, tileSize, tileSize));
                        break;
                    case 'P':
                        pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                        break;
                    case ' ':
                        foods.add(new Block(null, x + 14, y + 14, 4, 4));
                        break;
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        for (Block ghost : ghosts)
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);

        for (Block wall : walls)
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);

        g.setColor(Color.WHITE);
        for (Block food : foods)
            g.fillRect(food.x, food.y, food.width, food.height);

        g.setFont(new Font("Arial", Font.PLAIN, 18));

        if (gameOver)
            g.drawString("Game Over: " + score, tileSize / 2, tileSize / 2);
        else
            g.drawString("x" + lives + " Score: " + score, tileSize / 2, tileSize / 2);
    }

    public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        // wall collision
        for (Block wall : walls) {
            if (collision(pacman, wall)) {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

        // ghost collision
        for (Block ghost : ghosts) {
            if (collision(ghost, pacman)) {
                lives--;
                if (lives == 0) {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;

            for (Block wall : walls) {
                if (collision(ghost, wall)) {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                }
            }
        }

        // food collision
        Block eaten = null;
        for (Block food : foods) {
            if (collision(pacman, food)) {
                eaten = food;
                score += 10;
            }
        }
        foods.remove(eaten);

        if (foods.isEmpty()) {
            loadMap();
            resetPositions();
        }
    }

    public boolean collision(Block a, Block b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    public void resetPositions() {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;

        for (Block ghost : ghosts) {
            ghost.reset();
            ghost.updateDirection(directions[random.nextInt(4)]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) gameLoop.stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver = false;
            gameLoop.start();
        }

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP)     pacman.updateDirection('U');
        if (code == KeyEvent.VK_DOWN)   pacman.updateDirection('D');
        if (code == KeyEvent.VK_LEFT)   pacman.updateDirection('L');
        if (code == KeyEvent.VK_RIGHT)  pacman.updateDirection('R');

        switch (pacman.direction) {
            case 'U': pacman.image = pacmanUpImage; break;
            case 'D': pacman.image = pacmanDownImage; break;
            case 'L': pacman.image = pacmanLeftImage; break;
            case 'R': pacman.image = pacmanRightImage; break;
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pac-Man");
        PacMan game = new PacMan();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

