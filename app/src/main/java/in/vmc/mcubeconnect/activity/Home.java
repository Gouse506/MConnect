package in.vmc.mcubeconnect.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vmc.mcubeconnect.R;
import in.vmc.mcubeconnect.adapter.VisitAdapter;
import in.vmc.mcubeconnect.callbacks.Popupcallback;
import in.vmc.mcubeconnect.fragment.FragmentAll;
import in.vmc.mcubeconnect.fragment.FragmentLike;
import in.vmc.mcubeconnect.fragment.FragmentOffer;
import in.vmc.mcubeconnect.fragment.FragmentVisit;
import in.vmc.mcubeconnect.model.Model;
import in.vmc.mcubeconnect.model.OptionsData;
import in.vmc.mcubeconnect.model.VisitData;
import in.vmc.mcubeconnect.parser.Parser;
import in.vmc.mcubeconnect.parser.Requestor;
import in.vmc.mcubeconnect.utils.ConnectivityReceiver;
import in.vmc.mcubeconnect.utils.FirstVisitDailog;
import in.vmc.mcubeconnect.utils.JSONParser;
import in.vmc.mcubeconnect.utils.ReferDialogFragment;
import in.vmc.mcubeconnect.utils.SingleTon;
import in.vmc.mcubeconnect.utils.TAG;
import in.vmc.mcubeconnect.utils.Utils;

