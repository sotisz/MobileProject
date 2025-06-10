package com.example.mobileproject2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdate;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ImageButton btnChecklist;

    ImageButton btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 위치 클라이언트
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 버튼 클릭 시 현재 위치 요청
        btnLocation = findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(v -> checkLocationPermissionAndShow());

        btnChecklist = findViewById(R.id.btnChecklist);
        btnChecklist.setOnClickListener(v -> goToChecklistActivity());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(37.584, 126.925); // 신촌역 근처 예시
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    }
    
    // 위치 권한 요청 및 처리
    private void checkLocationPermissionAndShow() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // 이미 권한이 있을 경우
            moveToCurrentLocation();
        }
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                moveToCurrentLocation();


                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mMap != null) mMap.setMyLocationEnabled(true);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "위치 권한 설정 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "위치 권한이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 현재 위치 버튼
    private void moveToCurrentLocation() {
        // 1. 위치 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "위치 권한이 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null && mMap != null) {
                            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
                        } else {
                            Toast.makeText(this, "현재 위치를 찾을 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
            // 내 위치 마커 켜기 (지도에서 파란 점 보이게)
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "보안 예외 발생: 권한 필요", Toast.LENGTH_SHORT).show();
        }
    }
    private void goToChecklistActivity() {
        Toast.makeText(this, "ChecklistActivity로 이동합니다", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChecklistActivity.class);
        startActivity(intent);
    }



}