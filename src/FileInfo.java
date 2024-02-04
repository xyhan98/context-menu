import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileInfo {
    private String path;
    private File file;
    private String name;
    private String extension;
    private long size;
    private String md5;
    private static final int BUFFER_SIZE = 8192;

    public FileInfo() {
    }

    public FileInfo(String path) {
        this.path = path;
        this.file = new File(this.path);
    }

    public boolean validate() {
        return this.file.exists() && this.file.isFile();
    }

    public String[] generateInfo() {
        this.name = this.file.getName();
        this.extension = this.name.lastIndexOf('.') > 0
                ? this.name.substring(this.name.lastIndexOf('.') + 1) : "";
        this.size = this.file.length();
        this.md5 = generateMd5(this.file);
        return display(this);
    }

    private static String generateMd5(File file) {
        StringBuilder sb = new StringBuilder();
        String md5;
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] byteArray = new byte[BUFFER_SIZE];
            int bytesCount = 0;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            };
            byte[] bytes = digest.digest();

            for (int i = 0; i < bytes.length; i++) {
                // the following line converts the decimal into
                // hexadecimal format and appends that to the
                // StringBuilder object
                sb.append(Integer
                        .toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return md5;
    }

    private static String[] display(FileInfo info) {
        return new String[]{info.path, info.name, info.extension, String.valueOf(info.size), info.md5};
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public long getSize() {
        return size;
    }

    public String getMd5() {
        return md5;
    }
}
