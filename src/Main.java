import javax.swing.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String[] files = args;

        String[][] data = Arrays.stream(files)
                .map(FileInfo::new)
                .filter(info -> info.validate())
                .map(info -> info.generateInfo())
                .toArray(String[][]::new);

        if (files.length != data.length) {
            JOptionPane.showMessageDialog(null, "One or more items selected are not files",
                    "Alert", JOptionPane.INFORMATION_MESSAGE);
        }

        GUI gui = new GUI(data);
    }
}