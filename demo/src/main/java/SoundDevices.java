
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.EnumControl;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

public class SoundDevices {
    public static void main(String[] args) {
        try {
            System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "/"
                    + System.getProperty("os.arch") + "\nJava: " + System.getProperty("java.version") + " ("
                    + System.getProperty("java.vendor") + ")\n");
            for (Mixer.Info thisMixerInfo : AudioSystem.getMixerInfo()) {
                System.out.println("Mixer: " + thisMixerInfo.getDescription() + " [" + thisMixerInfo.getName() + "]");
                Mixer thisMixer = AudioSystem.getMixer(thisMixerInfo);
                for (Line.Info thisLineInfo : thisMixer.getSourceLineInfo()) {
                    if (thisLineInfo instanceof javax.sound.sampled.Port.Info) {
                        Line thisLine = thisMixer.getLine(thisLineInfo);
                        thisLine.open();
                        System.out.println("  Source Port: " + thisLineInfo.toString());
                        for (Control thisControl : thisLine.getControls()) {
                            System.out.println(analyzeControl(thisControl));
                        }
                        thisLine.close();
                    }
                }
                for (Line.Info thisLineInfo : thisMixer.getTargetLineInfo()) {
                    if (thisLineInfo instanceof javax.sound.sampled.Port.Info) {
                        Line thisLine = thisMixer.getLine(thisLineInfo);
                        thisLine.open();
                        System.out.println("  Target Port: " + thisLineInfo.toString());
                        for (Control thisControl : thisLine.getControls()) {
                            System.out.println(analyzeControl(thisControl));
                        }
                        thisLine.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String analyzeControl(Control thisControl) {
        String type = thisControl.getType().toString();
        if (thisControl instanceof BooleanControl) {
            return new StringBuilder("    Control: ").append(type).append(" (boolean)").toString();
        }
        if (thisControl instanceof CompoundControl) {
            System.out.println("    Control: " + type + " (compound - values below)");
            StringBuilder toReturn = new StringBuilder();
            for (Control children : ((CompoundControl) thisControl).getMemberControls()) {
                toReturn.append("  ").append(analyzeControl(children)).append("\n");
            }
            return toReturn.substring(0, toReturn.length() - 1);
        }
        if (thisControl instanceof EnumControl) {
            return new StringBuilder("    Control:").append(type).append(" (enum: ").append(thisControl.toString())
                    .append(")").toString();
        }
        if (thisControl instanceof FloatControl) {
            return new StringBuilder("    Control: ").append(type).append(" (float: from ")
                    .append(((FloatControl) thisControl).getMinimum()).append(" to ")
                    .append(((FloatControl) thisControl).getMaximum()).append(")").toString();
        }
        return "    Control: unknown type";
    }
}
