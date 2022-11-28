module project.mytunes {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.mytunes to javafx.fxml;
    exports project.mytunes;
}