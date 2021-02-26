var g = canvas.getGraphicsContext2D();
main();

function main() {
    draw();
}

function draw() {
    g.setFill(javafx.scene.paint.Color.BLACK);
    g.fillText("Hello from JS", 100, 100);
    g.strokeText("Hello from JS", 100, 150);
    g.setFill(javafx.scene.paint.Color.BLUE);
    g.fillRect(300, 300, 25, 50);
    g.setFill(javafx.scene.paint.Color.GREEN);
    g.fillRoundRect(200, 200, 100, 100, 20, 20);
}