package in.mesway.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import in.mesway.Adapters.LocationListAdapter;
import in.mesway.Models.LocationModels;
import in.mesway.R;
import in.mesway.Rusable.Constant;
import in.mesway.Rusable.EncryptedSharedPreferencesInstance;
import in.mesway.Rusable.Reusable;
import in.mesway.ViewModels.ImportantViewmodel;
import in.mesway.activity.App;
import in.mesway.activity.MainActivity;
import in.mesway.client.ApiClient;
import in.mesway.client.ApiInterface;
import in.mesway.databinding.FragmentLocationSearchBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LocationSearchFragment extends Fragment {

    private FragmentLocationSearchBinding locationSearchBinding;
    private NavController navController;
    private ArrayList<LocationModels> locationModels;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private SharedPreferences sharedPreferences;
    private ApiInterface apiInterface;
    private AlertDialog alertDialog;
    private String longitude = null, latitude = null;
    private Activity activity;
    private Snackbar snackbar;
    private ImportantViewmodel importantViewmodel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        locationSearchBinding = FragmentLocationSearchBinding.inflate(inflater, container, false);
        return locationSearchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.mainBinding.menuBottom.setVisibility(View.GONE);


        initView();
        getFocusOnLocationEt();
        queryLocationList(view);
        listViewClickHandle();
        backImgClick();

        clickGetCurrentLocation();


    }

    private void initView() {
        importantViewmodel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ImportantViewmodel.class);
        Retrofit retrofit = ApiClient.getClient();
        apiInterface = retrofit.create(ApiInterface.class);
        alertDialog = Reusable.alertDialog(activity);
        sharedPreferences = EncryptedSharedPreferencesInstance.getSharedPreferences(Constant.MY_GLOBAL_PREFERENCES, activity);
    }


    private void startLocationListener() {
        locationListener = new LocationListener() {


            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
                try {
                    onGPS();
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Plz turn on location", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());


                callGeoCoder(latitude, longitude);
                importantViewmodel.setLocation_update(true);


            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //getting error
                //LocationListener.super.onStatusChanged(provider, status, extras);
            }


        };


    }

    private void callGeoCoder(String latitude, String longitude) {
        alertDialog.show();
        Call<String> getAddress = apiInterface.get_address_from_coordinate(App.getAPIKey(), latitude, longitude);
        getAddress.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.homeFragment, true)
                                .build();
                        Bundle bundle = new Bundle();
                        bundle.putString("main_location", "location");
                        bundle.putString("secondary_location", response.body());

                        sharedPreferences.edit()
                                .putString(Constant.LATITUDE, latitude)
                                .putString(Constant.LONGITUDE, longitude)
                                .apply();
                        alertDialog.dismiss();
                        try {
                            navController.navigate(R.id.action_locationSearchFragment_to_homeFragment, bundle, navOptions);
                        } catch (Exception ignored) {

                        }


                    } else {

                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.homeFragment, true)
                                .build();
                        Bundle bundle = new Bundle();
                        bundle.putString("main_location", "location");
                        bundle.putString("secondary_location", latitude + ", " + longitude);

                        sharedPreferences.edit()
                                .putString(Constant.LATITUDE, latitude)
                                .putString(Constant.LONGITUDE, longitude)
                                .apply();
                        alertDialog.dismiss();

                        try {
                            navController.navigate(R.id.action_locationSearchFragment_to_homeFragment, bundle, navOptions);
                        } catch (Exception ignored) {

                        }
                    }

                } else {

                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.homeFragment, true)
                            .build();
                    Bundle bundle = new Bundle();
                    bundle.putString("main_location", "location");
                    bundle.putString("secondary_location", latitude + ", " + longitude);

                    sharedPreferences.edit()
                            .putString(Constant.LATITUDE, latitude)
                            .putString(Constant.LONGITUDE, longitude)
                            .apply();
                    alertDialog.dismiss();


                    try {
                        navController.navigate(R.id.action_locationSearchFragment_to_homeFragment, bundle, navOptions);
                    } catch (Exception ignored) {

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build();
                Bundle bundle = new Bundle();
                bundle.putString("main_location", "location");
                bundle.putString("secondary_location", latitude + ", " + longitude);

                sharedPreferences.edit()
                        .putString(Constant.LATITUDE, latitude)
                        .putString(Constant.LONGITUDE, longitude)
                        .apply();
                alertDialog.dismiss();


                try {
                    navController.navigate(R.id.action_locationSearchFragment_to_homeFragment, bundle, navOptions);
                } catch (Exception ignored) {

                }

            }
        });

    }

    private void onGPS() throws Settings.SettingNotFoundException {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        if (getLocationMode(activity) == 2) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                    .setTitle("Plz Enable precise location")
                    .setMessage("Mesway uses precise location to serve you better.")
                    .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;

        }
        builder.setMessage("Plz enable your location")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        android.app.AlertDialog locationOnDialog = builder.create();
        locationOnDialog.show();

    }

    private void clickGetCurrentLocation() {
        locationSearchBinding.lnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                startLocationListener();
                try {
                    requestLocationPermission();
                } catch (Settings.SettingNotFoundException e) {
                    Toast.makeText(activity, "Turn on Your location", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }


            }
        });

    }

    private void requestLocationPermission() throws Settings.SettingNotFoundException {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                Criteria locationCriteria = new Criteria();
                locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    onGPS();
                } else {
                    locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria,true),60000000,0,locationListener);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    if (locationManager.getLastKnownLocation(locationManager.getBestProvider(locationCriteria, true)) == null) {

                                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                            try {
                                                onGPS();
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            Toast.makeText(activity, "network", Toast.LENGTH_SHORT).show();
                                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
                                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);

                                        }
                                    }

                                }
                            }

                        }
                    },2000);
                   /* locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
             */   }

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Plz Enable precise location")
                            .setMessage("Mesway uses precise location to serves you better.")
                            .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                    settingsIntent.setData(uri);
                                    startActivity(settingsIntent);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setMessage("It looks like you have turned off permission required for this features. It can be enabled under Phone Settings> App> Mesway > Permission")
                            .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                    settingsIntent.setData(uri);
                                    startActivity(settingsIntent);
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            } else {
                requestFinePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                    .setMessage("It looks like you have turned off permission required for this features. It can be enabled under Phone Settings> App> Mesway > Permission")
                    .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            settingsIntent.setData(uri);
                            startActivity(settingsIntent);
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }

    }

    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String> requestFinePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                    Criteria locationCriteria = new Criteria();
                    locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                    locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        try {
                            onGPS();
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(activity, "Plz, Turn on location", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria,true),60000000,0,locationListener);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        if (locationManager.getLastKnownLocation(locationManager.getBestProvider(locationCriteria, true)) == null) {

                                            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                                try {
                                                    onGPS();
                                                } catch (Settings.SettingNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {

                                                Toast.makeText(activity, "network", Toast.LENGTH_SHORT).show();

                                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
                                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);

                                            }
                                        }

                                    }
                                }

                            }
                        },2000);
                       /* locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
*/

                    }
                } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Plz Enable precise location")
                            .setMessage("Mesway uses precise location to serve you better.")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        requestLocationPermission();
                                    } catch (Settings.SettingNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Location Permission Denied")
                            .setMessage("Mesway uses this permission to detect your current location and show you great tifin provider around you. Are you sure you want to deny this permission?")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        requestLocationPermission();
                                    } catch (Settings.SettingNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });


    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {

                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                        Criteria locationCriteria = new Criteria();
                        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
                        locationCriteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            try {
                                onGPS();
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(activity, "Plz turn on location", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            locationManager.requestLocationUpdates(locationManager.getBestProvider(locationCriteria,true),60000000,0,locationListener);

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                            if (locationManager.getLastKnownLocation(locationManager.getBestProvider(locationCriteria, true)) == null) {

                                                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                                    try {
                                                        onGPS();
                                                    } catch (Settings.SettingNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    Toast.makeText(activity, "network", Toast.LENGTH_SHORT).show();

                                                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
                                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);

                                                }
                                            }

                                        }
                                    }

                                }
                            },2000);
                           /* locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000000, 0, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000000, 0, locationListener);
                   */     }

                    } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                                    .setTitle("Plz Enable precise location")
                                    .setMessage("Mesway uses precise location to serve you better.")
                                    .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            try {
                                                requestLocationPermission();
                                            } catch (Settings.SettingNotFoundException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();


                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                                    .setMessage("It looks like you have turned off permission required for this features. It can be enabled under Phone Settings> App> Mesway > Permission")
                                    .setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                            settingsIntent.setData(uri);
                                            startActivity(settingsIntent);
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }

                    } else {
                        requesetFineLocation();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle("Location Permission Denied")
                            .setMessage("Mesway uses this permission to detect your current location and show you great tifin provider around you. Are you sure you want to deny this permission?")
                            .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        requestLocationPermission();
                                    } catch (Settings.SettingNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });

    private void requesetFineLocation() {
        requestFinePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

    }

    private void getFocusOnLocationEt() {
        locationSearchBinding.etLocationQuery.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(locationSearchBinding.etLocationQuery, InputMethodManager.SHOW_IMPLICIT);

    }

    private void backImgClick() {
        locationSearchBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    navController.popBackStack();
                } catch (Exception ignored) {
                }

            }
        });

    }

    private void listViewClickHandle() {
        locationSearchBinding.ltvLocationSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build();
                Bundle bundle = new Bundle();
                bundle.putString("main_location", locationModels.get(i).getMain_location());
                bundle.putString("secondary_location", locationModels.get(i).getSecondary_location());
                try {

                    navController.navigate(R.id.action_locationSearchFragment_to_homeFragment, bundle, navOptions);
                } catch (Exception ignored) {
                }

            }
        });
    }

    private void queryLocationList(View view) {
        locationModels = new ArrayList<>();


        LocationListAdapter locationListAdapter = new LocationListAdapter(activity, locationModels);
        locationSearchBinding.ltvLocationSearch.setAdapter(locationListAdapter);
        locationListAdapter.notifyDataSetChanged();


        locationSearchBinding.etLocationQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                snackbar = Snackbar.make(view, "We will working on it, Kindly use your current location", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideKeyboard() {
        View views = activity.getCurrentFocus();
        if (views != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(views.getWindowToken(), 0);

        }

    }


    //for fragment not attach to error just replace all context(activity, requireContext()) type with activity

    private int getLocationMode(Context context) throws Settings.SettingNotFoundException {
        return Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = context instanceof Activity ? (Activity) context : null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager!=null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}