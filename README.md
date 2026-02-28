# 🔒 Secure File Vault

A beautiful, lightweight, and secure Java Desktop Application that encrypts and decrypts files using **AES-256** standard encryption.

## 🚀 Features
- **AES-256 Encryption**: Your files are protected using military-grade security.
- **Password-Protected Options**: Encrypt and Decrypt using your own custom password (PBKDF2 derivative).
- **Supports All File Types**: Encrypt images, videos, documents, and archives!
- **Sleek GUI**: Easy to use, attractive, and lightweight graphical interface.
- **Large File Support**: Processes files efficiently in chunks so your computer's memory is safe.

## 📥 How to Run (For Friends / Users)
1. Download the `SecureFileEncryptor.exe` file.
2. Double click the `.exe` file to run the application (No installation required).
3. _Note: You must have Java (JRE 8 or newer) installed on your computer to run this application._

### Usage Guide
1. Click **Browse...** to select the file you want to target.
2. Type a secure **Password** into the text box.
3. Click **Encrypt File** to secure the file. The app will create a new `.enc` file in the same folder.
4. To reverse the process, select the `.enc` file, enter your exact password, and click **Decrypt File**.

## 🛠️ Developers: How to Compile from Source
If you are developing this app or making modifications:
1. Ensure the Java JDK is installed.
2. Run the compiler:
```bash
javac FileEncryptor.java
```
3. Run the App:
```bash
java FileEncryptor
```

---
*Created by [Your Name / higgs-pixel]! Enjoy privacy and stay secure.*
