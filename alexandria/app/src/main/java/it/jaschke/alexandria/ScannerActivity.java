package it.jaschke.alexandria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import java.util.ArrayList;
import java.util.List;

public class ScannerActivity extends ActionBarActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZBarScannerView(this);
        setupFormats();
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AddBook.SCAN_CONTENTS, rawResult.getContents());
        resultIntent.putExtra(AddBook.SCAN_FORMAT, rawResult.getBarcodeFormat().getName());
        setResult(Activity.RESULT_OK, resultIntent);
        // Leaving this for debugging.
//        Toast.makeText(this, "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        //formats.add(BarcodeFormat.ISBN10);
        formats.add(BarcodeFormat.ISBN13);
        formats.add(BarcodeFormat.EAN13);
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }
}