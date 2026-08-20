package com.aadil;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Launcher extends Service {
	
	private Menu menu;
	
	private WindowManager windowManager;
	private View connectView;
	
	private final Handler handler = new Handler();
	
	private boolean destroyed = false;
	
	native String Aadil();
	
	native String Aadil2();
	
	/*
	* Вспомогательный метод для перевода DP в PX
	*/
	private int dp(int value) {
		return (int) TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		value,
		getResources().getDisplayMetrics()
		);
	}
	
	private void startForegroundIfNeeded() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			String channelId = "aadil_menu_channel";
			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			NotificationChannel channel = new NotificationChannel(channelId, "Mod Menu Service", NotificationManager.IMPORTANCE_MIN);
			channel.setShowBadge(false);
			manager.createNotificationChannel(channel);
			
			Notification notification = new Notification.Builder(this, channelId)
			.setContentTitle("Aadil Mod Menu")
			.setContentText("Running")
			.setSmallIcon(android.R.drawable.ic_menu_manage)
			.setPriority(Notification.PRIORITY_MIN)
			.setOngoing(true)
			.build();
			
			startForeground(1, notification);
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		destroyed = false;
		
		startForegroundIfNeeded();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
		!Settings.canDrawOverlays(this)) {
			return;
		}
		
		showConnectWindow();
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				if (destroyed) {
					return;
				}
				
				Thread();
				
				handler.postDelayed(this, 1000);
			}
		}, 1000);
	}
	
	private void showConnectWindow() {
		
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		if (windowManager == null) return;
		
		final int purple = Color.parseColor("#BB00FF");
		final int white = Color.WHITE;
		
		/*
		* MAIN CONTAINER
		*/
		LinearLayout mainLayout = new LinearLayout(this);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setGravity(Gravity.CENTER);
		mainLayout.setPadding(dp(2), dp(2), dp(2), dp(2));
		
		GradientDrawable mainBackground = new GradientDrawable();
		mainBackground.setColor(purple);
		mainBackground.setStroke(dp(2), white);
		mainLayout.setBackground(mainBackground);
		
		/*
		* MESSAGE SECTION
		*/
		LinearLayout messageLayout = new LinearLayout(this);
		messageLayout.setOrientation(LinearLayout.HORIZONTAL);
		messageLayout.setGravity(Gravity.CENTER_VERTICAL);
		messageLayout.setPadding(dp(10), dp(10), dp(10), dp(10));
		
		final ProgressBar progressBar = new ProgressBar(this);
		progressBar.setIndeterminate(true);
		if (progressBar.getIndeterminateDrawable() != null) {
			progressBar.getIndeterminateDrawable().setColorFilter(white, PorterDuff.Mode.SRC_ATOP);
		}
		progressBar.setVisibility(View.GONE);
		
		final TextView textView = new TextView(this);
		textView.setText("Click on \"CONNECT\" button to connect the app to the Server.");
		textView.setTextSize(17.0f);
		textView.setTypeface(Typeface.DEFAULT_BOLD);
		textView.setTextColor(white);
		textView.setShadowLayer(7.0f, 0.0f, 0.0f, purple);
		
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
		0,
		LinearLayout.LayoutParams.WRAP_CONTENT,
		1.0f
		);
		textView.setLayoutParams(textParams);
		
		/*
		* BUTTONS CONTAINER (RelativeLayout как в исходном коде)
		*/
		RelativeLayout buttonsLayout = new RelativeLayout(this);
		buttonsLayout.setPadding(0, dp(4), 0, 0);
		
		/*
		* CLOSE BUTTON
		*/
		final Button closeButton = new Button(this);
		RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(
		dp(135),
		dp(48)
		);
		closeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		closeButton.setLayoutParams(closeParams);
		closeButton.setText("CLOSE");
		closeButton.setTextSize(16.0f);
		closeButton.setAllCaps(true);
		closeButton.setTypeface(Typeface.DEFAULT_BOLD);
		closeButton.setTextColor(white);
		
		GradientDrawable closeBackground = new GradientDrawable();
		closeBackground.setColor(purple);
		closeBackground.setStroke(dp(2), white);
		// Скругляем только верхний правый угол
		closeBackground.setCornerRadii(new float[]{
			0, 0,
			dp(35), dp(35),
			0, 0,
			0, 0
		});
		closeButton.setBackground(closeBackground);
		
		closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeConnectWindow();
				stopSelf();
				Process.killProcess(Process.myPid());
			}
		});
		
		/*
		* CONNECT BUTTON
		*/
		final Button connectButton = new Button(this);
		RelativeLayout.LayoutParams connectParams = new RelativeLayout.LayoutParams(
		dp(135),
		dp(48)
		);
		connectParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		connectButton.setLayoutParams(connectParams);
		connectButton.setText("CONNECT");
		connectButton.setTextSize(16.0f);
		connectButton.setAllCaps(true);
		connectButton.setTypeface(Typeface.DEFAULT_BOLD);
		connectButton.setTextColor(white);
		
		GradientDrawable connectBackground = new GradientDrawable();
		connectBackground.setColor(purple);
		connectBackground.setStroke(dp(2), white);
		// Скругляем только верхний левый угол
		connectBackground.setCornerRadii(new float[]{
			dp(35), dp(35),
			0, 0,
			0, 0,
			0, 0
		});
		connectButton.setBackground(connectBackground);
		
		/*
		* WEBVIEW LOGIC
		*/
		final WebView webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if (title == null) return;
				
				String expectedTitle = null;
				try {
					expectedTitle = Aadil2();
				} catch (Throwable ignored) {}
				
				if (expectedTitle != null && title.contains(expectedTitle)) {
					try {
						menu = new Menu(getApplicationContext());
						menu.SetWindowManagerWindowService();
						menu.ShowMenu();
						removeConnectWindow();
						} catch (Throwable e) {
						e.printStackTrace();
					}
					return;
				}
				
				progressBar.setVisibility(View.GONE);
				textView.setText("It seems like You are offline or the owner of this App has disabled it due to some reason. Check Your internet connection or Try Again later.");
			}
		});
		
		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				progressBar.setVisibility(View.VISIBLE);
				textView.setText("Connecting to the Server...");
				textView.setPadding(dp(10), 0, 0, 0);
				connectButton.setText("Try Again");
				
				try {
					String url = Aadil();
					if (url == null || url.length() == 0) {
						progressBar.setVisibility(View.GONE);
						textView.setText("Unable to connect to the Server.");
						return;
					}
					webView.loadUrl(url);
					} catch (Throwable e) {
					progressBar.setVisibility(View.GONE);
					textView.setText("Unable to connect to the Server.");
				}
			}
		});
		
		messageLayout.addView(progressBar);
		messageLayout.addView(textView);
		
		buttonsLayout.addView(closeButton);
		buttonsLayout.addView(connectButton);
		
		mainLayout.addView(messageLayout);
		mainLayout.addView(buttonsLayout);
		
		/*
		* WINDOW MANAGER PARAMS
		*/
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
			} else {
			params.type = WindowManager.LayoutParams.TYPE_PHONE;
		}
		
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		params.format = -3;
		params.gravity = Gravity.CENTER;
		
		connectView = mainLayout;
		
		try {
			windowManager.addView(connectView, params);
			} catch (Throwable e) {
			connectView = null;
			e.printStackTrace();
		}
	}
	
	private void removeConnectWindow() {
		if (connectView == null || windowManager == null) {
			return;
		}
		
		try {
			windowManager.removeView(connectView);
			} catch (Throwable ignored) {
		}
		
		connectView = null;
	}
	
	private boolean isNotInGame() {
		ActivityManager.RunningAppProcessInfo info = new ActivityManager.RunningAppProcessInfo();
		ActivityManager.getMyMemoryState(info);
		
		return info.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
	}
	
	private void Thread() {
		if (menu == null) {
			return;
		}
		
		try {
			if (isNotInGame()) {
				menu.setVisibility(View.VISIBLE);
			}
			} catch (Throwable ignored) {
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		destroyed = true;
		
		handler.removeCallbacksAndMessages(null);
		
		removeConnectWindow();
		
		if (menu != null) {
			try {
				menu.onDestroy();
				} catch (Throwable ignored) {
			}
			
			menu = null;
		}
		
		super.onDestroy();
	}
	
	@Override
	public void onTaskRemoved(Intent intent) {
		stopSelf();
		super.onTaskRemoved(intent);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_NOT_STICKY;
	}
}