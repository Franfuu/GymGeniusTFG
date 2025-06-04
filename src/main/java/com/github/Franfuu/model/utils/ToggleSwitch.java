package com.github.Franfuu.model.utils;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ToggleSwitch extends StackPane {
    private final Rectangle back = new Rectangle(30, 15, Color.RED);
    private final Circle trigger = new Circle(7.5);
    private final StackPane triggerHolder = new StackPane(trigger);
    private final Timeline animation = new Timeline();
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public ToggleSwitch() {
        back.setArcHeight(20);
        back.setArcWidth(20);
        back.setFill(Color.valueOf("#cf5044"));
        trigger.setFill(Color.GRAY);
        triggerHolder.setTranslateX(-7.5);

        getChildren().addAll(back, triggerHolder);

        animation.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(triggerHolder.translateXProperty(), -7.5)),
                new KeyFrame(Duration.millis(100), new KeyValue(triggerHolder.translateXProperty(), 7.5))
        );

        selected.addListener((obs, oldState, newState) -> {
            if (newState) {
                back.setFill(Color.valueOf("#80C49E"));
                animation.setRate(1);
                animation.play();
            } else {
                back.setFill(Color.valueOf("#cf5044"));
                animation.setRate(-1);
                animation.play();
            }
        });

        setOnMouseClicked(e -> selected.set(!selected.get()));
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }
}
