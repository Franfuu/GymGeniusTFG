package com.github.Franfuu.controllers;

public enum Scenes {
    WELCOME("view/Welcome.fxml"), //ELEGIR ROL
    ROOT("view/layout.fxml"),
    LOGINCLIENTE("view/ClienteLoginView.fxml"),
    MAQUINAS("view/MaquinasView.fxml"),
    SALAS("view/SalasView.fxml"),
    CLASES("view/ClasesView.fxml"),
    LOGINEMPLEADO("view/EmpleadoLoginView.fxml"),
    MAINPAGE_CLIENTE("/com/github/Franfuu/view/ClienteMainPageView.fxml"),
    MAINPAGE_EMPLEADO("view/EmpleadoMainPageView.fxml");


    private final String url;

    Scenes(String url) {
        this.url = url;
    }

    public String getURL() {
        return url;
    }
}
