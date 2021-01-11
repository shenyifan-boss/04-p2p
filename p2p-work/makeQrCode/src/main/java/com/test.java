package com;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 奕凡
 * <p>
 * <p>
 * 2021/1/8
 **/
public class test {
    public static void main(String[] args) {
        String srt="超哥超哥,我是你爹！超哥超哥,我是你爹！超哥超哥,我是你爹！超哥超哥,我是你爹！";
        Map<EncodeHintType,Object> map = new HashMap<EncodeHintType, Object>();
        map.put(EncodeHintType.CHARACTER_SET,"utf-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().
                    encode(srt, BarcodeFormat.QR_CODE, 200, 200,map);
            Path path = FileSystems.getDefault().getPath("d://","first.jpg");
            MatrixToImageWriter.writeToPath(bitMatrix, "jpg", path);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
