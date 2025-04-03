package com.example.recycleease_customerapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    // ✅ Convert Bitmap to Base64 String
    public static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) return null; // Prevent NullPointerException

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream); // 🔥 Use JPEG (90% Quality)
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT); // ✅ Change to DEFAULT
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Bitmap compressImage(Bitmap original, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] byteArray = stream.toByteArray();
        return android.graphics.BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    // ✅ Convert Base64 String to Bitmap
    public static Bitmap base64ToBitmap(String base64Str) {
        if (base64Str == null || base64Str.trim().isEmpty()) return null; // ✅ Prevent crashes

        try {
            byte[] decodedBytes = Base64.decode(base64Str.trim(), Base64.DEFAULT); // ✅ Ensure proper decoding
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // ✅ Handle decoding failure safely
        }
    }
}
