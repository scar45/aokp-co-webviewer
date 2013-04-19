package com.scar45.aokp.co.webviewer;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.method.BaseMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
	WebView myWebView;
	ProgressDialog myProgress;
    ProgressBar loadingProgressBar,loadingTitle;
    String urlAOKP = "http://clients.it-foundry.com/aokp/";
    String linkDomain = "clients.it-foundry.com";
    
    String urlDonateVersion = "https://play.google.com/store/apps/details?id=com.teambroccoli.theme.pcbblue";
    String urlscar45Play = "https://play.google.com/store/apps/developer?id=scar45";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = (WebView) findViewById(R.id.webview);
    	myWebView.setBackgroundColor(0xFF180E17);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setPluginsEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        myProgress = ProgressDialog.show(MainActivity.this, Html.fromHtml("Loading <b>AOKP.co</b>"), Html.fromHtml("<big><b><font color='" + getResources().getColor(R.color.aokp_pink) + "'><big>Unicorns</big></font></b> are fetching the official website for you nao.</big>"));
        //myProgress = ProgressDialog.show(this, "Loading AOKP.co", "Please wait...\n\nUnicorns are fetching the official website for you nao.");
        
        loadingProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);
        myWebView.setWebChromeClient(new WebChromeClient() {

            // this will be called on page loading progress

            @Override

            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);


                loadingProgressBar.setProgress(newProgress);
                //loadingTitle.setProgress(newProgress);
                // hide the progress bar if the loading is complete

                if (newProgress == 100) {
                loadingProgressBar.setVisibility(View.GONE);

                } else{
                loadingProgressBar.setVisibility(View.VISIBLE);

                }

            }

        });        
                  
        myWebView.setWebViewClient(new WebViewClient() {
		    @Override
		    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		        // Show the ProgressDialog on page load
                // myProgress.show();
        		
	            // If the site/domain matches, do not override; let myWebView load the page
		        if (Uri.parse(url).getHost().equals(linkDomain)) {
		            return false;
		        }

		        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
		        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		        startActivity(intent);
		        return true;
		    }
		    // When finishing loading page, dismiss the ProgressDialog if it is showing
		    public void onPageFinished(WebView view, String url) {
			    if(myProgress.isShowing()) {
			        myProgress.dismiss();
			    }
		    }
	    });
	    
	    myWebView.loadUrl(urlAOKP);

    }

    // Menu Selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case R.id.action_about:
                showDialog(11);
                break;
                
            case R.id.action_donate:
                Toast.makeText(getApplicationContext(),
                "Your support is very much appreciated.",
                Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlDonateVersion));
                startActivity(intent);
                break;
                
            default:
                break;
        }
        return true;
    }

    public void onBackPressed (){
        if (myWebView.isFocused() && myWebView.canGoBack()) {
                myWebView.goBack();       
        }else {
                //openMyDialog(null);
                MainActivity.this.finish();

        }
    }

    public void openMyDialog(WebView view) {
        showDialog(10);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case 10:
            // Create our Quit Dialog
            Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("\nAre you sure you want to exit?\n")
                .setTitle("Quit AOKP.co?")
                .setCancelable(true)
                .setPositiveButton("Exit AOKP.co",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                // Ends the activity
                                MainActivity.this.finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                Toast.makeText(getApplicationContext(),
                                        "Enjoy your stay!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            return builder.create();
        }
        switch (id) {    
            case 11:
            // Create our About Dialog
            TextView aboutMsg  = new TextView(this);
            aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
            aboutMsg.setPadding(30, 30, 30, 30);
            aboutMsg.setText(Html.fromHtml("<big>A simple app which gives you quick access to the official home of the Unicorns.<br><br>It was <b><font color='white'>developed by scar45</font></b>, the same member of Team Kang who built and maintains the AOKP.co website (which is mobile-friendly).<br><br><b><font color='white'>Please consider purchasing the</font> <a href=\""+urlDonateVersion+"\">Donate version</a></b><font color='white'>, as your contribution would help a lot!</font></big>"));
            
            Builder builder = new AlertDialog.Builder(this);
                builder.setView(aboutMsg)
                .setTitle(Html.fromHtml("About <b><font color='" + getResources().getColor(R.color.aokp_pink) + "'>AOKP.co</font></b>"))
                .setIcon(R.drawable.ic_launcher)
                .setCancelable(true)
                .setPositiveButton("Apps/Themes by scar45\non Google Play",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                int which) {
                                    Toast.makeText(getApplicationContext(),
                                    "Your support is very much appreciated.",
                                    Toast.LENGTH_LONG).show();
                                    // Loads the donation version Play Store link
		                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlscar45Play));
		                            startActivity(intent);
                            }
                        })
                .setNegativeButton("Keep on Kangin'",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                    Toast.makeText(getApplicationContext(),
                                    "Swagga baby!",
                                    Toast.LENGTH_LONG).show();
                            }
                        });

            return builder.create();
        }
        switch (id) {
        case 12:
            // Create our Donate Dialog
            Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("\nAlthough this app is simple, it wasn't the same to code (for myself, being a novice).\n\nDonating from this app supports me, scar45, the developer of the AOKP.co website and this Android app.\n")
                .setCancelable(true)
                .setPositiveButton("Donate to scar45 (TY!)",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                int which) {
                                    Toast.makeText(getApplicationContext(),
                                    "Your support is very much appreciated.",
                                    Toast.LENGTH_SHORT).show();
                                    // Loads the donation version Play Store link
		                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlDonateVersion));
		                            startActivity(intent);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                int which) {
                                    Toast.makeText(getApplicationContext(),
                                    "Enjoy the AOKP.co Website",
                                    Toast.LENGTH_SHORT).show();
                            }
                        });
            return builder.create();
        }
        
        return super.onCreateDialog(id);
    }
    

    
}
