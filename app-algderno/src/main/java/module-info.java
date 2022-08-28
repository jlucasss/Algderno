module com.algderno {
	
	requires javafx.base;
	requires javafx.fxml;
	requires javafx.controls;
	requires javafx.graphics;

	opens com.algderno.controllers to javafx.fxml;
	opens com.algderno.controllers.helper to javafx.fxml;
	opens com.algderno.controllers.subscreens to javafx.fxml;
	opens com.algderno.models to javafx.base;

	requires com.jsubmeter;
	requires org.json;
	//requires org.junit.jupiter.api;

	opens com.algderno;

	exports com.algderno;
	
}
