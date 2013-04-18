package co.aokp.webviewer;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
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
        myProgress = ProgressDialog.show(this, "Loading AOKP.co", "Please wait while the Unicorns fetch your data...");
        
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
		        if (Uri.parse(url).getHost().equals("aokp.co")) {
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
	    
	    myWebView.loadUrl("http://aokp.co/");

    }

    // Menu Selections
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case R.id.action_about:
                showDialog(11);
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
                openMyDialog(null);

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
                .setTitle("Quit AOKP.co")
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
                .setNegativeButton("Stay with the Unicorns!",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                Toast.makeText(getApplicationContext(),
                                        "The Unicorns thank you :) Enjoy browsing!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

            return builder.create();
        }
        switch (id) {    
            case 11:
            // Create our About Dialog
            Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("\nAOKP.co is a simple app which gives you quick access to the AOKP website.\n\nIt was developed by scar45, the same member of Team Kang who built and maintains the AOKP.co website.\n\nIf you purchased the Donate version, he thanks you VERY much!\n")
                .setTitle("About AOKP.co")
                .setCancelable(true)
                .setNegativeButton("Kang On!",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                            }
                        });

            return builder.create();
        }
        return super.onCreateDialog(id);
    }
    

    
}
