package com.mfc.ncd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnScrollChangedListener,ScrollViewListener
{

    WebView webView;
    SwipeRefreshLayout mySwipeRefreshLayout;
    ProgressBar progressBar;
    String URL = "https://cpt.stageibb.com/cpt_stage/cpt_mis/";
    // Staging
    /*String URL="https://ncd.ibbtrade.com";*/
    int totalHeight,webview,width;
    LinearLayout webViewLayout;
    View Vview;
    ScrollView scrollView;
    RelativeLayout parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);



       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.indeterminateBar);
        scrollView=findViewById(R.id.scrollView);
        webViewLayout=findViewById(R.id.wvLayout);
        Vview=findViewById(R.id.textView1);
        parentLayout=findViewById(R.id.parentLayout);


        totalHeight = scrollView.getChildAt(0).getHeight();
        mySwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swipeContainer);
        webView = findViewById(R.id.webview_main);
        WebSettings webSettings = webView.getSettings();
        webview=(int) Math.floor(webView.getContentHeight() * webView.getScale());

        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.FAR;
        webView.getSettings().setDisplayZoomControls(true);
        webView.clearCache(true);
        webView.clearHistory();
      //  webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDefaultZoom(zoomDensity);
        webView.requestFocus(View.FOCUS_DOWN);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.setWebViewClient(new myWebClient());
        webView.setWebChromeClient(new WebChromeClient());
        /*webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);*/
        webView.getViewTreeObserver().addOnScrollChangedListener(this);


        webView.loadUrl(URL);





        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webView.reload();
                        if (mySwipeRefreshLayout.isRefreshing()) {
                            if (progressBar.getVisibility() == View.VISIBLE) {
                                progressBar.setVisibility(View.GONE);
                            }
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
        );
        mySwipeRefreshLayout.isNestedScrollingEnabled();
        ViewTreeObserver vto = parentLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    parentLayout.getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this);
                } else {
                    parentLayout.getViewTreeObserver()
                            .removeGlobalOnLayoutListener(this);
                }
                width  = parentLayout.getMeasuredWidth();
                totalHeight = parentLayout.getMeasuredHeight();

            }
        });


    }

    /**
     * Callback method to be invoked when something in the view tree
     * has been scrolled.
     */
    @Override
    public void onScrollChanged() {



        if (webView.getScrollY() == 0) {
            mySwipeRefreshLayout.setEnabled(true);
        } else {
            mySwipeRefreshLayout.setEnabled(false);
        }

    }

     @Override
    public void onScrollChanged(ScrollViewExt scrollView, int x, int y, int oldx, int oldy)
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    public boolean isScrollDown()
    {
        boolean result=false;
        View view = (View) scrollView.getChildAt(scrollView.getChildCount()-1);
        int diff = (view.getBottom() - (totalHeight + scrollView.getScrollY()));

        if(scrollView.getHeight()==totalHeight)
        {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) webViewLayout.getLayoutParams();
            p.bottomMargin=300;

            webView.setLayoutParams(p);

            Log.d("ScrollTest", "MyScrollView: Bottom has been reached" );
        }
        else if(scrollView.getHeight()!=totalHeight)
        {

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) webViewLayout.getLayoutParams();
            p.bottomMargin=10;

            webView.setLayoutParams(p);

            Log.d("ScrollTest", "MyScrollView:Didn't reach Bottom " );

        }



        return result;
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            if (progressBar.getVisibility() == View.GONE) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

    @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
/* private class CustomeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1 == null || e2 == null) return false;
            if(e1.getPointerCount() > 1 || e2.getPointerCount() > 1) return false;
            else {
                try { // right to left swipe .. go to next page
                    if(e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 800) {
                        //do your stuff
                        return true;
                    } //left to right swipe .. go to prev page
                    else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 800) {
                        //do your stuff
                        return true;
                    } //bottom to top, go to next document
                    else if(e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 800
                            && webView.getScrollY() >= webView.getScale() * (webView.getContentHeight() - webView.getHeight())) {
                        //do your stuff
                        return true;
                    } //top to bottom, go to prev document
                    else if (e2.getY() - e1.getY() > 100 && Math.abs(velocityY) > 800 ) {
                        //do your stuff
                        return true;
                    }
                } catch (Exception e) { // nothing
                }
                return false;
            }
        }
    }


     //Vview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500));
            //webViewLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300));
             /*if(diff>5)
        {

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) webViewLayout.getLayoutParams();
            p.bottomMargin=300;

            webView.setLayoutParams(p);

            Log.d("ScrollTest", "MyScrollView: Bottom has been reached" );
        }
        else if(diff==0)
        {

            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) webViewLayout.getLayoutParams();
            p.bottomMargin=10;

            webView.setLayoutParams(p);

            Log.d("ScrollTest", "MyScrollView:Didn't reach Bottom " );

        }
        */
