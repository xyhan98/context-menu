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

    /***
     * Constructor of the FileInfo class
     */
    public FileInfo() {
    }

    /***
     * Constructor of the FileInfo class
     * @param path file path being passed in from the Windows Explorer
     */
    public FileInfo(String path) {
        this.path = path;
        this.file = new File(this.path);
    }

    /***
     * Validate the file path
     * @return true if the file exists, or false otherwise
     */
    public boolean validate() {
        return this.file.exists() && this.file.isFile();
    }

    /***
     * Generate all the information about the file, including file name, file extension, file size, and MD5
     * @return an array of Strings
     */
    public String[] generateInfo() {
        this.name = this.file.getName();
        this.extension = this.name.lastIndexOf('.') > 0
                ? this.name.substring(this.name.lastIndexOf('.') + 1) : "";
        this.size = this.file.length();
        this.md5 = generateMd5(this.file);
        return display(this);
    }

    /***
     * A static private util method to generate md5 for the file object passed in
     * @param file File object
     * @return 32-character String
     */
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

    /***
     * A static private method to display all the information for the fileinfo object passed in
     * @param info FileInfo object
     * @return an array of Strings
     */
    private static String[] display(FileInfo info) {
        return new String[]{info.path, info.name, info.extension, info.size + " bytes", info.md5};
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
