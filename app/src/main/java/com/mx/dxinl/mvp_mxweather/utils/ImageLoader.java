package com.mx.dxinl.mvp_mxweather.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.mx.dxinl.mvp_mxweather.model.NetworkHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by DengXinliang on 2016/3/14.
 */
public class ImageLoader {
	private static final int MAX_SIZE = 1;
	private static ImageLoader INSTANCE = null;
	private final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	private final String IMAGE_PATH = "MxWeather" + File.separator + "Image";
	private final String IMG_SUFFIX = ".png";

	private final HashMap<String, AsyncTask<Object, Object, Bitmap>> taskList = new HashMap<>();
	private LinkedHashMap<String, Bitmap> bitmapCache;
	private int size;

	private ImageLoader() {
		bitmapCache = new LinkedHashMap<>();
	}

	public static ImageLoader get() {
		if (INSTANCE == null) {
			INSTANCE = new ImageLoader();
		}

		return INSTANCE;
	}

	public void setImageBitmap(ImageView view, String code) {
		Bitmap bitmap = getBitmapFromMemory(code);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			return;
		}

		bitmap = getBitmapFromDisk(code);
		if (bitmap != null) {
			view.setImageBitmap(bitmap);
			return;
		}

		setBitmapFromNetwork(view, code);
	}

	public Bitmap getImageBitmap(String code) {
		Bitmap bitmap = getBitmapFromMemory(code);
		if (bitmap != null) {
			return bitmap;
		}

		bitmap = getBitmapFromDisk(code);
		if (bitmap != null) {
			return bitmap;
		}

		bitmap = getBitmapFromNetwork(code);
		return bitmap;
	}

	public synchronized void cancelGetImgTask() {
		for (AsyncTask task : taskList.values()) {
			task.cancel(true);
		}
		taskList.clear();
	}

	private Bitmap getBitmapFromMemory(String code) {
		return bitmapCache.get(code);
	}

	private Bitmap getBitmapFromDisk(String code) {
		String path = SDCARD_PATH + File.separator + IMAGE_PATH;
		File imgDir = new File(path);
		if (imgDir.exists() && imgDir.isDirectory()) {
			String imgPath = path + File.separator + code + IMG_SUFFIX;
			File img = new File(imgPath);
			if (img.exists() && img.isFile()) {
				Bitmap bmp = BitmapFactory.decodeFile(imgPath);
				if (bmp != null) {
					putToBitmapCache(code, bmp);
					return bmp;
				} else {
					return null;
				}
			}
		}

		return null;
	}

	private synchronized void setBitmapFromNetwork(ImageView view, String code) {
		if (taskList.get(code) == null) {
			NetworkImageLoaderTask task = new NetworkImageLoaderTask(code);
			task.addView(view);
			task.execute();
			taskList.put(code, task);
		} else {
			((NetworkImageLoaderTask) taskList.get(code)).addView(view);
		}
	}

	private synchronized void putToBitmapCache(String code, Bitmap bitmap) {
		int newBmpSize = sizeOf(bitmap);
		while (true) {
			if (size + newBmpSize < MAX_SIZE * 1024 * 1024) {
				bitmapCache.put(code, bitmap);
				size += newBmpSize;
				break;
			}

			Map.Entry<String, Bitmap> items = bitmapCache.entrySet().iterator().next();
			if (items != null) {
				size -= sizeOf(items.getValue());
				bitmapCache.remove(items.getKey());
			} else {
				Log.e(ImageLoader.class.getSimpleName(), "This Image's Size is Larger Than Cache Collection.");
				break;
			}
		}
	}

	private int sizeOf(Bitmap bitmap) {
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	private Bitmap getBitmapFromNetwork(String code) {
		try {
			Bitmap bitmap = NetworkHelper.get().getBitmapFromNetwork(30000, code);
			if (bitmap == null) {
				throw new IOException("Cannot GET Bitmap From Network.");
			}

			String imgDirPath = SDCARD_PATH + File.separator + IMAGE_PATH;
			File imgDir = new File(imgDirPath);
			if (!imgDir.exists() || !imgDir.isDirectory()) {
				if (!imgDir.mkdirs()) {
					Log.e(ImageLoader.class.getSimpleName(), "Cannot Save Image Cause By Creating File Failed.");
					return null;
				}
			}

			File imgFile = new File(imgDirPath + File.separator + code + IMG_SUFFIX);
			if (!imgFile.exists() || !imgFile.isFile()) {
				boolean createFile = false;
				try {
					createFile = imgFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				if (!createFile) {
					Log.e(ImageLoader.class.getSimpleName(), "Cannot Save Image Cause By Creating File Failed.");
					return null;
				}
			}

			FileOutputStream outputStream = new FileOutputStream(imgFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			putToBitmapCache(code, bitmap);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (TimeoutException e) {
			e.printStackTrace();
			return null;
		}
	}

	private final class NetworkImageLoaderTask extends AsyncTask<Object, Object, Bitmap> {
		private final List<ImageView> viewList = new ArrayList<>();
		private String code;

		public NetworkImageLoaderTask(String code) {
			this.code = code;
		}

		public void addView(ImageView view) {
			synchronized (ImageLoader.this) {
				viewList.add(view);
			}
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			return getBitmapFromNetwork(code);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			synchronized (ImageLoader.this) {
				if (bitmap != null) {
					for (ImageView view : viewList) {
						if (view == null) {
							continue;
						}

						view.setImageBitmap(bitmap);
					}
				} else {
					Log.e(ImageLoader.class.getSimpleName(), "Cannot Get Img From Network.");
				}

				taskList.remove(code);
			}
		}
	}
}
