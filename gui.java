package Memory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.Runnable;

public class gui {
    static int turns = 0;
    static ArrayList<String> turned = new ArrayList<String>();
    static int curplayer = 1;
    static int player1w = 0;
    static int player2w = 0;
    static int timeLeft = 6000;// 600,0 seconds; 10 minutes
    static boolean started = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Memory");
        frame.setSize(400, 420);
        JPanel memory = new JPanel();
        JPanel text = new JPanel();
        JLabel text1 = new JLabel("Player 1 (0)");
        JLabel text2 = new JLabel("Player 2 (0)");
        JPanel time = new JPanel();
        JLabel timetext = new JLabel("10:00,0");
        text1.setForeground(Color.GREEN);
        text2.setForeground(Color.BLACK);
        text1.setFont(new Font("Arial", Font.PLAIN, 25));
        text2.setFont(new Font("Arial", Font.PLAIN, 25));
        timetext.setFont(new Font("Arial", Font.PLAIN, 25));
        text.add(text1);
        text.add(text2);
        time.add(timetext);
        ArrayList<JButton> memoryButtons = new ArrayList<JButton>();
        for (int var1 = 1; var1 != 37; var1 += 1) {
            JButton button = new JButton("*");
            button.setFont(new Font("Arial", Font.PLAIN, 25));
            memoryButtons.add(button);
            memory.add(button);
        }
        ArrayList<String> chars = new ArrayList<>(Arrays.asList("+", "/", "-", "!", "?", "#", "@", "|", "=", "^", "~",
                "(", ")", "&", "°", "§", ".", ","));
        chars.addAll(chars);
        Collections.shuffle(chars);
        for (JButton button : memoryButtons) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (button.getText().equals("*")) {
                        button.setText(chars.get(memoryButtons.indexOf(button)));
                        turns++;
                        turned.add(chars.get(memoryButtons.indexOf(button)));
                        if (turns % 2 == 0) {
                            if (turned.get(turned.size() - 2) != turned.get(turned.size() - 1)) {
                                turns--;
                                turns--;
                                text1.setText("Player 1 (" + player1w + ")");
                                text2.setText("Player 2 (" + player2w + ")");
                                if (curplayer == 1) {
                                    curplayer++;
                                    text1.setForeground(Color.BLACK);
                                    text2.setForeground(Color.GREEN);
                                } else {
                                    curplayer--;
                                    text1.setForeground(Color.GREEN);
                                    text2.setForeground(Color.BLACK);
                                }
                            } else {
                                if (curplayer == 1) {
                                    player1w++;
                                    text1.setText("Player 1 (" + player1w + ")");
                                    text2.setText("Player 2 (" + player2w + ")");
                                    text1.setForeground(Color.GREEN);
                                    text2.setForeground(Color.BLACK);
                                } else {
                                    player2w++;
                                    text1.setText("Player 1 (" + player1w + ")");
                                    text2.setText("Player 2 (" + player2w + ")");
                                    text1.setForeground(Color.BLACK);
                                    text2.setForeground(Color.GREEN);
                                }
                                if (player1w + player2w == 18) {
                                    System.out.print("Memory: ");
                                    if (player1w > player2w) {
                                        text1.setForeground(Color.GREEN);
                                        text2.setForeground(Color.GREEN);
                                        text1.setText("Player 1");
                                        text2.setText("won");
                                        System.out.print("Player 1 won");
                                    } else if (player2w > player1w) {
                                        text1.setForeground(Color.RED);
                                        text2.setForeground(Color.RED);
                                        text1.setText("Player 2");
                                        text2.setText("won");
                                        System.out.print("Player 2 won");
                                    } else {
                                        text1.setForeground(Color.BLACK);
                                        text2.setForeground(Color.BLACK);
                                        text1.setText("Draw");
                                        text2.setText("");
                                        System.out.print("Draw");
                                    }
                                }
                            }
                        } else {
                            if (turned.size() > 2) {
                                if (turned.get(turned.size() - 3) != turned.get(turned.size() - 2)) {
                                    for (JButton button1 : memoryButtons) {
                                        if (button1.getText() == turned.get(turned.size() - 3)) {
                                            if (button1 != button) {
                                                button1.setText("*");
                                            }
                                        }
                                    }
                                    for (JButton button2 : memoryButtons) {
                                        if (button2.getText() == turned.get(turned.size() - 2)) {
                                            if (button2 != button) {
                                                button2.setText("*");
                                            }
                                        }
                                    }
                                    turned.remove(turned.get(turned.size() - 3));
                                    turned.remove(turned.get(turned.size() - 2));
                                }
                            } else {
                                started = true;
                            }
                        }
                    }
                }
            });
        }
        memory.setLayout(new GridLayout(6, 6));
        frame.getContentPane().add(BorderLayout.NORTH, text);
        frame.getContentPane().add(BorderLayout.CENTER, memory);
        frame.getContentPane().add(BorderLayout.SOUTH, time);
        frame.setVisible(true);
        ScheduledExecutorService exeserve = Executors.newSingleThreadScheduledExecutor();
        exeserve.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (started && timeLeft > 0) {
                    timeLeft--;
                    timetext.setText(time(timeLeft));
                } else if (timeLeft <= 0) {
                    System.out.print("Memory: Draw");
                    text1.setForeground(Color.BLACK);
                    text2.setForeground(Color.BLACK);
                    text1.setText("Draw");
                    text2.setText("");
                    for (JButton button : memoryButtons) {
                        button.setEnabled(false);
                    }
                }
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }

    public static String time(int timeLeft) {
        int timeLeftFormatLoop = 0;
        while (timeLeft >= 600) {
            timeLeftFormatLoop++;
            timeLeft = timeLeft - 600;
        }
        float timeLeftFloat = timeLeft;
        timeLeftFloat = timeLeftFloat / 10;
        String timeLeftFormat = Integer.toString(timeLeftFormatLoop) + ":" + Float.toString(timeLeftFloat);
        return timeLeftFormat;
    }
}
