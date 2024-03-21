package com.adith.walk.helper;
import com.adith.walk.exceptions.IncompatibleImageException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class ImageCropper {

    // Java Program to Crop Image Using BufferedImage Class

// Importing required packages


        // Main driver method
        public byte[] cropImage(File f) throws IncompatibleImageException {

            try {

                // Reading original image from given path

                BufferedImage originalImg = ImageIO.read(f);

                //printing original image dimensions
                System.out.println("Original Image Dimension: "
                        + originalImg.getWidth()
                        + "x"
                        + originalImg.getHeight());

                //checking whether image have proper size
                if(originalImg.getHeight()<600||originalImg.getWidth()<800)
                    throw new IncompatibleImageException("Image should have size of 800X600");

                // Creating a cropped Image of given dimensions
                BufferedImage SubImg
                        = originalImg.getSubimage(0, 0, 800, 600 );
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                // Writing image in new file created
                ImageIO.write(SubImg, "jpg", baos);
                byte[] bytes=baos.toByteArray();
                baos.flush();
                return bytes;


            }

            // Catch block to handle the exceptions
            catch (IOException e) {

                e.printStackTrace();
            }
            return new byte[0];
        }


}
