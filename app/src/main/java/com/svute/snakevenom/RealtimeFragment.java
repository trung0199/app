package com.svute.snakevenom;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.util.concurrent.ListenableFuture;
import com.svute.snakevenom.ml.ModelClassifier;
import com.svute.snakevenom.ml.ModelGarbage;
import com.svute.snakevenom.ml.ModelOnR;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.ExecutionException;

public class RealtimeFragment extends Fragment {

    PreviewView mPreviewView;
    TextView tvResults;
    Spinner spinner;
    int mChoose = 0;
    int size = 256;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime, container, false);
        addControls(view);
        setUpSpinner();
        startCamera();
        return view;
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mChoose = i;
                Log.d("ahihi", "onItemSelected: " +i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void addControls(View view) {
        mPreviewView = view.findViewById(R.id.previewViewRealtime);
        tvResults = view.findViewById(R.id.textViewResultRealtime);
        spinner = view.findViewById(R.id.spinnerRealtime);
    }

    private void startCamera(){
        ListenableFuture<ProcessCameraProvider>
                cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                            bindPreview(cameraProvider);
                        } catch (ExecutionException | InterruptedException e) { }
                    }
                },
                ActivityCompat.getMainExecutor(getContext())
        );
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        ImageCapture.Builder builder = new ImageCapture.Builder();
        ImageCapture imageCapture = builder.build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK).build();
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ActivityCompat.getMainExecutor(getContext()),
                new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(@NonNull ImageProxy image) {
                        String result;
                        result = classifyVenom(image);
                        tvResults.setText(result);
                        image.close();
                    }
                });
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector,
                preview, imageAnalysis, imageCapture);
    }

    private String classifyVenom(ImageProxy image) {
        @SuppressLint("UnsafeOptInUsageError")
        Image imgage = image.getImage();
        Bitmap bitmap = toBitmap(imgage);
        Bitmap img = Bitmap.createScaledBitmap(bitmap, size, size, true);
        if(mChoose == 0){
            try {
                ModelOnR model = ModelOnR.newInstance(getContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);

                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * size * size * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int[] intValues = new int[size * size];
                img.getPixels(intValues, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
                int pixel = 0;
                //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                for(int i = 0; i < size; i ++){
                    for(int j = 0; j < size; j++){
                        int val = intValues[pixel++]; // RGB
                        byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                        byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                        byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                    }
                }

                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                ModelOnR.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                float[] confidences = outputFeature0.getFloatArray();
                // find the index of the class with the biggest confidence.
                int maxPos = 0;
                float maxConfidence = 0;
                for (int i = 0; i < confidences.length; i++) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i];
                        maxPos = i;
                    }
                }
                if( Float.compare(confidences[maxPos], 0.55f)< 0){
                    return "Không phải rác";
                }
                String[] classes = {"RÁC TÁI CHẾ KHÔNG ĐƯỢC", "RÁC TÁI CHẾ ĐƯỢC"};

                // Releases model resources if no longer used.
                model.close();

                return classes[maxPos];
            } catch (IOException e) {
                // TODO Handle the exception
            }
        }else{
            try {
                ModelClassifier model = ModelClassifier.newInstance(getContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);

                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * size * size * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int[] intValues = new int[size * size];
                img.getPixels(intValues, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
                int pixel = 0;
                //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                for(int i = 0; i < size; i ++){
                    for(int j = 0; j < size; j++){
                        int val = intValues[pixel++]; // RGB
                        byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                        byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                        byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                    }
                }

                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                ModelClassifier.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                float[] confidences = outputFeature0.getFloatArray();
                // find the index of the class with the biggest confidence.
                int maxPos = 0;
                float maxConfidence = 0;
                for (int i = 0; i < confidences.length; i++) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i];
                        maxPos = i;
                    }
                }
                if( Float.compare(confidences[maxPos], 0.55f)< 0){
                    return "Không phải rác";
                }
                String[] classes = {"Pin","Rác thải hữu cơ","Rác thải hữu cơ","Thủy tinh nâu","Giấy carton","Quần áo cũ",
                        "Thủy tinh xanh","Kim loại","Giấy","Nhựa","Giày cũ","Rác thải sinh hoạt","Thủy tinh trắng"};

                // Releases model resources if no longer used.
                model.close();

                return classes[maxPos];
            } catch (IOException e) {
                // TODO Handle the exception
            }
        }


        return "";
    }

    private Bitmap toBitmap(Image imgage) {
        Image.Plane[] planes = imgage.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, imgage.getWidth(), imgage.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 100, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }


}