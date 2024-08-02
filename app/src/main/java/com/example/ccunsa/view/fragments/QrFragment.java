package com.example.ccunsa.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.ccunsa.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.barcode.common.Barcode;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QrFragment extends Fragment {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private ExecutorService cameraExecutor;
    private PreviewView previewView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        previewView = view.findViewById(R.id.preview_view);

        // Verificar permisos de cámara
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            startCamera();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(getActivity(), "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(getActivity(), "Failed to start camera", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void analyzeImage(@NonNull ImageProxy imageProxy) {
        @SuppressLint("UnsafeExperimentalUsageError")
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            scanBarcodes(image, imageProxy);
        }
    }

    private void scanBarcodes(InputImage image, ImageProxy imageProxy) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        String rawValue = barcode.getRawValue();
                        if (rawValue != null && rawValue.contains("pinturaID:")) {
                            String idStr = rawValue.split("pinturaID:")[1].trim();
                            try {
                                int pinturaId = Integer.parseInt(idStr);
                                navigateToPinturaFragment(pinturaId);
                            } catch (NumberFormatException e) {
                                Toast.makeText(getActivity(), "Invalid pinturaID format", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    imageProxy.close();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error processing QR code", Toast.LENGTH_SHORT).show();
                    imageProxy.close();
                });
    }

    private void navigateToPinturaFragment(int pinturaId) {
        Log.d("QrFragment", "Navigating to PinturaFragment with pinturaId: " + pinturaId); // Log navigation

        // Crear un Bundle y agregar el argumento
        Bundle bundle = new Bundle();
        bundle.putInt("pinturaId", pinturaId);

        // Obtener el NavController y navegar al fragmento destino
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_qrFragment_to_pinturaFragment, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cameraExecutor == null) {
            cameraExecutor = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
            cameraExecutor = null;
        }
    }
}
