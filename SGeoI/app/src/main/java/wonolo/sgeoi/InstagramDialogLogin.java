package wonolo.sgeoi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import wonolo.sgeoi.DataApp.InstagramData;
import wonolo.sgeoi.Interface.IOAuthDialogListener;

public class InstagramDialogLogin extends Dialog{

    private String mUrl;
    private TextView mTitleDialog;
    private WebView mWebViewDialog;
    private IOAuthDialogListener mListener;

    public InstagramDialogLogin(Context context, String url, IOAuthDialogListener listener) {
        super(context);
        mUrl = url;
        mListener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_login);

        mTitleDialog = (TextView) findViewById(R.id.tv_title_login);
        mWebViewDialog = (WebView) findViewById(R.id.wv_login);

        setUpWebView();

        // Reset cookie, when start app -> login again
        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private void setUpWebView() {
        mWebViewDialog.setVerticalScrollBarEnabled(false);
        mWebViewDialog.setHorizontalScrollBarEnabled(false);
        mWebViewDialog.setWebViewClient(new OAuthWebViewClient());
        mWebViewDialog.getSettings().setJavaScriptEnabled(true);
        mWebViewDialog.loadUrl(mUrl);
    }

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(InstagramData.CALLBACK_URL)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }
}
