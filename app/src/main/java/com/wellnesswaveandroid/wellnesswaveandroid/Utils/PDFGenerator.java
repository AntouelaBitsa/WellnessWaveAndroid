package com.wellnesswaveandroid.wellnesswaveandroid.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Diagnosis;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Doctor;
import com.wellnesswaveandroid.wellnesswaveandroid.Entities.Patient;
import com.wellnesswaveandroid.wellnesswaveandroid.R;

import java.io.File;
import java.io.FileOutputStream;

public class PDFGenerator {

    private final Context context;

    public PDFGenerator(Context context) {
        this.context = context;
    }

    public void generatePDF(Patient patientData, Doctor doctorData, Diagnosis diagnosisData){

//        // Check if external storage is writable
//        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            Log.e("PDF_ERROR", "External storage not available or not writable");
//            Toast.makeText(context, "External storage is not writable", Toast.LENGTH_SHORT).show();
//            return;
//        }

        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        //Page 1
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595,842,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
//        Set Geologica font to be used in PDF
//        Typeface geologicaFont = Typeface.createFromAsset(context.getAssets(), "res/font/geologica_cursive_regular.ttf");

        // Background color
        canvas.drawColor(Color.WHITE);

        //DONE: Wellness Wave Logo - Top/Right Corner
        Drawable logoSmallDrawable = context.getResources().getDrawable(R.drawable.wellness_wave_logo, null);
        if (logoSmallDrawable != null){
            Bitmap bitmap = Bitmap.createBitmap((int) 30, (int) 29.46, Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(bitmap);
            logoSmallDrawable.setBounds(0,0, tempCanvas.getWidth(), tempCanvas.getHeight());
            logoSmallDrawable.draw(tempCanvas);

            canvas.drawBitmap(bitmap, 546, 11, null);
        }

        //DONE: Wellness Wave Logo - Center/Right Page Side - Decorator

        Drawable logoDecoratorDrawable = context.getResources().getDrawable(R.drawable.logo_decorator, null);
        if (logoDecoratorDrawable != null){
            Bitmap bitmap2 = Bitmap.createBitmap((int) 348, (int) 344, Bitmap.Config.ARGB_8888);
            Canvas tempCanvas2 = new Canvas(bitmap2);
            logoDecoratorDrawable.setBounds(0,0, tempCanvas2.getWidth(), tempCanvas2.getHeight());
            logoDecoratorDrawable.draw(tempCanvas2);

            canvas.drawBitmap(bitmap2, 288, 283, null);
        }

        Typeface typeface = ResourcesCompat.getFont(context, R.font.geologica_cursive_regular);
        Typeface boldTypeface = Typeface.create(typeface, Typeface.BOLD);
        int navyBlue = ContextCompat.getColor(context, R.color.navyBlue);
        int skyBlue = ContextCompat.getColor(context, R.color.skyblue);
        float cornerRadius = 10f;

        //DONE: Provided by Wellness Wave - Small text Top/Left Corner
//        paint.setColor(Color.LTGRAY);
        paint.setTypeface(typeface);
        paint.setTextSize(10);
        paint.setColor(navyBlue);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Provided by Wellness Wave", 20, 30, paint);

        //DONE: Title Medical Diagnosis Document
        paint.setColor(navyBlue);
        paint.setTypeface(boldTypeface);
        paint.setTextSize(16);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Medical Diagnosis Document", pageInfo.getPageWidth()/2, 70, paint);

        //DONE: Rectangle Decorators Patient and Doctor
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(skyBlue);
        paint.setStrokeWidth(1);
        canvas.drawRoundRect(20,90,280,240, cornerRadius, cornerRadius,paint); //Patient Rectangle
        canvas.drawRoundRect(315,90,575,240, cornerRadius, cornerRadius, paint); //Doctor Rectangle

//        Paint label = new Paint();
//        label.setTypeface(boldTypeface);
//        label.setColor(navyBlue);
//        label.setTextSize(11);
//
//        Paint data = new Paint();
//        data.setTypeface(typeface);
//        data.setColor(navyBlue);
//        data.setTextSize(11);

        //DONE: Patient Details Text
        paint.setTypeface(boldTypeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(navyBlue);
        paint.setTextSize(12);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Patient Details", 32, 115, paint);
        paint.setTypeface(typeface);
        paint.setTextSize(11);
        canvas.drawText("Patient ID: " + patientData.getPatientId(), 32, 140, paint);
//        Showing label and data with different styling --> not working
//        String labelText = "Patient ID: ";
//        String patientId = String.valueOf(patientData.getPatientId());
//        canvas.drawText(labelText + patientData.getPatientId(), 32, 140, label);
//        float labelWidth = label.measureText("Patient ID: ");
//        canvas.drawText(patientId, 32 + labelWidth, 140, data);
        canvas.drawText("Full Name: " + patientData.getPatFirstName().concat(" ").concat(patientData.getPatLastName()), 32, 160, paint);
        canvas.drawText("AMKA: " + patientData.getPatSecuredNum(), 32, 180, paint);
        canvas.drawText("Birthdate: " + patientData.getPatDob(), 32, 200, paint);
        canvas.drawText("Ph. Num.: " + patientData.getPatPhoneNum(), 32, 220, paint);

        //DONE: Doctor Details Text
        paint.setTypeface(boldTypeface);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(navyBlue);
        paint.setTextSize(12);
        canvas.drawText("Doctor Details", 325, 115, paint);
        paint.setTypeface(typeface);
        paint.setTextSize(11);
        canvas.drawText("Doctor ID: " + doctorData.getDocId(), 325, 140, paint);
        canvas.drawText("Full Name: " + doctorData.getDocFirstName().concat(" ").concat(doctorData.getDocLastName()), 325, 160, paint);
        canvas.drawText("Address: " + doctorData.getDocAddress(), 325, 180, paint);
        canvas.drawText("Profession: " + doctorData.getDocProfession(), 325, 200, paint);
        canvas.drawText("Ph. Num.: " + doctorData.getDocPhoneNum(), 325, 220, paint);

        //DONE: Diagnosis Section & Decorator
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(skyBlue);
        canvas.drawRoundRect(20,260,575,370, cornerRadius, cornerRadius, paint); //Diagnosis Rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(navyBlue);
        paint.setTypeface(boldTypeface);
        paint.setTextSize(13);
        canvas.drawText("Diagnosis", 30, 290, paint);
        paint.setTypeface(typeface);
        paint.setTextSize(11);
        canvas.drawText("Diagnosis Type: " + diagnosisData.getDiagnType(), 30, 320, paint);
        canvas.drawText("Instructions: " + diagnosisData.getDiagnInfo(), 30, 340, paint);

        //DONE: Treatment Section & Decorator
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(skyBlue);
        canvas.drawRoundRect(20,390,575,540, cornerRadius,cornerRadius, paint); //Diagnosis Rectangle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(navyBlue);
        paint.setTypeface(boldTypeface);
        paint.setTextSize(13);
        canvas.drawText("Treatment", 30, 420, paint);
        paint.setTypeface(typeface);
        paint.setTextSize(11);
        canvas.drawText("Treatment Name: " + diagnosisData.getTreatment(), 30, 450, paint);
        canvas.drawText("Treatment Dose: " + diagnosisData.getTreatmDose(), 30, 470, paint);
        canvas.drawText("Start Date: " + diagnosisData.getStartDate(), 30, 490, paint);
        canvas.drawText("End Date: " + diagnosisData.getEndDate(), 30, 510, paint);

        //DONE: Diagnosis Document provided by Wellness Wave Application
        paint.setTextSize(10);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Diagnosis Document provided by Wellness Wave Application", pageInfo.getPageWidth() / 2, 820, paint);

        //Finish Page
        pdfDocument.finishPage(page);

//        File documentDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File documentDir = Environment.getExternalStorageDirectory();
        if (!documentDir.exists()){
            boolean isCreated = documentDir.mkdirs(); //Creates the directory
            Log.d("PDF_PATH on Dir Creation", "Documents directory created: " + isCreated);
        }

        //Create & Save PDF to external Storage
        File file = new File(documentDir, "WellnessWaveDiagnosis.pdf");
        try{
            pdfDocument.writeTo(new FileOutputStream(file));
            Log.d("PDF_PATH", "PDF file saved at: " + file.getAbsolutePath());
        }catch (Exception e){
            Log.e("PDF_ERROR", "Error while saving PDF: " + e.getMessage());
            e.printStackTrace();
        }

        pdfDocument.close();
    }
}
