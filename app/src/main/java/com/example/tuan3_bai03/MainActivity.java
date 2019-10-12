package com.example.tuan3_bai03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button btn_read_internal, btn_write_internal,btn_read_static,
            btn_write_cache, btn_read_external,btn_write_external;
    EditText edt_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_input=findViewById(R.id.edt_input);
        btn_read_static=findViewById(R.id.btn_ReadStatic);
        btn_read_external=findViewById(R.id.btn_ReadExternal);
        btn_read_internal=findViewById(R.id.btn_ReadInternal);
        btn_write_cache=findViewById(R.id.btn_WriteCache);
        btn_write_internal=findViewById(R.id.btn_WriteInternal);
        btn_write_external=findViewById(R.id.btn_WriteExternal);
        btn_read_static.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadStaticFile();
            }
        });
        btn_read_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadFileInternal("song.txt" );
            }
        });
        btn_write_internal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=edt_input.getText().toString();
                WriteFileInternal("song.txt",data);
            }
        });
        btn_write_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteCacheFile("song",edt_input.getText().toString());
            }
        });
        btn_read_external.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadFileExternalFile("song");
            }
        });
        btn_write_external.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteFileExternalFile("song",edt_input.getText().toString());
            }
        });
    }
    private void WriteCacheFile(String filename,String data){
        SharedPreferences sharedPreferences=getSharedPreferences("song",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(filename,data);
        editor.commit();
        Toast.makeText(this, "Write Cache file is success", Toast.LENGTH_SHORT).show();
    }
    private void ReadStaticFile(){
        InputStream inputStream=getResources().openRawResource(R.raw.song);
        Scanner in=new Scanner(inputStream);
        String data="";
        while (in.hasNextLine()){
            data+=in.nextLine()+"\n";
        }
        in.close();
        edt_input.setText(data);
    }
    private void WriteFileInternal(String filename,String data){
        try {
            FileOutputStream fileOutputStream= openFileOutput(filename,MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Write file is success", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "Cannot write file", Toast.LENGTH_SHORT).show();

        }
    }
    private void ReadFileInternal(String filename){
        try {
            FileInputStream fileInputStream=openFileInput(filename);
            StringBuffer stringBuffer=new StringBuffer();
            int buffer;
            while ((buffer=fileInputStream.read())>0){
                stringBuffer.append(Character.toString((char)buffer));
            }
            edt_input.setText(stringBuffer);
            fileInputStream.close();
        } catch (java.io.IOException e) {
            Toast.makeText(this, "File is not exist", Toast.LENGTH_SHORT).show();
        }
    }
    private int GetStateExternal(){
        String state= Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED))
            return 0;// readable writeable
        else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            return 1;//read only
        return -1;
    }

    private void WriteFileExternalFile(String filename,String data){
        if(GetStateExternal()==0){
            File folder=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File myfile=new File(folder,filename);
            try {
                FileOutputStream fstream=new FileOutputStream(myfile);
                fstream.write(data.getBytes());
                fstream.close();
            }
            catch (Exception e){
                Toast.makeText(this, "Error to write file", Toast.LENGTH_SHORT).show();
            }


        }
        else
            Toast.makeText(this, "Cannot access read file", Toast.LENGTH_SHORT).show();

    }
    private void ReadFileExternalFile(String filename){
        if(GetStateExternal()>=0){
            File folder=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File myfile=new File(folder,filename);
            try {
                FileInputStream fin=new FileInputStream(folder);
                StringBuffer buffer=new StringBuffer();
                int i;
                while ((i=fin.read())>0){
                    buffer.append(Character.toString((char)i));
                }
                fin.close();
                edt_input.setText(buffer.toString());
            }catch (Exception e){
                Toast.makeText(this, "Error to read file because file not exist", Toast.LENGTH_SHORT).show();

            }
        }
        else
            Toast.makeText(this, "Cannot access write file", Toast.LENGTH_SHORT).show();
    }
}
