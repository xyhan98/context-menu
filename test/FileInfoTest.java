import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileInfoTest {
    @Test
    public void testValidateWithExistPath() {
        FileInfo fileInfo = new FileInfo("/Users/xinyihan/Desktop/Others/haskell/algorithms/qsort.hs");
        assertTrue(fileInfo.validate());
    }

    @Test
    public void testValidateWithNonExistPath() {
        FileInfo fileInfo = new FileInfo("/Users/xinyihan/Desktop/Others/");
        assertFalse(fileInfo.validate());
    }

    @Test
    public void testGenerateInfo() {
        String path = "/Users/xinyihan/Desktop/Others/haskell/algorithms/qsort.hs";
        FileInfo fileInfo = new FileInfo(path);
        fileInfo.generateInfo();
        assertEquals(path, fileInfo.getPath());
        assertEquals("qsort.hs", fileInfo.getName());
        assertEquals("hs", fileInfo.getExtension());
        assertEquals(185L, fileInfo.getSize());
        assertEquals(32, fileInfo.getMd5().length());
    }
}
