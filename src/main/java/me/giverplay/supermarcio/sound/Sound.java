package me.giverplay.supermarcio.sound;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class Sound {
  public static Clips coin = load("/coin.wav", 1);
  public static Clips hit = load("/hit.wav", 1);
  public static Clips lose = load("/lose.wav", 1);
  public static Clips hit2 = load("/hit2.wav", 1);
  public static Clips jump = load("/jump.wav", 1);
  public static Clips life = load("/life.wav", 1);
  public static Clips up = load("/up.wav", 1);

  public static void init() {
    // Apenas corrigir o lag
  }

  private static Clips load(String name, int count) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream(name));

      byte[] buffer = new byte[1024];

      int read = 0;

      while((read = dis.read(buffer)) >= 0) {
        baos.write(buffer, 0, read);
      }

      dis.close();

      byte[] data = baos.toByteArray();

      try {
        return new Clips(data, count);
      } catch(IllegalArgumentException e) {
        System.out.println("Argumentos inv�lidos: " + e.getMessage());
      } catch(LineUnavailableException e) {
        System.out.println("Linha indispon�vel");
      } catch(UnsupportedAudioFileException e) {
        System.out.println("Arquivo de audio n�o suportado");
      }
    } catch(NullPointerException | IOException e) {
      try {
        try {
          return new Clips(null, count);
        } catch(IllegalArgumentException e1) {
          System.out.println("Argumentos inv�lidos: " + e.getMessage());
        } catch(LineUnavailableException e1) {
          System.out.println("Linha indispon�vel");
        } catch(UnsupportedAudioFileException e1) {
          System.out.println("Arquivo de audio n�o suportado");
        }
      } catch(IOException e1) {
        return null;
      }
    }

    return null;
  }

  public static class Clips {
    private final int count;
    public Clip[] clips;
    private int p;

    public Clips(byte[] buffer, int count) throws IllegalArgumentException, LineUnavailableException, IOException, UnsupportedAudioFileException {
      if(buffer == null) {
        throw new IllegalArgumentException("Buffer n�o pode ser nulo");
      }

      clips = new Clip[count];

      this.count = count;

      for(int i = 0; i < count; i++) {
        clips[i] = AudioSystem.getClip();
        clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
      }
    }

    public void play() throws IllegalStateException {
      if(clips == null) {
        throw new IllegalStateException("Lista de clips est� vazia");
      }

      clips[p].stop();
      clips[p].setFramePosition(0);
      clips[p].start();

      p++;

      if(p >= count) {
        p = 0;
      }
    }

    public void loop() throws IllegalStateException {
      if(clips == null) {
        throw new IllegalStateException("Lista de clips est� vazia");
      }

      clips[p].loop(300);
    }
  }
}
