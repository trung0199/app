package com.svute.snakevenom;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.svute.snakevenom.ml.ModelClassifier;
import com.svute.snakevenom.ml.ModelGarbage;
import com.svute.snakevenom.ml.ModelOnR;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CameraFragment extends Fragment {

    ImageView imageView;
    Bitmap imgRaw;
    private static int RESULT_LOAD_IMAGE = 1;
    Button btnOpenDialog;
    String mResultVenom = "";
    String mResultClass = "";
    String mIndexClass = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        addControls(view);
        checkData();
        addEvents();
        return view;
    }

    private void checkData() {
        if(UriImg.uriArrayList.size() != 0){
            imageView.setImageURI(UriImg.uriArrayList.get(UriImg.uriArrayList.size()-1));
        }
    }

    private void addEvents() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogResult();
            }
        });
    }

    private void openDialogResult() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_result);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtttibutes = window.getAttributes();
        windowAtttibutes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtttibutes);

        if(Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        imgRaw = drawable.getBitmap();


        Button btnVenom = dialog.findViewById(R.id.buttonVenomDialog);
        Button btnClassi = dialog.findViewById(R.id.buttonClassDia);

        btnVenom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.d("AHIHI_dongoc_1", "onClick: ");
                openDialogVenom();
            }
        });

        btnClassi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Log.d("AHIHI_dongoc", "onClick: ");
                openDialogClassi();
            }
        });
        dialog.show();
    }

    private void openDialogClassi() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_result_class);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtttibutes = window.getAttributes();
        windowAtttibutes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtttibutes);

        if(Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        Button btnClose = dialog.findViewById(R.id.buttonColoseDialogClass);
        TextView txtResult = dialog.findViewById(R.id.textViewResultDialogClass);
        Button btnInfo = dialog.findViewById(R.id.buttonInfoDialogClass);
        String[] mResult = xuLyPredic(imgRaw,1).split("/");

        txtResult.setText(mResult[0]);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mResult[1].equals("Null")){

                }else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("key",mResult[1]);
                    startActivity(intent);
                    dialog.dismiss();
                }

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogVenom() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_result);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtttibutes = window.getAttributes();
        windowAtttibutes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtttibutes);

        if(Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        Button btnClose = dialog.findViewById(R.id.buttonColoseDialogVenom);
        TextView txtResult = dialog.findViewById(R.id.textViewResultDialogVenom);

        txtResult.setText(xuLyPredic(imgRaw,0));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
                imageView.setRotation(90);
            } catch (FileNotFoundException e) {
            }

        }
    }

    private void addControls(View view) {
        imageView = view.findViewById(R.id.imageViewSnakePredic);
        btnOpenDialog = view.findViewById(R.id.buttonOpenResultDialog);
    }

    private void openDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cam_or_gallery);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAtttibutes = window.getAttributes();
        windowAtttibutes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtttibutes);

        if(Gravity.CENTER == Gravity.CENTER){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }
        Button btnCam = dialog.findViewById(R.id.buttonCamDialog);
        Button btnGallery = dialog.findViewById(R.id.buttonGalleryDia);

        btnCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), PreviewCameraX.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
            }
        });
        dialog.show();
    }

    private String xuLyPredic(Bitmap img, int type){
        img = Bitmap.createScaledBitmap(img, 256, 256, true);
        if (type == 0){
            try {
                ModelOnR model = ModelOnR.newInstance(getContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int[] intValues = new int[256 * 256];
                img.getPixels(intValues, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
                int pixel = 0;
                //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                for(int i = 0; i < 256; i ++){
                    for(int j = 0; j < 256; j++){
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
                    return "Không phải rác ";
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
                Log.d("AHIHI_dongoc", "xuLyPredic: ");
                ModelClassifier model = ModelClassifier.newInstance(getContext());

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 256 * 256 * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int[] intValues = new int[256 * 256];
                img.getPixels(intValues, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
                int pixel = 0;
                //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
                for(int i = 0; i < 256; i ++){
                    for(int j = 0; j < 256; j++){
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
                    return "Không phải rác "+"/" +"Null";
                }
                String[] classes = {"Pin","Rác thải hữu cơ","Thủy tinh nâu","Giấy carton","Quần áo cũ",
                "Thủy tinh xanh","Kim loại","Giấy","Nhựa","Giày cũ","Rác thải sinh hoạt","Thủy tinh trắng"};
                // Releases model resources if no longer used.
                model.close();
                return classes[maxPos] + "/" +(maxPos);
            } catch (IOException e) {
                // TODO Handle the exception
            }
        }
        return null;
    }
}