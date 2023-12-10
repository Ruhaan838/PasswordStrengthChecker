//importing the modules
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.time.LocalDate;

// main class of the password 
public class PasswordStrengthChecker extends Application {
    //main method 
    public static void main(String[] args) {
        launch(args);
    }
    //some comman variables 
    private TextField passwordField;
    private TextField generatedPasswordField;
    private ObservableList<String> passwordHistory;
    private ListView<String> passwordHistoryListView;

    //start method for the our primary stage 
    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Password Strength Checker"); //title of the app
        Label passwordLabel = new Label("Enter Password:");//text enter password
        Button checkButton = new Button("Check Strength");//button of the check strength
        Button generateButton = new Button("Generate Password");//button of the generate password
        Button Export = new Button("Export");//button of the export txt file 
        Label strengthLabel = new Label("");//base label of the strangth 
        Label passwordhistory = new Label("Password History"); //text of the password history 
        Label generateLabel = new Label("Generated Password:");//text of the generated password
        ChoiceBox<String> strengthChoiceBox = new ChoiceBox<>(); //check box 
        passwordField = new TextField(); // text fild for password field 
        generatedPasswordField = new TextField(); //text fild for the generatedpassword
        passwordHistory = FXCollections.observableArrayList(); //this is arraylist of the password history
        passwordHistoryListView = new ListView<>(passwordHistory); //this is the list view of the password history
        strengthChoiceBox.getItems().addAll("Very Weak","Weak", "Moderate", "Strong","Very Strong"); // option of the choice box 
        strengthChoiceBox.setValue("Very Weak");//set the default value of the password

        passwordField.setPrefWidth(230);//this is the width of the password history field
        passwordHistoryListView.setPrefHeight(300); //this is the hight of the password history field
        passwordLabel.setStyle("-fx-text-fill: white;");
        strengthLabel.setStyle("-fx-font-weight: bold;-fx-text-fill: white;-fx-font-size: 20px;");
        generateLabel.setStyle("-fx-text-fill: white;");
        strengthChoiceBox.setStyle("-fx-background-color: #adb5bd; -fx-text-fill: white; -fx-font-size: 14px;");
        passwordhistory.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        passwordField.setStyle("-fx-background-color: #adb5bd; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px;");
        generatedPasswordField.setStyle("-fx-background-color: #adb5bd; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8px;");
        generatedPasswordField.setEditable(false); //this will block the text fields of the generate password

        //this is the action of the export button it will give the text file of the password history
        Export.setOnAction(event -> {
            if (!passwordHistory.isEmpty()) { // this is for if the password history is have password it will give the txt file 
                LocalDate currentDate = LocalDate.now();
                SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
                String currentTime = sdf.format(new Date());
                String filePath = System.getProperty("user.dir") + "/password_history"+currentDate +" "+currentTime+" "+".txt";
                exportToTextFile(passwordHistory, filePath);
            } else { //if the password have no history it will give the error
                showAlert(Alert.AlertType.ERROR, "Error", "No Password list");
            }
        });   

        //this button will call the function of the chack password strangth  
        checkButton.setOnAction(event -> {
            String password = passwordField.getText();//the getText method it give the text the of the give password fild

            if (password.isEmpty()) { //check if the password is empty 
                showAlert(Alert.AlertType.ERROR, "Error", "Please Enter The Password ..!!");
                return;
            }
            String strength = checkPasswordStrength(password);//this is the function check the password strangth and give the sting formate 

            if (strength.equals("Very Strong")) { 
                strengthLabel.setText("Very Strong Password");
                strengthLabel.setTextFill(Color.LIME);
                strengthLabel.setStyle("-fx-font-size: 20px;");
            } else if (strength.equals("Strong")) {
                strengthLabel.setText("Strong Password");
                strengthLabel.setTextFill(Color.LIME);
                strengthLabel.setStyle("-fx-font-size: 20px;");
            } else if (strength.equals("Moderate")) {
                strengthLabel.setText("Moderate Password");
                strengthLabel.setTextFill(Color.ORANGE);
                strengthLabel.setStyle("-fx-font-size: 20px;");
            } else if (strength.equals("Weak")){
                strengthLabel.setText("Weak Password");
                strengthLabel.setTextFill(Color.RED);
                strengthLabel.setStyle("-fx-font-size: 20px;");
            } else if (strength.equals("Very Weak")){
                strengthLabel.setText("Very Weak Password");
                strengthLabel.setTextFill(Color.RED);
                strengthLabel.setStyle("-fx-font-size: 20px;");
            }
            updatePasswordHistory(password , strength); // this function is do the update the password history
        });

        //this setOnAction is call the generate password function  
        generateButton.setOnAction(event -> {
            String passwordstrength = strengthChoiceBox.getValue(); //this getValue is get the value of the strengthchoicebox
            String generatedPassword = generatePassword(passwordstrength); //this generate the password value of the function 
            generatedPasswordField.setText(generatedPassword);//this setText fun is set the value of the genrated password into the genrated password field
            updatePasswordHistory(generatedPassword ,passwordstrength);//this is also give store the history of the genratedpassword with there strngh
        });

