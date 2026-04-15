import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class Main {

    static Color pink = new Color(255, 182, 193);
    static Color softPink = new Color(255, 204, 229);
    static Color darkBg = new Color(40, 40, 40);
    static Color darkField = new Color(70, 70, 70);

    static boolean darkTheme = false;

    public static void main(String[] args) {
        showAuthMenu();
    }

    static void showAuthMenu() {
        JFrame frame = new JFrame("💖 Добро пожаловать");
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        stylePanel(panel);

        JLabel title = new JLabel("💖 Выберите действие", SwingConstants.CENTER);

        JButton loginBtn = new JButton("Войти 🔐");
        JButton registerBtn = new JButton("Регистрация 📝");
        JButton themeBtn = new JButton("Сменить тему 🌙");

        styleButton(loginBtn);
        styleButton(registerBtn);
        styleButton(themeBtn);

        loginBtn.addActionListener(e -> {
            frame.dispose();
            showLogin();
        });

        registerBtn.addActionListener(e -> {
            frame.dispose();
            showRegister();
        });

        themeBtn.addActionListener(e -> {
            darkTheme = !darkTheme;
            frame.dispose();
            showAuthMenu();
        });

        panel.add(title);
        panel.add(loginBtn);
        panel.add(registerBtn);
        panel.add(themeBtn);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void showRegister() {
        JFrame frame = new JFrame("💗 Регистрация");
        frame.setSize(450, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        stylePanel(panel);

        JTextField name = new JTextField();
        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();
        JPasswordField confirm = new JPasswordField();

        JCheckBox showPass = new JCheckBox("Показать пароль");

        JLabel result = new JLabel("", SwingConstants.CENTER);

        JButton register = new JButton("Создать 💕");
        JButton back = new JButton("Назад");

        styleButton(register);
        styleButton(back);

        showPass.addActionListener(e -> {
            char c = showPass.isSelected() ? (char)0 : '*';
            pass.setEchoChar(c);
            confirm.setEchoChar(c);
        });

        register.addActionListener(e -> {
            try {
                FileWriter writer = new FileWriter("users.txt", true);
                if (name.getText().isEmpty() || email.getText().isEmpty() ||
                        pass.getPassword().length == 0 || confirm.getPassword().length == 0) {
                    result.setText("Заполните все поля");
                    return;
                }

                String p1 = new String(pass.getPassword());
                String p2 = new String(confirm.getPassword());

                if (!p1.equals(p2)) {
                    result.setText("Пароли не совпадают");
                    return;
                }

                writer.write(name.getText() + "," + email.getText() + "," + p1 + "\n");
                writer.close();

                result.setText("Готово 💖");
            } catch (Exception ex) {
                result.setText("Ошибка");
            }
        });

        back.addActionListener(e -> {
            frame.dispose();
            showAuthMenu();
        });

        panel.add(new JLabel("Имя:"));
        panel.add(name);
        panel.add(new JLabel("Email:"));
        panel.add(email);
        panel.add(new JLabel("Пароль:"));
        panel.add(pass);
        panel.add(new JLabel("Повтор:"));
        panel.add(confirm);
        panel.add(showPass);
        panel.add(new JLabel(""));
        panel.add(register);
        panel.add(back);
        panel.add(result);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void showLogin() {
        JFrame frame = new JFrame("🔐 Вход");
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        stylePanel(panel);

        JTextField email = new JTextField();
        JPasswordField pass = new JPasswordField();

        JCheckBox showPass = new JCheckBox("Показать пароль");
        JLabel result = new JLabel("", SwingConstants.CENTER);

        JButton login = new JButton("Войти 💖");
        JButton back = new JButton("Назад");

        styleButton(login);
        styleButton(back);

        showPass.addActionListener(e -> {
            pass.setEchoChar(showPass.isSelected() ? (char)0 : '*');
        });

        login.addActionListener(e -> {
            HashMap<String, String> users = loadUsers();
            String eMail = email.getText();
            String p = new String(pass.getPassword());

            if (users.containsKey(eMail) && users.get(eMail).equals(p)) {
                frame.dispose();
                showProfile(eMail);
            } else {
                result.setText("Неверные данные");
            }
        });

        back.addActionListener(e -> {
            frame.dispose();
            showAuthMenu();
        });

        panel.add(new JLabel("Email:"));
        panel.add(email);
        panel.add(new JLabel("Пароль:"));
        panel.add(pass);
        panel.add(showPass);
        panel.add(new JLabel(""));
        panel.add(login);
        panel.add(back);
        panel.add(result);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void showProfile(String email) {
        JFrame frame = new JFrame("👤 Профиль");
        frame.setSize(350, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        stylePanel(panel);

        JLabel avatar = new JLabel("👩", SwingConstants.CENTER);
        avatar.setFont(new Font("Arial", Font.PLAIN, 40));

        JLabel info = new JLabel("Вы вошли как: " + email, SwingConstants.CENTER);

        JButton logout = new JButton("Выйти");
        styleButton(logout);

        logout.addActionListener(e -> {
            frame.dispose();
            showAuthMenu();
        });

        panel.add(avatar);
        panel.add(info);
        panel.add(logout);

        frame.add(panel);
        frame.setVisible(true);
    }

    static HashMap<String, String> loadUsers() {
        HashMap<String, String> map = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                map.put(data[1], data[2]);
            }
            reader.close();
        } catch (Exception ignored) {}
        return map;
    }

    static void stylePanel(JPanel panel) {
        if (darkTheme) {
            panel.setBackground(darkBg);
        } else {
            panel.setBackground(pink);
        }
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    }

    static void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        if (darkTheme) {
            btn.setBackground(darkField);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(softPink);
        }

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 105, 180));
            }
            public void mouseExited(MouseEvent e) {
                if (darkTheme) btn.setBackground(darkField);
                else btn.setBackground(softPink);
            }
        });
    }
}