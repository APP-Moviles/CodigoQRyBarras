package com.example.usrgam.codigoqrbarras;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
//import android.Manifest.permission.CAMARA;

import javax.xml.transform.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static  final int REQUESTCAMERA=1;
    private  ZXingScannerView scannerView;
    private static  int camID= Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //comprobar permisos de acceso a la camara
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //controlar los permisos
            if(verificarPermisos()){
                //mensaje
                Toast.makeText(getApplicationContext(),"permiso otrogado",Toast.LENGTH_LONG).show();
            }else{
                //solicitopermiso
                solicitarPermisos();
            }
        }
    }
    public boolean verificarPermisos(){
        //es el contexto de la aplicacion
        return ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;


    }
    public void  solicitarPermisos(){
        ActivityCompat.requestPermissions(this,new  String[]{Manifest.permission.CAMERA},REQUESTCAMERA);
    }

    @Override
    //para resetear
    protected void onResume() {
        super.onResume();
        int apiVersion=Build.VERSION.SDK_INT;
        if (apiVersion>Build.VERSION_CODES.M) {
            if (verificarPermisos()){
                if (scannerView==null) {
                    scannerView=new ZXingScannerView(this);
                    setContentView(scannerView);
                    scannerView.setResultHandler(this);
                    scannerView.startCamera();
                }else{
                    solicitarPermisos();
                }
            }
        }
    }

    @Override
    //para la camara

    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUESTCAMERA:
                if(grantResults.length>0){
                    boolean aceptaPermiso=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(aceptaPermiso){
                        //msj
                    }else {
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M ){
                            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                                requestPermissions(new String[]{Manifest.permission.CAMERA},REQUESTCAMERA);
                            }
                        }
                    }
                }
        }
    }

    @Override
    public void handleResult(com.google.zxing.Result result) {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setMessage(result.getText());//texto y formato codigo y barras
        alertDialog.show();
        Log.e("Resultado:",result.getText());
        Log.e("ResultadoBar:",result.getBarcodeFormat().toString());

    }
}


