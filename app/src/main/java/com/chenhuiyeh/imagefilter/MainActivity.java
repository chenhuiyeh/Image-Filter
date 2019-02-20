package com.chenhuiyeh.imagefilter;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chenhuiyeh.imagefilter.Adapter.ViewPagerAdapter;
import com.chenhuiyeh.imagefilter.Interfaces.AddTextFragmentListner;
import com.chenhuiyeh.imagefilter.Interfaces.BrushFragmentListener;
import com.chenhuiyeh.imagefilter.Interfaces.EditImageFragmentListener;
import com.chenhuiyeh.imagefilter.Interfaces.FilterListFragmentListener;
import com.chenhuiyeh.imagefilter.Utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class MainActivity extends AppCompatActivity implements FilterListFragmentListener,
        EditImageFragmentListener,
        BrushFragmentListener, AddTextFragmentListner {

    public static final String PICTURE_NAME = "flash.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    CoordinatorLayout coordinator_layout;

    Bitmap originalBmp, filteredBmp, finalBmp;

    FilterListFragment mFilterListFragment;
    EditImageFragment mEditImageFragment;

    CardView btn_edit, btn_filter_list, btn_brush, btn_text;
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float contrastFinal = 1.0f;

    // loading image filters lib
    static{
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Filter");

        // Views
        photoEditorView = (PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .build();
        coordinator_layout = findViewById(R.id.coordinator);

        btn_edit = (CardView) findViewById(R.id.btn_edit);
        btn_filter_list = (CardView) findViewById(R.id.btn_filters_list);
        btn_brush = (CardView) findViewById(R.id.btn_brush);
        btn_text = (CardView) findViewById(R.id.btn_text);
        btn_filter_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterListFragment filterListFragment = FilterListFragment.getInstance();
                filterListFragment.setListener(MainActivity.this);
                filterListFragment.show(getSupportFragmentManager(), filterListFragment.getTag());
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(), editImageFragment.getTag());
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enable brush mode
                photoEditor.setBrushDrawingMode(true);
                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setBrushFragmentListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());
            }
        });

        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setListener(MainActivity.this);
                addTextFragment.show(getSupportFragmentManager(), addTextFragment.getTag());
            }
        });
        loadImage();

    }

    private void loadImage() {
        originalBmp = BitmapUtils.getBitmapFromAssets(this, PICTURE_NAME, 300, 300);
        filteredBmp = originalBmp.copy(Bitmap.Config.ARGB_8888, true);
        finalBmp = originalBmp.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(originalBmp);
    }

    private void setUpViewPager(ViewPager view_pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        mFilterListFragment = new FilterListFragment();
        mFilterListFragment.setListener(this);

        mEditImageFragment = new EditImageFragment();
        mEditImageFragment.setListener(this);

        adapter.addFragment(mFilterListFragment, "FILTERS");
        adapter.addFragment(mEditImageFragment, "EdIT");

        view_pager.setAdapter(adapter);



    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBrightnessChanged(int val) {
        brightnessFinal = val;
        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(val));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBmp.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(float val) {
        saturationFinal = val;
        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(val));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBmp.copy(Bitmap.Config.ARGB_8888, true)));

    }

    @Override
    public void onContrastChanged(float val) {
        contrastFinal = val;
        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubFilter(val));
        photoEditorView.getSource().setImageBitmap(filter.processFilter(finalBmp.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bmp = filteredBmp.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        filter.addSubFilter(new SaturationSubfilter(saturationFinal));
        filter.addSubFilter(new ContrastSubFilter(contrastFinal));

        finalBmp = filter.processFilter(bmp);
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControl();
        filteredBmp = originalBmp.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBmp));
        finalBmp = filteredBmp.copy(Bitmap.Config.ARGB_8888, true);

    }

    private void resetControl() {
        if (mEditImageFragment != null)
            mEditImageFragment.resetControls();
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        contrastFinal = 1.0f;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_open) {
            openImageGallery();
            return true;
        } else if (id == R.id.action_save) {
            saveImageToGallery();
            return true;
        }
        return false;
    }

    private void openImageGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_PICK_IMAGE);
                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
        .check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path), "image/*");
        startActivity(intent);
    }

    private void saveImageToGallery(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                           final String path = BitmapUtils.insertImage(getContentResolver(),
                                   finalBmp,
                                   System.currentTimeMillis()+"_profile.jpg",
                                   null);

                           if (!TextUtils.isEmpty(path)) {
                               Snackbar snackbar = Snackbar.make(coordinator_layout,
                                       "Image saved to gallery!",
                                       Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       openImage(path);
                                   }
                               });
                               snackbar.show();
                           } else {
                               Snackbar snackbar = Snackbar.make(coordinator_layout,
                                       "Unable to save image",
                                       Snackbar.LENGTH_LONG);
                               snackbar.show();
                           }

                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
        .check(); // remember to add check()
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if (isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onAddTextButtonClick(String text, int color) {
        photoEditor.addText(text, color);
    }
}