        VBox vBox = new VBox(40);//Vbox object 
        vBox.setAlignment(Pos.TOP_CENTER);//set to the top of the center 
        vBox.setStyle("-fx-background-color: #3a506b; -fx-text-fill: white;");//give the style  
        
        GridPane gridPane = new GridPane();//gridpane object
        gridPane.setAlignment(Pos.CENTER);//set ti the center
        gridPane.setHgap(10);//horizontol gap
        gridPane.setVgap(5);//vertical gap
        gridPane.setStyle("-fx-background-color: #3a506b; -fx-text-fill: white;"); 
        
        //text , button and checkbutton arrangement with the grid method
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(checkButton, 6, 1);
        gridPane.add(strengthLabel, 8, 1, 6, 2);
        gridPane.add(generateButton, 10, 4);
        gridPane.add(generateLabel, 0, 4);
        gridPane.add(generatedPasswordField, 1, 4);
        gridPane.add(strengthChoiceBox, 6, 4);
        gridPane.add(passwordHistoryListView, 0, 12, 12, 4);
        gridPane.add(passwordhistory, 0, 10);
        gridPane.add(Export,11,10);

        //give the animation and other cool effect 
        addChoiceBoxHoverEffect(strengthChoiceBox, "#00a8e8");
        applyButtonStyle(checkButton, "#4CAF50", "-fx-text-fill: white;");
        applyButtonStyle(generateButton, "#2196F3", "-fx-text-fill: white;");
        applyButtonStyle(Export, "#f48c06", "-fx-text-fill: white;");
        addButtonHoverEffect(checkButton, "#40916c", "#52b788");
        addButtonHoverEffect(generateButton, "#0353a4", "#0466c8");
        addButtonHoverEffect(Export, "#f48c06", "#e85d04");

        //this main scene
        Scene scene = new Scene(gridPane, 850, 500);//how much amount of hte 
        scene.setFill(Color.BLACK); 
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();        
    }

    private String checkPasswordStrength(String password) {
        int length = password.length();
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasDigits = password.matches(".*\\d.*");
        boolean hasSpecialChars = password.matches(".*[!@#$%^&*()-=_+\\[\\]{};':\",.<>/?].*");
    
        int complexityPoints = 0;
    
        if (length >= 8) {
            complexityPoints++;
        }
        if (length >= 12) {
            complexityPoints++;
        }
        if (hasUpperCase) {
            complexityPoints++;
        }
        if (hasLowerCase){
            complexityPoints++;
        }
        if (hasDigits) {
            complexityPoints++;
        }
        if (hasSpecialChars) {
            complexityPoints++;
        }
        if (length >= 16) {
            complexityPoints++;
        }
        if (length >= 18) {
            complexityPoints++;
        }
    
        if (complexityPoints >= 8) {
            return "Very Strong";
        } else if (complexityPoints > 6) {
            return "Strong";
        } else if (complexityPoints >= 4) {
            return "Moderate";
        } else if (complexityPoints > 2) {
            return "Weak";
        } else {
            return "Very Weak";
        }
    }
    
    private String generatePassword(String strength) {
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder();
    
        if (strength.equals("Very Weak")) {
            
            for (int i = 0; i < 6; i++) {
                password.append((char) (random.nextInt(26) + 'a'));
            }
        } else if (strength.equals("Weak")) {
            
            for (int i = 0; i < 8; i++) {
                int choice = random.nextInt(2);
                if (choice == 0) {
                    password.append((char) (random.nextInt(26) + 'a'));
                } else {
                    password.append(random.nextInt(10));
                }
            }
        } else if (strength.equals("Moderate")) {
            
            for (int i = 0; i < 12; i++) {
                int choice = random.nextInt(3);
                if (choice == 0) {
                    password.append((char) (random.nextInt(26) + 'a'));
                } else if (choice == 1) {
                    password.append((char) (random.nextInt(26) + 'A'));
                } else {
                    password.append(random.nextInt(10));
                }
            }
        } else if (strength.equals("Strong")) {
            
            String symbols = "!@#$%^&*()-=_+[]{};':\",.<>/?";
            for (int i = 0; i < 16; i++) {
                int choice = random.nextInt(4);
                if (choice == 0) {
                    password.append((char) (random.nextInt(26) + 'a'));
                } else if (choice == 1) {
                    password.append((char) (random.nextInt(26) + 'A'));
                } else if (choice == 2) {
                    password.append(random.nextInt(10));
                } else {
                    password.append(symbols.charAt(random.nextInt(symbols.length())));
                }
            }
        } else if (strength.equals("Very Strong")) {
            
            String symbols = "!@#$%^&*()-=_+[]{};':\",.<>/?";
            for (int i = 0; i < 25; i++) {
                int choice = random.nextInt(4);
                if (choice == 0) {
                    password.append((char) (random.nextInt(26) + 'a'));
                } else if (choice == 1) {
                    password.append((char) (random.nextInt(26) + 'A'));
                } else if (choice == 2) {
                    password.append(random.nextInt(10));
                } else {
                    password.append(symbols.charAt(random.nextInt(symbols.length())));
                }
            }
        }
    
        return password.toString();
    }

    void applyButtonStyle(Button button, String defaultColor, String textFill) {
        button.setStyle("-fx-background-color: " + defaultColor + "; -fx-font-size: 14px; -fx-padding: 10px; " + textFill);
    }
    
    private void updatePasswordHistory(String password, String strength) {
        
        int passwordWidth = 50;
        int strengthWidth = 20;
    
        String formattedEntry = String.format("%-" + passwordWidth + "s %-" + strengthWidth + "s",
                password, strength);
    
        passwordHistory.add(0, formattedEntry);
    
        // Add event handler for password history item click
        passwordHistoryListView.setOnMouseClicked(event -> {
            if (!passwordHistoryListView.getSelectionModel().isEmpty()) {
                String selectedPassword = passwordHistoryListView.getSelectionModel().getSelectedItem().split("\\s+")[0];
                String selectedItem = passwordHistoryListView.getSelectionModel().getSelectedItem();
                String[] parts = selectedItem.split("\\s+");
                int startIndex = 1; // Assuming the strength starts from the second part
                String selectedStrength = String.join(" ", Arrays.copyOfRange(parts, startIndex, parts.length));

                // Open a new window with detailed information
                showPasswordInfoWindow(selectedPassword, selectedStrength);
            }
        });
    }       
    
    private void showPasswordInfoWindow(String password, String strength) {
        PasswordInfoWindow infoWindow = new PasswordInfoWindow(password, strength);
        infoWindow.show();
    }
    
    private void addButtonHoverEffect(Button button, String defaultColor, String hoverColor) {
        button.setStyle("-fx-background-color: " + defaultColor + "; -fx-font-size: 14px; -fx-padding: 8px; -fx-text-fill: white;");
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: " + hoverColor + "; -fx-font-size: 14px; -fx-padding: 8px; -fx-text-fill: white;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: " + defaultColor + "; -fx-font-size: 14px; -fx-padding: 8px; -fx-text-fill: white;"));
    }

    private void addChoiceBoxHoverEffect(ChoiceBox<String> choiceBox, String hoverColor) {
        choiceBox.setOnMouseEntered(event -> choiceBox.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-size: 14px;"));
        choiceBox.setOnMouseExited(event -> choiceBox.setStyle("-fx-background-color: #adb5bd; -fx-text-fill: white; -fx-font-size: 14px;"));
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    void exportToTextFile(ObservableList<String> data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String password : data) {
                writer.write(password);
                writer.newLine();
            }
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Password history exported successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "OPPS !\n" + e.getMessage());
        }
    }

    private static void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
