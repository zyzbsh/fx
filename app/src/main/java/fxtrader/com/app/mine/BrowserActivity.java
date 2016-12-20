//package fxtrader.com.app.mine;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//import android.annotation.SuppressLint;
//import android.annotation.TargetApi;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.pm.ResolveInfo;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.webkit.CookieSyncManager;
//import android.webkit.DownloadListener;
//import android.webkit.JsPromptResult;
//import android.webkit.URLUtil;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebHistoryItem;
//import android.webkit.WebIconDatabase;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import fxtrader.com.app.R;
//import fxtrader.com.app.base.BaseActivity;
//
//public class BrowserActivity extends BaseActivity implements OnClickListener {
//
//	private WebView mWebView;
//
//	private boolean mInLoad;
//	private boolean mPageStarted;
//	private boolean mActivityInPause = true;
//	private boolean mMenuIsDown;
//
//	private final static int LOCK_ICON_UNSECURE = 0;
//	private final static int LOCK_ICON_SECURE = 1;
//	private final static int LOCK_ICON_MIXED = 2;
//
//	private int mLockIconType = LOCK_ICON_UNSECURE;
//	private int mPrevLockType = LOCK_ICON_UNSECURE;
//
//	private int type = 0;//1表示从启动广告页跳转过来的，0表示从其他地方跳转
//	private String title;
//	private WebViewInject webViewInject;
//	private String pluginName;
//	protected ValueCallback<Uri> mUploadMessage;
//	public ValueCallback<Uri[]> mUploadMessageForAndroid5;
//	public static final int FILECHOOSER_RESULTCODE = 100;
//	public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 101;
//	private Map<String,String> extraHeaders = new HashMap<String, String>();
//	public static final int REQUEST_LOC_SETTINGS_ON = 201;
//	private static String currentUrl;
//	private static String tmpUrl;
//
//
//	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
//	@Override
//	public void onCreate(Bundle icicle) {
//		super.onCreate(icicle);
//		requestWindowFeature(Window.FEATURE_LEFT_ICON);
//		requestWindowFeature(Window.FEATURE_RIGHT_ICON);
//		requestWindowFeature(Window.FEATURE_PROGRESS);
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		CookieSyncManager.createInstance(this);
//		setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
//		setContentView(R.layout.browser);
//
//		mWebView = (WebView) findViewById(R.id.webview);
//		mWebView.setWebChromeClient(mWebChromeClient);
//		mWebView.setWebViewClient(mWebViewClient);
//		mWebView.getSettings().setSupportMultipleWindows(false);
//		mWebView.getSettings().setBuiltInZoomControls(false);
//		mWebView.getSettings().setJavaScriptEnabled(true);
//		mWebView.getSettings().setSavePassword(false);
//		// localstore
//		mWebView.getSettings().setDomStorageEnabled(true);
//		mWebView.getSettings().setDatabaseEnabled(true);
//		mWebView.getSettings().setAppCacheMaxSize(1024*1024*8);
//		String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//		mWebView.getSettings().setAppCachePath(appCachePath);
//		mWebView.getSettings().setAllowFileAccess(true);
//		mWebView.getSettings().setAppCacheEnabled(true);
//		mWebView.getSettings().setGeolocationEnabled(true);
//		String dir = getApplicationContext().getDir("database", 0).getPath();
//		mWebView.getSettings().setGeolocationDatabasePath(dir);
//
//		//https 页面加载非http链接内容 // 大于KK
//		if (Build.VERSION.SDK_INT > 19) {
//			try {
//				mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		// ua 加特定标识mcontact
//		String userAgentString = mWebView.getSettings().getUserAgentString();
//		mWebView.getSettings().setUserAgentString(
//				userAgentString + ";CMCCCONTACTS/"+ApplicationUtils.getVersionName(mContext));
//		bh = new BrowserHandle(mContext, mWebView);
//
//		webViewInject = WebViewInject.getInstance();
//		if (hasJellyBeanMR1()) {
//			mWebView.addJavascriptInterface(new JavaScriptInterface(
//					bh.mWebJsHandler), "contact");
//		} else {
//			webViewInject.getmJsInterfaceMap().put("contact",
//					new JavaScriptInterface(bh.mWebJsHandler));
//		}
//
//		removeSearchBoxImpl();// 删除掉Android默认注册的JS接口
//
//		mWebView.setDownloadListener(new DownloadListener() {
//			@Override
//			public void onDownloadStart(String url, String userAgent,
//					String contentDisposition, String mimetype,
//					long contentLength) {
//				// 使用系统浏览器进行下载
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				Uri downloadUri = Uri.parse(url);
//				i.setData(downloadUri);
//				startActivity(i);
//			}
//		});
//
//		registerForContextMenu(mWebView);
//		Intent intent = getIntent();
//		String url = intent.getStringExtra("url");
//		title = intent.getStringExtra("title");
//		type = intent.getIntExtra("type", 0);
//		pluginName = title;
//		currentUrl = url;
//
//			// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//			// new
//			// LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
//			// params.gravity = Gravity.CENTER;
//			// mRefresh.setLayoutParams(params);
//		}
//		resetTitleAndIcon(mWebView);
//		SyncSP.saveCloudSyncNoticeStatus(this, false);
//
//		// mWebView.loadUrl("http://txl.cytxl.com.cn/wap/activity.php?token=null&endpointId=3e16e03b-56e0-4f26-b701-fac0c9e93567");
//		// mWebView.loadUrl("http://txl.cytxl.com.cn/wap/activity.php");
//		// mWebView.loadUrl("file:///android_asset/test.html");
//	}
//
//	private void initTitle() {
//		mIcloudActionBar = getIcloudActionBar();
//			mIcloudActionBar
//				.setNavigationMode(IcloudActionBar.DISPLAY_AS_UP_TITLE_BTN);
//			if (!TextUtils.isEmpty(title)) {
//				changeActionBarTitle(title);
//				} else {
//					changeActionBarTitle("  ");
//					}
//
//			/*if(getIntent().getIntExtra("type",-1) == CalllogsListFragment.NEWS_LIST){
//				mIcloudActionBar.setDisplayAsUpBack(R.drawable.iab_green_back, new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						if (!mWebView.getUrl().equals(MainSP.getNewsUrl(mContext))) {
//							if(mWebView.canGoBack()){
//								mWebView.goBack();
//							}
//						}else{
//							finish();
//						}
//					}
//				});
//			}
//			if(getIntent().getIntExtra("type",-1) == ConversationListFragment.NEWS_DETAIL){
//				mIcloudActionBar.setDisplayAsUpBack(R.drawable.iab_green_back, new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						AspMobclickAgent.onEvent(BrowserActivity.this, "recentcall_headline_click");
//						if(mWebView.getUrl() == null) finish();
//						else mWebView.loadUrl(MainSP.getNewsUrl(getApplicationContext()));
//					}
//				});
//			}else{
//				mIcloudActionBar.setDisplayAsUpBack(R.drawable.iab_green_back, this);
//			}*/
//			mIcloudActionBar.setDisplayAsUpBack(R.drawable.iab_green_back, this);
//			mIcloudActionBar.setDisplayAsUpTitleBtn("", null);
//			//判断是否为新闻列表，是则不显示头部
//			if(currentUrl!=null&&currentUrl.equals(MainSP.getNewsUrl(mContext))){
//				mIcloudActionBar.findViewById(R.id.iab_view).setVisibility(View.GONE);
//				ivBack.setVisibility(View.VISIBLE);
//			}
//		}
//
//
//	private void changeActionBarTitle(String title) {
//		mIcloudActionBar.setDisplayAsUpTitle(title);
//	}
//
//	public static Intent createIntent(Context context, String url, String title) {
//		Intent intent = new Intent(context, BrowserActivity.class);
//		intent.putExtra("url", url);
//		intent.putExtra("title", title);
//		return intent;
//	}
//	public static Intent createIntent(Context context, String url, String title,int type) {
//		Intent intent = new Intent(context, BrowserActivity.class);
//		intent.putExtra("url", url);
//		intent.putExtra("title", title);
//		intent.putExtra("type", type);
//		return intent;
//	}
//
//	/**
//	 * 回退到主界面
//	 * @param intent
//	 * @return
//	 */
//	private Intent createBackIntent(Intent intent) {
//		Intent intent2 = null;
//		if (GlobalConfig.NOT_AGREEMENT || MainSP.getAgreementFlag(this)) {
//			if (OtherSP.getIsfirstAgree(this) || GlobalConfig.NOT_AGREEMENT) {
//				intent2 = new Intent(this, Main.class);
//			}
//		} else {
//			Intent intent3 = new Intent(this, PushService.class);
//			this.stopService(intent3);
//			Intent intent4 = new Intent(this, AoeService.class);
//			this.stopService(intent4);
//			OtherSP.saveIsfirstAgree(this, false);
//			intent2 = new Intent(this, AgreementForFirst.class);
//		}
//
//		Bundle bundle = intent.getExtras();
//		if (bundle != null && intent2 != null) {
//			intent2.putExtras(bundle);
//		}
//
//		String action = intent.getAction();
//		if (action != null && intent2 != null) {
//			intent2.setAction(intent.getAction());
//		}
//
//		Uri data = intent.getData();
//
//		if (data != null && intent2 != null) {
//			intent2.setData(intent.getData());
//		}
//		if (intent2 != null) {
//			intent2.setFlags(intent.getFlags());
//		}
//		if (intent2 == null) {
//			intent2 = new Intent(this, AgreementForFirst.class);
//		}
//		return intent2;
//	}
//
//	private void stopLoading() {
//		resetTitleAndRevertLockIcon();
//		mWebView.stopLoading();
//		mWebViewClient.onPageFinished(mWebView, mWebView.getUrl());
//
//	}
//
//	@Override
//	public void onActivityResult(int req_code, int res_code, Intent intent) {
//		switch (req_code) {
//		case ComposeMessageActivity.REQUEST_CODE_ADD_RECIPIENT:
//			// 选择收件人
//			bh.onActivityResultAddRecipient(intent, mWebView);
//			break;
//		case BrowserHandle.JS_ACTION_LOGIN:
//			BrowserHandle.onActivityResultLoginRequest(mWebView, mContext);
//			break;
//		case BrowserHandle.JS_ACTION_ADD_CONTACT:
//			BrowserHandle.onActivityAddContact(mWebView);
//			break;
//		case BrowserHandle.JS_ACTION_EDIT_CONTACT:
//			BrowserHandle.onActivityEditContact(mWebView);
//			break;
//		case BrowserHandle.JS_ACTION_SEND_MESSAGE:
//			BrowserHandle.onActivitySendSms(mWebView);
//			break;
//		case BrowserHandle.JS_ACTION_LOGIN_RETURN_INFO:
//			BrowserHandle.onActivityReturnLoginInfo(mWebView);
//			break;
//		case BrowserHandle.JS_ACTION_PRE_CALL_AFTER_CONTACT_CHOOSE:
//			if (res_code == RESULT_CANCELED) {
//				BrowserHandle.onActivityPreCallAfterContactChoose(mWebView,
//						BrowserHandle.CODE_EXPTION);
//			} else {
//				String number = intent.getExtras().getString("number");
//				if (!TextUtils.isEmpty(number)) {
//					ApplicationUtils.placeCall(mContext, number);
//				}
//				BrowserHandle.onActivityPreCallAfterContactChoose(mWebView,
//						BrowserHandle.CODE_NORMAL); // normal
//			}
//			break;
//		case FILECHOOSER_RESULTCODE:
//			if (null == mUploadMessage)
//				return;
//			Uri result = intent == null || res_code != RESULT_OK ? null : intent
//					.getData();
//			mUploadMessage.onReceiveValue(result);
//			mUploadMessage = null;
//			break;
//		case FILECHOOSER_RESULTCODE_FOR_ANDROID_5:
//			if (null == mUploadMessageForAndroid5)
//				return;
//			Uri resultUri = (intent == null || res_code != RESULT_OK) ? null
//					: intent.getData();
//			if (resultUri != null) {
//				mUploadMessageForAndroid5
//						.onReceiveValue(new Uri[] { resultUri });
//			} else {
//				mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
//			}
//			mUploadMessageForAndroid5 = null;
//			break;
//		case REQUEST_LOC_SETTINGS_ON:
//			if (isGeolocationOK()) {
//				mWebView.loadUrl(currentUrl);
//			}else{
//				Toast.makeText(BrowserActivity.this, "系统定位未开启~", Toast.LENGTH_SHORT).show();
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		// And yet,WebView is not load. Process Bar is setting 1% that is better
//		// UE. longshulin
//		progressbar.setProgress(1);
//		if (!mActivityInPause) {
//			return;
//		}
//
//		mActivityInPause = false;
//		resumeWebView();
//	}
//
//	@Override
//	protected void onStop() {
//		LogUtils.i("king", "BrowserActivity onStop");
//		// 退出分享分享正在加载的对话框
//		bh.dimissShareLoadingDialog();
//		super.onStop();
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//
//		pauseWebView();
//
//		if (mActivityInPause) {
//			return;
//		}
//
//		mActivityInPause = true;
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		WebIconDatabase.getInstance().close();
//		LogUtils.i("king", "pluginName " + pluginName);
//		if (pluginName != null && pluginName.contains(PlugInsManager.homeGroup)
//				&& LoginInfoSP.isLogin(mContext)) {
//			FamilyNetUtils.getInstance().updateFamilyMember(mContext);
//		}
//
//		if (mWebView != null)
//			if (ApplicationUtils.isNetworkAvailable(mContext)) {
//				mWebView.loadUrl("about:blank");
//			} else {
//				mWebView.setVisibility(View.INVISIBLE);
//				noNetLayout.setVisibility(View.VISIBLE);
//			}
//
//	}
//
//	/**
//	 * Resets the browser title-view to whatever it must be (for example, if we
//	 * load a page from history).
//	 */
//	@SuppressWarnings("unused")
//	private void resetTitle() {
//		resetLockIcon();
//		resetTitleIconAndProgress();
//	}
//
//	/**
//	 * Resets the lock icon. This method is called when the icon needs to be
//	 * reset but we do not know whether we are loading a secure or not secure
//	 * page.
//	 */
//	private void resetLockIcon() {
//		// Save the lock-icon state (we revert to it if the load gets cancelled)
//		saveLockIcon();
//
//		mLockIconType = LOCK_ICON_UNSECURE;
//
//		updateLockIconImage(LOCK_ICON_UNSECURE);
//	}
//
//	/**
//	 * Resets the lock icon. This method is called when we start a new load and
//	 * know the url to be loaded.
//	 */
//	private void resetLockIcon(String url) {
//		// Save the lock-icon state (we revert to it if the load gets cancelled)
//		saveLockIcon();
//
//		mLockIconType = LOCK_ICON_UNSECURE;
//		if (URLUtil.isHttpsUrl(url)) {
//			mLockIconType = LOCK_ICON_SECURE;
//		}
//
//		updateLockIconImage(LOCK_ICON_UNSECURE);
//	}
//
//	/**
//	 * Resets the browser title-view to whatever it must be (for example, if we
//	 * had a loading error) When we have a new page, we call resetTitle, when we
//	 * have to reset the titlebar to whatever it used to be (for example, if the
//	 * user chose to stop loading), we call resetTitleAndRevertLockIcon.
//	 */
//	/* package */void resetTitleAndRevertLockIcon() {
//		revertLockIcon();
//		resetTitleIconAndProgress();
//	}
//
//	/**
//	 * Saves the current lock-icon state before resetting the lock icon. If we
//	 * have an error, we may need to roll back to the previous state.
//	 */
//	private void saveLockIcon() {
//		mPrevLockType = mLockIconType;
//	}
//
//	/**
//	 * Reverts the lock-icon state to the last saved state, for example, if we
//	 * had an error, and need to cancel the load.
//	 */
//	private void revertLockIcon() {
//		mLockIconType = mPrevLockType;
//
//		updateLockIconImage(mLockIconType);
//	}
//
//	/**
//	 * Updates the lock-icon image in the title-bar.
//	 */
//	private void updateLockIconImage(int lockIconType) {
//		Drawable d = null;
//		if (lockIconType == LOCK_ICON_SECURE) {
//			d = mSecLockIcon;
//		} else if (lockIconType == LOCK_ICON_MIXED) {
//			d = mMixLockIcon;
//		}
//
//		getWindow().setFeatureDrawable(Window.FEATURE_RIGHT_ICON, d);
//	}
//
//	private void resetTitleIconAndProgress() {
//		if (mWebView == null) {
//			return;
//		}
//		resetTitleAndIcon(mWebView);
//		int progress = mWebView.getProgress();
//		mWebChromeClient.onProgressChanged(mWebView, progress);
//	}
//
//	// Reset the title and the icon based on the given item.
//	private void resetTitleAndIcon(WebView view) {
//		WebHistoryItem item = view.copyBackForwardList().getCurrentItem();
//		if (item != null) {
//			setUrlTitle(item.getUrl(), item.getTitle());
//			setFavicon(item.getFavicon());
//		} else {
//			setUrlTitle(null, null);
//			setFavicon(null);
//		}
//	}
//
//	// Set the favicon in the title bar.
//	private void setFavicon(Bitmap icon) {
//		// While the tab overview is animating or being shown, block changes to
//		// the favicon.
//		/**
//		 * Don't set icon now, because we use google format proxy Drawable[]
//		 * array = new Drawable[2]; PaintDrawable p = new
//		 * PaintDrawable(Color.WHITE); p.setCornerRadius(3f); array[0] = p; if
//		 * (icon == null) { array[1] = mGenericFavicon; } else { array[1] = new
//		 * BitmapDrawable(icon); } LayerDrawable d = new LayerDrawable(array);
//		 * d.setLayerInset(1, 2, 2, 2, 2);
//		 * getWindow().setFeatureDrawable(Window.FEATURE_LEFT_ICON, d);
//		 */
//	}
//
//	/**
//	 * Sets a title composed of the URL and the title string.
//	 *
//	 * @param url
//	 *            The URL of the site being loaded.
//	 * @param title
//	 *            The title of the site being loaded.
//	 */
//	private void setUrlTitle(String url, String title) {
//		setTitle(buildUrlTitle(url, title));
//	}
//
//	/**
//	 * Builds and returns the page title, which is some combination of the page
//	 * URL and title.
//	 *
//	 * @param url
//	 *            The URL of the site being loaded.
//	 * @param title
//	 *            The title of the site being loaded.
//	 * @return The page title.
//	 */
//	private String buildUrlTitle(String url, String title) {
//		String urlTitle = "";
//
//		if (url != null) {
//			String titleUrl = buildTitleUrl(url);
//
//			if (title != null && 0 < title.length()) {
//				if (titleUrl != null && 0 < titleUrl.length()) {
//					urlTitle = titleUrl + ": " + title;
//				} else {
//					urlTitle = title;
//				}
//			} else {
//				if (titleUrl != null) {
//					urlTitle = titleUrl;
//				}
//			}
//		}
//
//		return urlTitle;
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			// 这里重写返回键
//
//
//			if (mWebView.canGoBack()) {
//				mWebView.stopLoading();// 解决：家庭网主界面，刷新的时候，点击手机自带的返回键，有较大的概率会提示“和通讯录停止运行”
//				if(mWebView.getUrl().equals(MainSP.getNewsUrl(mContext))){
//					finish();
//				}else{
//					mWebView.goBack();
//				}
//
//			} else {
//				//信息列表的新闻详情页返回到新闻列表页
//				if( mWebView.getUrl()!=null && mWebView.getUrl().equals(tmpUrl)){
//					AspMobclickAgent.onEvent(BrowserActivity.this, "recentcall_headline_click");
//
//					if (getIntent().getIntExtra("type",-1) == ConversationListFragment.NEWS_DETAIL) {
//						 AspMobclickAgent.onEvent(mContext, "sms_new_list");
//					}
//					//新闻详情页无网络时返回到信息列表页
//					if(!ApplicationUtils.isNetworkAvailable(BrowserActivity.this)){
//						BaseToast.makeText(BrowserActivity.this, "网络不给力,请检查网络设置", 1000).show();
//						finish();
//					}else{
//						mWebView.loadUrl(MainSP.getNewsUrl(mContext));
//					}
//					return true;
//				}else if (type == 1) {
//					Intent intent2 = createBackIntent(getIntent());
//					startActivity(intent2);
//					finish();
//				}else {
//					finish();
//				}
//
//			}
//			return true;
//		}
//		return false;
//		// return super.onKeyDown(keyCode, event);
//	}
//
//	/**
//	 * @param url
//	 *            The URL to build a title version of the URL from.
//	 * @return The title version of the URL or null if fails. The title version
//	 *         of the URL can be either the URL hostname, or the hostname with
//	 *         an "https://" prefix (for secure URLs), or an empty string if,
//	 *         for example, the URL in question is a file:// URL with no
//	 *         hostname.
//	 */
//	private String buildTitleUrl(String url) {
//		String titleUrl = null;
//
//		if (url != null) {
//			try {
//				// parse the url string
//				URL urlObj = new URL(url);
//				if (urlObj != null) {
//					titleUrl = "";
//
//					String protocol = urlObj.getProtocol();
//					String host = urlObj.getHost();
//
//					if (host != null && 0 < host.length()) {
//						titleUrl = host;
//						if (protocol != null) {
//							// if a secure site, add an "https://" prefix!
//							if (protocol.equalsIgnoreCase("https")) {
//								titleUrl = protocol + "://" + host;
//							}
//						}
//					}
//				}
//			} catch (MalformedURLException e) {
//			}
//		}
//
//		return titleUrl;
//	}
//
//	private boolean resumeWebView() {
//		if ((!mActivityInPause && !mPageStarted)
//				|| (mActivityInPause && mPageStarted)) {
//			CookieSyncManager.getInstance().startSync();
//			if (mWebView != null) {
//				mWebView.resumeTimers();
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	private boolean pauseWebView() {
//		if (mActivityInPause && !mPageStarted) {
//			CookieSyncManager.getInstance().stopSync();
//			if (mWebView != null) {
//				mWebView.pauseTimers();
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	private final WebViewClient mWebViewClient = new WebViewClient() {
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			LogUtils.d("jjw", "onPageStarted = " + System.currentTimeMillis());
//
//			if (!hasJellyBeanMR1()) {
//				webViewInject.injectJavascriptInterfaces(view);
//			}
//
//			resetLockIcon(url);
//			setUrlTitle(url, null);
//			// Call updateIcon instead of setFavicon so the bookmark
//			// database can be updated.
//			setFavicon(favicon);
//
//			if (!mPageStarted) {
//				mPageStarted = true;
//				// if onResume() has been called, resumeWebView() does nothing.
//				resumeWebView();
//			}
//
//			// reset sync timer to avoid sync starts during loading a page
//			CookieSyncManager.getInstance().resetSync();
//
//			mInLoad = true;
//
//			//判断是否为新闻列表，是则不显示头部
//			if(url.equals(MainSP.getNewsUrl(mContext))){
//				mIcloudActionBar.findViewById(R.id.iab_view).setVisibility(View.GONE);
//				ivBack.setVisibility(View.VISIBLE);
//			}
//		}
//
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			LogUtils.d("jjw", "onPageFinished = " + System.currentTimeMillis());
//
//			if (!hasJellyBeanMR1()) {
//				webViewInject.injectJavascriptInterfaces(view);
//			}
//			// Reset the title and icon in case we stopped a provisional
//			// load.
//
//			if(!url.equals(MainSP.getNewsUrl(mContext))){
//				mIcloudActionBar.findViewById(R.id.iab_view).setVisibility(View.VISIBLE);
//				ivBack.setVisibility(View.GONE);
//			}
//
//			if (mWebView.canGoBack()) {
//				mBack.setEnabled(true);
//				mBack.setImageDrawable(getResources().getDrawable(
//						R.drawable.goback));
//			} else {
//				mBack.setEnabled(false);
//				mBack.setImageDrawable(getResources().getDrawable(
//						R.drawable.goback_noaction));
//
//			}
//			if (mWebView.canGoForward()) {
//				mForword.setEnabled(true);
//				mForword.setImageDrawable(getResources().getDrawable(
//						R.drawable.goforword));
//			} else {
//				mForword.setEnabled(false);
//				mForword.setImageDrawable(getResources().getDrawable(
//						R.drawable.goforword_noaction));
//			}
//			resetTitleAndIcon(view);
//
//			// Update the lock icon image only once we are done loading
//			updateLockIconImage(mLockIconType);
//
//			WebHistoryItem item = view.copyBackForwardList().getCurrentItem();
//			if (item != null) {
//				String title = item.getTitle();
//				if (!TextUtils.isEmpty(title)) {
//					//changeActionBarTitle(title);
//				}
//			}
//		}
//
//		/**
//		 * Updates the lock icon. This method is called when we discover another
//		 * resource to be loaded for this page (for example, javascript). While
//		 * we update the icon type, we do not update the lock icon itself until
//		 * we are done loading, it is slightly more secure this way.
//		 */
//		@Override
//		public void onLoadResource(WebView view, String url) {
//
//			if (!hasJellyBeanMR1()) {
//				webViewInject.injectJavascriptInterfaces(view);
//			}
//
//			if (url != null && url.length() > 0) {
//				// It is only if the page claims to be secure
//				// that we may have to update the lock:
//				if (mLockIconType == LOCK_ICON_SECURE) {
//					// If NOT a 'safe' url, change the lock to mixed content!
//					if (!(URLUtil.isHttpsUrl(url) || URLUtil.isDataUrl(url) || URLUtil
//							.isAboutUrl(url))) {
//						mLockIconType = LOCK_ICON_MIXED;
//					}
//				}
//			}
//		}
//
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			// TODO Auto-generated method stub
//			if (!TextUtils.isEmpty(url)) {
//				currentUrl = url;
//				LogUtils.i("king", url);
//				if (url.startsWith("wtai://")) {
//					int starttel = 13;
//					String phone = url.substring(starttel);
//					ApplicationUtils.placeCall(mContext, phone);
//					return true;
//				}
//				if (url.startsWith("http://") || url.startsWith("https://")
//						|| url.startsWith("www.")) {
//					Map<String, String> extraHeaders = new HashMap<String, String>();
//					extraHeaders.put("Referer", view.getUrl());
//					view.loadUrl(url, extraHeaders);
//					return true;
//				} else {
//					Uri overrideUri = Uri.parse(url);
//					String scheme = overrideUri.getScheme();
//					if ("tel".equals(scheme)) {
//						Intent intent = null;
//						String model = ApplicationUtils.getMobileModel();
//						if (model != null && model.startsWith("vivo")) {
//							intent = new Intent();
//							intent.setAction(Intent.ACTION_DIAL); // android.intent.action.DIAL
//							intent.setData(overrideUri);
//						} else {
//							intent = new Intent(
//									"android.intent.action.CALL",
//									overrideUri);
//						}
//						BrowserActivity.this.startActivity(intent);
//						return true;
//					} else if ("sms".equals(scheme)) {
//						Intent intent = new Intent(
//								"android.intent.action.SENDTO", overrideUri);
//						BrowserActivity.this.startActivity(intent);
//						return true;
//					} else {// 打开第三方应用
//						Intent schemeIntent = null;
//						String pkgName = null;
//						Uri intentUri = null;
//						String actionName = null;
//						try {
//							schemeIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//							if (schemeIntent == null)
//								return true;
//							pkgName = schemeIntent.getPackage();
//							intentUri = schemeIntent.getData();
//							actionName = schemeIntent.getAction();
//							if(CommonConstants.DEBUG)
//							Log.i("aspire",
//									"pkgName:" + pkgName + ",intentUri:" + intentUri + "actionName:" + actionName);
//							if (TextUtils.isEmpty(actionName) || intentUri == null || "".equals(intentUri)) {
//								return true;
//							}
//							Intent intent = new Intent(actionName, intentUri);
//							BrowserActivity.this.startActivity(intent);
//						} catch (Exception e) {
//							if (TextUtils.isEmpty(actionName) || TextUtils.isEmpty(pkgName)) {
//								return true;
//							}
//							try {
//								// 没有安装应用，默认打开应用市场去下载
//								Intent intent = new Intent(actionName, Uri.parse("market://details?id=" + pkgName));
//								BrowserActivity.this.startActivity(intent);
//							} catch (Exception e1) {
//								e1.printStackTrace();
//							}
//						}
//					}
//				}
//			}
//			return true;
//		}
//
//		/**
//		 * Show a dialog informing the user of the network error reported by
//		 * WebCore.
//		 */
//		@Override
//		public void onReceivedError(WebView view, int errorCode,
//				String description, String failingUrl) {
//			super.onReceivedError(view, errorCode, description, failingUrl);
//			// We need to reset the title after an error.
//			resetTitleAndRevertLockIcon();
//			if (!ApplicationUtils.isNetworkAvailable(BrowserActivity.this)) {
//				mWebView.setVisibility(View.INVISIBLE);
//				noNetLayout.setVisibility(View.VISIBLE);
//				ivBack.setVisibility(View.GONE);
//				mIcloudActionBar.findViewById(R.id.iab_view).setVisibility(View.VISIBLE);
//			}
//		}
//
//		/**
//		 * Insert the url into the visited history database.
//		 *
//		 * @param url
//		 *            The url to be inserted.
//		 * @param isReload
//		 *            True if this url is being reloaded.
//		 */
//		@Override
//		public void doUpdateVisitedHistory(WebView view, String url,
//				boolean isReload) {
//
//			if (!hasJellyBeanMR1()) {
//				webViewInject.injectJavascriptInterfaces(view);
//			}
//
//			if (url.regionMatches(true, 0, "about:", 0, 6)) {
//				return;
//			}
//			WebIconDatabase.getInstance().retainIconForPageUrl(url);
//		}
//
//		@Override
//		public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//			if (mMenuIsDown) {
//				// only check shortcut key when MENU is held
//				return getWindow().isShortcutKey(event.getKeyCode(), event);
//			} else {
//				return false;
//			}
//		}
//	};
//
//	/* package */WebChromeClient getWebChromeClient() {
//		return mWebChromeClient;
//	}
//
//	private final WebChromeClient mWebChromeClient = new WebChromeClient() {
//
//		public void onGeolocationPermissionsShowPrompt(final String origin, final android.webkit.GeolocationPermissions.Callback callback) {
//
//			callback.invoke(origin, true, true);
//			/*AlertDialog.Builder builder = new AlertDialog.Builder(BrowserActivity.this);
//			builder.setMessage("允许获取您的地址位置信息?");
//			android.content.DialogInterface.OnClickListener dialogButtonOnClickListener = new android.content.DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int clickedButton) {
//					if (DialogInterface.BUTTON_POSITIVE == clickedButton) {
//						// callback.invoke(origin, true, true);
//						if (isGeolocationOK()) {
//							callback.invoke(origin, true, true);
//						} else {
//							// open geolocation settings
//							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//							startActivityForResult(intent, REQUEST_LOC_SETTINGS_ON);
//							callback.invoke(origin, true, true);
//						}
//
//					} else if (DialogInterface.BUTTON_NEGATIVE == clickedButton) {
//						callback.invoke(origin, false, false);
//					}
//				}
//			};
//			builder.setPositiveButton("允许", dialogButtonOnClickListener);
//			builder.setNegativeButton("拒绝", dialogButtonOnClickListener);
//			builder.show();*/
//			super.onGeolocationPermissionsShowPrompt(origin, callback);
//
//		};
//
//		@Override
//		public void onProgressChanged(WebView view, int newProgress) {
//			super.onProgressChanged(view, newProgress);
//
//			if (!hasJellyBeanMR1()) {
//				webViewInject.injectJavascriptInterfaces(view);
//			}
//			// Block progress updates to the title bar while the tab overview
//			// is animating or being displayed.
//			getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
//					newProgress * 100);
//
//			progressbar.setProgress(newProgress);
//
//			if (newProgress == 100) {
//				// onProgressChanged() is called for sub-frame too while
//				// onPageFinished() is only called for the main frame. sync
//				// cookie and cache promptly here.
//				CookieSyncManager.getInstance().sync();
//				if (mInLoad) {
//					mInLoad = false;
//				}
//				progressbar.setVisibility(View.GONE);
//			} else {
//				// onPageFinished may have already been called but a subframe
//				// is still loading and updating the progress. Reset mInLoad
//				// and update the menu items.
//				progressbar.setVisibility(View.VISIBLE);
//				if (!mInLoad) {
//					mInLoad = true;
//				}
//			}
//		}
//
//		@Override
//		public boolean onJsPrompt(WebView view, String url, String message,
//				String defaultValue, JsPromptResult result) {
//			if (!hasJellyBeanMR1()) {
//				if (view instanceof WebView) {
//					if (webViewInject.handleJsInterface(view, url, message,
//							defaultValue, result)) {
//						return true;
//					}
//				}
//			}
//			return super.onJsPrompt(view, url, message, defaultValue, result);
//		}
//
//		@Override
//		public void onReceivedTitle(WebView view, String title) {
//
//			if (!hasJellyBeanMR1()) {
//				webViewInject.injectJavascriptInterfaces(view);
//			}
//
//			String url = view.getUrl();
//
//			// here, if url is null, we want to reset the title
//			LogUtils.i("king", "url " + url + " title " + title);
//			setUrlTitle(url, title);
//			//changeActionBarTitle(title);
//
//		}
//
//		@Override
//		public void onReceivedIcon(WebView view, Bitmap icon) {
//			setFavicon(icon);
//		}
//
//		// The undocumented magic method override
//		// Eclipse will swear at you if you try to put @Override here
//		// For Android 3.0-
//		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//			openFileChooserImpl(uploadMsg);
//		}
//
//		// For Android 3.0+
//		public void openFileChooser(ValueCallback<Uri> uploadMsg,
//				String acceptType) {
//			openFileChooserImpl(uploadMsg);
//		}
//
//		// For Android 4.1
//		public void openFileChooser(ValueCallback<Uri> uploadMsg,
//				String acceptType, String capture) {
//			openFileChooserImpl(uploadMsg);
//		}
//
//        // For Android 5.0+
//		public boolean onShowFileChooser(WebView webView,
//				ValueCallback<Uri[]> uploadMsg,
//				FileChooserParams fileChooserParams) {
//			openFileChooserImplForAndroid5(uploadMsg);
//			return true;
//		}
//	};
//
//    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
//        mUploadMessage = uploadMsg;
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "文件选择"), FILECHOOSER_RESULTCODE);
//    }
//
//    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg) {
//        mUploadMessageForAndroid5 = uploadMsg;
//        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        contentSelectionIntent.setType("image/*");
//
//        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
//        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
//        chooserIntent.putExtra(Intent.EXTRA_TITLE, "文件选择");
//
//        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
//    }
//
//	// 跳转回发现
//	public void jumpFindView() {
//		Intent startIntent = null;
//		String token = ContactAccessor.getAuth(mContext).getSession();
//		String endpointId = ApplicationUtils.getUUID(mContext);
//		String url = GlobalAPIURLs.FIND_ACTIVITIES_URL + "token=" + token
//				+ "&endpointId=" + endpointId;
//		startIntent = new Intent(mContext, BrowserActivity.class);
//		startIntent.putExtra("url", url);
//		startIntent.putExtra("title", "发现");
//		mContext.startActivity(startIntent);
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case R.id.browser_back:
//			if (mWebView.canGoBack()) {
//				mWebView.goBack();
//			}
//
//			break;
//
//		case R.id.browser_forword:
//			if (mWebView.canGoForward()) {
//				mWebView.goForward();
//			}
//
//			break;
//		case R.id.browser_refresh:
//
//			if (mInLoad) {
//				stopLoading();
//			} else {
//			}
//			mWebView.reload();
//			break;
//
//		case R.id.browser_ad_refresh:
//
//			if (mInLoad) {
//				stopLoading();
//			} else {
//			}
//			mWebView.reload();
//			break;
//
//		case R.id.browser_ad_find:
//
//			jumpFindView();
//			break;
//		case R.id.iab_back_area:
//			  if(getIntent().getIntExtra("type",-1) == ConversationListFragment.NEWS_DETAIL){
//	            	if(!ApplicationUtils.isNetworkAvailable(BrowserActivity.this)){
//	            		BaseToast.makeText(BrowserActivity.this, "网络不给力,请检查网络设置", 1000).show();
//	            		finish();
//	            		}else{
//	            			mWebView.loadUrl(MainSP.getNewsUrl(getApplicationContext()));
//	            			AspMobclickAgent.onEvent(mContext, "sms_new_list");
//	            			}
//				}else if (mWebView.getUrl()!=null && !mWebView.getUrl().equals(MainSP.getNewsUrl(mContext))) {
//					if (mWebView.getUrl().contains("toutiao")) {
//						mWebView.goBack();
//						AspMobclickAgent.onEvent(mContext, "toutiao_new_list");
//					}
//					else if (type == 1) {
//						Intent intent2 = createBackIntent(getIntent());
//						startActivity(intent2);
//						finish();
//					}else {
//						onBackPressed();
//					}
//				}else if(!ApplicationUtils.isNetworkAvailable(BrowserActivity.this)){
//					finish();
//				}
//
//
//			break;
//		case R.id.iv_back_area:
//			finish();
//			break;
//		}
//	}
//
//	private boolean hasHoneycomb() {
//		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
//	}
//
//	private boolean hasJellyBeanMR1() {
//		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
//	}
//
//	@SuppressLint("NewApi")
//	private void removeSearchBoxImpl() {
//		// remove searchBoxJavaBridge_
//		try {
//			if (hasHoneycomb() && !hasJellyBeanMR1()) {
//				mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
//			}
//		} catch (Exception e) {
//		}
//		// remove accessibility,accessibilityTraversal
//		try {
//			mWebView.removeJavascriptInterface("accessibility");
//			mWebView.removeJavascriptInterface("accessibilityTraversal");
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * 打开APP
//	 * @deprecated
//	 * @param packageName
//	 *            应用包名
//	 */
//	private void openApp(String packagename) {
//		// TODO Auto-generated method stub
//		PackageManager pManager = getPackageManager();
//		PackageInfo pInfo = null;
//		try {
//			pInfo = pManager.getPackageInfo(packagename, 0);
//
//			Intent resolveIntent = new Intent();
//			resolveIntent.setAction(Intent.ACTION_MAIN);
//			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//			resolveIntent.setPackage(pInfo.packageName);
//
//			List<ResolveInfo> apps = pManager.queryIntentActivities(
//					resolveIntent, 0);
//			if (apps.size() > 0) {
//				ResolveInfo ri = apps.iterator().next();
//				if (ri != null) {
//					String packageName = ri.activityInfo.packageName;
//					String className = ri.activityInfo.name;
//
//					Intent intent = new Intent(Intent.ACTION_MAIN);
//					intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//					ComponentName cn = new ComponentName(packageName, className);
//
//					intent.setComponent(cn);
//					startActivity(intent);
//				}
//			}
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			Log.v("openAPP()", "openAPP fail.");
//			return;
//		}
//	}
//
//	private boolean isGeolocationOK() {
//		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		boolean gpsProviderOK = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//		boolean networkProviderOK = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//		boolean geolocationOK = gpsProviderOK || networkProviderOK;
//		if (CommonConstants.DEBUG)
//			Log.i("aspire", "gpsProviderOK = " + gpsProviderOK + "; networkProviderOK = " + networkProviderOK
//					+ "; geoLocationOK=" + geolocationOK);
//		return geolocationOK;
//	}
//}
