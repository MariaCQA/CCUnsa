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

/**
 * Fragmento para la captura de imágenes de códigos QR utilizando la cámara y procesarlas para extraer datos.
 */
public class QrFragment extends Fragment {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101; // Código de solicitud para permisos de cámara
    private ExecutorService cameraExecutor; // Executor para manejar operaciones en un hilo separado
    private PreviewView previewView; // Vista para mostrar la vista previa de la cámara

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_qr, container, false);
        previewView = view.findViewById(R.id.preview_view);

        // Verificar permisos de cámara
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no se tienen
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Iniciar la cámara si los permisos ya están concedidos
            startCamera();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Manejar la respuesta a la solicitud de permisos
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Iniciar la cámara si se concede el permiso
                startCamera();
            } else {
                // Mostrar un mensaje si el permiso es denegado
                Toast.makeText(getActivity(), "Camera permission is required to use this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCamera() {
        // Obtener una instancia del proveedor de cámara
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            try {
                // Obtener el proveedor de cámara
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                // Configurar la vista previa de la cámara
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Mostrar un mensaje de error y registrar el error
                Toast.makeText(getActivity(), "Failed to start camera", Toast.LENGTH_SHORT).show();
                Log.e("QrFragment", "Error starting camera", e);
            }
        }, ContextCompat.getMainExecutor(getActivity()));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Configurar la vista previa de la cámara
        Preview preview = new Preview.Builder().build();
        // Configurar el análisis de imágenes para la detección de códigos QR
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720)) // Establecer la resolución de destino
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) // Estrategia de retroalimentación
                .build();

        // Seleccionar la cámara trasera
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Configurar el proveedor de superficie para la vista previa
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Configurar el analizador de imágenes
        imageAnalysis.setAnalyzer(cameraExecutor, this::analyzeImage);

        // Vincular el ciclo de vida de la cámara con el fragmento
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private void analyzeImage(@NonNull ImageProxy imageProxy) {
        // Obtener la imagen del proxy
        @SuppressLint("UnsafeExperimentalUsageError")
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            // Crear un objeto InputImage desde la imagen del medio
            InputImage inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            // Analizar la imagen para detectar códigos QR
            scanBarcodes(inputImage, imageProxy);
        } else {
            // Registrar un error si la imagen es nula
            Log.e("QrFragment", "MediaImage is null");
            imageProxy.close(); // Cerrar el proxy de la imagen
        }
    }

    private void scanBarcodes(InputImage image, ImageProxy imageProxy) {
        // Configurar las opciones del escáner de códigos de barras para detectar códigos QR
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();
        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        // Procesar la imagen para detectar códigos QR
        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (barcodes.isEmpty()) {
                        // Mostrar un mensaje si no se encuentran códigos
                        Toast.makeText(getActivity(), "No barcodes found", Toast.LENGTH_SHORT).show();
                        Log.d("QrFragment", "No barcodes detected");
                    }
                    for (Barcode barcode : barcodes) {
                        // Obtener el valor del código QR
                        String rawValue = barcode.getRawValue();
                        if (rawValue != null && rawValue.contains("pinturaID:")) {
                            // Extraer el ID de pintura del código QR
                            String idStr = rawValue.split("pinturaID:")[1].trim();
                            try {
                                int pinturaId = Integer.parseInt(idStr);
                                // Navegar al fragmento de pintura con el ID extraído
                                navigateToPinturaFragment(pinturaId);
                            } catch (NumberFormatException e) {
                                // Mostrar un mensaje y registrar un error si el formato del ID es inválido
                                Toast.makeText(getActivity(), "Invalid pinturaID format", Toast.LENGTH_SHORT).show();
                                Log.e("QrFragment", "Invalid pinturaID format", e);
                            }
                        }
                    }
                    imageProxy.close(); // Cerrar el proxy de la imagen
                })
                .addOnFailureListener(e -> {
                    // Mostrar un mensaje de error y registrar el error si el procesamiento falla
                    Toast.makeText(getActivity(), "Error processing QR code", Toast.LENGTH_SHORT).show();
                    Log.e("QrFragment", "Error processing QR code", e);
                    imageProxy.close(); // Cerrar el proxy de la imagen
                });
    }

    private void navigateToPinturaFragment(int pinturaId) {
        Log.d("QrFragment", "Navigating to PinturaFragment with pinturaId: " + pinturaId); // Registrar la navegación

        // Crear un Bundle con el ID de pintura
        Bundle bundle = new Bundle();
        bundle.putInt("pinturaId", pinturaId);

        // Obtener el NavController y navegar al fragmento destino
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        try {
            navController.navigate(R.id.action_qrFragment_to_pinturaFragment, bundle);
        } catch (IllegalArgumentException e) {
            // Mostrar un mensaje y registrar un error si la acción de navegación no se encuentra
            Toast.makeText(getActivity(), "Navigation action not found", Toast.LENGTH_SHORT).show();
            Log.e("QrFragment", "Navigation action not found", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Inicializar el ExecutorService cuando el fragmento se reanuda
        if (cameraExecutor == null) {
            cameraExecutor = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Detener el ExecutorService cuando el fragmento se pausa
        if (cameraExecutor != null) {
            cameraExecutor.shutdown();
            cameraExecutor = null;
        }
    }
}
