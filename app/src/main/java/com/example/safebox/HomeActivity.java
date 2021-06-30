package com.example.safebox;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.safebox.utils.MyEncrypter;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class HomeActivity extends AppCompatActivity {
    String FILE_NAME_ENC;
    Button btn,logout;
    FirebaseAuth mauth;
    Intent myFileIntent;
    String filepath;
//    Bitmap bitmapofgalleryimage;
    InputStream galleryimageinputstream;
    File myDir;
    String my_key="P7q0kXUrBg7I6Ilg";
    String my_spec_key="f0pwGwwCuB3U73NA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializefield();
        Dexter.withContext(this).
                withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener(){
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                btn.setEnabled(true);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                           PermissionToken permissionToken) {
                Toast.makeText(HomeActivity.this,"grant permission",
                        Toast.LENGTH_SHORT).show();
            }
        }).check();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            myFileIntent=new Intent(Intent.ACTION_GET_CONTENT);
            mGetContent.launch("image/*");

            }

        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void encryptimage() {
        File outputFileEncr=new File(myDir,FILE_NAME_ENC);
        try {
            MyEncrypter.encryptToFile(my_key,my_spec_key,
                    galleryimageinputstream,new FileOutputStream(outputFileEncr));
            Toast.makeText(HomeActivity.this,"encrypted!!!!!!!!!!",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    filepath=uri.getPath();
                    galleryimageinputstream=uriToBitmap(uri);
                    encryptimage();
                }
            });

    private InputStream uriToBitmap(Uri uri) {
        try {
//            ParcelFileDescriptor parcelFileDescriptor=
//                    getContentResolver().openFileDescriptor(uri,"r");
//            FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();
//            Bitmap image= BitmapFactory.decodeFileDescriptor(fileDescriptor);
//            ByteArrayOutputStream stream=new ByteArrayOutputStream();
//            image.compress(Bitmap.CompressFormat.PNG,100,stream);
//            InputStream inputStream=new ByteArrayInputStream(stream.toByteArray());
//            parcelFileDescriptor.close();
//            return inputStream;
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            InputStream inputStream=new ByteArrayInputStream(stream.toByteArray());
            return inputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void initializefield() {
        btn=findViewById(R.id.upload_btn);
        logout=findViewById(R.id.logout_btn);
        mauth=FirebaseAuth.getInstance();
//         getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
        myDir=new File(Environment.getExternalStorageDirectory()+"/safebox_dir");
//        myDir=new File(getApplication().getExternalFilesDirs(Environment.getExternalStorageState("/safebox_dir")));
        FILE_NAME_ENC=new SimpleDateFormat("yyyyMMddhhmmss'.txt'").format(new Date());
    }
}