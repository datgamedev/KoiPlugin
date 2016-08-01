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

public class KoiCameraActivity extends Activity {

	private static final String KOI_CAMERA_CALL_BACK = "KoiCamera_PickDone";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		openCamera();

	}

	public void openCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, RESULT_TAKE_NEW);
	}

	private static int RESULT_TAKE_NEW = 1337;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		try {
			if (resultCode == RESULT_OK && null != data) {
				Bitmap bm;
				if (requestCode == RESULT_TAKE_NEW) {

					Bundle b = data.getExtras();
					Bitmap tmp = (Bitmap) b.get("data");
					if (tmp != null) {
						int w = tmp.getWidth(), h = tmp.getHeight();
						if (w > 500) {
							h = h * 500 / w;
							w = 500;
						}

						if (h > 500) {
							w = w * 500 / h;
							h = 500;
						}

						bm = Bitmap.createScaledBitmap(tmp, w, h, true);
						bm = Util.crop(bm);

						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
						byte[] byteArray = stream.toByteArray();

						JSONObject jso = new JSONObject();
						try {
							jso.put("width", bm.getWidth());
							jso.put("height", bm.getHeight());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						JSONArray jsa = new JSONArray();
						jsa.put(jso);

						Log.d(Koi.KOI_TAG, "imagedata " + byteArray.length);
						Koi.instance.setPluginData(byteArray);
						Koi.sendMessageToKoiObject(KOI_CAMERA_CALL_BACK, jsa);

					}

				}

			}

			else {
				super.onActivityResult(requestCode, resultCode, data);
			}

		} finally {
			Koi.instance.backToUnity(this);
		}

	}

}
