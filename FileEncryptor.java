import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class FileEncryptor extends JFrame {

    private JTextField filePathField;
    private JPasswordField passwordField;
    private JButton encryptBtn, decryptBtn, browseBtn;
    private JLabel statusLabel;
    private JProgressBar progressBar;

    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    public FileEncryptor() {
        initUI();
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Secure File Encryptor");
        setSize(500, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(new Color(245, 248, 250));

        // Header Title
        JLabel titleLabel = new JLabel("Secure File Vault");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Encrypt and Decrypt Files Easily");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // File Selection Section
        JPanel filePanel = new JPanel(new BorderLayout(10, 0));
        filePanel.setOpaque(false);
        filePanel.setBorder(new EmptyBorder(20, 0, 15, 0));
        
        filePathField = new JTextField();
        filePathField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filePathField.setEditable(false);
        filePathField.setPreferredSize(new Dimension(250, 35));
        
        browseBtn = createStyledButton("Browse...", new Color(52, 152, 219), Color.WHITE);
        browseBtn.addActionListener(e -> selectFile());
        
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseBtn, BorderLayout.EAST);

        // Password Section
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setOpaque(false);
        passwordPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pwdLabel.setForeground(new Color(52, 73, 94));
        
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setPreferredSize(new Dimension(0, 35));
        
        passwordPanel.add(pwdLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        // Buttons Section
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);
        
        encryptBtn = createStyledButton("Encrypt File", new Color(46, 204, 113), Color.WHITE);
        encryptBtn.addActionListener(e -> processFile(Cipher.ENCRYPT_MODE));
        
        decryptBtn = createStyledButton("Decrypt File", new Color(231, 76, 60), Color.WHITE);
        decryptBtn.addActionListener(e -> processFile(Cipher.DECRYPT_MODE));
        
        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);

        // Progress and Status
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        progressBar.setPreferredSize(new Dimension(420, 25));
        progressBar.setMaximumSize(new Dimension(420, 25));
        progressBar.setVisible(false);
        
        statusLabel = new JLabel("Ready.");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(new Color(127, 140, 141));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        statusPanel.add(progressBar);
        statusPanel.add(statusLabel);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitleLabel);
        mainPanel.add(filePanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(statusPanel);
        
        setContentPane(mainPanel);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        
        // Windows Look & Feel override to ensure background color paints correctly
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filePathField.setText(file.getAbsolutePath());
            statusLabel.setText("Selected: " + file.getName());
            statusLabel.setForeground(new Color(52, 73, 94));
        }
    }

    private void setUIEnabled(boolean enabled) {
        browseBtn.setEnabled(enabled);
        encryptBtn.setEnabled(enabled);
        decryptBtn.setEnabled(enabled);
        passwordField.setEnabled(enabled);
    }

    private void processFile(int mode) {
        String filepath = filePathField.getText();
        String password = new String(passwordField.getPassword());
        
        if (filepath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a file first.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File inputFile = new File(filepath);
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "The selected file does not exist.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String operationName = (mode == Cipher.ENCRYPT_MODE) ? "Encryption" : "Decryption";
        
        // Define Output File extension logic
        String outPath;
        if (mode == Cipher.ENCRYPT_MODE) {
            outPath = filepath + ".enc";
        } else {
            if (filepath.endsWith(".enc")) {
                outPath = filepath.substring(0, filepath.length() - 4);
            } else {
                outPath = filepath + ".dec";
            }
        }
        File outputFile = new File(outPath);

        setUIEnabled(false);
        progressBar.setValue(0);
        progressBar.setVisible(true);
        statusLabel.setText(operationName + " in progress...");
        statusLabel.setForeground(new Color(230, 126, 34));

        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (FileInputStream fis = new FileInputStream(inputFile);
                     FileOutputStream fos = new FileOutputStream(outputFile)) {

                    long totalBytes = inputFile.length();
                    long processedBytes = 0;
                    byte[] buffer = new byte[8192];
                    int bytesRead;

                    if (mode == Cipher.ENCRYPT_MODE) {
                        // Generate Salt and IV for Encryption
                        SecureRandom random = new SecureRandom();
                        byte[] salt = new byte[SALT_LENGTH];
                        random.nextBytes(salt);
                        byte[] iv = new byte[IV_LENGTH];
                        random.nextBytes(iv);

                        // Save Salt and IV at the start of the file
                        fos.write(salt);
                        fos.write(iv);

                        SecretKey secretKey = generateKey(password, salt);
                        Cipher cipher = Cipher.getInstance(ALGORITHM);
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

                        try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                            while ((bytesRead = fis.read(buffer)) != -1) {
                                cos.write(buffer, 0, bytesRead);
                                processedBytes += bytesRead;
                                setProgress((int) ((processedBytes * 100) / totalBytes));
                            }
                        }
                    } else {
                        // Decrypt Mode
                        if (totalBytes < SALT_LENGTH + IV_LENGTH) {
                            throw new Exception("Invalid or corrupted encrypted file. (Too small)");
                        }

                        byte[] salt = new byte[SALT_LENGTH];
                        byte[] iv = new byte[IV_LENGTH];
                        fis.read(salt);
                        fis.read(iv);
                        processedBytes += SALT_LENGTH + IV_LENGTH;

                        SecretKey secretKey = generateKey(password, salt);
                        Cipher cipher = Cipher.getInstance(ALGORITHM);
                        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

                        try (CipherInputStream cis = new CipherInputStream(fis, cipher)) {
                            while ((bytesRead = cis.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                                processedBytes += bytesRead;
                                int progressValue = (int) ((processedBytes * 100) / totalBytes);
                                setProgress(Math.min(progressValue, 100)); // clamp to 100
                            }
                        }
                    }
                } catch (Exception e) {
                    // Cleanup broken output file
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    throw e;
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                if (!chunks.isEmpty()) {
                    progressBar.setValue(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                try {
                    get(); // This will throw exception if doInBackground threw one
                    progressBar.setValue(100);
                    statusLabel.setText(operationName + " completed successfully! -> " + outputFile.getName());
                    statusLabel.setForeground(new Color(46, 204, 113));
                    JOptionPane.showMessageDialog(FileEncryptor.this, operationName + " Success!\nSaved to: " + outputFile.getAbsolutePath(), "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    statusLabel.setText("Error during " + operationName);
                    statusLabel.setForeground(new Color(231, 76, 60));
                    JOptionPane.showMessageDialog(FileEncryptor.this, "Error:\n" + e.getCause().getMessage(), "Operation Failed", JOptionPane.ERROR_MESSAGE);
                } finally {
                    setUIEnabled(true);
                    passwordField.setText(""); // clear password for security
                }
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                progressBar.setValue((Integer) evt.getNewValue());
            }
        });

        worker.execute();
    }

    private SecretKey generateKey(String password, byte[] salt) throws Exception {
        // Fallback to PBKDF2WithHmacSHA1 if SHA256 is not supported on older JREs if needed, 
        // but SHA256 is standard in Java 8+
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FileEncryptor().setVisible(true);
        });
    }
}
