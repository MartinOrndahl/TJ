package com.beatem.tj.Camera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.beatem.tj.R;

/**
 * Created by JoelBuhrman on 16-05-04.
 */
public class DisplayTry extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaytry);

        ImageView imageView= (ImageView)findViewById(R.id.displaytry_image);

        Intent intent = getIntent();byte[] bytes = intent.getByteArrayExtra("BMP");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);


        imageView.setImageBitmap(bmp);
    }
}
