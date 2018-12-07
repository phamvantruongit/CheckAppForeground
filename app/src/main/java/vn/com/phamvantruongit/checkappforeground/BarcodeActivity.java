package vn.com.phamvantruongit.checkappforeground;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.util.EnumMap;
import java.util.Map;

public class BarcodeActivity extends AppCompatActivity {
    // barcode data
    static final String BARCODE_DATA = "12345";
    //QR Code data
    static final String QR_CODE_DATA = "www.skholingua.com";

    // barcode image
    Bitmap bitmap = null;
    ImageView outputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        outputImage = (ImageView) findViewById(R.id.imageView);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qr_code_btn:
                try {

                    bitmap = encodeAsBitmap(QR_CODE_DATA, BarcodeFormat.QR_CODE, 512, 512);
                    outputImage.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.barcode_btn:
                try {

                    bitmap = encodeAsBitmap(BARCODE_DATA, BarcodeFormat.CODE_128, 600, 300);
                    outputImage.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.scanner_btn:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);   //To scan all types of Barcodes
                integrator.setPrompt("Scan");   //Set message as SCAN
                integrator.setCameraId(0);  //Default camera as back camera/main camera
                integrator.setBeepEnabled(false);   //Enable scan sound for success or failure
                integrator.initiateScan();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this, "FORMAT: " + result.getFormatName() + " \nCONTENT: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // this is method call from on create and return bitmap image of Barcode/QRCode.
    public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int imgWidth, int imgHeight) throws WriterException {
        if (contents == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contents, format, imgWidth, imgHeight, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
