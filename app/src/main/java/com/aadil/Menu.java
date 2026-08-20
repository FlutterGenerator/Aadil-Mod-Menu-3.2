package com.aadil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Menu {
	
	public static final String TAG = "Mod_Menu";
	
	private Context getContext;
	
	private LinearLayout mCollapse;
	private RelativeLayout mCollapsed;
	private LinearLayout mExpanded;
	private RelativeLayout mRootContainer;
	private LinearLayout mSettings;
	private WindowManager mWindowManager;
	private LinearLayout mods;
	private boolean overlayRequired;
	private FrameLayout rootFrame;
	
	private LinearLayout.LayoutParams scrlLL;
	private LinearLayout.LayoutParams scrlLLExpanded;
	
	private ScrollView scrollView;
	private ImageView startimage;
	
	private boolean stopChecking;
	
	private WindowManager.LayoutParams vmParams;
	
	private int TEXT_COLOR = Color.parseColor("#FFFFFF");
	private int TEXT_COLOR_2 = Color.parseColor("#BB00FF");
	private int TEXT_COLOR_3 = Color.parseColor("#BB00FF");
	private int TEXT_COLOR_4 = Color.parseColor("#FFFFFF");
	
	private int BTN_COLOR = Color.parseColor("#1C262D");
	
	private int MENU_BG_COLOR = Color.parseColor("#00000000");
	private int MENU_FEATURE_BG_COLOR = Color.parseColor("#00000000");
	
	private int MENU_WIDTH = 240;
	private int MENU_HEIGHT = 330;
	
	private int POS_X = 0;
	private int POS_Y = 100;
	
	private float MENU_CORNER = 4.0f;
	
	private int ICON_SIZE = 45;
	private float ICON_ALPHA = 0.9f;
	
	private int ToggleThumbON = Color.parseColor("#BB00FF");
	private int ToggleThumbOFF = Color.parseColor("#FFFFFF");
	
	private int ToggleTrackON = Color.parseColor("#BB00FF");
	private int ToggleTrackOFF = Color.parseColor("#FFFFFF");
	
	private int ToggleON = -16711936;
	private int ToggleOFF = -65536;
	
	private int BtnON = Color.parseColor("#1b5e20");
	private int BtnOFF = Color.parseColor("#7f0000");
	
	private int CategoryBG = Color.parseColor("#BB00FF");
	
	private int SeekBarColor = Color.parseColor("#FFFFFF");
	private int SeekBarProgressColor = Color.parseColor("#BB00FF");
	
	private int CheckBoxColor = Color.parseColor("#FFFFFF");
	private int RadioColor = Color.parseColor("#FFFFFF");
	
	private String NumberTxtColor = "#aa00ff";
	
	/*
	* Native methods
	*/
	
	native String AirJumpHeight3();
	native String CompleteAchievement3();
	native String FollowCamera3();
	native String[] GetFeatureList();
	native String God1_3();
	native String God2_3();
	native String God3_3();
	native String Icon();
	native String IconWebViewData();
	native void Init(Context context, TextView textView, TextView textView2);
	native String InstantLane3();
	native boolean IsGameLibLoaded();
	native String JumpHeight3();
	native String JumpLimit3();
	native String NoBoun3();
	native String NoGravity3();
	native String Score3();
	native String[] SettingsList();
	native String Speed1_3();
	native String Speed2_3();
	native String StopCamera3();
	native String StopTrain3();
	native String UnlimitedAll3();
	
	/*
	* Constructor
	*/
	
	public Menu(Context context) {
		
		this.getContext = context;
		
		Preferences.context = context;
		
		rootFrame = new FrameLayout(context);
		rootFrame.setOnTouchListener(onTouchListener());
		
		mRootContainer = new RelativeLayout(context);
		
		mCollapsed = new RelativeLayout(context);
		mCollapsed.setVisibility(View.VISIBLE);
		mCollapsed.setAlpha(ICON_ALPHA);
		
		mExpanded = new LinearLayout(context);
		mExpanded.setVisibility(View.GONE);
		mExpanded.setBackgroundColor(MENU_BG_COLOR);
		mExpanded.setOrientation(LinearLayout.VERTICAL);
		
		mExpanded.setLayoutParams(
		new LinearLayout.LayoutParams(
		dp(MENU_WIDTH),
		dp(MENU_HEIGHT)
		)
		);
		
		/*
		* Collapse container
		*/
		
		mCollapse = new LinearLayout(context);
		mCollapse.setOrientation(LinearLayout.VERTICAL);
		mCollapse.setLayoutParams(
		new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.WRAP_CONTENT,
		ViewGroup.LayoutParams.WRAP_CONTENT
		)
		);
		
		try {
			
			mCollapse.setBackground(
			Drawable.createFromStream(
			context.getAssets().open("aadil/gamer.aadil"),
			null
			)
			);
			
			mExpanded.setBackground(
			Drawable.createFromStream(
			context.getAssets().open("aadil/gamer.aadil"),
			null
			)
			);
			
			/*
			* Floating icon
			*/
			
			startimage = new ImageView(context);
			
			RelativeLayout.LayoutParams imageParams =
			new RelativeLayout.LayoutParams(
			dp(ICON_SIZE),
			dp(ICON_SIZE)
			);
			
			imageParams.topMargin = convertDipToPixels(10);
			
			startimage.setLayoutParams(imageParams);
			startimage.setScaleType(ImageView.ScaleType.FIT_XY);
			
			String iconData = Icon();
			
			if (iconData != null && iconData.length() > 0) {
				
				byte[] decoded =
				Base64.decode(iconData, Base64.DEFAULT);
				
				startimage.setImageBitmap(
				BitmapFactory.decodeByteArray(
				decoded,
				0,
				decoded.length
				)
				);
			}
			
			startimage.setOnTouchListener(onTouchListener());
			
			startimage.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					
					mCollapsed.setVisibility(View.GONE);
					mExpanded.setVisibility(View.VISIBLE);
				}
			}
			);
			
			/*
			* WebView icon
			*/
			
			WebView webView = new WebView(context);
			
			RelativeLayout.LayoutParams webParams =
			new RelativeLayout.LayoutParams(
			dp(ICON_SIZE),
			dp(ICON_SIZE)
			);
			
			webView.setLayoutParams(webParams);
			
			String webIcon = IconWebViewData();
			
			if (webIcon != null) {
				
				String html =
				"<html>" +
				"<head></head>" +
				"<body style=\"margin:0;padding:0\">" +
				"<img src=\"" +
				webIcon +
				"\" width=\"" +
				ICON_SIZE +
				"\" height=\"" +
				ICON_SIZE +
				"\">" +
				"</body>" +
				"</html>";
				
				webView.loadData(
				html,
				"text/html",
				"utf-8"
				);
			}
			
			webView.setBackgroundColor(Color.TRANSPARENT);
			webView.setAlpha(ICON_ALPHA);
			
			webView.setOnTouchListener(onTouchListener());
			
			/*
			* Settings
			*/
			
			mSettings = new LinearLayout(context);
			mSettings.setOrientation(LinearLayout.VERTICAL);
			
			featureList(
			SettingsList(),
			mSettings
			);
			
			/*
			* Header
			*/
			
			RelativeLayout header = new RelativeLayout(context);
			
			header.setPadding(
			10,
			5,
			10,
			5
			);
			
			header.setVerticalGravity(GravityCompat.CENTER_VERTICAL);
			
			TextView title = new TextView(context);
			
			title.setTextColor(TEXT_COLOR);
			title.setTextSize(19.0f);
			
			title.setTypeface(
			Typeface.createFromAsset(
			context.getAssets(),
			"aadil/aadil.mods"
			)
			);
			
			title.setShadowLayer(
			7.0f,
			0.0f,
			0.0f,
			TEXT_COLOR_2
			);
			
			title.setGravity(GravityCompat.CENTER);
			
			RelativeLayout.LayoutParams titleParams =
			new RelativeLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
			);
			
			titleParams.addRule(
			RelativeLayout.CENTER_HORIZONTAL
			);
			
			title.setLayoutParams(titleParams);
			
			/*
			* Subtitle
			*/
			
			TextView subtitle = new TextView(context);
			
			subtitle.setEllipsize(
			TextUtils.TruncateAt.MARQUEE
			);
			
			subtitle.setMarqueeRepeatLimit(-1);
			subtitle.setSingleLine(true);
			subtitle.setSelected(true);
			
			subtitle.setTextColor(TEXT_COLOR);
			
			subtitle.setShadowLayer(
			7.0f,
			0.0f,
			0.0f,
			TEXT_COLOR_2
			);
			
			subtitle.setTextSize(10.0f);
			
			subtitle.setGravity(
			GravityCompat.CENTER
			);
			
			subtitle.setPadding(
			0,
			0,
			0,
			5
			);
			
			/*
			* ScrollView
			*/
			
			scrollView = new ScrollView(context);
			
			scrlLL = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
			);
			
			scrlLLExpanded = new LinearLayout.LayoutParams(
			mExpanded.getLayoutParams()
			);
			
			scrlLLExpanded.weight = 1.0f;
			
			scrollView.setLayoutParams(scrlLLExpanded);
			scrollView.setBackgroundColor(MENU_FEATURE_BG_COLOR);
			
			/*
			* Mods container
			*/
			
			mods = new LinearLayout(context);
			mods.setOrientation(LinearLayout.VERTICAL);
			
			/*
			* Bottom buttons
			*/
			
			RelativeLayout bottomLayout =
			new RelativeLayout(context);
			
			bottomLayout.setVerticalGravity(
			GravityCompat.CENTER_VERTICAL
			);
			
			/*
			* Settings button
			*/
			
			RelativeLayout.LayoutParams settingsParams =
			new RelativeLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
			);
			
			settingsParams.addRule(
			RelativeLayout.ALIGN_PARENT_LEFT
			);
			
			final Button settingsButton =
			new Button(context);
			
			settingsButton.setLayoutParams(settingsParams);
			settingsButton.setText("SETTING");
			settingsButton.setTextColor(TEXT_COLOR_4);
			settingsButton.setTypeface(Typeface.DEFAULT_BOLD);
			
			GradientDrawable settingsBackground =
			new GradientDrawable();
			
			settingsBackground.setColor(TEXT_COLOR_3);
			
			settingsBackground.setCornerRadii(
			new float[]{
				0, 0,
				60, 60,
				0, 0,
				0, 0
			}
			);
			
			settingsBackground.setStroke(
			3,
			TEXT_COLOR_4
			);
			
			settingsButton.setBackground(
			settingsBackground
			);
			
			settingsButton.setOnClickListener(
			new View.OnClickListener() {
				
				private boolean settingsOpen = false;
				
				@Override
				public void onClick(View view) {
					
					settingsOpen = !settingsOpen;
					
					try {
						
						if (settingsOpen) {
							
							settingsButton.setText("CLOSE");
							
							scrollView.removeAllViews();
							
							scrollView.addView(
							mSettings
							);
							
							scrollView.scrollTo(
							0,
							0
							);
							
							} else {
							
							settingsButton.setText("SETTING");
							
							scrollView.removeAllViews();
							
							scrollView.addView(
							mods
							);
						}
						
						} catch (Exception ignored) {
					}
				}
			}
			);
			
			/*
			* Minimize button
			*/
			
			RelativeLayout.LayoutParams minimizeParams =
			new RelativeLayout.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT,
			ViewGroup.LayoutParams.WRAP_CONTENT
			);
			
			minimizeParams.addRule(
			RelativeLayout.ALIGN_PARENT_RIGHT
			);
			
			Button minimizeButton =
			new Button(context);
			
			minimizeButton.setLayoutParams(
			minimizeParams
			);
			
			minimizeButton.setBackgroundColor(
			Color.TRANSPARENT
			);
			
			minimizeButton.setText("MINIMIZE");
			minimizeButton.setTextColor(TEXT_COLOR_4);
			minimizeButton.setTypeface(
			Typeface.DEFAULT_BOLD
			);
			
			GradientDrawable minimizeBackground =
			new GradientDrawable();
			
			minimizeBackground.setColor(
			TEXT_COLOR_3
			);
			
			minimizeBackground.setCornerRadii(
			new float[]{
				60, 60,
				0, 0,
				0, 0,
				0, 0
			}
			);
			
			minimizeBackground.setStroke(
			3,
			TEXT_COLOR_4
			);
			
			minimizeButton.setBackground(
			minimizeBackground
			);
			
			minimizeButton.setOnClickListener(
			new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					mCollapsed.setVisibility(
					View.VISIBLE
					);
					
					mCollapsed.setAlpha(
					ICON_ALPHA
					);
					
					mExpanded.setVisibility(
					View.GONE
					);
				}
			}
			);
			
			/*
			* Add views
			*/
			
			mRootContainer.addView(mCollapsed);
			mRootContainer.addView(mExpanded);
			
			if (webIcon != null &&
			webIcon.length() > 0) {
				
				mCollapsed.addView(webView);
				
				} else {
				
				mCollapsed.addView(startimage);
			}
			
			header.addView(title);
			
			LinearLayout mainLayout =
			new LinearLayout(context);
			
			mainLayout.setOrientation(
			LinearLayout.VERTICAL
			);
			
			mainLayout.setLayoutParams(
			new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT
			)
			);
			
			mainLayout.addView(header);
			mainLayout.addView(subtitle);
			
			scrollView.addView(mods);
			
			mainLayout.addView(scrollView);
			
			bottomLayout.addView(settingsButton);
			bottomLayout.addView(minimizeButton);
			
			mainLayout.addView(bottomLayout);
			
			mExpanded.addView(mainLayout);
			
			Init(
			context,
			title,
			subtitle
			);
			
			} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	/*
	* Show menu
	*/
	
	public void ShowMenu() {
		
		rootFrame.addView(mRootContainer);
		
		final Handler handler = new Handler();
		
		handler.postDelayed(
		new Runnable() {
			
			private boolean viewLoaded = false;
			
			@Override
			public void run() {
				
				try {
					
					if (Preferences.loadPref &&
					!IsGameLibLoaded() &&
					!stopChecking) {
						
						if (!viewLoaded) {
							
							Category(
							mods,
							"Save preferences was been enabled. " +
							"Waiting for game lib to be loaded...\n\n" +
							"Force load menu may not apply mods instantly. " +
							"You would need to reactivate them again"
							);
							
							Button(
							mods,
							-100,
							"Force load menu"
							);
							
							viewLoaded = true;
						}
						
						handler.postDelayed(
						this,
						600
						);
						
						return;
					}
					
					mods.removeAllViews();
					
					featureList(
					GetFeatureList(),
					mods
					);
					
					} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		},
		500
		);
	}
	
	/*
	* Window manager for service / overlay
	*/
	
	@SuppressLint("WrongConstant")
	public void SetWindowManagerWindowService() {
		
		int windowType;
		
		if (Build.VERSION.SDK_INT >= 26) {
			
			windowType =
			WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
			
			} else {
			
			windowType =
			WindowManager.LayoutParams.TYPE_PHONE;
		}
		
		vmParams =
		new WindowManager.LayoutParams(
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.WRAP_CONTENT,
		windowType,
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		PixelFormat.TRANSLUCENT
		);
		
		vmParams.gravity =
		GravityCompat.TOP |
		GravityCompat.LEFT;
		
		vmParams.x = POS_X;
		vmParams.y = POS_Y;
		
		mWindowManager =
		(WindowManager)
		getContext.getSystemService(
		Context.WINDOW_SERVICE
		);
		
		mWindowManager.addView(
		rootFrame,
		vmParams
		);
		
		overlayRequired = true;
	}
	
	/*
	* Window manager for Activity
	*/
	
	@SuppressLint("WrongConstant")
	public void SetWindowManagerActivity() {
		
		vmParams =
		new WindowManager.LayoutParams(
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.WRAP_CONTENT,
		WindowManager.LayoutParams.TYPE_APPLICATION,
		WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
		PixelFormat.TRANSLUCENT
		);
		
		vmParams.gravity =
		GravityCompat.TOP |
		GravityCompat.LEFT;
		
		vmParams.x = POS_X;
		vmParams.y = POS_Y;
		
		mWindowManager =
		((Activity) getContext)
		.getWindowManager();
		
		mWindowManager.addView(
		rootFrame,
		vmParams
		);
	}
	
	/*
	* Touch listener
	*/
	
	private View.OnTouchListener onTouchListener() {
		
		return new View.OnTouchListener() {
			
			private float initialTouchX;
			private float initialTouchY;
			
			private int initialX;
			private int initialY;
			
			@Override
			public boolean onTouch(
			View view,
			MotionEvent event
			) {
				
				switch (event.getAction()) {
					
					case MotionEvent.ACTION_DOWN:
					
					if (vmParams == null) {
						return true;
					}
					
					initialX = vmParams.x;
					initialY = vmParams.y;
					
					initialTouchX =
					event.getRawX();
					
					initialTouchY =
					event.getRawY();
					
					return true;
					
					case MotionEvent.ACTION_UP:
					
					int rawX =
					(int) (
					event.getRawX()
					- initialTouchX
					);
					
					int rawY =
					(int) (
					event.getRawY()
					- initialTouchY
					);
					
					mExpanded.setAlpha(1.0f);
					mCollapsed.setAlpha(1.0f);
					
					if (rawX >= 10 ||
					rawY >= 10 ||
					!isViewCollapsed()) {
						
						return true;
					}
					
					try {
						
						mCollapsed.setVisibility(
						View.GONE
						);
						
						mExpanded.setVisibility(
						View.VISIBLE
						);
						
						} catch (Exception ignored) {
					}
					
					return true;
					
					case MotionEvent.ACTION_MOVE:
					
					if (vmParams == null ||
					mWindowManager == null) {
						
						return true;
					}
					
					mExpanded.setAlpha(0.5f);
					mCollapsed.setAlpha(0.5f);
					
					vmParams.x =
					initialX +
					(int) (
					event.getRawX()
					- initialTouchX
					);
					
					vmParams.y =
					initialY +
					(int) (
					event.getRawY()
					- initialTouchY
					);
					
					try {
						
						mWindowManager.updateViewLayout(
						rootFrame,
						vmParams
						);
						
						} catch (Exception ignored) {
					}
					
					return true;
					
					default:
					return false;
				}
			}
		};
	}
	
	/*
	* Feature list
	*/
	
	private void featureList(
	String[] features,
	LinearLayout layout
	) {
		
		if (features == null) {
			return;
		}
		
		int offset = 0;
		
		for (int index = 0;
		index < features.length;
		index++) {
			
			String feature = features[index];
			
			if (feature == null) {
				continue;
			}
			
			boolean enabled =
			feature.contains("_True");
			
			if (enabled) {
				
				feature =
				feature.replaceFirst(
				"_True",
				""
				);
			}
			
			LinearLayout targetLayout;
			
			if (feature.contains("CollapseAdd_")) {
				
				targetLayout = mCollapse;
				
				feature =
				feature.replaceFirst(
				"CollapseAdd_",
				""
				);
				
				} else {
				
				targetLayout = layout;
			}
			
			String[] firstSplit =
			feature.split("_");
			
			int featureNumber;
			
			if (firstSplit.length > 0 &&
			(
			TextUtils.isDigitsOnly(
			firstSplit[0]
			)
			||
			firstSplit[0].matches(
			"-[0-9]*"
			)
			)) {
				
				featureNumber =
				Integer.parseInt(
				firstSplit[0]
				);
				
				feature =
				feature.replaceFirst(
				firstSplit[0] + "_",
				""
				);
				
				offset++;
				
				} else {
				
				featureNumber =
				index - offset;
			}
			
			String[] parts =
			feature.split("_");
			
			if (parts.length == 0) {
				continue;
			}
			
			String type = parts[0];
			
			if ("Toggle".equals(type)) {
				
				if (parts.length >= 2) {
					
					Switch(
					targetLayout,
					featureNumber,
					parts[1],
					enabled
					);
				}
				
				} else if ("SeekBar".equals(type)) {
				
				if (parts.length >= 4) {
					
					try {
						
						SeekBar(
						targetLayout,
						featureNumber,
						parts[1],
						Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3])
						);
						
						} catch (NumberFormatException ignored) {
					}
				}
				
				} else if ("Button".equals(type)) {
				
				if (parts.length >= 2) {
					
					Button(
					targetLayout,
					featureNumber,
					parts[1]
					);
				}
				
				} else if ("Category".equals(type)) {
				
				if (parts.length >= 2) {
					
					Category(
					targetLayout,
					parts[1]
					);
				}
			}
		}
	}
	
	/*
	* Switch
	*/
	
	private void Switch(
	LinearLayout layout,
	final int featureNumber,
	final String featureName,
	boolean defaultValue
	) {
		
		final Switch switchView =
		new Switch(getContext);
		
		switchView.setText(featureName);
		switchView.setTextColor(TEXT_COLOR_4);
		
		switchView.setShadowLayer(
		7.0f,
		0.0f,
		0.0f,
		TEXT_COLOR_3
		);
		
		switchView.setPadding(
		10,
		10,
		5,
		10
		);
		
		final GradientDrawable track =
		new GradientDrawable();
		
		track.setSize(
		dp(45),
		dp(20)
		);
		
		track.setCornerRadius(
		40.0f
		);
		
		final GradientDrawable thumb =
		new GradientDrawable();
		
		thumb.setSize(
		dp(20),
		dp(20)
		);
		
		thumb.setShape(
		GradientDrawable.OVAL
		);
		
		boolean checked =
		Preferences.loadPrefBool(
		featureName,
		featureNumber,
		defaultValue
		);
		
		updateSwitchDrawable(
		switchView,
		track,
		thumb,
		checked
		);
		
		switchView.setChecked(
		checked
		);
		
		switchView.setOnCheckedChangeListener(
		new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(
			CompoundButton buttonView,
			boolean checked
			) {
				
				Preferences.changeFeatureBool(
				featureName,
				featureNumber,
				checked
				);
				
				updateSwitchDrawable(
				switchView,
				track,
				thumb,
				checked
				);
				
				if (featureNumber == -1) {
					
					Preferences
					.with(
					switchView.getContext()
					)
					.writeBoolean(
					-1,
					checked
					);
					
					if (!checked) {
						
						Preferences
						.with(
						switchView.getContext()
						)
						.clear();
					}
				}
			}
		}
		);
		
		layout.addView(
		switchView
		);
	}
	
	private void updateSwitchDrawable(
	Switch switchView,
	GradientDrawable track,
	GradientDrawable thumb,
	boolean checked
	) {
		
		if (checked) {
			
			track.setColor(
			ToggleTrackON
			);
			
			track.setStroke(
			3,
			ToggleThumbOFF
			);
			
			thumb.setColor(
			ToggleThumbON
			);
			
			thumb.setStroke(
			3,
			ToggleThumbOFF
			);
			
			} else {
			
			track.setColor(
			ToggleTrackOFF
			);
			
			track.setStroke(
			3,
			ToggleThumbON
			);
			
			thumb.setColor(
			ToggleThumbOFF
			);
			
			thumb.setStroke(
			3,
			ToggleThumbON
			);
		}
		
		switchView.setTrackDrawable(
		track
		);
		
		switchView.setThumbDrawable(
		thumb
		);
	}
	
	/*
	* SeekBar
	*/
	
	private void SeekBar(
	LinearLayout layout,
	final int featureNumber,
	final String featureName,
	final int min,
	int max
	) {
		
		int savedValue =
		Preferences.loadPrefInt(
		featureName,
		featureNumber
		);
		
		LinearLayout container =
		new LinearLayout(getContext);
		
		container.setPadding(
		10,
		5,
		0,
		5
		);
		
		container.setOrientation(
		LinearLayout.VERTICAL
		);
		
		container.setGravity(
		GravityCompat.CENTER
		);
		
		final TextView textView =
		new TextView(getContext);
		
		if (savedValue == 0) {
			
			textView.setText(
			featureName +
			" -> DEFAULT"
			);
			
			} else {
			
			textView.setText(
			featureName +
			" -> (SAVED VALUE)"
			);
		}
		
		textView.setTextColor(
		TEXT_COLOR_4
		);
		
		textView.setShadowLayer(
		7.0f,
		0.0f,
		0.0f,
		TEXT_COLOR_3
		);
		
		final SeekBar seekBar =
		new SeekBar(getContext);
		
		seekBar.setPadding(
		25,
		10,
		35,
		10
		);
		
		seekBar.setMax(max);
		
		if (Build.VERSION.SDK_INT >= 26) {
			seekBar.setMin(min);
		}
		
		if (savedValue == 0) {
			savedValue = min;
		}
		
		seekBar.setProgress(
		savedValue
		);
		
		GradientDrawable thumb =
		new GradientDrawable();
		
		thumb.setColor(
		TEXT_COLOR_4
		);
		
		thumb.setSize(
		dp(20),
		dp(20)
		);
		
		thumb.setShape(
		GradientDrawable.OVAL
		);
		
		thumb.setStroke(
		5,
		TEXT_COLOR_3
		);
		
		seekBar.setThumb(
		thumb
		);
		
		if (seekBar.getProgressDrawable() != null) {
			
			seekBar
			.getProgressDrawable()
			.setColorFilter(
			SeekBarProgressColor,
			PorterDuff.Mode.SRC_ATOP
			);
		}
		
		seekBar.setOnSeekBarChangeListener(
		new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStartTrackingTouch(
			SeekBar seekBar
			) {
			}
			
			@Override
			public void onStopTrackingTouch(
			SeekBar seekBar
			) {
			}
			
			@Override
			public void onProgressChanged(
			SeekBar seekBar,
			int progress,
			boolean fromUser
			) {
				
				int value =
				progress < min
				? min
				: progress;
				
				seekBar.setProgress(
				value
				);
				
				Preferences.changeFeatureInt(
				featureName,
				featureNumber,
				value
				);
				
				if (progress == 1) {
					
					textView.setText(
					featureName +
					" -> DEFAULT"
					);
					
					} else {
					
					textView.setText(
					featureName +
					" -> " +
					value +
					"x"
					);
				}
				
				switch (featureNumber) {
					
					case -8:
					
					startimage
					.getLayoutParams()
					.height =
					progress * 5;
					
					startimage
					.getLayoutParams()
					.width =
					progress * 5;
					
					startimage.requestLayout();
					
					if (progress == 28) {
						
						textView.setText(
						featureName +
						" -> " +
						progress +
						" [DEFAULT]"
						);
					}
					
					break;
					
					case -7:
					
					ICON_ALPHA =
					progress / 10.0f;
					
					mCollapsed.setAlpha(
					ICON_ALPHA
					);
					
					if (progress <= 1) {
						
						textView.setText(
						featureName +
						" -> " +
						progress +
						"x"
						);
						
						} else if (progress == 9) {
						
						textView.setText(
						featureName +
						" -> " +
						progress +
						" [DEFAULT]"
						);
					}
					
					break;
					
					case 58:
					
					if (progress <= 0) {
						
						textView.setText(
						featureName +
						" -> DEFAULT"
						);
						
						} else if (progress == 1) {
						
						textView.setText(
						featureName +
						" -> 0x [FREEZE]"
						);
						
					} else if (
					progress > 1 &&
					progress <= 10
					) {
						
						textView.setText(
						featureName +
						" -> 0." +
						(progress - 1) +
						"x"
						);
						
						} else {
						
						textView.setText(
						featureName +
						" -> " +
						(progress - 10) +
						"x"
						);
					}
					
					break;
					
					case 69:
					
					if (progress <= 1) {
						
						textView.setText(
						featureName +
						" -> DEFAULT"
						);
						
						} else if (progress == 6) {
						
						textView.setText(
						featureName +
						" -> INFINITY"
						);
					}
					
					break;
					
					default:
					break;
				}
			}
		}
		);
		
		container.addView(
		textView
		);
		
		container.addView(
		seekBar
		);
		
		layout.addView(
		container
		);
	}
	
	/*
	* Button
	*/
	
	private void Button(
	LinearLayout layout,
	final int featureNumber,
	final String featureName
	) {
		
		Button button =
		new Button(getContext);
		
		LinearLayout.LayoutParams params =
		new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT,
		ViewGroup.LayoutParams.WRAP_CONTENT
		);
		
		params.setMargins(
		7,
		5,
		7,
		5
		);
		
		button.setLayoutParams(
		params
		);
		
		button.setTextColor(
		TEXT_COLOR_4
		);
		
		GradientDrawable background =
		new GradientDrawable();
		
		background.setColor(
		Color.parseColor(
		"#77BB00FF"
		)
		);
		
		background.setCornerRadii(
		new float[]{
			60, 60,
			0, 0,
			60, 60,
			0, 0
		}
		);
		
		background.setStroke(
		4,
		TEXT_COLOR_4,
		4.0f,
		4.0f
		);
		
		button.setBackground(
		background
		);
		
		button.setAllCaps(false);
		
		button.setText(
		Html.fromHtml(
		featureName
		)
		);
		
		button.setOnClickListener(
		new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				handleButtonClick(
				featureNumber,
				featureName
				);
			}
		}
		);
		
		layout.addView(
		button
		);
	}
	
	/*
	* Button actions
	*/
	
	private void handleButtonClick(
	int featureNumber,
	String featureName
	) {
		
		switch (featureNumber) {
			
			case -100:
			
			stopChecking = true;
			break;
			
			case -9:
			
			mCollapsed.setVisibility(
			View.VISIBLE
			);
			
			mCollapsed.setAlpha(
			0.0f
			);
			
			mExpanded.setVisibility(
			View.GONE
			);
			
			Toast.makeText(
			getContext.getApplicationContext(),
			"Icon hidden. Remember the hidden icon position",
			Toast.LENGTH_SHORT
			).show();
			
			break;
			
			case -6:
			
			scrollView.removeAllViews();
			
			scrollView.addView(
			mods
			);
			
			break;
			
			case 10:
			
			showSearchingDialog();
			break;
			
			case 20:
			
			showOffsetsDialog();
			break;
			
			default:
			break;
		}
		
		Preferences.changeFeatureInt(
		featureName,
		featureNumber,
		0
		);
	}
	
	/*
	* Searching dialog
	*/
	
	private void showSearchingDialog() {
		
		final ProgressDialog progress =
		new ProgressDialog(
		getContext
		);
		
		progress.setMessage(
		"Searching Mods..."
		);
		
		progress.setCancelable(false);
		
		if (Build.VERSION.SDK_INT >= 19 &&
		progress.getWindow() != null) {
			
			if (Build.VERSION.SDK_INT >= 26) {
				
				progress.getWindow().setType(
				WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
				);
				
				} else {
				
				progress.getWindow().setType(
				WindowManager.LayoutParams.TYPE_PHONE
				);
			}
		}
		
		progress.show();
		
		new Timer().schedule(
		new TimerTask() {
			
			@Override
			public void run() {
				
				try {
					
					progress.dismiss();
					
					} catch (Exception ignored) {
				}
			}
		},
		3000
		);
	}
	
	/*
	* Offset dialog
	*/
	
	private void showOffsetsDialog() {
		
		final AlertDialog dialog =
		new AlertDialog.Builder(
		getContext,
		AlertDialog.THEME_DEVICE_DEFAULT_DARK
		).create();
		
		if (Build.VERSION.SDK_INT >= 19 &&
		dialog.getWindow() != null) {
			
			if (Build.VERSION.SDK_INT >= 26) {
				
				dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
				);
				
				} else {
				
				dialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_PHONE
				);
			}
		}
		
		/*
		* Главный контейнер
		*/
		
		LinearLayout main =
		new LinearLayout(getContext);
		
		main.setOrientation(
		LinearLayout.VERTICAL
		);
		
		main.setGravity(
		GravityCompat.CENTER_HORIZONTAL
		);
		
		try {
			
			main.setBackground(
			Drawable.createFromStream(
			getContext
			.getAssets()
			.open("aadil/gamer.aadil"),
			null
			)
			);
			
			} catch (IOException ignored) {
		}
		
		main.setLayoutParams(
		new LinearLayout.LayoutParams(
		dp(320),
		dp(450)
		)
		);
		
		/*
		* TITLE
		*/
		
		TextView title =
		new TextView(getContext);
		
		title.setText(
		Html.fromHtml(
		"<u>Subway Surf Offsets<br>" +
		"by Gamer Aadil</u>"
		)
		);
		
		title.setTextColor(
		TEXT_COLOR_4
		);
		
		title.setTextSize(
		18.0f
		);
		
		title.setTypeface(
		Typeface.DEFAULT_BOLD
		);
		
		title.setGravity(
		GravityCompat.CENTER
		);
		
		title.setShadowLayer(
		7.0f,
		0.0f,
		0.0f,
		TEXT_COLOR_3
		);
		
		title.setPadding(
		5,
		4,
		5,
		4
		);
		
		title.setBackgroundColor(
		Color.parseColor("#80BB00FF")
		);
		
		LinearLayout.LayoutParams titleParams =
		new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT,
		dp(57)
		);
		
		title.setLayoutParams(
		titleParams
		);
		
		main.addView(
		title
		);
		
		/*
		* SCROLL
		*/
		
		ScrollView offsetScroll =
		new ScrollView(getContext);
		
		LinearLayout.LayoutParams scrollParams =
		new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT,
		0
		);
		
		scrollParams.weight = 1.0f;
		
		offsetScroll.setLayoutParams(
		scrollParams
		);
		
		offsetScroll.setBackgroundColor(
		Color.TRANSPARENT
		);
		
		final TextView offsetText =
		new TextView(getContext);
		
		String text =
		createOffsetText();
		
		offsetText.setText(
		Html.fromHtml(text)
		);
		
		offsetText.setTextColor(
		TEXT_COLOR_4
		);
		
		offsetText.setTextSize(
		14.0f
		);
		
		offsetText.setGravity(
		GravityCompat.LEFT
		);
		
		offsetText.setShadowLayer(
		7.0f,
		0.0f,
		0.0f,
		TEXT_COLOR_3
		);
		
		offsetText.setPadding(
		dp(6),
		dp(5),
		dp(6),
		dp(8)
		);
		
		offsetText.setTextIsSelectable(
		true
		);
		
		offsetScroll.addView(
		offsetText
		);
		
		main.addView(
		offsetScroll
		);
		
		/*
		* BOTTOM BUTTONS
		*/
		
		RelativeLayout bottom =
		new RelativeLayout(getContext);
		
		LinearLayout.LayoutParams bottomParams =
		new LinearLayout.LayoutParams(
		ViewGroup.LayoutParams.MATCH_PARENT,
		dp(52)
		);
		
		bottom.setLayoutParams(
		bottomParams
		);
		
		/*
		* CLOSE
		*/
		
		Button close =
		new Button(getContext);
		
		close.setText(
		"CLOSE"
		);
		
		close.setTextColor(
		TEXT_COLOR_4
		);
		
		close.setTextSize(
		14.0f
		);
		
		close.setTypeface(
		Typeface.DEFAULT_BOLD
		);
		
		close.setAllCaps(
		true
		);
		
		RelativeLayout.LayoutParams closeParams =
		new RelativeLayout.LayoutParams(
		dp(90),
		dp(52)
		);
		
		closeParams.addRule(
		RelativeLayout.ALIGN_PARENT_LEFT
		);
		
		close.setLayoutParams(
		closeParams
		);
		
		GradientDrawable closeBackground =
		new GradientDrawable();
		
		closeBackground.setColor(
		TEXT_COLOR_3
		);
		
		closeBackground.setCornerRadii(
		new float[]{
			0, 0,
				60, 60,
				0, 0,
				0, 0
		}
		);
		
		closeBackground.setStroke(
		2,
		TEXT_COLOR_4
		);
		
		close.setBackground(
		closeBackground
		);
		
		close.setOnClickListener(
		new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				dialog.dismiss();
			}
		}
		);
		
		/*
		* COPY
		*/
		
		Button copy =
		new Button(getContext);
		
		copy.setText(
		"COPY"
		);
		
		copy.setTextColor(
		TEXT_COLOR_4
		);
		
		copy.setTextSize(
		14.0f
		);
		
		copy.setTypeface(
		Typeface.DEFAULT_BOLD
		);
		
		copy.setAllCaps(
		true
		);
		
		RelativeLayout.LayoutParams copyParams =
		new RelativeLayout.LayoutParams(
		dp(90),
		dp(52)
		);
		
		copyParams.addRule(
		RelativeLayout.ALIGN_PARENT_RIGHT
		);
		
		copy.setLayoutParams(
		copyParams
		);
		
		GradientDrawable copyBackground =
		new GradientDrawable();
		
		copyBackground.setColor(
		TEXT_COLOR_3
		);
		
		copyBackground.setCornerRadii(
		new float[]{
			60, 60,
				0, 0,
				0, 0,
				0, 0
		}
		);
		
		copyBackground.setStroke(
		2,
		TEXT_COLOR_4
		);
		
		copy.setBackground(
		copyBackground
		);
		
		copy.setOnClickListener(
		new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				ClipboardManager clipboard =
				(ClipboardManager)
				getContext.getSystemService(
				Context.CLIPBOARD_SERVICE
				);
				
				if (clipboard != null) {
					
					clipboard.setPrimaryClip(
					ClipData.newPlainText(
					null,
					offsetText.getText()
					)
					);
				}
				
				dialog.dismiss();
				
				Toast.makeText(
				getContext,
				"Copied Text Successfully",
				Toast.LENGTH_SHORT
				).show();
			}
		}
		);
		
		bottom.addView(
		close
		);
		
		bottom.addView(
		copy
		);
		
		main.addView(
		bottom
		);
		
		dialog.setView(
		main
		);
		
		dialog.show();
		
		if (dialog.getWindow() != null) {
			
			dialog.getWindow().setLayout(
			dp(320),
			dp(450)
			);
			
			dialog.getWindow().setGravity(
			GravityCompat.CENTER
			);
			
			dialog.getWindow().setBackgroundDrawableResource(
			android.R.color.transparent
			);
		}
	}
	
	/*
	* Create offset information
	*/
	
	private String createOffsetText() {
		
		String versionName = "Unknown";
		int versionCode = 0;
		
		try {
			
			PackageInfo packageInfo =
			getContext
			.getPackageManager()
			.getPackageInfo(
			getContext.getPackageName(),
			0
			);
			
			versionName =
			packageInfo.versionName;
			
			if (Build.VERSION.SDK_INT >= 28) {
				
				versionCode =
				(int) packageInfo.getLongVersionCode();
				
				} else {
				
				versionCode =
				packageInfo.versionCode;
			}
			
			} catch (PackageManager.NameNotFoundException ignored) {
		}
		
		StringBuilder sb =
		new StringBuilder();
		
		sb.append(
		"Game Name -> Subway Surf<br>"
		);
		
		sb.append(
		"Version -> v"
		);
		
		sb.append(
		versionName
		);
		
		sb.append(
		" ("
		);
		
		sb.append(
		versionCode
		);
		
		sb.append(
		") [Arm-7]<br>"
		);
		
		sb.append(
		"Offsets by Gamer Aadil<br><br>"
		);
		
		sb.append(
		"1. Unlimited Everything " +
		"(Coins,Keys etc.)<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		UnlimitedAll3()
		);
		
		sb.append(
		"<br>Type -> INT<br>"
		);
		
		sb.append(
		"Replace -> 02 01 E0 E3 " +
		"1E FF 2F E1<br><br>"
		);
		
		sb.append(
		"2. No Gravity<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		NoGravity3()
		);
		
		sb.append(
		"<br>Type -> BOOL<br>"
		);
		
		sb.append(
		"Replace -> 00 00 E0 E3 " +
		"1E FF 2F E1<br><br>"
		);
		
		sb.append(
		"3. God Hack<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		God1_3()
		);
		
		sb.append(
		"<br>Offset -> 0x"
		);
		
		sb.append(
		God2_3()
		);
		
		sb.append(
		"<br>Type -> BOOL<br>"
		);
		
		sb.append(
		"Replace -> 00 00 E0 E3 " +
		"1E FF 2F E1<br><br>"
		);
		
		sb.append(
		"4. Jump Limit<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		JumpLimit3()
		);
		
		sb.append(
		"<br>Type -> INT<br>"
		);
		
		sb.append(
		"Replace -> FF 0A A0 E3 " +
		"1E FF 2F E1<br><br>"
		);
		
		sb.append(
		"5. Jump Height<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		JumpHeight3()
		);
		
		sb.append(
		"<br>Type -> FLOAT<br>"
		);
		
		sb.append(
		"Replace -> 07 04 43 E3 " +
		"1E FF 2F E1<br><br>"
		);
		
		sb.append(
		"6. Air Jump Height<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		AirJumpHeight3()
		);
		
		sb.append(
		"<br>Type -> FLOAT<br>"
		);
		
		sb.append(
		"Replace -> 07 04 43 E3 " +
		"1E FF 2F E1<br><br>"
		);
		
		sb.append(
		"7. Instant Lane Change<br>"
		);
		
		sb.append(
		"Offset -> 0x"
		);
		
		sb.append(
		InstantLane3()
		);
		
		sb.append(
		"<br>Type -> FLOAT<br>"
		);
		
		sb.append(
		"Replace -> 00 00 40 E3 " +
		"1E FF 2F E1"
		);
		
		return sb.toString();
	}
	
	/*
	* Category
	*/
	
	private void Category(
	LinearLayout layout,
	String text
	) {
		
		TextView category =
		new TextView(getContext);
		
		category.setBackgroundColor(
		CategoryBG
		);
		
		category.setText(
		Html.fromHtml(text)
		);
		
		category.setGravity(
		Gravity.CENTER
		);
		
		category.setTextColor(
		TEXT_COLOR_4
		);
		
		category.setShadowLayer(
		7.0f,
		0.0f,
		0.0f,
		TEXT_COLOR_3
		);
		
		category.setTypeface(
		null,
		Typeface.BOLD
		);
		
		category.setPadding(
		0,
		5,
		0,
		5
		);
		
		layout.addView(
		category
		);
	}
	
	/*
	* Check collapsed state
	*/
	
	private boolean isViewCollapsed() {
		
		return rootFrame == null ||
		mCollapsed.getVisibility()
		== View.VISIBLE;
	}
	
	/*
	* dp -> px
	*/
	
	private int convertDipToPixels(
	int dp
	) {
		
		return (int)
		(
		dp *
		getContext
		.getResources()
		.getDisplayMetrics()
		.density
		+ 0.5f
		);
	}
	
	private int dp(int value) {
		
		return (int)
		TypedValue.applyDimension(
		TypedValue.COMPLEX_UNIT_DIP,
		value,
		getContext
		.getResources()
		.getDisplayMetrics()
		);
	}
	
	/*
	* Visibility
	*/
	
	public void setVisibility(
	int visibility
	) {
		
		if (rootFrame != null) {
			
			rootFrame.setVisibility(
			visibility
			);
		}
	}
	
	/*
	* Destroy
	*/
	
	public void onDestroy() {
		
		try {
			
			if (rootFrame != null &&
			mWindowManager != null) {
				
				mWindowManager.removeView(
				rootFrame
				);
				
				rootFrame = null;
			}
			
			} catch (Exception ignored) {
		}
	}
	
	/*
	* Small compatibility class.
	*
	* Keeps the source compatible with old Android projects
	* without requiring AndroidX.
	*/
	
	private static final class GravityCompat {
		
		static final int CENTER =
		android.view.Gravity.CENTER;
		
		static final int CENTER_VERTICAL =
		android.view.Gravity.CENTER_VERTICAL;
		
		static final int CENTER_HORIZONTAL =
		android.view.Gravity.CENTER_HORIZONTAL;
		
		static final int TOP =
		android.view.Gravity.TOP;
		
		static final int LEFT =
		android.view.Gravity.LEFT;
	}
}
