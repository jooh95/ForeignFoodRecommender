package com.example.joonoh.forfoodreco;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import io.paperdb.Paper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    ArrayList<Double> lat, lng;
    ArrayList<String> name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader rd = new BufferedReader(new InputStreamReader(is), 4096);
        String line;
        StringBuilder sb =  new StringBuilder();
        try {
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String contentOfMyInputStream = sb.toString();
        return contentOfMyInputStream;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        GpsInfo gps = new GpsInfo(MapsActivity.this);

//        if (gps.isGetLocation()) {

            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();


            LatLng currentPos = new LatLng(latitude , longitude );

            MarkerOptions currentOption = new MarkerOptions().position(currentPos);

            currentOption.title("Current Position");
            currentOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(currentOption);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPos ));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));



        Intent intent = getIntent();
        final String food_name = intent.getStringExtra("food_name");

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {

                    String urlStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword="+food_name+"&location="+ Double.toString(latitude)+"," + Double.toString(longitude) + "&radius=1000&type=restaurant&key=AIzaSyDeTgD4A_ZLTF7CzNAP0cN3xyudXboBNQk";

                    Log.d("url", urlStr);

                    URL url = new URL(urlStr);

                    HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();

                    if (myConnection.getResponseCode() == 200) {

                        InputStream responseBody = myConnection.getInputStream();

                        String temp = convertStreamToString(responseBody);

                        JSONObject json = new JSONObject(temp);

                        JSONArray jsonArray = json.getJSONArray("results");

                       lat = new ArrayList<Double>();
                        lng = new ArrayList<Double>();
                        name = new ArrayList<String>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json_data = jsonArray.getJSONObject(i);

                            lat.add(json_data.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                            lng.add(json_data.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                            name.add(json_data.getString("name"));

                            Log.d("lat", Double.toString(lat.get(i)));
                            Log.d("lng", Double.toString(lng.get(i)));
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try{
            mThread.join();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        for(int i = 0; i < lat.size(); i++){
            LatLng point = new LatLng(lat.get(i), lng.get(i));
            // 마커 생성
            MarkerOptions mOptions2 = new MarkerOptions();
            mOptions2.title(name.get(i));

            mOptions2.position(point);
            // 마커 추가
            mMap.addMarker(mOptions2);
        }




//
//        // 버튼 이벤트
//        button.setOnClickListener(new Button.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                String str=editText.getText().toString();
//                List<Address> addressList = null;
//                try {
//                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
//                    addressList = geocoder.getFromLocationName(
//                            str, // 주소
//                            10); // 최대 검색 결과 개수
//
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(addressList.get(0).toString());
//                // 콤마를 기준으로 split
//                String []splitStr = addressList.get(0).toString().split(",");
//                String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
//                System.out.println(address);
//
//                String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
//                String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
//                System.out.println(latitude);
//                System.out.println(longitude);
//
//                // 좌표(위도, 경도) 생성
//                LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                // 마커 생성
//                MarkerOptions mOptions2 = new MarkerOptions();
//                mOptions2.title("search result");
//                mOptions2.snippet(address);
//                mOptions2.position(point);
//                // 마커 추가
//                mMap.addMarker(mOptions2);
//                // 해당 좌표로 화면 줌
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
//            }
//        });
//        ////////////////////

        // Add a marker in Sydney and move the camera






    }
}
