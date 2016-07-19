package com.scar45.aokp.co.webviewer;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Html;
import android.text.method.BaseMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    protected FrameLayout webViewPlaceholder;
	protected WebView webView;
	protected View aokpWebview;
	protected ViewGroup parentViewGroup;

    ProgressBar loadingProgressBar,loadingTitle;

    String urlAOKP = "http://aokp.co/";
    String linkDomain = "aokp.co";
    String urlDonateVersion = "https://play.google.com/store/apps/details?id=com.scar45.aokp.co.webviewer.donate";
    String urlscar45Play = "https://play.google.com/store/apps/developer?id=scar45";
    String urlscar45 = "http://twitter.com/scar45";
    String urlGithub = "https://github.com/scar45/aokp-co-webviewer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.state_preserving_impl);

		// Initialize the UI
		initUI();
	}

    protected void initUI()
	{
		// Retrieve UI elements
		webViewPlaceholder = ((FrameLayout)findViewById(R.id.webViewPlaceholder));
	
		// Initialize the WebView if necessary
		if (aokpWebview == null)
		{
			// Create the webview
            setContentView(R.layout.activity_main);
        	aokpWebview = (View) findViewById(R.id.aokpWebview);
            parentViewGroup = (ViewGroup)aokpWebview.getParent();
			webView = (WebView) findViewById(R.id.webview);
			webView.getSettings().setSupportZoom(false);
			webView.getSettings().setBuiltInZoomControls(false);
			webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			webView.setScrollbarFadingEnabled(true);
			webView.getSettings().setLoadsImagesAutomatically(true);
            //webView.getSettings().setPluginsEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            
            // Enable downloads of files within webView
            webView.setDownloadListener(new DownloadListener() {
                public void onDownloadStart(String url, String userAgent,
                        String contentDisposition, String mimetype,
                        long contentLength) {
                  Intent i = new Intent(Intent.ACTION_VIEW);
                  i.setData(Uri.parse(url));
                  startActivity(i);
                }
            });


			// Attach the ProgressBar layout
            loadingProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal);

            webView.setWebChromeClient(new WebChromeClient() {

                // this will be called on page loading progress
                @Override
                public void onProgressChanged(WebView view, int newProgress) {

                    super.onProgressChanged(view, newProgress);

                    loadingProgressBar.setProgress(newProgress);

                    // hide the progress bar if the loading is complete
                    if (newProgress == 100) {
                    loadingProgressBar.setVisibility(View.GONE);
                    } else{
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    }
                }
                
            });   

            webView.setWebViewClient(new WebViewClient() {
            
		        @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {


	                // If the site/domain matches, do not override; let myWebView load the page
		            if (Uri.parse(url).getHost().equals(linkDomain)) {
		                return false;
		            }

		            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
		            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		            startActivity(intent);
		            return true;
		        }

            
	        });
	        
			// Load the first page
			webView.loadUrl(urlAOKP);

		}
		parentViewGroup.removeView(aokpWebview);
		// Attach the WebView to its placeholder
		parentViewGroup.addView(aokpWebview);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		if (webView != null)
		{
			// Remove the WebView from the old placeholder
			parentViewGroup.removeView(aokpWebview);
		}

		// Load the layout resource for the new configuration
        setContentView(R.layout.state_preserving_impl);

		// Reinitialize the UI
		initUI();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		// Save the state of the WebView
		webView.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		// Restore the state of the WebView
		webView.restoreState(savedInstanceState);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
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

            case R.id.action_reload:
                Toast.makeText(getApplicationContext(),
                "Fresh coat of Swagga, baby!",
                Toast.LENGTH_LONG).show();
                webView.reload();
                break;

            case R.id.action_exit:
                Toast.makeText(getApplicationContext(),
                "Thanks for using AOKP.co!",
                Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
                break;

            case R.id.action_downloads:
                webView.loadUrl(urlAOKP + "devices");
                break;

            case R.id.action_blog:
                webView.loadUrl(urlAOKP + "blog");
                break;
                

            case R.id.action_learn:
                webView.loadUrl(urlAOKP + "learn");
                break;
                
            case R.id.action_sitemenu:
                webView.loadUrl("javascript:toggleNavMenu();");
                break;
                                
            default:
                break;

        }
        return true;
    }

    public void onBackPressed (){
        if (webView.isFocused() && webView.canGoBack()) {
                webView.goBack();       
        }else {
                MainActivity.this.finish();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {    
            case 11:
            // Create our About Dialog
            TextView aboutMsg  = new TextView(this);
            aboutMsg.setMovementMethod(LinkMovementMethod.getInstance());
            aboutMsg.setPadding(30, 30, 30, 30);
            aboutMsg.setText(Html.fromHtml("<big>A simple app which gives you quick access to the official home of the Unicorns.<br><br><font color='white'>Developed by</font> <a href=\""+urlscar45+"\">scar45</a><font color='white'>, source on</font> <a href=\""+urlGithub+"\">Github</a><font color='white'>.</font><br><br><b><font color='white'>Please consider purchasing the</font> <a href=\""+urlDonateVersion+"\">Donate version</a></b><font color='white'>, as your contribution would surely help <em>a lot!</em></font></big>"));

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

        return super.onCreateDialog(id);
    }

}
