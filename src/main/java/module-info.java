module com.example.software_1_project {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                        
    opens com.example.software_1_project to javafx.fxml;
    exports com.example.software_1_project;
    exports model;
    opens model to javafx.fxml;
}