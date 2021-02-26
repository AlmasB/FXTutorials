package com.almasb.rpg;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class RPGApp extends Application {

    private enum Action {
        ATTACK, CHARGE, BLOCK;

        private static final Map<Action, Action> winMap = new HashMap<>();

        static {
            winMap.put(ATTACK, CHARGE);
            winMap.put(CHARGE, BLOCK);
            winMap.put(BLOCK, ATTACK);
        }

        ActionResult checkAgainst(Action other) {
            if (this == other)
                return ActionResult.DRAW;

            return winMap.get(this) == other ? ActionResult.WIN : ActionResult.LOSE;
        }
    }

    private enum ActionResult {
        WIN, LOSE, DRAW
    }

    private static class Char {
        int hp;
        int attackPower;
        int chargePower;
        int blockPower;

        public Char(int hp, int attackPower, int chargePower, int blockPower) {
            this.hp = hp;
            this.attackPower = attackPower;
            this.chargePower = chargePower;
            this.blockPower = blockPower;
        }

        int calcDamage(Action action) {
            switch (action) {
                case ATTACK:
                    return attackPower + (crit(35) ? (15) : 0);
                case CHARGE:
                    return chargePower;
                case BLOCK:
                    return blockPower;
            }

            throw new AssertionError("Unknown action : " + action);
        }

        @Override
        public String toString() {
            return "HP: " + hp;
        }
    }

    private Char player = new Char(100, 15, 20, 5);
    private Char ai = new Char(100, 10, 30, 8);

    private TextArea output = new TextArea();

    private Parent createContent() {
        VBox root = new VBox(10);
        root.setPrefSize(800, 600);
        root.setPadding(new Insets(15));

        Button btnAttack = new Button("ATTACK");
        btnAttack.setOnAction(e -> makeMove(Action.ATTACK));

        Button btnCharge = new Button("CHARGE");
        btnCharge.setOnAction(e -> makeMove(Action.CHARGE));

        Button btnBlock = new Button("BLOCK");
        btnBlock.setOnAction(e -> makeMove(Action.BLOCK));

        output.setPrefHeight(450);
        output.setFont(Font.font(26));

        updateInfo();

        root.getChildren().addAll(btnAttack, btnCharge, btnBlock, output);

        return root;
    }

    private void makeMove(Action userAction) {
        Action aiAction = makeAIMove();

        ActionResult result = userAction.checkAgainst(aiAction);

        if (result == ActionResult.DRAW) {

            output.appendText("DRAW\n");

        } else if (result == ActionResult.WIN) {

            int dmg = player.calcDamage(userAction);

            ai.hp -= dmg;

            output.appendText("Player deals " + dmg + " to AI \n");

            updateInfo();

        } else { // LOSE

            int dmg = ai.calcDamage(aiAction);

            player.hp -= dmg;

            output.appendText("AI deals " + dmg + " to player \n");

            updateInfo();
        }
    }

    private void updateInfo() {
        output.appendText("Player: " + player + ", AI: " + ai + "\n");
    }

    private Action makeAIMove() {
        return Action.values()[(int) (Math.random() * Action.values().length)];
    }

    private static Random random = new Random();

    private static boolean crit(int chance) {
        return chance > random.nextInt(100);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
