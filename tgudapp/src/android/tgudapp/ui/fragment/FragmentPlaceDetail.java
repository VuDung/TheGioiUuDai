package android.tgudapp.ui.fragment;

import com.squareup.picasso.Picasso;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.tgudapp.model.Place;
import android.tgudapp.toolbox.UrlImageParser;
import android.tgudapp.ui.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentPlaceDetail extends Fragment implements OnClickListener {
	
	private ImageView imgPlaceDetail;
	private TextView tvNamePlaceDetail;
	private TextView tvAddressPlaceDetail;
	private TextView tvTelPlaceDetail;
	private TextView tvPromotionPercentPlaceDetail;
	private LinearLayout llAddressPlaceDetail;
	private LinearLayout llCallPlaceDetail;
	private LinearLayout llClickEntry;
	private LinearLayout llUseCondition;
	private LinearLayout llViewFromWP;
	private Place placeItem;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setUpActionBar();
		View v = inflater.inflate(R.layout.fragment_place_detail, container, false);
		placeItem = (Place) getArguments().getSerializable(FragmentMain.PLACEITEM);
		Log.d("name place fragment detail place", placeItem.getNamePlace());
		imgPlaceDetail = (ImageView)v.findViewById(R.id.imgPlaceDetail);
		tvNamePlaceDetail = (TextView)v.findViewById(R.id.tvNamePlaceDetail);
		tvAddressPlaceDetail = (TextView)v.findViewById(R.id.tvAddressPlaceDetail);
		tvTelPlaceDetail = (TextView)v.findViewById(R.id.tvTelPlaceDetail);
		tvPromotionPercentPlaceDetail = (TextView)v.findViewById(R.id.tvPromotionPercentPlaceDetail);
		llAddressPlaceDetail = (LinearLayout)v.findViewById(R.id.llAddressPlaceDetail);
		llAddressPlaceDetail.setOnClickListener(this);
		llCallPlaceDetail = (LinearLayout)v.findViewById(R.id.llCallPlaceDetail);
		llCallPlaceDetail.setOnClickListener(this);
		llClickEntry = (LinearLayout)v.findViewById(R.id.llClickEntry);
		llClickEntry.setOnClickListener(this);
		llUseCondition = (LinearLayout)v.findViewById(R.id.llUseCondition);
		llUseCondition.setOnClickListener(this);
		llViewFromWP = (LinearLayout)v.findViewById(R.id.llViewFromWP);
		llViewFromWP.setOnClickListener(this);
		Picasso.with(getActivity())
				.load(placeItem.getImageURL())
				.fit()
				.placeholder(R.drawable.empty_photo)
				.error(R.drawable.empty_photo)
				.into(imgPlaceDetail);
		tvNamePlaceDetail.setText(placeItem.getNamePlace());
		tvAddressPlaceDetail.setText(placeItem.getAddressPlace());
		tvTelPlaceDetail.setText(placeItem.getTelPlace());
		tvPromotionPercentPlaceDetail.setText("Giảm " + placeItem.getProPercent() + " %");
		return v;
	}
	
	private void setUpActionBar(){
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);
		getActivity().getActionBar().setTitle(R.string.place_detail);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llAddressPlaceDetail:
			FragmentManager fragManager = getActivity().getSupportFragmentManager();
			FragmentTransaction fragTransaction = fragManager.beginTransaction();
			FragmentMapPlaceDetail fragMap = new FragmentMapPlaceDetail();
			Bundle args = new Bundle();
			args.putSerializable(FragmentMain.PLACEITEM, placeItem);
			fragMap.setArguments(args);
			fragTransaction.replace(R.id.frameContent, fragMap)
							.addToBackStack(null)
							.commit();
			break;
		case R.id.llCallPlaceDetail:
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setTitle("Call");
			builder.setMessage(R.string.do_u_want_call_them);
			builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String tel = "tel:" + tvTelPlaceDetail.getText().toString();
					Intent iCall = new Intent(Intent.ACTION_CALL);
					iCall.setData(Uri.parse(tel));
					startActivity(iCall);
				}
			});
			builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			AlertDialog dialogCall = builder.create();
			dialogCall.show();
			break;
		case R.id.llClickEntry:
			final Dialog dialogEntry = new Dialog(getActivity());
			dialogEntry.setTitle(placeItem.getNamePlace());
			dialogEntry.setCancelable(true);
			dialogEntry.setContentView(R.layout.dialog_webview);
			WebView wv = (WebView)dialogEntry.findViewById(R.id.wvEntryItem);
			wv.getSettings().setJavaScriptEnabled(true);
			wv.loadData(placeItem.getDescription(), "text/html; charset=UTF-8", null);
			dialogEntry.show();
//			View dialogEntryView = getActivity().getLayoutInflater().inflate(R.layout.dialog_entry_place, null);
//			AlertDialog.Builder builderEntry = new Builder(getActivity());
//			builderEntry.setTitle(placeItem.getNamePlace());
//			builderEntry.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builderEntry.setView(dialogEntryView);
//			final AlertDialog dialogEntry = builderEntry.create();
//			dialogEntry.show();
//			TextView tvEntryContent = (TextView)dialogEntryView.findViewById(R.id.tvEntryContent);
//			UrlImageParser urlImageParser = new UrlImageParser(tvEntryContent, getActivity());
//			Spanned httpSpan = Html.fromHtml(placeItem.getDescription(), urlImageParser, null);
//			tvEntryContent.setText(httpSpan);
//			if(llEntryDescription.getVisibility() == View.GONE){
//				llEntryDescription.setVisibility(View.VISIBLE);
//				UrlImageParser urlImageParser = new UrlImageParser(tvEntryDescription, getActivity());
//				Spanned httpSpan = Html.fromHtml(placeItem.getDescription(), urlImageParser, null);
//				tvEntryDescription.setText(httpSpan);
//			}else{
//				llEntryDescription.setVisibility(View.GONE);
//			}
			break;
		case R.id.llUseCondition:
			View dialogConditionView = getActivity().getLayoutInflater().inflate(R.layout.dialog_entry_place, null);
			AlertDialog.Builder builderCondition = new Builder(getActivity());
			builderCondition.setTitle(getResources().getString(R.string.use_condition));
			builderCondition.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builderCondition.setView(dialogConditionView);
			final AlertDialog dialogCondition = builderCondition.create();
			dialogCondition.show();
			TextView tvConditionContent = (TextView)dialogConditionView.findViewById(R.id.tvEntryContent);
			tvConditionContent.setText(Html.fromHtml(placeItem.getConditions()).toString());
			break;
		case R.id.llViewFromWP:
			View dialogViewFromView = getActivity().getLayoutInflater().inflate(R.layout.dialog_entry_place, null);
			AlertDialog.Builder builderView = new Builder(getActivity());
			builderView.setTitle(getResources().getString(R.string.view_from_wordpromotion));
			builderView.setNegativeButton("Close", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builderView.setView(dialogViewFromView);
			final AlertDialog dialogViewFrom = builderView.create();
			dialogViewFrom.show();
			TextView tvViewContent = (TextView)dialogViewFromView.findViewById(R.id.tvEntryContent);
			tvViewContent.setText(Html.fromHtml(placeItem.getFeatures()).toString());
			break;
		default:
			break;
		}
	}
	
}