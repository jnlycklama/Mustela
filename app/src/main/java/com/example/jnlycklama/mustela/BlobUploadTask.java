package com.example.jnlycklama.mustela;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This sample illustrates basic usage of the various Blob Primitives provided
 * in the Storage Client Library including CloudBlobContainer, CloudBlockBlob
 * and CloudBlobClient.
 */
public class BlobUploadTask extends AsyncTask<File, Void, Void> {

    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;" +
                    "AccountName=mustelastorage;" +
                    "AccountKey=eIPq/9Auo89l22PMITENnhHWEVGCav/s1QMm+e8xGbD/kilKkRRgzAaVqjfjT/Zm6lmSZ5zbRRN7hVuNN4DiGA==";


    @Override
    protected Void doInBackground(File... arg0) {
        try {
            // Retrieve storage account from connection-string.
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

            // Get a reference to a container.
            // The container name must be lower case
            CloudBlobContainer container = blobClient.getContainerReference("images");

            // Create the container if it does not exist.
            container.createIfNotExists();

            // Define the path to a local file.
            //final String filePath = getOutputMediaFile().getPath() + "IMG_20160305_031107.jpg";
            final String filePath = arg0[0].getPath();
            // Create or overwrite the "myimage.jpg" blob with contents from a local file.
            CloudBlockBlob blob = container.getBlockBlobReference(arg0[0].getName());
            blob.upload(new FileInputStream(arg0[0]), arg0[0].length());
            System.out.println("yo");
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }

        return null;
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        System.out.println("Path: "+mediaStorageDir.getPath());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        System.out.println("File name"+mediaFile.getName());
        return mediaFile;
    }
}