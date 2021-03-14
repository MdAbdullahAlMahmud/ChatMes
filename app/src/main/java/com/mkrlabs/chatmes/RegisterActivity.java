package com.mkrlabs.chatmes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mkrlabs.chatmes.model.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class RegisterActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CAMERA_CODE = 111;
    private static final int IMAGE_PICK_GALLERY_CODE = 222;
    TextView haveAnAccountTV;
    ImageButton imageAddButton;
    private final  int PERMISSION_CODE=100;
    private Uri image_uri;
    private CircleImageView userSelectedPhoto;

    private TextView haveAnAccount;
    private EditText firstNameEdt,lastNameEdt,emailEdt,passwordEdt,confirmPasswordEdt;
    private CardView signUp;
    private String firstName,lastName,email,password,confirmPassword;

    private StorageReference storageReference;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private ProgressDialog dialog;


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
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNecessaryField();
            }
        });

    }

    private void checkNecessaryField() {
        firstName = firstNameEdt.getText().toString();
        lastName = lastNameEdt.getText().toString();
        email = emailEdt.getText().toString();
        password = passwordEdt.getText().toString();
        confirmPassword = confirmPasswordEdt.getText().toString();

        if (firstName.isEmpty()){
            firstNameEdt.setError("required");
            return;
        }
        if (lastName.isEmpty()){
            lastNameEdt.setError("required");
            return;
        } if (email.isEmpty()){
            emailEdt.setError("required");
            return;
        } if (password.isEmpty()){
            passwordEdt.setError("required");
            return;
        } if (confirmPassword.isEmpty()){
            confirmPasswordEdt.setError("required");
            return;
        }

        if (!confirmPassword.equals(password)){
            Toast.makeText(this, "Password Mismatch", Toast.LENGTH_SHORT).show();
            return;
        }
        if (image_uri==null){
            Toast.makeText(this, "Select an Image", Toast.LENGTH_SHORT).show();
            //snackBar(this,"Select an Image");
            return;
        }
        uploadImage();
    }

    private void uploadImage() {
        dialog.show();
        String pushKey = reference.push().getKey();
        Bitmap bitmap = ((BitmapDrawable)userSelectedPhoto.getDrawable()).getBitmap();
        File file = bitmapToFile(bitmap,"photo");

        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {

            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                registration(downloadUrl);

            }

            @Override
            public void onUploadProgress(int progress) {

            }

            @Override
            public void onUploadCancelled() {

            }
        },storageReference.child("user_photos").child(pushKey));
        firebaseUploader.uploadImage(RegisterActivity.this,file);


    }

    private void registration(String downloadUrl) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        uploadUserInfo(downloadUrl,task.getResult().getUser().getUid());

                }else {
                    Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadUserInfo(String downloadUrl, String uid) {
        User user = new User(firstName,lastName,email,downloadUrl,uid,true);


        reference.child("Users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Intent intent = new Intent(RegisterActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private File bitmapToFile(Bitmap bitmap, String name) {
        File filesDir = getCacheDir();
        File imageFile = new File(filesDir, name + ".jpg");
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("Bitmap Error", "Error writing bitmap", e);
        }
        return imageFile;
    }
    private void showUserSelectionDialog() {
        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle("Select Images");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    pickCamera();
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

    private void pickCamera() {
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

        firstNameEdt=findViewById(R.id.registrationFirstNameEDT);
        lastNameEdt=findViewById(R.id.registrationLastNameEDT);
        emailEdt=findViewById(R.id.loginEmailEDT);
        passwordEdt=findViewById(R.id.registrationPasswordEDT);
        confirmPasswordEdt=findViewById(R.id.registrationConfirmPasswordEDT);
        signUp=findViewById(R.id.loginCardView);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Account Registration");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("please wait...");


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