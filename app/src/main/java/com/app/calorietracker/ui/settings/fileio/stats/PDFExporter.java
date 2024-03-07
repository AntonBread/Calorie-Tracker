package com.app.calorietracker.ui.settings.fileio.stats;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;

import com.app.calorietracker.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDFExporter {
    
    public static boolean export(PdfDocument doc, Context context) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File outFile = new File(directory, createFileName(context));
        try {
            doc.writeTo(new FileOutputStream(outFile));
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        doc.close();
        return true;
    }
    
    private static String createFileName(Context context) {
        String base = context.getString(R.string.settings_file_export_stats_file_name);
        String dateSuffix = LocalDate.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
        String extension = ".pdf";
        return base.concat(dateSuffix).concat(extension);
    }
}
