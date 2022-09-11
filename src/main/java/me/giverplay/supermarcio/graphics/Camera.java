package me.giverplay.supermarcio.graphics;

public class Camera {
  private int x;
  private int y;

  public Camera(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static int clamp(int value, int min, int max) {
    return value < min ? min : value > max ? max : value;
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public void setX(int x) {
    this.x = x;
  }
}
