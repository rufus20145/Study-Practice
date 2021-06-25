package practice;

import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

public final class FirstCase {
    private static final int TIME_TO_SLEEP = 50;
    private static final String DEFAULT_FILE = "/practice/sample.wav";
    private static final String YES_STRING = "y";
    private static Clip clip;

    private FirstCase() {
    }

    public static void start() {
        String filePath;
        System.out.println(
                "Вы выбрали режим воспроизведения звука из файла. Хотите указать свой файл (иначе будет использован файл по умолчанию)? (y/n)");
        String choose = Main.getStringValue();
        if (choose.equals(YES_STRING)) {
            System.out.println("Введите полное имя вашего файла:");
            filePath = Main.getStringValue();
        } else {
            filePath = DEFAULT_FILE;
        }
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, format);

        try {
            clip = (Clip) AudioSystem.getLine(dataInfo);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        try {
            URL soundUrl = FirstCase.class.getResource(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundUrl);
            clip.open(audioInputStream);
        } catch (Exception e) {
            System.out.println("1");
        }

        clip.start();

        do {
            try {
                Thread.sleep(TIME_TO_SLEEP);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Исключение поймано");
                e.printStackTrace();
            }
        } while (clip.isActive());
    }
}