public class Home extends AppCompatActivity implements TAG, YouTubePlayer.OnInitializedListener, ConnectivityReceiver.ConnectivityReceiverListener,
        Popupcallback, View.OnClickListener, ReferDialogFragment.ReferDialogListener, NavigationView.OnNavigationItemSelectedListener,
        RangeNotifier, BeaconConsumer, FirstVisitDailog.FirstVisitListener, VisitAdapter.ViewClickedListner {
    public static final String API_KEY = "AIzaSyBH6NLG3jlZoAvQ6znsqJk_XyNqJBLGBbc";
    // We can be in one of these 3 states
    public static final int NONE = 0;
    private static final String FIRST_TIME = "first_item";
    public static int currentPosition = 0;
    public String VIDEO_ID = "TZf4WquRGJU";
    public String username, email, authkey, profileImage;
    public Model mModel, mFirstVisit;
    public int widthPixels, heightPixels;
    public float widthDp, heightDp;
    /* @InjectView(R.id.circleView)*/
    TextView tvname, tvemail;
    boolean First = false;
    CircleImageView image;
    String[] products = {"Call", "Images", "Share", "Delete"};
    private LinearLayout sensor;
    private Toolbar toolbar;
    private ArrayList<Beacon> mbeacons = new ArrayList<Beacon>();
    private ArrayList<String> mvisitedBeacon = new ArrayList<String>();
    private int PICK_IMAGE_REQUEST = 1;
    private BeaconManager mBeaconManager;
    private LinearLayout layout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean mUserSawDrawer = false;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ImageView sensorCall, sensorLike, sensorPeople;
    private Fragment fragmnetAll;
    private ReferDialogFragment referDialogFragment;
    private FirstVisitDailog firstVisitDailog = new FirstVisitDailog();
    private Dialog dialog;
    private LinearLayout mroot;
    private boolean processing = false;
    private RequestQueue requestQueue;
    private SingleTon volleySingleton;
    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
    private ImageView sensorLogo;
    private TextView sensorName, sensorDesc;
    private boolean doubleBackToExitPressedOnce;
    private TextView sensorDesctext;
    private EditText sensorMessage;
    private LinearLayout playerView;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private YouTubePlayer videoPlayer;
    private boolean fullScreen;
    private boolean enable;
    private String siteid;
    private Button sensorsend;
    private String bid;
    private float scaleFactor;
    public static Snackbar snack;
    public static boolean SCAN = true;
    public boolean SCANN = true;
    public String CurentBeacon;

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        mroot = (LinearLayout) findViewById(R.id.rootLayout);
        snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
        scaleFactor = metrics.density;
        widthDp = widthPixels / scaleFactor;
        heightDp = heightPixels / scaleFactor;
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setItemIconTintList(null);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        playerView = (LinearLayout) findViewById(R.id.playerlayout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        View header = mDrawer.getHeaderView(0);
        tvname = (TextView) header.findViewById(R.id.name);
        tvemail = (TextView) header.findViewById(R.id.email);
        image = (CircleImageView) header.findViewById(R.id.circleView);
        image.setVisibility(View.GONE);
        username = Utils.getFromPrefs(Home.this, USERNAME, "n");
        email = Utils.getFromPrefs(Home.this, EMAIL, "n");
        authkey = Utils.getFromPrefs(Home.this, AUTHKEY, "n");
        profileImage = Utils.getFromPrefs(Home.this, IMAGE_1, "n");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            getAllPermision();
        }
        tvname.setText("Hi " + username);
        tvemail.setText(email);

        volleySingleton = SingleTon.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        //Sensor Initiliazation
        sensorLogo = (ImageView) findViewById(R.id.logo);
        sensorName = (TextView) findViewById(R.id.compnay_name);
        sensorDesc = (TextView) findViewById(R.id.company_desc);
        sensorCall = (ImageView) findViewById(R.id.sensorcall);
        sensorLike = (ImageView) findViewById(R.id.sensorlike);
        sensorPeople = (ImageView) findViewById(R.id.sensorpeople);
        sensorMessage = (EditText) findViewById(R.id.message);
        sensorsend = (Button) findViewById(R.id.send);


        sensorsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mModel != null && sensorMessage.getText().toString().length() > 0) {
                    onSubmitQuery("", "" + sensorMessage.getText().toString(), mModel.getSiteId(), mModel.getBeacinId());
                    Toast.makeText(Home.this, "Message send sucessfully", Toast.LENGTH_SHORT).show();
                    sensorMessage.setText("");
                } else {
                    Toast.makeText(Home.this, "Enter Message To Send.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        sensorDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.full_textdesc);
                dialog.setTitle("Site Description");
                sensorDesctext = (TextView) dialog.findViewById(R.id.textdesc);
                if (mModel != null) {
                    sensorDesctext.setText(mModel.getDescription());
                }
                dialog.show();
            }
        });


        if (authkey.equals("n") || username.equals("n") || email.equals("n")) {
            Intent intent = new Intent(Home.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            Home.this.startActivity(intent);

        }
        if (!profileImage.equals("n") && profileImage.length() > 30) {
            image.setImageBitmap(decodeBase64(profileImage));
            Log.d("image", "save");
        } else {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.defaultimage);
            image.setImageBitmap(bm);
        }


        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        if (Build.VERSION.SDK_INT > 19) {
            enableBluetooth();
            header.setBackgroundResource(R.drawable.side_nav_bar);
        } else {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
        }


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<large>MConnect</large>"));
        getSupportActionBar().setHomeButtonEnabled(false);      // Disable the button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Remove the left caret
        getSupportActionBar().setDisplayShowHomeEnabled(false); // Remove the icon

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        sensor = (LinearLayout) findViewById(R.id.sensor);
        sensor.setVisibility(View.GONE);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        layout = (LinearLayout) findViewById(R.id.linear);
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().
                findFragmentById(R.id.youtube_fragment);


        sensorCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mModel != null) {
                    Utils.makeAcall(mModel.getPhone(), Home.this);
                }
            }
        });
        sensorPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.ZoomOut).duration(300).playOn(v);
                YoYo.with(Techniques.ZoomIn).duration(300).playOn(v);
                if (mModel != null)
                    showReferDailogDialog(mModel.getSiteId());
            }
        });


        sensorLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLikeUnlike(sensorLike, siteid, bid);
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onNavigationItemSelected(mDrawer.getMenu().getItem(position));
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (isPopopVisible()) {
            if (toolbar.isShown()) {
                if (snack.isShown()) {
                    snack.dismiss();
                }
                getSupportActionBar().hide();
            }
        }

    }

    private void enableBluetooth() {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable Bluetooth");
            builder.setMessage("MConnect requires a bluetooth to process")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            mBluetoothAdapter.enable();
                            enable = true;

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            enable = false;

                        }
                    });
            AlertDialog alert = builder.create();
            try {
                alert.show();
            } catch (Exception e) {

            }

        } else {
            enable = false;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
        SCANN = true;
        if (!profileImage.equals("n") && profileImage.length() > 30) {
            image.setImageBitmap(decodeBase64(profileImage));
            Log.d("image", "save");
        } else {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.defaultimage);
            image.setImageBitmap(bm);
        }

        try {
            mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            mBeaconManager.setForegroundScanPeriod(10000l);
            mBeaconManager.setForegroundBetweenScanPeriod(500l);
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mBeaconManager.bind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SCANN = false;
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
            mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
            mBeaconManager.setForegroundScanPeriod(10000l);
            mBeaconManager.setForegroundBetweenScanPeriod(500l);
            mBeaconManager.updateScanPeriods();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, org.altbeacon.beacon.Region region) {

        mbeacons = new ArrayList<Beacon>(beacons);

        final ArrayList<Beacon> min = new ArrayList<Beacon>();
        for (Beacon x : beacons) {
            if (min.size() == 0 || x.getDistance() == min.get(0).getDistance())
                min.add(x);
            else if (x.getDistance() < min.get(0).getDistance()) {
                min.clear();
                min.add(x);
            }
        }

        if (!isPopopVisible() && !firstVisitDailog.isVisible() && min.size() > 0 && SCANN) {
            Log.d("CurrentBeacon", min.get(0).getId2().toString());
            CheckVisit(min.get(0));
        } else if (min.size() > 0 && SCANN) {
            if (MyApplication.getWritableDatabase().isBeaconVisited(min.get(0).getId2().toString())) {
                Log.d("TAG", "id exist" + min.get(0).getId2().toString());
            } else {
                Log.d("TAG", "id not exist" + min.get(0).getId2().toString());
                if (!snack.isShown() && !firstVisitDailog.isVisible() && isPopopVisible() && !mvisitedBeacon.contains(min.get(0).toString())) {

                    mvisitedBeacon.add(min.get(0).toString());
                    snack = Snackbar.make(mroot, "New Location Detected", Snackbar.LENGTH_LONG)
                            .setAction("Show", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    hidePopup();
                                    CheckVisit(min.get(0));

                                    Log.d("TAG", "New Location Detected");

                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snack.show();

                }
            }


        }

    }


    private boolean didUserSeeDrawer() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);

    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Utils.isLogout(Home.this);
                break;

        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(4);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmnetAll = new FragmentAll();
        adapter.addFragment(fragmnetAll, "All");
        adapter.addFragment(new FragmentLike(), "Like");
        adapter.addFragment(new FragmentOffer(), "Offer");
        adapter.addFragment(new FragmentVisit(), "Visit");
        viewPager.setAdapter(adapter);


    }

    public void onShowPopup(Model model) {
        if (model != null) {

            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
            sensorMessage.setText("");
            siteid = model.getSiteId();
            bid = model.getBid();
            mModel = model;
            if (model.getVedioUrl() != null) {
                VIDEO_ID = model.getVedioUrl();
                youTubePlayerFragment.initialize(API_KEY, this);
                if (playerView.getVisibility() == View.GONE) {
                    playerView.setVisibility(View.VISIBLE);
                }

            } else {
                if (playerView.getVisibility() == View.VISIBLE) {
                    playerView.setVisibility(View.GONE);
                }
            }
            if (model.getImages() != null) {
                showImages(model.getImages());
                if (layout.getVisibility() == View.GONE) {
                    layout.setVisibility(View.VISIBLE);
                }
            }
            if (model.isLike()) {
                sensorLike.setBackgroundResource(R.drawable.ic_liked);
            } else {
                sensorLike.setBackgroundResource(R.drawable.ic_like);
            }


            sensorName.setText(model.getName());
            sensorDesc.setText(model.getDescription());
            //  sensorDesctext.setText(model.getDescription());
            new GetImageFromUrl(model.getLogo(), sensorLogo, true).execute();


            if (viewPager.getVisibility() == View.VISIBLE) {
                viewPager.setVisibility(View.GONE);
            }
            if (sensor.getVisibility() == View.GONE) {
                YoYo.with(Techniques.SlideInUp).duration(1000).playOn(sensor);
                sensor.setVisibility(View.VISIBLE);
            }
            if (referDialogFragment != null && referDialogFragment.isVisible()) {
                referDialogFragment.dismiss();
            }
            fragmnetAll.onPause();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tabLayout.getVisibility() == View.VISIBLE) {
                        tabLayout.setVisibility(View.GONE);
                    }

                    if (toolbar.isShown()) {
                        getSupportActionBar().hide();
                    }
                }
            }, 800);


        }
    }

    public void hidePopup() {
        if (sensor.getVisibility() == View.VISIBLE) {
            YoYo.with(Techniques.SlideOutDown).duration(1000).playOn(sensor);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tabLayout.getVisibility() == View.GONE) {
                        tabLayout.setVisibility(View.VISIBLE);
                    }
                    if (!toolbar.isShown()) {
                        getSupportActionBar().show();
                    }
                }
            }, 300);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sensor.setVisibility(View.GONE);
                    if (viewPager.getVisibility() == View.GONE) {
                        viewPager.setVisibility(View.VISIBLE);
                    }

                }
            }, 1000);
            fragmnetAll.onResume();

        }
        if (snack.isShown()) {
            snack.dismiss();
        }


    }

    private void navigate(int mSelectedId) {

        if (mSelectedId == R.id.all) {
            setSelection(0);
        }
        if (mSelectedId == R.id.like) {
            setSelection(1);
        }
        if (mSelectedId == R.id.offer) {
            setSelection(2);
        }
        if (mSelectedId == R.id.visit) {
            setSelection(3);
        }
        if (mSelectedId == R.id.nav_logout) {
            MyApplication.getWritableDatabase().deleteData();
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Utils.isLogout(Home.this);
        }
        if (mSelectedId == R.id.nav_settings) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(Home.this, UpdateProfile.class));

        }
        if (mSelectedId == R.id.nav_help_feedback) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            Intent i = new Intent(Home.this, FeedBack.class);
            i.putExtra(AUTHKEY, authkey);
            startActivity(i);

        }

        invalidateOptionsMenu();
    }

    private void setSelection(int item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        tabLayout.setScrollPosition(item, 0f, true);
        viewPager.setCurrentItem(item);
        //  onNavigationItemSelected(mDrawer.getMenu().getItem(0));


    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    public Boolean isPopopVisible() {
        if (sensor.getVisibility() == View.VISIBLE) {
            return true;
        }
        if (tabLayout.getVisibility() == View.GONE) {
            return true;
        }
        return viewPager.getVisibility() == View.GONE;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean b) {
        player.setFullscreenControlFlags(0);
        player.setShowFullscreenButton(false);
        player.setPlayerStateChangeListener(playerStateChangeListener);
        player.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        videoPlayer = player;
        videoPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {

            @Override
            public void onFullscreen(boolean _isFullScreen) {
                fullScreen = _isFullScreen;

            }
        });
        if (!b) {
            player.cueVideo(VIDEO_ID);

        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Failured to Initialize!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {


        if (isPopopVisible()) {

            if (Home.snack.isShown()) {
                Home.snack.dismiss();
            } else {
                hidePopup();
            }

        } else {
            if (!toolbar.isShown()) {
                getSupportActionBar().show();
            }
            if (tabLayout.getVisibility() == View.GONE) {
                tabLayout.setVisibility(View.VISIBLE);
            } else {

                if (doubleBackToExitPressedOnce) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter.isEnabled() && enable) {
                        mBluetoothAdapter.disable();

                    }
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);

            }
        }
    }

    @Override
    public void onClick(View v) {

        if (mModel != null && ConnectivityReceiver.isConnected()) {
            Intent intent = new Intent(Home.this, DatailImgeView.class);
            intent.putExtra("mylist", mModel.getImages());

            startActivity(intent);

        } else {
            if (!snack.isShown()) {
                snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT);
                snack.show();
            }
        }
    }

    public synchronized void CheckVisit(final Beacon BeaconId) {

        if (!processing) {
            if (ConnectivityReceiver.isConnected()) {
                new CheckVisit(BeaconId).execute();
                if (snack.isShown()) {
                    snack.dismiss();
                }
            } else {
                if (!snack.isShown()) {
                    snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                            .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CheckVisit(BeaconId);

                                }
                            })
                            .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snack.show();
                }
            }
        }
    }

    @Override
    public void OnItemClick(int position, View v, VisitData visitData) {
        if (ConnectivityReceiver.isConnected()) {
            SiteDetail.siteId = visitData.getSiteid();
            SiteDetail.authkey = authkey;
            Intent intent = new Intent(Home.this, SiteDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("ID", visitData.getSiteid());
            intent.putExtra("authkey", authkey);
            this.startActivity(intent);
            this.onPause();
        } else {
            Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onItemClick(int position) {
        // imageview click handls show image method
    }

    public void showImages(ArrayList<String> images) {
        layout.removeAllViews();
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(8, 8, 8, 8);
            new GetImageFromUrl(images.get(i), imageView, false).execute();
            // imageView.setImageBitmap();
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
            imageView.setOnClickListener(this);

        }
    }

    @Override
    public void onFinishRefralInputDialog(String name, String email, String msg, String phn, String SiteId) {
        Log.d("REFER", name + " " + email + " " + msg + " " + phn + " " + SiteId);

        onReferSumbit(authkey, SiteId, name, msg, phn, email);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        navigate(item.getItemId());
        return true;
    }

    public void showReferDailogDialog(String siteid) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        referDialogFragment = new ReferDialogFragment();
        referDialogFragment.setCancelable(false);
        referDialogFragment.setDialogTitle("Refer To Your Contact");
        referDialogFragment.setSiteID(siteid);
        referDialogFragment.show(fragmentManager, "Input Dialog");
    }

    public void showFirstVisitDialog(Model model) {
        // firstVisitDailog = new FirstVisitDailog();
        FragmentManager fragmentManager = getSupportFragmentManager();
        firstVisitDailog.setCancelable(true);
        firstVisitDailog.setDialogTitle("Share Your Interest");
        firstVisitDailog.setOptions(model);
        firstVisitDailog.show(fragmentManager, "Input Dialog");
    }

    @Override
    public void onFinishFirstVisit(String message, String interest) {
        if (mFirstVisit != null) {
            onSubmitQuery(interest, message, mFirstVisit.getSiteId(), mFirstVisit.getBeacinId());
            Log.d("RADIO", message + " " + interest);
        }
    }

    public synchronized void GetLikeUnlike(final ImageView imageView, final String siteid, final String bid) {

        if (ConnectivityReceiver.isConnected()) {
            new SetLikeUnlike(imageView, siteid, bid).execute();
            if (snack.isShown()) {
                snack.dismiss();
            }
        } else {
            if (!snack.isShown()) {
                snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GetLikeUnlike(imageView, siteid, bid);

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                snack.show();
            }
        }

    }

    public void onSubmitQuery(final String interest, final String query, final String siteid, final String beaconId) {

        if (ConnectivityReceiver.isConnected()) {
            new SubmitQuery(authkey, interest, query, siteid, beaconId).execute();
            if (snack.isShown()) {
                snack.dismiss();
            }
        } else {
            if (!snack.isShown()) {
                snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onSubmitQuery(interest, query, siteid, beaconId);

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                snack.show();
            }
        }

    }

    public void onReferSumbit(final String authkey, final String siteid, final String name, final String messagee, final String phone, final String email) {

        if (ConnectivityReceiver.isConnected()) {
            new ReferQuery(authkey, siteid, name, messagee, phone, email).execute();
            if (snack.isShown()) {
                snack.dismiss();
            }
        } else {
            if (!snack.isShown()) {
                snack = Snackbar.make(mroot, "No Internet Connection", Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.text_tryAgain), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onReferSumbit(authkey, siteid, name, messagee, phone, email);

                            }
                        })
                        .setActionTextColor(ContextCompat.getColor(Home.this, R.color.accent));
                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);
                snack.show();
            }
        }

    }

    public void ResetVisitedBeaconList() {
        mvisitedBeacon = new ArrayList<String>();

    }

    class CheckVisit extends AsyncTask<Void, Void, Model> {
        String message = "n";
        String code = "n";
        String name = "n", logo, desc;
        JSONObject response = null;
        Model model = null;

        Beacon BeaconId;

        private JSONObject mediaArray = null;
        private JSONObject data;

        public CheckVisit(Beacon BeaconId) {
            this.BeaconId = BeaconId;

        }

        @Override
        protected void onPreExecute() {
            processing = true;

            super.onPreExecute();
        }


        @Override
        protected Model doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = Requestor.requestCheckVisitJASON(requestQueue, CHECK_VISIT, authkey, BeaconId.getId2().toString());
                Log.d("RESPONSE", " "+response);
               model = Parser.ParseData(response);
                //response = JSONParser.CheckVisitJASON(CHECK_VISIT, authkey, BeaconId.getId2().toString());
//
            }
                catch(Exception e){
                    Log.d("error", BeaconId.getId2().toString() + e.getMessage());
                }
                return model;
            }

            @Override
            protected void onPostExecute (Model data){
                processing = false;
                //    Toast.makeText(Home.this, "Current Beacon :" + BeaconId.getId2(), Toast.LENGTH_SHORT).show();
                if (data != null) {
                    Log.d("LOG", "Beacon :" + BeaconId + " " + data.toString());


                    if (data.getCode().equals("202")) {

                        if (!isPopopVisible()) {
                            try {
                                mFirstVisit = data;
                                showFirstVisitDialog(data);
                                //  mvisitedBeacon.remove(BeaconId.getId2());
                            } catch (Exception e) {
                            }
                        }

                    } else if (data.getCode().equals("200") || data.getCode().equals("201")) {
                        //   Toast.makeText(Home.this, "Detail sent :" + BeaconId.getId2(), Toast.LENGTH_SHORT).show();
                        Log.d("RESPONSE", "msg");
                    } else if (data.getCode().equals("400")) {
                        // Toast.makeText(Home.this, "Current Processed Beacon :" + BeaconId.getId2(), Toast.LENGTH_SHORT).show();
                        MyApplication.getWritableDatabase().insertBeacon(BeaconId.getId2().toString(), data.getSiteId());
                        CurentBeacon = BeaconId.getId2().toString();

                        // mvisitedBeacon = new ArrayList(new HashSet(mvisitedBeacon));
                        if (!isPopopVisible()) {
                            onShowPopup(data);
                        }
//                    else {
//                        hidePopup();
//                        onShowPopup(data);
//                    }
                    }

                }

            }


        }

        class GetImageFromUrl extends AsyncTask<Void, Void, Bitmap> {
            String url;
            Bitmap bitmap;
            ImageView imageView;
            boolean logo;

            public GetImageFromUrl(String url, ImageView imageView, boolean logo) {
                this.url = url;
                this.imageView = imageView;
                this.logo = logo;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected Bitmap doInBackground(Void... params) {
                // TODO Auto-generated method stub

                try {
                    bitmap = JSONParser.getBitmapFromURL(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap data) {

                if (data != null) {
                    if (logo) {
                        imageView.setImageBitmap(data);
                    } else {
                        int WIDTH = data.getWidth();
                        int HEIGHT = data.getHeight();
                        if (WIDTH > HEIGHT) {
                            //Landsscape
                            Bitmap resize = Bitmap.createScaledBitmap(data, 600, 300, false);
                       /* if (mModel.getVedioUrl().length() > 3) {
                            resize = Bitmap.createScaledBitmap(data, 600, 300, false);
                        } else {
                            resize = Bitmap.createScaledBitmap(data, 800, 400, false);
                        }*/
                            imageView.setImageBitmap(resize);

                        } else if (WIDTH < HEIGHT) {
                            //portrait
                            Bitmap resize = Bitmap.createScaledBitmap(data, 100, 200, false);
                            imageView.setImageBitmap(resize);
                        } else {
                            Bitmap resize = Bitmap.createScaledBitmap(data, 400, 400, false);
                            imageView.setImageBitmap(resize);
                        }

                    }
                }
            }

        }

        public class DistanceCompare implements Comparator<Beacon> {

            @Override
            public int compare(Beacon lhs, Beacon rhs) {

                Double distance = calculateDistance(lhs.getTxPower(), lhs.getRssi());
                Double distance1 = calculateDistance(rhs.getTxPower(), rhs.getRssi());
                if (distance.compareTo(distance1) < 0)
                    return -1;
                else if (distance.compareTo(distance1) > 0)
                    return 1;
                else
                    return 0;


            }


            protected double calculateDistance(int txPower, double rssi) {
                if (rssi == 0) {
                    return -1.0; // if we cannot determine distance, return -1.
                }

                double ratio = rssi * 1.0 / txPower;
                if (ratio < 1.0) {
                    return Math.pow(ratio, 10);
                } else {
                    double accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;
                    return accuracy;
                }
            }


        }

        class ViewPagerAdapter extends FragmentPagerAdapter {
            private final List<Fragment> mFragmentList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public ViewPagerAdapter(FragmentManager manager) {
                super(manager);
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            public void addFragment(Fragment fragment, String title) {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);

            }
        }

        class SetLikeUnlike extends AsyncTask<Void, Void, String> {
            String message = "n";
            String code = "n";
            JSONObject response = null;
            ImageView imageView;
            String siteid;
            String bid;

            public SetLikeUnlike(ImageView imageView, String siteid, String bid) {
                this.imageView = imageView;
                this.siteid = siteid;
                this.bid = bid;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }


            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                try {
                    response = Requestor.requestGetlikeunlike(requestQueue, LIKE, authkey, siteid, bid);
                    //response = JSONParser.Getlikeunlike(LIKE, authkey, siteid, bid);
                    Log.d("RESPONSE", response.toString());


                    if (response.has(CODE)) {
                        code = response.getString(CODE);

                    }
                    if (response.has(MESSAGE)) {
                        message = response.getString(MESSAGE);
                    }


                } catch (Exception e) {

                }
                return code;
            }

            @Override
            protected void onPostExecute(String data) {

                if (data != null) {

                    if (code.equals("400")) {
                        Log.d("Check ", "Like Clicked " + data);
                        imageView.setBackgroundResource(R.drawable.ic_liked);
                        YoYo.with(Techniques.Flash).duration(1000).playOn(imageView);
                    }
                    if (code.equals("200")) {
                        Log.d("Check ", "Like Clicked " + data);
                        imageView.setBackgroundResource(R.drawable.ic_like);
                        YoYo.with(Techniques.Flash).duration(1000).playOn(imageView);
                    }
                }


            }


        }

        class ReferQuery extends AsyncTask<Void, Void, String> {
            String message = "n";
            String code = "n";
            JSONObject response = null;
            String authkey, siteid, name, messagee, phone, email;

            public ReferQuery(String authkey, String siteid, String name, String messagee, String phone, String email) {
                this.siteid = siteid;
                this.authkey = authkey;
                this.messagee = messagee;
                this.name = name;
                this.phone = phone;
                this.email = email;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            }


            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                try {
                    response = Requestor.requestReferQuery(requestQueue, GET_REFER, authkey, siteid, name, messagee, phone, email);
                   //  response = JSONParser.ReferQuery(GET_REFER, authkey, siteid, name, messagee, phone, email);
                    Log.d("REFER", response+"");

                    if (response.has(CODE)) {
                        code = response.getString(CODE);

                    }
                    if (response.has(MESSAGE)) {
                        message = response.getString(MESSAGE);
                    }


                } catch (Exception e) {
                    Log.d("REFER", e.getMessage());
                }
                return code;
            }

            @Override
            protected void onPostExecute(String data) {

                if (data != null) {

                    if (code.equals("400")) {
                        Log.d("REFER", messagee);
                        Toast.makeText(getApplicationContext(), "You reffered successfully.", Toast.LENGTH_SHORT).show();
                    }
                }


            }


        }

        class SubmitQuery extends AsyncTask<Void, Void, String> {
            String message = "n";
            String code = "n";
            JSONObject response = null;
            String interest, query, authkey;
            ImageView imageView;
            String siteid;
            String beaconId;

            public SubmitQuery(String authkey, String interest, String query, String siteid, String beaconId) {
                this.siteid = siteid;
                this.query = query;
                this.authkey = authkey;
                this.interest = interest;
                this.beaconId = beaconId;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }


            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub

                try {
                    response = Requestor.requestSubmitQuery(requestQueue, SEND_QUERY, authkey, interest, query, siteid, beaconId);
                    // response = JSONParser.SubmitQuery(SEND_QUERY, authkey, interest, query, siteid, beaconId);
                    Log.d("QUERY", response.toString());

                    if (response.has(CODE)) {
                        code = response.getString(CODE);

                    }
                    if (response.has(MESSAGE)) {
                        message = response.getString(MESSAGE);
                    }


                } catch (Exception e) {

                }
                return code;
            }

            @Override
            protected void onPostExecute(String data) {

                if (data != null) {


                }


            }


        }

        final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

        @TargetApi(Build.VERSION_CODES.M)
        private void getAllPermision() {

            requestPermissions(new String[]{
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }


        @Override
        public void onNetworkConnectionChanged(boolean isConnected) {
            showSnack(isConnected);
        }


        private void showSnack(boolean isConnected) {
            String message;
            int color;
            if (!isConnected) {
                message = "Sorry! Not connected to internet";
                color = Color.RED;

                Snackbar snackbar = Snackbar
                        .make(mroot, message, Snackbar.LENGTH_LONG);

                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();
            }
        }
    }
