package com.example.quang.studenthousing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quang.studenthousing.adapter.GridViewImageAdapter;
import com.example.quang.studenthousing.object.City;
import com.example.quang.studenthousing.object.District;
import com.example.quang.studenthousing.object.House;
import com.example.quang.studenthousing.object.Ward;
import com.example.quang.studenthousing.services.APIClient;
import com.example.quang.studenthousing.services.DataClient;
import com.example.quang.studenthousing.utils.DatabaseUtils;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtTitle;
    private Spinner spinnerCity;
    private Spinner spinnerDistrict;
    private Spinner spinnerWard;
    private EditText edtStreet;
    private RadioGroup groupObject;
    private RadioButton radObjectMale;
    private RadioButton radObjectFemale;
    private RadioButton radObjectBoth;
    private EditText edtDesc;
    private EditText edtPhone;
    private SeekBar seekBarAcreage;
    private TextView tvAcreage;
    private SeekBar seekBarPrice;
    private TextView tvPrice;
    private EditText edtMaxPeo;
    private GridView gvImage;
    private Button btnBrowserImage;
    private Button btnCreate;
    private Button btnBack;
    private ArrayList<String> arrImage;
    private GridViewImageAdapter imageAdapter;

    private int idUser;
    private boolean checkClickCreate;

    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        findID();
        getData();
        initViews();
        loadSpinner();

    }




    private void findID() {
        edtTitle = findViewById(R.id.edtTitleAddPost);
        spinnerCity = findViewById(R.id.spinnerCityAddPost);
        spinnerDistrict = findViewById(R.id.spinnerDistrictAddPost);
        spinnerWard = findViewById(R.id.spinnerWardAddPost);
        edtStreet = findViewById(R.id.edtStreetAddPost);
        groupObject = findViewById(R.id.groupObjectAddPost);
        radObjectMale = findViewById(R.id.radMaleAddPost);
        radObjectFemale = findViewById(R.id.radFemaleAddPost);
        radObjectBoth = findViewById(R.id.radBothAddPost);
        edtDesc = findViewById(R.id.edtDescAddPost);
        edtPhone = findViewById(R.id.edtPhoneAddPost);
        seekBarAcreage = findViewById(R.id.seekBarAcreageAddPost);
        tvAcreage = findViewById(R.id.tvAcreageAddPost);
        seekBarPrice = findViewById(R.id.seekBarPriceAddPost);
        tvPrice = findViewById(R.id.tvPriceAddPost);
        edtMaxPeo = findViewById(R.id.edtMaxpeoAddPost);
        gvImage = findViewById(R.id.gvImageAddPost);
        btnBrowserImage = findViewById(R.id.btnBrowserAddPost);
        btnCreate = findViewById(R.id.btnCreateAddPost);
        btnBack = findViewById(R.id.btnBackAddPost);
    }

    private void getData(){
        Intent intent = getIntent();
        idUser = intent.getIntExtra("idUser",0);
    }

    private void initViews() {
        arrImage = new ArrayList<>();
        imageAdapter = new GridViewImageAdapter(this,R.layout.item_gridview_image,arrImage);
        gvImage.setAdapter(imageAdapter);

        seekBarAcreage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                double r = (double)i/(double)100;
//                tvAcreage.setText((double)Math.round(r*10)/10 + " " + getString(R.string.meter2));

                tvAcreage.setText(i + " " + getString(R.string.meter2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                double r = (double)i/(double)10;
                tvPrice.setText((double)Math.round(r*10)/10 + " " + getString(R.string.million));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        checkClickCreate = true;

        btnBrowserImage.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
    }


    private void loadSpinner(){
        DatabaseUtils databaseUtils = new DatabaseUtils(this);
        final ArrayList<City> arrCity = databaseUtils.getCity();
        final ArrayList<District> arrDistrict = databaseUtils.getDistrict();
        final ArrayList<Ward> arrWard = databaseUtils.getWard();

        ArrayList<String> arrCityString = new ArrayList<>();
        ArrayList<String> arrDistrictString = new ArrayList<>();
        ArrayList<String> arrWardString = new ArrayList<>();

        for (int i=0; i<arrCity.size();i++){
            arrCityString.add(arrCity.get(i).getName());
        }

        // spinner city
        ArrayAdapter<String> adapterCity=new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, arrCityString);
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapterCity.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinnerCity.setAdapter(adapterCity);


        // spinner district
        final ArrayAdapter<String> adapterDistrict = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, arrDistrictString);
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapterDistrict.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinnerDistrict.setAdapter(adapterDistrict);


        // spinner ward
        final ArrayAdapter<String> adapterWard = new ArrayAdapter<String>(this
                , android.R.layout.simple_spinner_item, arrWardString);
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapterWard.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinnerWard.setAdapter(adapterWard);

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = arrCityString.get(i);
                arrDistrictString.clear();
                for (int index=0 ;index<arrDistrict.size() ;index++){
                    if (arrDistrict.get(index).getMaCity().equalsIgnoreCase(arrCity.get(i).getMa())){
                        arrDistrictString.add(arrDistrict.get(index).getName());
                    }
                }
                adapterDistrict.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                arrWardString.clear();
                String maDistrict = null;
                for (District d : arrDistrict){
                    if (d.getName().equalsIgnoreCase(arrDistrictString.get(i))){
                        maDistrict = d.getMa();
                        break;
                    }
                }
                for (int index=0 ;index<arrWard.size() ;index++){
                    if (arrWard.get(index).getMaDistrict().equalsIgnoreCase(maDistrict)){
                        arrWardString.add(arrWard.get(index).getName());
                    }
                }
                adapterWard.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBrowserAddPost:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 123);
                break;
            case R.id.btnCreateAddPost:

                if (!checkClickCreate){
                    return;
                }else {
                    checkClickCreate = false;
                }

                String title = edtTitle.getText().toString();
                String street = edtStreet.getText().toString();
                String desc = edtDesc.getText().toString();
                String phone = edtPhone.getText().toString();


                String city = (String) spinnerCity.getSelectedItem();
                String distric = (String) spinnerDistrict.getSelectedItem();
                String ward = (String) spinnerWard.getSelectedItem();

                if (title.isEmpty() || street.isEmpty() || desc.isEmpty()
                        || phone.isEmpty() || edtMaxPeo.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(edtDesc, R.string.insert_info, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    checkClickCreate = true;
                    return;
                }

                if (arrImage.size() == 0){
                    Snackbar snackbar = Snackbar
                            .make(edtDesc, R.string.insert_image, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    checkClickCreate = true;
                    return;
                }

                String address = street + ", " + ward + ", " + distric + ", " + city;

                GetLatLng getLatLng = new GetLatLng();
                getLatLng.execute(address);

                break;
            case R.id.btnBackAddPost:
                finish();
                break;
        }
    }

    private void insert(){
        String title = edtTitle.getText().toString();
        String street = edtStreet.getText().toString();
        String desc = edtDesc.getText().toString();
        String phone = edtPhone.getText().toString();

        String city = (String) spinnerCity.getSelectedItem();
        String distric = (String) spinnerDistrict.getSelectedItem();
        String ward = (String) spinnerWard.getSelectedItem();

        int maxPeo = Integer.parseInt(edtMaxPeo.getText().toString());

        String address = street + ", " + ward + ", " + distric + ", " + city;

        int object = 1;
        if (radObjectMale.isChecked()){
            object = 1;
        }else if (radObjectFemale.isChecked()) {
            object = 2;
        }else if (radObjectBoth.isChecked()){
            object = 3;
        }

        String[] arrAcreage = tvAcreage.getText().toString().split(" ");
        float acreage = Float.parseFloat(arrAcreage[0]);
        String[] arrPrice = tvPrice.getText().toString().split(" ");
        float price = Float.parseFloat(arrPrice[0]);


        DataClient dataClient = APIClient.getData();
        Call<List<House>> callBack = dataClient.createPost(title,address,object
                , arrImage.get(0), desc, phone, acreage, price, maxPeo
                , Calendar.getInstance().getTime().toString(), idUser, currentLocation.toString());
        callBack.enqueue(new Callback<List<House>>() {
            @Override
            public void onResponse(Call<List<House>> call, Response<List<House>> response) {
                ArrayList<House> arrHouse = (ArrayList<House>) response.body();
                if (arrHouse.size() > 0){
                    for (int i=0; i<arrImage.size(); i++){
                        DataClient dataClient = APIClient.getData();
                        Call<String> callback = dataClient.insertImageForHouse(arrImage.get(i),arrHouse.get(0).getIDHOUSE());
                        int finalI = i;
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body().equals("success")){
                                    if (finalI == arrImage.size() - 1){
                                        Snackbar snackbar = Snackbar
                                                .make(edtDesc, R.string.verify_create_post, Snackbar.LENGTH_LONG);
                                        snackbar.show();

                                        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
                                            @Override
                                            public void onTick(long l) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                finish();
                                            }
                                        }.start();
                                    }
                                }else {
                                    Toast.makeText(CreatePostActivity.this, "fail", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(CreatePostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else {
                    Snackbar snackbar = Snackbar
                            .make(edtDesc, R.string.fail, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<List<House>> call, Throwable t) {
                Toast.makeText(CreatePostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class GetLatLng extends AsyncTask<String, Void, LatLng> {

        @Override
        protected LatLng doInBackground(String... strings) {
            return getLatLng(strings[0]);
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            currentLocation = latLng;

            insert();
        }
    }

    //Hamf chuyen name sang latlng
    protected LatLng getLatLng(String address) {
        String key = Uri.encode(address);
        String uri = "http://maps.google.com/maps/api/geocode/json?address="
                + key + "&sensor=false";

        HttpGet httpGet = new HttpGet(uri);

        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();

            InputStream stream = entity.getContent();

            int byteData;
            while ((byteData = stream.read()) != -1) {
                stringBuilder.append((char) byteData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        double lat = 0.0, lng = 0.0;

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");
            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng latLngAddress = new LatLng(lat,lng);
        return latLngAddress;
    }

    ArrayList<String> arrBase = new ArrayList<>();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            String realPath = getRealPathFromURI(uri);

            File file = new File(realPath);
            String file_path = file.getAbsolutePath();
            String[] arrFileName = file_path.split("/");
            String imageName = System.currentTimeMillis() + arrFileName[arrFileName.length - 1];
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                arrBase.add(getStringFromBitMap(bitmap));

                DataClient dataClient = APIClient.getData();
                Call<String> callback = dataClient.uploadImage(getStringFromBitMap(bitmap),imageName);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("success")){

                        }else {
                            Toast.makeText(CreatePostActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //Toast.makeText(CreatePostActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            arrImage.add("images/" + imageName);
            imageAdapter.notifyDataSetChanged();


        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    // Encode bitmap to String
    public String getStringFromBitMap(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
