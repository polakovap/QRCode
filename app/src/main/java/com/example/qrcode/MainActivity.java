package com.example.qrcode;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;

//add imports
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;

public class MainActivity extends AppCompatActivity {
    private Button buttonScan;
    private EditText etName, etAddress;

    // camera permission request code
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //initialize views
        buttonScan = findViewById(R.id.buttonScan);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);


        // Check if the app has permission to use the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request camera permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }

        // Set up click listener for the "Scan QR Code" button
        buttonScan.setOnClickListener(v -> {
            // Check permission again before starting the scanner activity
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Create an intent to start the ZXing QR code scanner
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 0);  // Request code 0
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Extract the scanned QR code content (URL or text)
            String scannedData = data.getStringExtra("SCAN_RESULT");

            // Display the scanned data
            Toast.makeText(this, "Scanned: " + scannedData, Toast.LENGTH_LONG).show();

            // If the scanned data is a URL, open it in the browser
            if (scannedData != null && (scannedData.startsWith("http://") || scannedData.startsWith("https://"))) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannedData));
                startActivity(browserIntent);
            }
        } else {
            Toast.makeText(this, "Scan failed or canceled", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the permission result (for Android 6.0 and above)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}