class PasswordInfoWindow extends Stage {

     public PasswordInfoWindow(String password, String strength) {
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));
        root.setStyle("-fx-background-color: #3a506b;");

        Label infoLabel = new Label();
        infoLabel.setMaxWidth(450);
        infoLabel.setWrapText(true);

        ObservableList<String> infoText = FXCollections.observableArrayList();
        infoText.add("Password: " + password);
        infoText.add("Strength: " + strength);
        infoText.add("Created Date: " + formatCreatedDate());
        infoText.add("Expiry Date: " + calculateExpiryDate(strength));

        PasswordStrengthChecker ps = new PasswordStrengthChecker();

        Button exportButton = new Button("Export");
        exportButton.setOnAction(event -> {
            LocalDate currentDate = LocalDate.now();
            SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
            String currentTime = sdf.format(new Date());
            String filePath = System.getProperty("user.dir") + "/password_history" + currentDate + " " + currentTime + ".txt";
            ps.exportToTextFile(infoText, filePath);
        });
        ps.applyButtonStyle(exportButton, "#f48c06", "-fx-text-fill: white;");

        root.getChildren().addAll(infoLabel, exportButton);

        // Display the information in the Label
        infoLabel.setText(String.join("\n", infoText));
        infoLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Scene scene = new Scene(root);
        setScene(scene);
        setTitle("Password Information");
        setResizable(false);
    }
    private String calculateExpiryDate(String strength) {
        int monthsToAdd;

        switch (strength) {
            case "Very Strong":
                monthsToAdd = 5;
                break;
            case "Strong":
                monthsToAdd = 4;
                break;
            case "Moderate":
                monthsToAdd = 3;
                break;
            case "Weak":
                monthsToAdd = 2;
                break;
            case "Very Weak":
                monthsToAdd = 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid strength: " + strength);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate expiryDate = currentDate.plusMonths(monthsToAdd);

        // Format the expiry date using DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        return expiryDate.format(formatter);
    }
    private String formatCreatedDate() {
        LocalDateTime createdDate = LocalDateTime.now();

        // Format the created date using DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
        return createdDate.format(formatter);
    }
}