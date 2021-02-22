package com.mkrlabs.chatmes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CAMERA_CODE = 111;
    private static final int IMAGE_PICK_GALLERY_CODE = 222;
    TextView haveAnAccountTV;
    ImageButton imageAddButton;
    private final  int PERMISSION_CODE=100;
    private Uri image_uri;
    private CircleImageView userSelectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtils.systembartransparent(this);
        setContentView(R.layout.activity_register);
        init();

        haveAnAccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    imageSelection();
                }else {
                    showUserSelectionDialog();
                }*/
                showUserSelectionDialog();
            }
        });
    }

    private void showUserSelectionDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Select Images");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    pickCemera();
                } else if (i == 1) {
                    pickGallery();
                }
            }
        });

        dialog.show();
     }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCemera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to Text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void imageSelection() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            showUserSelectionDialog();
        }else {
            checkPermission();
        }

    }

    private void checkPermission() {
        String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this,permissions,PERMISSION_CODE);
    }

    private void init() {
        haveAnAccountTV=findViewById(R.id.alreadyHaveAnAccountTV);
        imageAddButton=findViewById(R.id.imageAddButton);
        userSelectedPhoto=findViewById(R.id.userSelectedPhoto);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)     //enable image guideline
                        .start(this);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)     //enable image guideline
                        .start(this);
            }

        }


//        get Crop image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri(); // get image uri
                // set image to image view
                image_uri=resultUri;
                userSelectedPhoto.setImageURI(image_uri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

            }
        }
    }
}