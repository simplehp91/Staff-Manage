package jp.ac.jec.a16cm0209.staffmanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by nguyenhiep on 10/21/16 AD.
 */
public class AdapterStaff extends BaseAdapter {

    Activity context;
    ArrayList<Staff> list;
    final String DATABASE_NAME = "EmployeeDB.sqlite";

    public AdapterStaff(Activity context, ArrayList<Staff> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Positon getView
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.listview_row, null);
        ImageView imgView = (ImageView) row.findViewById(R.id.imgView);
        TextView txtId = (TextView) row.findViewById(R.id.txtId);
        TextView txtName = (TextView) row.findViewById(R.id.txtName);
        TextView txtPhoneNumber = (TextView) row.findViewById(R.id.txtPhoneNumber);
        TextView txtEmail = (TextView) row.findViewById(R.id.textEmail);
        TextView txtAdress = (TextView) row.findViewById(R.id.textAdress);
        Button btnRepair = (Button) row.findViewById(R.id.btnRepair);
        Button btnDelete = (Button) row.findViewById(R.id.btnDelate);

        //Set value
        final Staff staff = list.get(position);
        txtId.setText(staff.id + "");
        txtName.setText(staff.name);
        txtPhoneNumber.setText(staff.phonenumber);
        txtEmail.setText(staff.email);
        txtAdress.setText(staff.address);

        Bitmap bmimgView = BitmapFactory.decodeByteArray(staff.image, 0, staff.image.length);
        imgView.setImageBitmap(bmimgView);

        //Set sự kiện cho Button  変更
        btnRepair.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, UpdateActivity.class);
                /*
                Hàm startActivity không phải là phương thức của class AdapterStaff mà là phương thức của lớp ContextWrapper
                nên muốn gọi hàm startActivity thì phải có 1 đối tượng kiểu ContextWrapper hoặc thuộc cây kế thừa
                ví dụ: kiểu Activity, AppCompatActivity
                Đổi context về kiểu Activity
                */

                intent.putExtra("ID", staff.id);
                context.startActivity(intent);

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("確認");
                builder.setMessage("削除します？");
                builder.setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(staff.id);
                    }
                });
                builder.setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return row;
    }

    private void delete(int idStaff) {
        SQLiteDatabase database  = Database.initDatabase(context, DATABASE_NAME);
        database.delete("staff", "ID = ?", new String[]{idStaff + ""});

        Cursor cursor = database.rawQuery("SELECT * FROM staff", null);
        	list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String email = cursor.getString(4);
            String adress = cursor.getString(5);

            list.add(new Staff(id, name, phone, image, email, adress));
        }
        notifyDataSetChanged();
    }
}
