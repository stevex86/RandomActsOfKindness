package activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import adapters.MainActivityPageAdapter;
import data.PreferencesLayer;
import fragments.GlobalInfo;
import fragments.Home;
import fragments.UserInfo;
import me.dstny.activities.R;

public class MainActivity extends Activity {

    private ArrayList<Fragment> fragments;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ViewPager viewPager;
    private MainActivityPageAdapter mainActivityPageAdapter;

    private UserInfo userInfo;
    private Home homeFragment;
    private GlobalInfo globalInfo;

    private Button actButton;
    private Button callButton;
    private Button postButton;
    private Button donateButton;
    private Button mailButton;

    private TextView callStats;
    private TextView socialStats;
    private TextView donateStats;
    private TextView mailStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfo = new UserInfo();
        homeFragment = new Home();
        globalInfo = new GlobalInfo();
        fragments = new ArrayList<Fragment>();
        fragments.add(userInfo);
        fragments.add(homeFragment);
        fragments.add(globalInfo);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fm = getFragmentManager();
        mainActivityPageAdapter = new MainActivityPageAdapter(fm, fragments);
        viewPager.setAdapter(mainActivityPageAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void assembleButtons(Button act, Button call, Button post, Button donate, Button mail, TextView cstats, TextView pstats, TextView dstats, TextView mstats) {

        actButton = act;
        callButton = call;
        postButton = post;
        donateButton = donate;
        mailButton = mail;

        callStats = cstats;
        socialStats = pstats;
        donateStats = dstats;
        mailStats = mstats;

        PreferencesLayer preferencesLayer = PreferencesLayer.getInstance();
        callButton.setActivated(preferencesLayer.getCallPref());
        postButton.setActivated(preferencesLayer.getPostPref());
        donateButton.setActivated(preferencesLayer.getDonatePref());
        mailButton.setActivated(preferencesLayer.getMailPref());

        if(callButton.isActivated()) {
            callStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        if(postButton.isActivated()) {
            socialStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        if(donateButton.isActivated()) {
            donateStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        if(mailButton.isActivated()) {
            mailStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
    }

    public void kindnessButtonPressed(View view) {
        if(callButton.isActivated() || postButton.isActivated() || donateButton.isActivated() || mailButton.isActivated()) {
            ArrayList<Button> activatedButtons = new ArrayList<Button>();
            if(callButton.isActivated()) {
                activatedButtons.add(callButton);
            }
            if(postButton.isActivated()) {
                activatedButtons.add(postButton);
            }
            if(donateButton.isActivated()) {
                activatedButtons.add(donateButton);
            }
            if(mailButton.isActivated()) {
                activatedButtons.add(mailButton);
            }
            Button selectedButton = activatedButtons.get(randomInt(activatedButtons.size()));

            if(selectedButton.equals(callButton)) {
                randomCall();
            }
            else if (selectedButton.equals(postButton)) {
                randomPost();
            }
            else if (selectedButton.equals(donateButton)) {
                randomDonate();
            }
            else {
                randomMail();
            }
        }
        else {
            Toast.makeText(this, "Please select a category in order to take part in Random Acts of Kindness.", Toast.LENGTH_LONG).show();
        }
    }

    public int randomInt(int n) {
        return new Random().nextInt(n);
    }

    public void randomCall() {

    }

    public void randomPost() {

    }

    public void randomDonate() {

    }

    public void randomMail() {

    }

    public void callButtonPressed(View view) {
        callButton.setActivated(!callButton.isActivated());
        PreferencesLayer.getInstance().setCallPref(callButton.isActivated());
        if(callButton.isActivated()) {
            callStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        else {
            callStats.setBackgroundResource(R.mipmap.orange_circle_filled);
        }
    }

    public void postButtonPressed(View view) {
        postButton.setActivated(!postButton.isActivated());
        PreferencesLayer.getInstance().setPostPref(postButton.isActivated());
        if(postButton.isActivated()) {
            socialStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        else {
            socialStats.setBackgroundResource(R.mipmap.orange_circle_filled);
        }
    }

    public void donateButtonPressed(View view) {
        donateButton.setActivated(!donateButton.isActivated());
        PreferencesLayer.getInstance().setDonatePref(donateButton.isActivated());
        if(donateButton.isActivated()) {
            donateStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        else {
            donateStats.setBackgroundResource(R.mipmap.orange_circle_filled);
        }
    }

    public void mailButtonPressed(View view) {
        mailButton.setActivated(!mailButton.isActivated());
        PreferencesLayer.getInstance().setMailPref(mailButton.isActivated());
        if(mailButton.isActivated()) {
            mailStats.setBackgroundResource(R.mipmap.l_blue_circle_filled);
        }
        else {
            mailStats.setBackgroundResource(R.mipmap.orange_circle_filled);
        }
    }

    public void settingsButtonPressed(View view) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
        finish();
    }

    public void statButtonPressed(View view) {

    }


    public void onBackPressed() {
        if(viewPager.getCurrentItem()==0) {
            viewPager.setCurrentItem(1);
        }
        else if(viewPager.getCurrentItem()==2) {
            viewPager.setCurrentItem(1);
        }
        else {
            Intent intent = new Intent(MainActivity.this, ExitSplashScreen.class);
            startActivity(intent);
            finish();
        }
    }
}
