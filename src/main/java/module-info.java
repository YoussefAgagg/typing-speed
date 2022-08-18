module org.github.youssefagagg.typing {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.fxmisc.richtext;

    opens com.github.youssefagagg.typing to javafx.fxml;
    exports com.github.youssefagagg.typing;
}
