package android.tgudapp.ui.fragment;

import java.util.List;

import org.json.JSONArray;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.tgudapp.adapter.ListViewPlaceAdapter;
import android.tgudapp.model.Place;
import android.tgudapp.service.DataParser;
import android.tgudapp.ui.MainActivity;
import android.tgudapp.ui.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FragmentListPlace extends Fragment {
	private String cityValue;
	private String categoryValue;
	private RequestQueue mVolleyRequest;
	private ProgressDialog mProgress;
	private JsonArrayRequest jArrRequest;
	private ListView lvPlaceByCategory;
	private List<Place> listPlace;
	private ListViewPlaceAdapter mLvPlaceAdapter;
	private final String TAG_REQUEST = "MY_TAG";
	private String categoryName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cityValue = getArguments().getString(MainActivity.CITY_VALUE_SELECTED);
		categoryValue = getArguments().getString(MainActivity.CATEGORY_VALUE_SELECTED);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setUpActionBar();
		View v = inflater.inflate(R.layout.fragment_list_place, container, false);
		lvPlaceByCategory = (ListView)v.findViewById(R.id.lvListPlaceByCateory);
		mVolleyRequest = Volley.newRequestQueue(getActivity());
		makeHttpRequest();
		showProcess();
		return v;
	}
	private void setUpActionBar(){
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);
		if(categoryValue.equalsIgnoreCase("1")){
			categoryName = getResources().getString(R.string.food);
		}else if (categoryValue.equalsIgnoreCase("2")) {
			categoryName = getResources().getString(R.string.travel);
		}else if (categoryValue.equalsIgnoreCase("3")) {
			categoryName = getResources().getString(R.string.shopping);
		}else if (categoryValue.equalsIgnoreCase("4")) {
			categoryName = getResources().getString(R.string.health);
		}else if (categoryValue.equalsIgnoreCase("5")) {
			categoryName = getResources().getString(R.string.entertainment);
		}else if (categoryValue.equalsIgnoreCase("6")) {
			categoryName = getResources().getString(R.string.coffee_scream);
		}
		getActivity().getActionBar().setTitle(categoryName);
	}
	private void showProcess(){
		mProgress = ProgressDialog.show(getActivity(), "", "Loading...");
	}
	private void stopProcess(){
		mProgress.cancel();
	}
	private void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
	private void makeHttpRequest() {
		String Url = "http://thegioiuudai.com.vn/apps/server.php/merchant/list";
		Uri.Builder builder = Uri.parse(Url).buildUpon();
		builder.appendQueryParameter("city", cityValue);
		builder.appendQueryParameter("industry", categoryValue);
		jArrRequest = new JsonArrayRequest(
				builder.toString(), 
				new Listener<JSONArray>() {

					@Override
					public void onResponse(JSONArray response) {
						try {
							DataParser parser = new DataParser();
							listPlace = parser.parserListPlaceResponse(response);
							mLvPlaceAdapter = new ListViewPlaceAdapter(getActivity(), listPlace);
							lvPlaceByCategory.setAdapter(mLvPlaceAdapter);
						} catch (Exception e) {
							e.printStackTrace();
						}
						stopProcess();
					}
				}, 
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if( error instanceof NetworkError) {
						} else if( error instanceof ServerError) {
						} else if( error instanceof AuthFailureError) {
						} else if( error instanceof ParseError) {
						} else if( error instanceof NoConnectionError) {
						} else if( error instanceof TimeoutError) {
						}

						stopProcess();
						showToast(error.getMessage());
					}
				});
		jArrRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		jArrRequest.setTag(TAG_REQUEST);
		mVolleyRequest.add(jArrRequest);
	}

	
}
