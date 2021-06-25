package practice;

import java.io.ByteArrayOutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class ThirdCase {
    /**
     *
     */
    private static final int BUFFER_SIZE_DIVIDER = 5;
    private static final int TIME_TO_RECORD_AND_PLAY = 2500;

    private ThirdCase() {
    }

    public static void start() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, 2, 4, 48000, false);

        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);
        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            sourceLine.open();
            targetLine.open();

            Thread sourceThread = new Thread() {
                @Override
                public void run() {
                    sourceLine.start();
                    while (sourceLine.isOpen()) {
                        sourceLine.write(out.toByteArray(), 0, out.size());
                    }
                }
            };

            Thread targetThread = new Thread() {
                @Override
                public void run() {
                    targetLine.start();
                    byte[] data = new byte[targetLine.getBufferSize() / BUFFER_SIZE_DIVIDER];
                    int readBytes;
                    while (targetLine.isOpen()) {
                        readBytes = targetLine.read(data, 0, data.length);
                        out.write(data, 0, readBytes);
                    }
                }
            };
            System.out.println("Начинаю запись.");
            targetThread.start();
            Thread.sleep(TIME_TO_RECORD_AND_PLAY);
            System.out.println("Запись завершена.");
            targetLine.stop();
            targetLine.close();

            System.out.println("Начинаю воспроизведение.");
            sourceThread.start();
            Thread.sleep(TIME_TO_RECORD_AND_PLAY);
            System.out.println("Воспроизведение завершено.");
            sourceLine.stop();
            sourceLine.close();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

    }
}
