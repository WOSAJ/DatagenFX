module discord.wosaj.datagenfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens discord.wosaj.datagenfx to javafx.fxml;
    exports discord.wosaj.datagenfx;
}