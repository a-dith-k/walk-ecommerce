package com.adith.walk.helper;
import com.adith.walk.exceptions.IncompatibleImageException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class ImageCropper {

    // Java Program to Crop Image Using BufferedImage Class

// Importing required packages


        // Main driver method
        public  void cropImage(File f,File output) throws IncompatibleImageException {

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


                // Writing image in new file created
                ImageIO.write(SubImg, "jpg", output);

            }

            // Catch block to handle the exceptions
            catch (IOException e) {

                e.printStackTrace();
            }
        }


}
