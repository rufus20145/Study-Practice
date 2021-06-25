package practice;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SecondCase {
    private static final int TIME_TO_SLEEP = 1000;
    private static final int SECONDS = 5;

    private SecondCase() {
    }

    public static void start() {
        System.out.println("Вы выбрали режим записи звука с микрофона в файл. Приготовьтесь.");

        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("У вас нет поддерживаемого микрофона.");
            }

            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open();

            System.out.println("Начинаю запись.");
            targetLine.start();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    AudioInputStream audioInputStream = new AudioInputStream(targetLine);
                    File audioFile = new File("result.wav");
                    try {
                        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    System.out.println("Запись завершена.");
                }
            };

            thread.start();
            for (int i = 0; i < SECONDS; i++) {
                System.out.println("Осталось " + (SECONDS - i) + " сек.");
                Thread.sleep(TIME_TO_SLEEP);
            }

            targetLine.stop();
            targetLine.close();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            ie.printStackTrace();
        } catch (LineUnavailableException lue) {

            lue.printStackTrace();
        }
    }
}
