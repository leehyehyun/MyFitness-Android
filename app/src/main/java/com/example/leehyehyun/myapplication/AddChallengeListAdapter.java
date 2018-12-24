package com.example.leehyehyun.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddChallengeListAdapter extends BaseAdapter {
    private static final int RESULT_OK = -1;
    private static final int SELECT_PICTURE = 123;
    private static final int REQ_SAVE = 124;

    private ArrayList<WorkOut> listViewItemList = new ArrayList<WorkOut>() ;

    private String selectedImagePath = "";
    private Activity activity;

    public AddChallengeListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_add_challenge, parent, false);
        }

        TextView tv_workout = convertView.findViewById(R.id.tv_workout);
        ImageView img_remove = convertView.findViewById(R.id.img_remove);

        WorkOut workoutItem = listViewItemList.get(position);

        String workoutName = workoutItem.getName();
        String imagePath = workoutItem.getImagePath();

        tv_workout.setText(workoutName);

        img_remove.setTag(position);
        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                removeItem(position);
            }
        });

        return convertView;
    }

    private void showGallery(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, SELECT_PICTURE);
    }

    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);

            if (cursor == null || cursor.getCount() < 1) {
                return;
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) {// no column index
                return; // DO YOUR ERROR HANDLING
            }

            selectedImagePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor

            try{
                Bitmap bitmap = decodeUri(context, uri);
                Log.v("is-","uri : "+uri.toString()+" *** selectedImagePath : "+selectedImagePath+" *** bitmap : "+bitmap);
                savePhoto_in_mydevice(context, bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private void savePhoto_in_mydevice(Context context, Bitmap bitmap) throws IOException {
        //특정경로에 이미지를 저장한다.

        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myFitness";
        File dirFile = new File(path);
        if(!dirFile.exists()) dirFile.mkdirs();

        String path2 = path +"/mf_"+ System.currentTimeMillis() + ".jpg";

        if (bitmap == null) {
            Log.v("is-","insertPhoto fail");
            return;
        }
        File imageFile = new File(path2);
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        outputStream.flush();
        outputStream.close();

        //갤러리 갱신(스캔)
        DMediaScanner scanner = new DMediaScanner(context);
        scanner.startScan(path2);

    }

    public void checkSavePermission() {
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_SAVE);
        } else {
            // 권한있음
            showGallery();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_SAVE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //권한있음
                showGallery();
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity.getApplicationContext(), "사진 접근 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    //권한재시도
                }
            } else {
                Toast.makeText(activity.getApplicationContext(), "사진 접근 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public WorkOut getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public ArrayList<WorkOut> getWorkoutList() {
        return listViewItemList;
    }

    public void addItem(WorkOut mWorkOut) {
        listViewItemList.add(mWorkOut);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        listViewItemList.remove(position);
        notifyDataSetChanged();
    }

}
