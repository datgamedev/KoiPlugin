package com.grepgame.android.plugin.grplugin;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Debug;
import android.provider.MediaStore;

import android.os.Bundle;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Console;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.unity3d.player.UnityPlayer;

public class KoiGalleryActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		openGallery();

	}

	public void openGallery() {
		
		Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}

	private static final int RESULT_LOAD_IMAGE = 1333;

	@Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {

			if (resultCode == RESULT_OK && null != data) {
				Bitmap bm;

				if (requestCode == RESULT_LOAD_IMAGE) {
					Uri selectedImage = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String picturePath = cursor.getString(columnIndex);

					if (picturePath != null) {

						Log.d(Koi.KOI_TAG, "picturePath " + picturePath);
						cursor.close();
						bm = Util.decodeSampledBitmapFromFile(picturePath, 500, 500);
						
						bm = Util.crop(bm);


						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						if (bm != null)
							bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
						byte[] byteArray = stream.toByteArray();

						Log.d(Koi.KOI_TAG, "imagedata " + byteArray.length);

						JSONObject jso = new JSONObject();
						try {
							jso.put("width", bm.getWidth());
							jso.put("height", bm.getHeight());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						JSONArray jsa = new JSONArray();
						jsa.put(jso);

						Koi.instance.setPluginData(byteArray);
						Koi.sendMessageToKoiObject(KOI_GALLERY_CALL_BACK, jsa);

					}
				}

			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}

		
		} finally {
			Koi.instance.backToUnity(this);
		}

	}

	public static final String KOI_GALLERY_CALL_BACK = "KoiGallery_PickDone";

}
