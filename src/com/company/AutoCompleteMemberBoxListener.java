package com.company;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import static com.company.SubGenApp.*;

public class AutoCompleteMemberBoxListener<T> implements EventHandler<KeyEvent> {

    private static ObservableList lastList = FXCollections.observableArrayList();
    private static boolean trueHidden = true;
    private ComboBox comboBox;
    private StringBuilder sb;
    private ObservableList<Member> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteMemberBoxListener(final ComboBox<Member> comboBox) {
        this.comboBox = comboBox;
        sb = new StringBuilder();
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(AutoCompleteMemberBoxListener.this);

        comboBox.setOnHidden(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if(trueHidden) {
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();

                    ObservableList list = FXCollections.observableArrayList();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).toString().toLowerCase().startsWith(
                                AutoCompleteMemberBoxListener.this.comboBox
                                        .getEditor().getText().toLowerCase())) {
                            list.add(data.get(i));
                        }
                    }
                    String t = comboBox.getEditor().getText();

                    list = data;
                    comboBox.setItems(list);
                    comboBox.getEditor().setText(t);
                    if (!moveCaretToPos) {
                        caretPos = -1;
                    }
                    moveCaret(t.length());
                    if (!list.isEmpty()) {
                        comboBox.hide();
                    }
                }
            }
        });
    }


    @Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
            return;
        }

        ObservableList list = FXCollections.observableArrayList();
        for (int i=0; i<data.size(); i++) {
            if(data.get(i).toString().toLowerCase().startsWith(
                    AutoCompleteMemberBoxListener.this.comboBox
                            .getEditor().getText().toLowerCase())) {
                list.add(data.get(i));
            }
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            if(!lastList.equals(list)) {
                trueHidden = false;
                comboBox.hide();
                comboBox.show();
                trueHidden = true;
            }
            comboBox.show();
        }
        lastList = list;
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }
}
