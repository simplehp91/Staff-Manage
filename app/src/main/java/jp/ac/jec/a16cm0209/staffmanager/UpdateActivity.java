package jp.ac.jec.a16cm0209.staffmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {

    final String DATABASE_NAME = "EmployeeDB.sqlite";
    final int RESQUEST_TAKE_PHOTO = 123;
    final int RESQUEST_CHOOSE_PHOTO = 321;
    int id = -1;

    Button btnTake, btnUpload, btnSave, btnCanel;
    EditText edtPhone, edtName, edtEmail, edtAdress;
    ImageView imgLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        addControls();
        initUI();
        addEvents();
    }

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM staff where ID = ? ",new String[]{id + ""} );
        cursor.moveToFirst();

        String name = cursor.getString(1);
        String phone = cursor.getString(2);
        byte[] image = cursor.getBlob(3);
        String email = cursor.getString(4);
        String adress = cursor.getString(5);

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imgLoad.setImageBitmap(bitmap);
        edtPhone.setText(phone);
        edtName.setText(name);
        edtEmail.setText(email);
        edtAdress.setText(adress);
    }

    private void addControls() {
        btnTake = (Button) findViewById(R.id.btnTake);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCanel = (Button) findViewById(R.id.btnCanel);
        edtPhone = (EditText) findViewById(R.id.editPhone);
        edtName = (EditText) findViewById(R.id.editName);
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtAdress = (EditText) findViewById(R.id.editAdress);
        imgLoad = (ImageView) findViewById(R.id.imgLoad);
    }

    private void addEvents(){
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESQUEST_TAKE_PHOTO);
    }

    private void choosePicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, RESQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == RESQUEST_CHOOSE_PHOTO){
                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgLoad.setImageBitmap(bitmap);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            } else  if (requestCode == RESQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgLoad.setImageBitmap(bitmap);
            }
        }
    }

    private void update(){
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String adress = edtAdress.getText().toString();
        byte[] image = getByteArrayFromImageView(imgLoad);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("PhoneNumber", phone);
        contentValues.put("Email", email);
        contentValues.put("Adress", adress);
        contentValues.put("Image", image);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.update("staff", contentValues, "ID = ?", new String[]{id + ""});
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByteArrayFromImageView(ImageView imgv){
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
     }
}
