package websheba.kawcher.exportdataandroidtoexcelsheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import org.apache.poi.ss.usermodel.Font;

import org.apache.poi.ss.usermodel.IndexedColors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText nameET, phoneET, emailET;
    private ImageView exportIV;
    private ArrayList<UserModel> userList;
    private DBHelper dbHelper;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        init();

        onClick();


    }

    private void onClick() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString().trim();
                String phone = phoneET.getText().toString().trim();
                String email = emailET.getText().toString().trim();

                //check edit text data
                if (TextUtils.isEmpty(name)) {
                    nameET.setError("required");
                    nameET.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    phoneET.setError("required");
                    phoneET.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailET.setError("required");
                    emailET.requestFocus();
                    return;
                }


                UserModel userModel = new UserModel(name, phone, email);
              long id=  dbHelper.insertUser(userModel);

              if(id>0){
                  Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
              }

            }
        });

        exportIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, 1);
                    } else {
                        importData();
                    }
                } else {
                    importData();
                }

            }
        });
    }


    private void importData() {
        //get data from edit text

        /*userList.add(new UserModel("kawcher", "0181238383", "nmkawcher112@gmail.com"));
        userList.add(new UserModel("shuvo", "0171238383", "demo1@gmail.com"));
        userList.add(new UserModel("hasan", "0161238383", "demo2@gmail.com"));
        userList.add(new UserModel("habib", "0151238383", "demo3@gmail.com"));
        userList.add(new UserModel("selim", "0131238383", "demo4@gmail.com"));
        userList.add(new UserModel("tayeb", "0121238383", "demo5@gmail.com"));
        userList.add(new UserModel("abul kalam", "0191238383", "demo6@gmail.com"));
        userList.add(new UserModel("Kamal", "0101238383", "demo7@gmail.com"));
*/

       userList= dbHelper.getAllLocalUser();

       if(userList.size()>0){
           createXlFile();
       } else {
           Toast.makeText(this, "list are empty", Toast.LENGTH_SHORT).show();
       }


    }

    private void createXlFile() {


        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();


        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Demo Excel Sheet");
        //Now column and row
        Row row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Person Name");


        cell = row.createCell(1);
        cell.setCellValue("Phone Number");


        cell = row.createCell(2);
        cell.setCellValue("Email Address");


        //column width
        sheet.setColumnWidth(0, (20 * 200));
        sheet.setColumnWidth(1, (30 * 200));
        sheet.setColumnWidth(2, (30 * 200));


        for (int i = 0; i < userList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(userList.get(i).getName());

            cell = row1.createCell(1);
            cell.setCellValue((userList.get(i).getPhoneNo()));
            //  cell.setCellStyle(cellStyle);

            cell = row1.createCell(2);
            cell.setCellValue(userList.get(i).getEmail());


            sheet.setColumnWidth(0, (20 * 200));
            sheet.setColumnWidth(1, (30 * 200));
            sheet.setColumnWidth(2, (30 * 200));

        }
        String folderName = "Import Excel";
        String fileName = folderName + System.currentTimeMillis() + ".xls";
        String path = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + fileName;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(getApplicationContext(), "Excel Created in " + path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Not OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }


    }

    private void init() {
        userList = new ArrayList<>();
        nameET = findViewById(R.id.et_name);
        phoneET = findViewById(R.id.et_phone);
        emailET = findViewById(R.id.et_email);
        exportIV = findViewById(R.id.iv_import);
        saveBtn = findViewById(R.id.btn_save);

        dbHelper = new DBHelper(MainActivity.this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            importData();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
}