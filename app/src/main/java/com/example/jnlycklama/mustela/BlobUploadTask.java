package com.example.jnlycklama.mustela;

import android.os.AsyncTask;
import android.widget.TextView;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.*;

import java.io.File;
import java.io.FileInputStream;

/**
 * This sample illustrates basic usage of the various Blob Primitives provided
 * in the Storage Client Library including CloudBlobContainer, CloudBlockBlob
 * and CloudBlobClient.
 */
public class BlobUploadTask extends AsyncTask<String, String, Void> {

    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;" +
                    "AccountName=mustelastorage;" +
                    "AccountKey=eIPq/9Auo89l22PMITENnhHWEVGCav/s1QMm+e8xGbD/kilKkRRgzAaVqjfjT/Zm6lmSZ5zbRRN7hVuNN4DiGA==";


    @Override
    protected Void doInBackground(String... arg0) {
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

            final String filePath = arg0[0];
            // Create or overwrite the "myimage.jpg" blob with contents from a local file.
            CloudBlockBlob blob = container.getBlockBlobReference(arg0[1]);
            File source = new File(filePath);
            blob.upload(new FileInputStream(source), source.length());
            System.out.println("yo");
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }

        return null;
    }
}